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

package cn.zhengcaiyun.idata.connector.sql.expr.table;

import cn.zhengcaiyun.idata.connector.sql.expr.SqlAlias;
import cn.zhengcaiyun.idata.connector.sql.expr.SqlExpression;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * @description: 表
 * @author: yangjianhua
 * @create: 2021-07-26 16:00
 **/
public class TableModel implements SqlExpression, SqlAlias<TableModel> {

    private final String name;
    private final String alias;

    private TableModel(String name, String alias) {
        checkArgument(isNotEmpty(name), "表名不能为空");
        this.name = name;
        this.alias = alias;
    }

    @Override
    public String genSql() {
        return this.name;
    }

    @Override
    public TableModel as(String alias) {
        checkArgument(isNotEmpty(alias), "表别名不能为空");
        return new TableModel(this.name, alias);
    }

    @Override
    public Optional<String> alias() {
        return Optional.ofNullable(this.alias);
    }

    @Override
    public String finalName() {
        return alias().orElse(this.name);
    }

    public String getName() {
        return name;
    }

    public static TableModel of(String name) {
        return new TableModel(name, null);
    }

    public String genSqlWithAlias() {
        return alias().map(a -> this.name + " as " + a)
                .orElse(this.name);
    }
}
