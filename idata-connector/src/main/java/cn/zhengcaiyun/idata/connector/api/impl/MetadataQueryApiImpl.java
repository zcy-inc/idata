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
import cn.zhengcaiyun.idata.commons.exception.ExecuteSqlException;
import cn.zhengcaiyun.idata.connector.api.MetadataQueryApi;
import cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto;
import cn.zhengcaiyun.idata.connector.bean.dto.TableTechInfoDto;
import cn.zhengcaiyun.idata.connector.spi.hive.HiveService;
import cn.zhengcaiyun.idata.system.dal.dao.SysConfigDao;
import cn.zhengcaiyun.idata.system.dal.model.SysConfig;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;
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

    @Override
    public List<String> getTableNames(DataSourceTypeEnum sourceTypeEnum, String host, Integer port, String username, String password, String dbName, String schema) {
        if (DataSourceTypeEnum.mysql != sourceTypeEnum && DataSourceTypeEnum.postgresql != sourceTypeEnum)
            return Lists.newArrayList();
        String jdbcUrl = getJdbcUrl(sourceTypeEnum, host, port, dbName, schema);
        if (StringUtils.isBlank(jdbcUrl)) {
            return Lists.newArrayList();
        }

        List<String> tableNames = Lists.newArrayList();
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             ResultSet rs = conn.getMetaData().getTables(dbName, "%", "%", new String[]{"TABLE"})) {
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");  //表名
                String tableType = rs.getString("TABLE_TYPE");  //表类型
                String remarks = rs.getString("REMARKS");       //表备注
                tableNames.add(tableName);
            }
        } catch (SQLException ex) {
            logger.warn("getTableNames failed. ex: {}", ex);
        }
        return tableNames;
    }

    @Override
    public List<ColumnInfoDto> getTableColumns(DataSourceTypeEnum sourceTypeEnum, String host, Integer port, String username, String password, String dbName, String schema, String tableName) {
        if (DataSourceTypeEnum.mysql != sourceTypeEnum && DataSourceTypeEnum.postgresql != sourceTypeEnum)
            return Lists.newArrayList();
        String jdbcUrl = getJdbcUrl(sourceTypeEnum, host, port, dbName, schema);
        if (StringUtils.isBlank(jdbcUrl)) {
            return Lists.newArrayList();
        }

        List<ColumnInfoDto> tableColumns = Lists.newArrayList();
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             ResultSet rs = conn.getMetaData().getColumns(dbName, "%", tableName, "%")) {
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");  //列名
                String dataTypeName = rs.getString("TYPE_NAME");  //java.sql.Types类型名称(列类型名称)
                String remarks = rs.getString("REMARKS");  //列描述
                ColumnInfoDto columnInfoDto = new ColumnInfoDto();
                columnInfoDto.setColumnName(columnName);
                columnInfoDto.setColumnComment(remarks);
                columnInfoDto.setColumnType(dataTypeName);
                tableColumns.add(columnInfoDto);
            }
        } catch (SQLException ex) {
            logger.warn("getTableColumns failed. ex: {}", ex);
        }
        return tableColumns;
    }

    @Override
    public List<ColumnInfoDto> getHiveTableColumns(String dbName, String tableName) {
        return hiveService.getHiveTableColumns(dbName, tableName);
    }

    @Override
    public List<ColumnInfoDto> getTablePrimaryKeys(DataSourceTypeEnum sourceTypeEnum, String host, Integer port, String username, String password, String dbName, String schema, String tableName) {
        if (DataSourceTypeEnum.mysql != sourceTypeEnum && DataSourceTypeEnum.postgresql != sourceTypeEnum)
            return Lists.newArrayList();
        String jdbcUrl = getJdbcUrl(sourceTypeEnum, host, port, dbName, schema);
        if (StringUtils.isBlank(jdbcUrl)) {
            return Lists.newArrayList();
        }

        List<ColumnInfoDto> tableColumns = Lists.newArrayList();
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             ResultSet rs = conn.getMetaData().getPrimaryKeys(dbName, "%", tableName)) {
            while (rs.next()) {
                short keySeq = rs.getShort("KEY_SEQ"); //序列号(主键内值1表示主键第一列，值2代表主键内的第二列)
                String pkName = rs.getString("PK_NAME"); //主键名称
                String columnName = rs.getString("COLUMN_NAME");  //列名
                ColumnInfoDto columnInfoDto = new ColumnInfoDto();
                columnInfoDto.setColumnName(columnName);
                columnInfoDto.setColumnComment(null);
                columnInfoDto.setColumnType(null);
                tableColumns.add(columnInfoDto);
            }
        } catch (SQLException ex) {
            logger.warn("getTablePrimaryKey failed. ex: {}", ex);
        }
        return tableColumns;
    }

    @Override
    public boolean existHiveTable(String dbName, String tableName) {
        String jdbcUrl = getConnectionCfg();
        try (Connection conn = DriverManager.getConnection(jdbcUrl);
             ResultSet rs = conn.getMetaData().getTables(dbName, "%", "%", new String[]{"TABLE"})) {
            while (rs.next()) {
                String hiveTableName = rs.getString("TABLE_NAME");  //表名
                if (StringUtils.equalsIgnoreCase(hiveTableName, tableName)) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            logger.warn("getTableNames failed. ex: {}", ex);
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
