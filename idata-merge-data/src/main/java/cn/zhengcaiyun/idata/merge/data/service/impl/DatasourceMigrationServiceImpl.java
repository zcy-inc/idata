/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.zhengcaiyun.idata.merge.data.service.impl;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.datasource.bean.dto.DataSourceDto;
import cn.zhengcaiyun.idata.datasource.bean.dto.DbConfigDto;
import cn.zhengcaiyun.idata.datasource.service.DataSourceService;
import cn.zhengcaiyun.idata.merge.data.dal.old.OldIDataDao;
import cn.zhengcaiyun.idata.merge.data.dto.MigrateResultDto;
import cn.zhengcaiyun.idata.merge.data.service.DatasourceMigrationService;
import cn.zhengcaiyun.idata.merge.data.util.IdPadTool;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-06 09:43
 **/
@Service
public class DatasourceMigrationServiceImpl implements DatasourceMigrationService {

    @Autowired
    private OldIDataDao oldIDataDao;
    @Autowired
    private DataSourceService dataSourceService;

    @Override
    @Transactional
    public List<MigrateResultDto> migrateDatasource() {
        List<MigrateResultDto> resultDtoList = Lists.newArrayList();
        // 查询旧版IData数据
        List<JSONObject> dataJsonList = fetchOldData();
        // 处理旧版数据，组装新版IData数据
        Set<String> datasourceTypes = getMigrateDatasourceType();
        Map<String, JSONObject> prodMap = Maps.newHashMap();
        Map<String, JSONObject> stagMap = Maps.newHashMap();
        dataJsonList.stream().filter(jsonObject -> datasourceTypes.contains(jsonObject.getString("type")))
                .filter(jsonObject -> "prod".equals(jsonObject.getString("status")))
                .forEach(jsonObject -> {
                    String name = jsonObject.getString("name");
                    if (StringUtils.isNotBlank(name)) {
                        if (prodMap.containsKey(name)) {
                            resultDtoList.add(new MigrateResultDto("DatasourceMigration", "prod环境名称重复的数据源", jsonObject.toJSONString()));
                        } else {
                            prodMap.put(name, jsonObject);
                        }
                    }
                });
        dataJsonList.stream().filter(jsonObject -> datasourceTypes.contains(jsonObject.getString("type")))
                .filter(jsonObject -> "staging".equals(jsonObject.getString("status")))
                .forEach(jsonObject -> {
                    String name = jsonObject.getString("name");
                    if (StringUtils.isNotBlank(name)) {
                        if (stagMap.containsKey(name)) {
                            resultDtoList.add(new MigrateResultDto("DatasourceMigration", "stag环境名称重复的数据源", jsonObject.toJSONString()));
                        } else {
                            stagMap.put(name, jsonObject);
                        }
                    }
                });

        List<DataSourceParam> sourceParamList = Lists.newArrayList();
        prodMap.forEach((name, jsonObject) -> {
            JSONObject prodJsonObject = jsonObject;
            // 查找对应stag环境数据源，先找stag开头的，再找同名的
            String stag_name = "stag_" + name;
            JSONObject stagJsonObject = stagMap.get(stag_name);
            if (Objects.isNull(stagJsonObject)) {
                stag_name = name;
                stagJsonObject = stagMap.get(stag_name);
            }
            if (!Objects.isNull(stagJsonObject)) {
                stagMap.remove(stag_name);
            }
            DataSourceDto sourceDto = buildDataSource(name, prodJsonObject, stagJsonObject);
            String old_owner = prodJsonObject.getString("owner");
            String old_operator = prodJsonObject.getString("operator");
            String nickname = StringUtils.isNotEmpty(old_owner) ? old_owner : old_operator;
            Operator operator = new Operator.Builder(0L).nickname(StringUtils.defaultString(nickname)).build();
            sourceParamList.add(new DataSourceParam(sourceDto, operator));
        });

        // 未匹配的stag数据
        stagMap.forEach((name, jsonObject) -> {
            JSONObject stagJsonObject = jsonObject;
            DataSourceDto sourceDto = buildDataSource(name, null, stagJsonObject);
            String old_owner = stagJsonObject.getString("owner");
            String old_operator = stagJsonObject.getString("operator");
            String nickname = StringUtils.isNotEmpty(old_owner) ? old_owner : old_operator;
            Operator operator = new Operator.Builder(0L).nickname(StringUtils.defaultString(nickname)).build();
            sourceParamList.add(new DataSourceParam(sourceDto, operator));
        });

        // 调用新版server接口，新增数据
        sourceParamList.stream()
                .forEach(dataSourceParam ->
                        dataSourceService.addDataSource(dataSourceParam.getDto(), dataSourceParam.getOperator()));

        // 返回迁移失败的数据 MigrateResultDto
        return resultDtoList;
    }

