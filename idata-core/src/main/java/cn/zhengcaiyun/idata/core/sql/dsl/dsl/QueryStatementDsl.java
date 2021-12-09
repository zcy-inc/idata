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

package cn.zhengcaiyun.idata.core.sql.dsl.dsl;

import cn.zhengcaiyun.idata.core.sql.dsl.expr.SelectExpr;
import cn.zhengcaiyun.idata.core.sql.dsl.expr.table.TableModel;
import cn.zhengcaiyun.idata.core.sql.dsl.statement.QueryStatement;
import cn.zhengcaiyun.idata.core.sql.dsl.clause.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-27 17:08
 **/
public class QueryStatementDsl {

    private SelectClause selectClause;
    private FromClause fromClause;
    private WhereClause whereClause;
    private GroupClause groupClause;
    private HavingClause havingClause;
    private OrderClause orderClause;
    private PagingClause pagingClause;

    private QueryStatementDsl(SelectClause selectClause) {
        this.selectClause = selectClause;
    }

    public static  QueryStatementDsl select(SelectExpr... sel){
        return new QueryStatementDsl(SelectClause.of().add(sel));
    }

    public QueryStatementDsl from(TableModel table){
        this.fromClause = FromClause.of(table);
        return this;
    }

    public QueryStatementDsl from(QueryStatement subStatement){
        this.fromClause = FromClause.of(subStatement);
        return this;
    }

    public QueryStatementDsl where(ConditionDslProvider provider){
//        this.fromClause = FromClause.of(table);
        return this;
    }

}
