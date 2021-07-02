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
package cn.zhengcaiyun.idata.label.compute.query.trino;


import cn.zhengcaiyun.idata.label.compute.query.Query;
import cn.zhengcaiyun.idata.label.compute.query.dto.ColumnDto;
import cn.zhengcaiyun.idata.label.compute.query.dto.ConnectionDto;
import cn.zhengcaiyun.idata.label.compute.query.dto.WideTableDataDto;
import cn.zhengcaiyun.idata.label.compute.query.enums.DataTypeEnum;
import cn.zhengcaiyun.idata.label.compute.query.enums.PrestoTypeEnum;
import cn.zhengcaiyun.idata.label.compute.query.enums.SqlTypeEnum;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 从itable copy过来，暂时先这样，后续需要做一个独立模块时再调整
 * @author shiyin(沐泽)
 * @date 2020/6/21 20:24
 */

public class PrestoQuery implements Query {

    @Override
    public Connection getConnection(ConnectionDto connectionDto) throws SQLException {
        String jdbcUrl = String.format("jdbc:presto://%s:%d/%s",
                connectionDto.getHost(), connectionDto.getPort(), connectionDto.getDbCatalog());
        return DriverManager.getConnection(jdbcUrl, connectionDto.getUsername(), connectionDto.getPassword());
    }

    @Override
    public List<String> getSchemas(ConnectionDto connectionDto) throws SQLException {
        List<String> schemas = new ArrayList<>();
        try (Connection connection = getConnection(connectionDto);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("show schemas")) {
            while (resultSet.next()) {
                schemas.add(resultSet.getString(1));
            }
        }
        return schemas;
    }

    @Override
    public List<String> getTables(ConnectionDto connectionDto, String schemaName) throws SQLException {
        List<String> tables = new ArrayList<>();
        try (Connection connection = getConnection(connectionDto);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("show tables in " + schemaName)) {
            while (resultSet.next()) {
                tables.add(resultSet.getString(1));
            }
        }
        return tables;
    }

    @Override
    public List<ColumnDto> getColumns(ConnectionDto connectionDto, String schemaName, String tableName) throws SQLException {
        List<ColumnDto> columns = new ArrayList<>();
        try (Connection connection = getConnection(connectionDto);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("show columns from " + schemaName + "." + tableName)) {
            while (resultSet.next()) {
                DataTypeEnum dataType = PrestoTypeEnum.toDataType(resultSet.getString("Type"));
                if (!DataTypeEnum.Ignore.equals(dataType)) {
                    ColumnDto column = new ColumnDto();
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
    public WideTableDataDto query(ConnectionDto connectionDto, String selectSql) throws SQLException {
        WideTableDataDto queryResult = new WideTableDataDto();
        List<ColumnDto> meta = new ArrayList<>();
        List<List<String>> data = new ArrayList<>();
        try (Connection connection = getConnection(connectionDto);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectSql)) {
            int columnCount = resultSet.getMetaData().getColumnCount();
            DataTypeEnum[] dataTypes = new DataTypeEnum[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                dataTypes[i-1] = SqlTypeEnum.toDataType(resultSet.getMetaData().getColumnType(i));
                if (!DataTypeEnum.Ignore.equals(dataTypes[i-1])) {
                    ColumnDto column = new ColumnDto();
                    column.setColumnName(resultSet.getMetaData().getColumnName(i));
                    column.setColumnType(resultSet.getMetaData().getColumnTypeName(i));
                    column.setDataType(dataTypes[i-1]);
                    meta.add(column);
                }
            }
            while (resultSet.next()) {
                List<String> record = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    if (!DataTypeEnum.Ignore.equals(dataTypes[i-1])) {
                        if (resultSet.getString(i) == null) {
                            record.add(null);
                        }
                        else if (DataTypeEnum.Decimal.equals(dataTypes[i-1])) {
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
