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

package cn.zhengcaiyun.idata.connector.sql.expr.fun;

import cn.zhengcaiyun.idata.connector.sql.expr.SelectExpr;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-26 15:18
 **/
public abstract class BaseFunction<T extends BaseFunction> implements SelectExpr<T> {

    protected final SelectExpr selectExpr;
    protected final String alias;

    protected BaseFunction(SelectExpr selectExpr, String alias) {
        checkArgument(nonNull(selectExpr), "函数作用的对象不能为空.");
        this.selectExpr = selectExpr;
        this.alias = alias;
    }

    @Override
    public String finalName() {
        return alias().orElse(genSql());
    }

}
