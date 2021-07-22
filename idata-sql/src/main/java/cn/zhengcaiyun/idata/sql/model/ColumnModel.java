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
package cn.zhengcaiyun.idata.sql.model;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 14:52
 **/
public class ColumnModel implements BaseColumn {
    private final String name;
    private final TableModel table;
    private final String alias;

    private ColumnModel(String name, TableModel table, String alias) {
        checkArgument(name != null, "列不能为空");
        checkArgument(table != null, "表不能为空");
        this.name = name;
        this.table = table;
        this.alias = alias;
    }

    @Override
    public Optional<String> alias() {
        return Optional.ofNullable(this.alias);
    }

    @Override
    public String renderSql() {
        return table.getName() + "." + this.name;
    }

    public static ColumnModel of(String name, TableModel table) {
        return new ColumnModel(name, table, null);
    }

    public static ColumnModel of(String name, TableModel table, String alias) {
        return new ColumnModel(name, table, alias);
    }
}
