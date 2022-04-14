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

package cn.zhengcaiyun.idata.datasource.api.impl;

import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.commons.enums.DriverTypeEnum;
import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import cn.zhengcaiyun.idata.commons.util.DesUtil;
import cn.zhengcaiyun.idata.datasource.api.DataSourceApi;
import cn.zhengcaiyun.idata.datasource.api.dto.DataSourceDetailDto;
import cn.zhengcaiyun.idata.datasource.api.dto.DataSourceDto;
import cn.zhengcaiyun.idata.datasource.bean.dto.DbConfigDto;
import cn.zhengcaiyun.idata.datasource.dal.model.DataSource;
import cn.zhengcaiyun.idata.datasource.dal.repo.DataSourceRepo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.nonNull;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-29 16:58
 **/
@Service
public class DataSourceApiImpl implements DataSourceApi {

    private final DataSourceRepo dataSourceRepo;

    @Autowired
    public DataSourceApiImpl(DataSourceRepo dataSourceRepo) {
        this.dataSourceRepo = dataSourceRepo;
    }

    @Override
    public DataSourceDto getDataSource(Long id) {
        checkArgument(nonNull(id), "数据源编号为空");
        Optional<DataSource> optional = dataSourceRepo.queryDataSource(id);
        checkArgument(optional.isPresent(), "数据源不存在");
        DataSource source = optional.get();
        checkState(Objects.equals(DeleteEnum.DEL_NO.val, source.getDel()), "数据源已删除");
        return DataSourceDto.from(source);
    }

    @Override
    public DataSourceDetailDto getDataSourceDetail(Long id, String env) {
        DataSourceDto dataSource = getDataSource(id);
        checkArgument(Objects.nonNull(dataSource), String.format("数据源不存在, DataSourceId:%d", id));
        List<DbConfigDto> rawDbConfigList = dataSource.getDbConfigList();
        checkArgument(CollectionUtils.isNotEmpty(rawDbConfigList), String.format("数据源配置缺失, DataSourceId:%d", id));

        List<DbConfigDto> envDbConfigList = rawDbConfigList.stream()
                .filter(e -> {
                    if (env != null) {
                        return e.getEnv().name().equals(env);
                    }
                    return true;
                }).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(envDbConfigList)) {
            throw new GeneralException("数据源配置为空，id：" + id + ", env：" + env);
        }

        DbConfigDto dbConfigDto = envDbConfigList.get(0);
        DataSourceDetailDto res = new DataSourceDetailDto();
        res.setDataSourceTypeEnum(dataSource.getType());

        String dbName = dbConfigDto.getDbName();
        res.setJdbcUrl(getJdbcUrl(dataSource.getType(), dbConfigDto.getHost(), dbConfigDto.getPort(), dbName));
        res.setUserName(dbConfigDto.getUsername());
        res.setPassword(DesUtil.encrypt(dbConfigDto.getPassword()));
        res.setDbName(dbName);
        res.setDriverTypeEnum(DriverTypeEnum.ofDataSourceTypeEnum(DataSourceTypeEnum.valueOf(dataSource.getType().name())));

        return res;
    }

    private String getJdbcUrl(DataSourceTypeEnum sourceTypeEnum, String host, Integer port, String dbName) {
        String protocol = null;
        if (DataSourceTypeEnum.mysql == sourceTypeEnum || DataSourceTypeEnum.doris == sourceTypeEnum) {
            protocol = "mysql";
        } else if (DataSourceTypeEnum.postgresql == sourceTypeEnum) {
            protocol = "postgresql";
        } else if (DataSourceTypeEnum.presto == sourceTypeEnum) {
            protocol = "presto";
        } else if (DataSourceTypeEnum.hive == sourceTypeEnum) {
            protocol = "hive2";
        }  else if (DataSourceTypeEnum.elasticsearch == sourceTypeEnum) {
            // es直接返回ip+port，不拼接jdbc，用于给htool
            return host + ":" + port;
        }  else if (DataSourceTypeEnum.kafka == sourceTypeEnum) {
            // kafka直接返回ip+port，不拼接jdbc，用于给htool
            return host + ":" + port;
        }  else if (DataSourceTypeEnum.mssql == sourceTypeEnum) {
            // es直接返回ip+port，不拼接jdbc，用于给htool
            return String.format("jdbc:sqlserver://%s:%d;databasename=%s", host, port, dbName);
        }
        if (StringUtils.isEmpty(protocol)) return null;

        String jdbcUrl;
        if (StringUtils.isNotEmpty(dbName)) {
            jdbcUrl = String.format("jdbc:%s://%s:%d/%s", protocol, host, port, dbName);
        } else {
            jdbcUrl = String.format("jdbc:%s://%s:%d", protocol, host, port);
        }
        return jdbcUrl;
    }

    @Override
    public String getPrimaryKey(Long id, String tableName) {
        DataSourceDetailDto detail = getDataSourceDetail(id, null);

        List<String> pkList = new ArrayList<>();
        try {
            // 加载驱动到JVM
            Class.forName("com.mysql.jdbc.Driver");
            // 获取连接
            Connection connection = DriverManager.getConnection(detail.getJdbcUrl(), detail.getUserName(), DesUtil.decrypt(detail.getPassword()));
            // 数据库的所有数据
            DatabaseMetaData metaData = connection.getMetaData();
            // 获取表的主键名字
            ResultSet pkInfo = metaData.getPrimaryKeys(null, "%", tableName);
            while (pkInfo.next()) {
//            System.out.print("数据库名称:"+pkInfo.getString("TABLE_CAT")+"                  ");
//            System.out.print("表名称:"+pkInfo.getString("TABLE_NAME")+"                  ");
//            System.out.print("主键列的名称:"+pkInfo.getString("COLUMN_NAME")+"                  ");
//            System.out.print("类型:"+pkInfo.getString("PK_NAME")+"                  ");
//            System.out.println("");
                pkList.add(pkInfo.getString("COLUMN_NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return StringUtils.join(pkList, ",");
    }
}
