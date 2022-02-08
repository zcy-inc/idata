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
import cn.zhengcaiyun.idata.core.sql.dsl.expr.cond.BaseCondition;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-27 16:41
 **/
public class HavingClause implements SqlExpression {

    private final List<BaseCondition> conditions;

    private HavingClause(BaseCondition condition) {
        this.conditions = Lists.newArrayList();
        this.conditions.add(condition);
    }

    @Override
    public String genSql() {
        return conditions.stream()
                .map(BaseCondition::genSql).collect(Collectors.joining(" ", "having ", ""));
    }

    public static HavingClause of(BaseCondition condition) {
        return new HavingClause(condition);
    }

    public HavingClause and(BaseCondition condition) {
        this.conditions.add(condition);
        return this;
    }
}
