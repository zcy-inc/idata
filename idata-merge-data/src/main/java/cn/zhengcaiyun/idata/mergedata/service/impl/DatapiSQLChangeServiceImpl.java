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

package cn.zhengcaiyun.idata.mergedata.service.impl;

import cn.zhengcaiyun.idata.mergedata.dal.old.OldIDataDao;
import cn.zhengcaiyun.idata.mergedata.dto.MigrateResultDto;
import cn.zhengcaiyun.idata.mergedata.service.DatapiSQLChangeService;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-02-23 17:14
 **/
@Service
public class DatapiSQLChangeServiceImpl implements DatapiSQLChangeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatapiSQLChangeServiceImpl.class);

    @Autowired
    private OldIDataDao oldIDataDao;

    @Override
    public List<MigrateResultDto> change(String apiIds) {
        List<MigrateResultDto> resultDtoList = Lists.newArrayList();
        List<JSONObject> oldDatapiCfgs = fetchOldData();
        if (CollectionUtils.isEmpty(oldDatapiCfgs)) {
            resultDtoList.add(new MigrateResultDto("迁移完成", "没有需要迁移的数据", ""));
            return resultDtoList;
        }

        Long api_id = null;
        List<Long> needChangeApis = null;
        if (StringUtils.isNotBlank(apiIds) && !"all".equals(apiIds)) {
            needChangeApis = Splitter.on(",")
                    .trimResults()
                    .omitEmptyStrings()
                    .splitToList(apiIds)
                    .stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        }
        List<Long> changedApi = Lists.newArrayList();
        for (JSONObject oldDatapiCfg : oldDatapiCfgs) {
            try {
                api_id = oldDatapiCfg.getLong("api_id");
                if (!CollectionUtils.isEmpty(needChangeApis) && !needChangeApis.contains(api_id)) {
                    continue;
                }
                String sql_template = oldDatapiCfg.getString("sql_template");
                if (StringUtils.isBlank(sql_template)) {
                    continue;
                }
                List<String> odsTableNames = extractOdsTableName(sql_template);
                if (CollectionUtils.isEmpty(odsTableNames))
                    continue;

                String destSql = changeOdsTableName(sql_template, odsTableNames);
                if (StringUtils.isBlank(destSql)) {
                    resultDtoList.add(new MigrateResultDto("订正错误", String.format("接口%s替换SQL失败", api_id), ""));
                } else {
                    oldDatapiCfg.put("destSql", destSql);
                    int ret = oldIDataDao.changeDatapiSql(oldDatapiCfg);
                    if (ret <= 0) {
                        resultDtoList.add(new MigrateResultDto("订正错误", String.format("接口%s插入数据失败", api_id), ""));
                    } else {
                        resultDtoList.add(new MigrateResultDto("迁移成功", String.format("接口%s订正表名成功", api_id), ""));
                        changedApi.add(api_id);
                        LOGGER.info("订正Datapi：{} 接口表名成功", api_id);
                    }
                }
            } catch (Exception ex) {
                LOGGER.warn("订正Datapi：{} 接口表名异常", api_id, Throwables.getStackTraceAsString(ex));
                resultDtoList.add(new MigrateResultDto("订正异常", String.format("接口%s订正表名异常", api_id), Throwables.getStackTraceAsString(ex)));
            }
        }
        LOGGER.info("*** *** 订正Datapi表名结束 *** *** 本次订正接口数作业数[{}]，作业[{}]", changedApi.size(), Joiner.on(", ").join(changedApi));
        return resultDtoList;
    }

    private List<JSONObject> fetchOldData() {
        List<String> columns = Lists.newArrayList("id", "creator", "api_id", "sql_template", "limit_count",
                "excel_template", "version", "edit_enable", "datasource_id");
        String filter = " del = false and 'PUBLISHED' = any(status)";
        return oldIDataDao.queryList("idata.api_config", columns, filter);
    }

    private List<String> extractOdsTableName(String sql) {
        // 兼容空格、注释、逗号和换行符
        return Arrays.stream(sql.split(" |,|--|\n")).filter(t -> t.contains("ods_") && t.contains(".sync_"))
                .collect(Collectors.toList());
    }

    private String changeOdsTableName(String sourceSql, List<String> odsTableNames) {
        if (CollectionUtils.isEmpty(odsTableNames))
            return sourceSql;

        Map<String, String> tableNameMap = new HashMap<>();
        for (String odsTableName : odsTableNames) {
            List<String> tablePartList = Arrays.stream(odsTableName.split("ods_|\\.sync_"))
                    .filter(StringUtils::isNotBlank)
                    .map(String::trim)
                    .collect(Collectors.toList());
            if (tablePartList.size() == 2) {
                tableNameMap.put(odsTableName, "ods.ods_" + tablePartList.get(0) + "_" + tablePartList.get(1));
            } else if (tablePartList.size() == 3) {
                tableNameMap.put(odsTableName, "ods.ods_" + tablePartList.get(0) + "_" + tablePartList.get(1) + "ods_" + tablePartList.get(2));
            } else {
                return null;
            }
        }
        String destSql = sourceSql;
        for (Map.Entry<String, String> values : tableNameMap.entrySet()) {
            destSql = destSql.replace(values.getKey(), values.getValue());
        }
        return destSql;
    }

}
