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

import java.util.Objects;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-27 17:06
 **/
public class PagingClause implements SqlExpression {
    private final Long limit;
    private final Long offset;

    private PagingClause(Long limit, Long offset) {
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public String genSql() {
        String pageSql = "";
        if (!Objects.isNull(offset)) {
            pageSql = pageSql + " offset " + offset;
        }
        if (!Objects.isNull(limit)) {
            pageSql = pageSql + " limit " + limit;
        }
        return pageSql;
    }

    public static PagingClause of(Long limit, Long offset) {
        return new PagingClause(limit, offset);
    }

    public static PagingClause of(Long limit) {
        return of(limit, null);
    }

}
