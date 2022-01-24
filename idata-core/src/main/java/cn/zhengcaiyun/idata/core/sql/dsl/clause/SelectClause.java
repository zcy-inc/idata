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

import cn.zhengcaiyun.idata.core.sql.dsl.expr.SelectExpr;
import cn.zhengcaiyun.idata.core.sql.dsl.expr.SqlExpression;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-27 15:51
 **/
public class SelectClause implements SqlExpression {

    private final List<SelectExpr> selects;

    private SelectClause() {
        selects = Lists.newArrayList();
    }

    public static SelectClause of() {
        return new SelectClause();
    }

    public SelectClause add(SelectExpr... selects) {
        if (selects != null && selects.length > 0) {
            for (SelectExpr expr : selects) {
                this.selects.add(expr);
            }
        }
        return this;
    }

    @Override
    public String genSql() {
        checkArgument(!CollectionUtils.isEmpty(this.selects), "查询列不能为空.");
        return this.selects.stream()
                .map(SelectExpr::genSqlWithAlias)
                .collect(Collectors.joining(", ", "select", ""));
    }

}
