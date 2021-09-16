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

package cn.zhengcaiyun.idata.connector.api.impl;

import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.connector.api.MetadataQueryApi;
import cn.zhengcaiyun.idata.connector.bean.dto.TableTechInfoDto;
import cn.zhengcaiyun.idata.connector.spi.hive.HiveService;
import cn.zhengcaiyun.idata.system.dal.dao.SysConfigDao;
import cn.zhengcaiyun.idata.system.dal.model.SysConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

import static cn.zhengcaiyun.idata.system.dal.dao.SysConfigDynamicSqlSupport.sysConfig;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.nonNull;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-20 11:50
 **/
@Service
public class MetadataQueryApiImpl implements MetadataQueryApi {

    private static final Logger logger = LoggerFactory.getLogger(MetadataQueryApiImpl.class);

    @Autowired
    private HiveService hiveService;
    @Autowired
    private SysConfigDao sysConfigDao;

    @Override
    public TableTechInfoDto getTableTechInfo(String db, String table) {
        String jdbcUrl = getConnectionCfg();
        String tableSize = hiveService.getTableSize(jdbcUrl, db, table);
        TableTechInfoDto techInfoDto = new TableTechInfoDto();
        techInfoDto.setTableSize(tableSize);
        return techInfoDto;
    }

    @Override
    public Boolean testConnection(DataSourceTypeEnum sourceTypeEnum, String host, Integer port, String username, String password, String dbName, String schema) {
        String jdbcUrl = getJdbcUrl(sourceTypeEnum, host, port, dbName, schema);
        if (StringUtils.isBlank(jdbcUrl)) {
            return false;
        }

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
            return nonNull(conn);
        } catch (SQLException ex) {
            logger.warn("test connection failed. ex: {}", ex);
        }
        return false;
    }

    private String getConnectionCfg() {
        SysConfig hiveConfig = sysConfigDao.selectOne(dsl -> dsl.where(sysConfig.keyOne, isEqualTo("hive-info")))
                .orElse(null);
        checkState(Objects.nonNull(hiveConfig), "数据源连接信息不正确");
        return hiveConfig.getValueOne();
    }

    private String getJdbcUrl(DataSourceTypeEnum sourceTypeEnum, String host, Integer port, String dbName, String schema) {
        String protocol = null;
        if (DataSourceTypeEnum.mysql == sourceTypeEnum) {
            protocol = "mysql";
        } else if (DataSourceTypeEnum.postgresql == sourceTypeEnum) {
            protocol = "postgresql";
        } else if (DataSourceTypeEnum.presto == sourceTypeEnum) {
            protocol = "presto";
        } else if (DataSourceTypeEnum.hive == sourceTypeEnum) {
            protocol = "hive2";
        }
        if (StringUtils.isEmpty(protocol)) return null;

        String jdbcUrl = String.format("jdbc:%s://%s:%d/%s", protocol, host, port, dbName);
        return jdbcUrl;
    }
}
