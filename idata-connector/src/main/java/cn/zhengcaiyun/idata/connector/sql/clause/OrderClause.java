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

package cn.zhengcaiyun.idata.connector.sql.clause;

import cn.zhengcaiyun.idata.connector.sql.expr.SelectExpr;
import cn.zhengcaiyun.idata.connector.sql.expr.SqlExpression;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-27 16:41
 **/
public class OrderClause implements SqlExpression {

    private final List<SortableExpr> sortableList;

    private OrderClause() {
        sortableList = Lists.newArrayList();
    }

    public static OrderClause of() {
        return new OrderClause();
    }

    public OrderClause asc(SelectExpr expr) {
        sortableList.add(SortableExpr.asc(expr));
        return this;
    }

    public OrderClause desc(SelectExpr expr) {
        sortableList.add(SortableExpr.desc(expr));
        return this;
    }

    @Override
    public String genSql() {
        checkArgument(ObjectUtils.isNotEmpty(sortableList), "排序对象不能为空.");
        return sortableList.stream()
                .map(SortableExpr::genSql)
                .collect(Collectors.joining(", ", "order by ", ""));
    }

    private static class SortableExpr implements SqlExpression {
        private SelectExpr expr;
        private String sort;

        private SortableExpr(SelectExpr expr, String sort) {
            this.expr = expr;
            this.sort = sort;
        }

        public static SortableExpr asc(SelectExpr expr) {
            return new SortableExpr(expr, "asc");
        }

        public static SortableExpr desc(SelectExpr expr) {
            return new SortableExpr(expr, "desc");
        }

        @Override
        public String genSql() {
            return this.expr.genSql() + " " + sort;
        }
    }
}
