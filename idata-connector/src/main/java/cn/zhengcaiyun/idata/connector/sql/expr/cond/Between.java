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


import cn.zhengcaiyun.idata.connector.sql.expr.SqlExpression;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 15:11
 **/
public class Between extends BaseCondition {

    private Between(SqlExpression sqlExpr, String littler, String bigger) {
        super(sqlExpr, littler, bigger);
    }

    @Override
    protected BaseCondition getThis() {
        return this;
    }

    @Override
    public String genSql() {
        return super.getConditionSubject() + " between " + getParam() + " and " + getAnotherParam();
    }

    public static Between of(SqlExpression sqlExpr, String littler, String bigger) {
        return new Between(sqlExpr, littler, bigger);
    }

}
