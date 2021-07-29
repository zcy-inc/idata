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
package cn.zhengcaiyun.idata.connector.sql.expr.cond;

import cn.zhengcaiyun.idata.connector.sql.expr.ConditionExpr;
import cn.zhengcaiyun.idata.connector.sql.expr.SqlExpression;
import com.google.common.collect.Lists;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 15:29
 **/
public abstract class BaseCondition implements ConditionExpr {
    protected final SqlExpression sqlExpr;
    protected final List<String> params;
    protected String connector;

    protected BaseCondition(SqlExpression sqlExpr, String... params) {
        checkNotNull(sqlExpr, "过滤条件对象不能为空.");
        checkArgument(params != null && params.length > 0, "过滤条件参数不能为空.");
        this.sqlExpr = sqlExpr;
        this.params = Lists.newArrayList(params);
    }

    protected BaseCondition(SqlExpression sqlExpr, List<String> params) {
        checkNotNull(sqlExpr, "过滤条件对象不能为空.");
        checkArgument(params != null && params.size() > 0, "过滤条件参数不能为空.");
        this.sqlExpr = sqlExpr;
        this.params = Lists.newArrayList(params);
    }

    protected String getConditionSubject() {
        return this.sqlExpr.genSql();
    }

    protected String getParam() {
        return this.params.get(0);
    }

    protected List<String> getParams() {
        return this.params;
    }

    protected String getAnotherParam() {
        return this.params.get(1);
    }

    public String getConnector() {
        return connector;
    }

    public void setConnector(String connector) {
        this.connector = connector;
    }

    protected abstract BaseCondition getThis();
}
