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
package cn.zhengcaiyun.idata.label.compute.query;

import cn.zhengcaiyun.idata.label.compute.query.dto.ColumnDto;
import cn.zhengcaiyun.idata.label.compute.query.dto.ConnectionDto;
import cn.zhengcaiyun.idata.label.compute.query.dto.WideTableDataDto;
import cn.zhengcaiyun.idata.label.compute.query.enums.ConnectionTypeEnum;
import cn.zhengcaiyun.idata.label.compute.query.trino.PrestoQuery;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 从itable copy过来，暂时先这样，后续需要做一个独立模块时再调整
 * @author shiyin(沐泽)
 * @date 2020/6/15 16:38
 */

@Service
public class QueryService implements Query {

    private final QueryFactory queryFactory = new QueryFactory();

    @Override
    public Connection getConnection(ConnectionDto connectionDto) throws SQLException {
        return queryFactory.getQuery(connectionDto.getType())
                .getConnection(connectionDto);
    }

    @Override
    public List<String> getSchemas(ConnectionDto connectionDto) throws SQLException {
        return queryFactory.getQuery(connectionDto.getType())
                .getSchemas(connectionDto);
    }

    @Override
    public List<String> getTables(ConnectionDto connectionDto, String schemaName) throws SQLException {
        return queryFactory.getQuery(connectionDto.getType())
                .getTables(connectionDto, schemaName);
    }

    @Override
    public List<ColumnDto> getColumns(ConnectionDto connectionDto, String schemaName, String tableName) throws SQLException {
        return queryFactory.getQuery(connectionDto.getType())
                .getColumns(connectionDto, schemaName, tableName);
    }

    @Override
    public WideTableDataDto query(ConnectionDto connectionDto, String selectSql) throws SQLException {
        return queryFactory.getQuery(connectionDto.getType())
                .query(connectionDto, selectSql);
    }

    static class QueryFactory {
        private final PrestoQuery prestoQuery = new PrestoQuery();

        public Query getQuery(ConnectionTypeEnum type) {
            if (ConnectionTypeEnum.presto.equals(type)) {
                return this.prestoQuery;
            }
            throw new RuntimeException("暂不支持数据源类型:" + type.name());
        }
    }

}
