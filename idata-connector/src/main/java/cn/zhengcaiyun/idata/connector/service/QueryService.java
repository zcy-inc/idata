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
package cn.zhengcaiyun.idata.connector.service;

import cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto;
import cn.zhengcaiyun.idata.connector.bean.dto.QueryResultDto;
import cn.zhengcaiyun.idata.connector.connection.ConnectionCfg;
import cn.zhengcaiyun.idata.connector.constant.enums.DataSourceEnum;
import cn.zhengcaiyun.idata.connector.spi.presto.PrestoQuery;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class QueryService implements Query {

    private final QueryFactory queryFactory = new QueryFactory();

    @Override
    public Connection getConnection(ConnectionCfg connectionCfg) throws SQLException {
        return queryFactory.getQuery(connectionCfg.getDataSourceEnum())
                .getConnection(connectionCfg);
    }

    @Override
    public List<String> getSchemas(ConnectionCfg connectionCfg) throws SQLException {
        return queryFactory.getQuery(connectionCfg.getDataSourceEnum())
                .getSchemas(connectionCfg);
    }

    @Override
    public List<String> getTables(ConnectionCfg connectionCfg, String schemaName) throws SQLException {
        return queryFactory.getQuery(connectionCfg.getDataSourceEnum())
                .getTables(connectionCfg, schemaName);
    }

    @Override
    public List<ColumnInfoDto> getColumns(ConnectionCfg connectionCfg, String schemaName, String tableName) throws SQLException {
        return queryFactory.getQuery(connectionCfg.getDataSourceEnum())
                .getColumns(connectionCfg, schemaName, tableName);
    }

    @Override
    public QueryResultDto query(ConnectionCfg connectionCfg, String selectSql) throws SQLException {
        return queryFactory.getQuery(connectionCfg.getDataSourceEnum())
                .query(connectionCfg, selectSql);
    }

    static class QueryFactory {
        private final PrestoQuery prestoQuery = new PrestoQuery();

        public Query getQuery(DataSourceEnum sourceEnum) {
            if (DataSourceEnum.PRESTO == sourceEnum) {
                return this.prestoQuery;
            }
            throw new RuntimeException("暂不支持数据源类型:" + sourceEnum.name());
        }
    }

}
