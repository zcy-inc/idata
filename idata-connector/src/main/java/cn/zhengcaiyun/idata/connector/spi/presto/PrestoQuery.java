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
package cn.zhengcaiyun.idata.connector.spi.presto;

import cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto;
import cn.zhengcaiyun.idata.connector.bean.dto.QueryResultDto;
import cn.zhengcaiyun.idata.connector.connection.ConnectionCfg;
import cn.zhengcaiyun.idata.connector.constant.enums.PrestoDataTypeEnum;
import cn.zhengcaiyun.idata.connector.constant.enums.SqlDataTypeEnum;
import cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum;
import cn.zhengcaiyun.idata.connector.service.Query;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 从itable copy过来，暂时先这样，后续需要做一个独立模块时再调整
 */

public class PrestoQuery implements Query {

    @Override
    public Connection getConnection(ConnectionCfg connectionCfg) throws SQLException {
        String jdbcUrl = String.format("jdbc:presto://%s:%d/%s",
                connectionCfg.getHost(), connectionCfg.getPort(), connectionCfg.getDbCatalog());
        return DriverManager.getConnection(jdbcUrl, connectionCfg.getUsername(), connectionCfg.getPassword());
    }

    @Override
    public List<String> getSchemas(ConnectionCfg connectionCfg) throws SQLException {
        List<String> schemas = new ArrayList<>();
        try (Connection connection = getConnection(connectionCfg);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("show schemas")) {
            while (resultSet.next()) {
                schemas.add(resultSet.getString(1));
            }
        }
        return schemas;
    }

    @Override
    public List<String> getTables(ConnectionCfg connectionCfg, String schemaName) throws SQLException {
        List<String> tables = new ArrayList<>();
        try (Connection connection = getConnection(connectionCfg);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("show tables in " + schemaName)) {
            while (resultSet.next()) {
                tables.add(resultSet.getString(1));
            }
        }
        return tables;
    }

    @Override
    public List<ColumnInfoDto> getColumns(ConnectionCfg connectionCfg, String schemaName, String tableName) throws SQLException {
        List<ColumnInfoDto> columns = new ArrayList<>();
        try (Connection connection = getConnection(connectionCfg);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("show columns from " + schemaName + "." + tableName)) {
            while (resultSet.next()) {
                WideDataTypeEnum dataType = PrestoDataTypeEnum.toDataType(resultSet.getString("Type"));
                if (!WideDataTypeEnum.Ignore.equals(dataType)) {
                    ColumnInfoDto column = new ColumnInfoDto();
                    column.setColumnName(resultSet.getString("Column"));
                    column.setColumnType(resultSet.getString("Type"));
                    column.setDataType(dataType);
                    columns.add(column);
                }
            }
        }
        return columns;
    }

    @Override
    public QueryResultDto query(ConnectionCfg connectionCfg, String selectSql) throws SQLException {
        QueryResultDto queryResult = new QueryResultDto();
        List<ColumnInfoDto> meta = new ArrayList<>();
        List<List<String>> data = new ArrayList<>();
        try (Connection connection = getConnection(connectionCfg);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSql)) {
            int columnCount = resultSet.getMetaData().getColumnCount();
            WideDataTypeEnum[] dataTypes = new WideDataTypeEnum[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                dataTypes[i-1] = SqlDataTypeEnum.toDataType(resultSet.getMetaData().getColumnType(i));
                if (!WideDataTypeEnum.Ignore.equals(dataTypes[i-1])) {
                    ColumnInfoDto column = new ColumnInfoDto();
                    column.setColumnName(resultSet.getMetaData().getColumnName(i));
                    column.setColumnType(resultSet.getMetaData().getColumnTypeName(i));
                    column.setDataType(dataTypes[i-1]);
                    meta.add(column);
                }
            }
            while (resultSet.next()) {
                List<String> record = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    if (!WideDataTypeEnum.Ignore.equals(dataTypes[i-1])) {
                        if (resultSet.getString(i) == null) {
                            record.add(null);
                        }
                        else if (WideDataTypeEnum.Decimal.equals(dataTypes[i-1])) {
                            record.add(resultSet.getBigDecimal(i).toPlainString());
                        }
                        else {
                            record.add(resultSet.getString(i));
                        }
                    }
                }
                data.add(record);
            }
        }
        queryResult.setMeta(meta);
        queryResult.setData(data);
        return queryResult;
    }

}
