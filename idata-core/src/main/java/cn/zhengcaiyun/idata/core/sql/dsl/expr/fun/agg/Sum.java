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
package cn.zhengcaiyun.idata.core.sql.dsl.expr.fun.agg;

import cn.zhengcaiyun.idata.core.sql.dsl.expr.SelectExpr;
import cn.zhengcaiyun.idata.core.sql.dsl.expr.fun.BaseFunction;

import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 15:00
 **/
public class Sum extends BaseFunction<Sum> {

    private Sum(SelectExpr selectExpr, String alias) {
        super(selectExpr, alias);
    }

    @Override
    public Optional<String> alias() {
        return Optional.ofNullable(super.alias);
    }

    @Override
    public Sum as(String alias) {
        return new Sum(super.selectExpr, alias);
    }

    public static Sum of(SelectExpr selectExpr, String alias) {
        return new Sum(selectExpr, alias);
    }

    public static Sum of(SelectExpr selectExpr) {
        return new Sum(selectExpr, null);
    }

    @Override
    public String genSql() {
        return "sum(" + super.selectExpr.genSql() + ")";
    }
}
