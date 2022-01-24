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

package cn.zhengcaiyun.idata.core.sql.dsl.clause;

import cn.zhengcaiyun.idata.core.sql.dsl.expr.SqlExpression;
import cn.zhengcaiyun.idata.core.sql.dsl.expr.table.TableModel;
import cn.zhengcaiyun.idata.core.sql.dsl.statement.QueryStatement;

import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-27 15:51
 **/
public class FromClause implements SqlExpression {

    private final TableModel tableModel;
    private final QueryStatement subStatement;

    private FromClause(TableModel tableModel) {
        this.tableModel = tableModel;
        this.subStatement = null;
    }

    private FromClause(QueryStatement subStatement) {
        this.tableModel = null;
        this.subStatement = subStatement;
    }

    public static FromClause of(TableModel tableModel) {
        return new FromClause(tableModel);
    }

    public static FromClause of(QueryStatement subStatement) {
        return new FromClause(subStatement);
    }

    @Override
    public String genSql() {
        String sql = Optional.ofNullable(subStatement)
                .map(QueryStatement::genSqlWithAlias)
                .orElse(this.tableModel.genSqlWithAlias());
        checkArgument(Objects.nonNull(sql), "表不能为空.");
        return sql;
    }

}
