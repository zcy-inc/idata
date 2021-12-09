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
import org.apache.commons.lang3.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-27 16:41
 **/
public class GroupClause implements SqlExpression {

    private final List<SelectExpr> selects;

    private GroupClause() {
        selects = Lists.newArrayList();
    }

    public static GroupClause of() {
        return new GroupClause();
    }

    public GroupClause add(SelectExpr... selects) {
        if (selects != null && selects.length > 0) {
            for (SelectExpr sel : selects) {
                this.selects.add(sel);
            }
        }
        return this;
    }

    @Override
    public String genSql() {
        checkArgument(ObjectUtils.isNotEmpty(this.selects), "分组条件不能为空.");
        return selects.stream()
                .map(SelectExpr::genSql)
                .collect(Collectors.joining(", ", "group by ", ""));
    }
}