    private List<JSONObject> fetchOldData() {
        List<String> columns = Lists.newArrayList("id", "type", "name", "description", "host", "port", "db_name",
                "db_user", "db_psw", "operator", "owner", "status");
        String filter = "is_del = false";
        return oldIDataDao.queryList("metadata.datasource", columns, filter);
    }

    private Set<String> getMigrateDatasourceType() {
        return Sets.newHashSet("presto", "postgresql", "phoenix", "mysql", "mssql", "kylin", "kafka", "hive", "elasticsearch", "doris");
    }

    private DataSourceDto buildDataSource(String name, JSONObject prodJsonObject, JSONObject stagJsonObject) {
        DataSourceDto dto = new DataSourceDto();
        String newName = IdPadTool.padId(Objects.isNull(prodJsonObject) ? null : prodJsonObject.getString("id"))
                + "#_"
                + IdPadTool.padId(Objects.isNull(stagJsonObject) ? null : stagJsonObject.getString("id"))
                + "#_"
                + name;
        dto.setName(newName);

        JSONObject mainJsonObject = Objects.isNull(prodJsonObject) ? stagJsonObject : prodJsonObject;
        DataSourceTypeEnum type = DataSourceTypeEnum.valueOf(mainJsonObject.getString("type"));
        dto.setType(type);
        dto.setRemark(mainJsonObject.getString("description"));

        List<EnvEnum> envList = Lists.newArrayList();
        if (!Objects.isNull(prodJsonObject)) {
            envList.add(EnvEnum.prod);
        }
        if (!Objects.isNull(stagJsonObject)) {
            envList.add(EnvEnum.stag);
        }

        List<DbConfigDto> dbConfigList = Lists.newArrayList();
        if (!Objects.isNull(prodJsonObject)) {
            dbConfigList.add(buildDbConfig(prodJsonObject));
        }
        if (!Objects.isNull(stagJsonObject)) {
            dbConfigList.add(buildDbConfig(stagJsonObject));
        }
        return dto;
    }

    private DbConfigDto buildDbConfig(JSONObject jsonObject) {
        DbConfigDto dto = new DbConfigDto();
        EnvEnum env = null;
        String status = jsonObject.getString("status");
        if (status.startsWith("prod")) {
            env = EnvEnum.prod;
        } else if (status.startsWith("stag")) {
            env = EnvEnum.stag;
        }
        dto.setEnv(env);
        dto.setDbName(StringUtils.defaultString(jsonObject.getString("db_name")));
        dto.setUsername(StringUtils.defaultString(jsonObject.getString("db_user")));
        dto.setPassword(StringUtils.defaultString(jsonObject.getString("db_psw")));
        dto.setHost(StringUtils.defaultString(jsonObject.getString("host")));
        dto.setPort(MoreObjects.firstNonNull(jsonObject.getInteger("port"), 0));
        dto.setSchema("");
        return dto;
    }

    public class DataSourceParam {
        private DataSourceDto dto;
        private Operator operator;

        public DataSourceParam(DataSourceDto dto, Operator operator) {
            this.dto = dto;
            this.operator = operator;
        }

        public DataSourceDto getDto() {
            return dto;
        }

        public void setDto(DataSourceDto dto) {
            this.dto = dto;
        }

        public Operator getOperator() {
            return operator;
        }

        public void setOperator(Operator operator) {
            this.operator = operator;
        }
    }

}
