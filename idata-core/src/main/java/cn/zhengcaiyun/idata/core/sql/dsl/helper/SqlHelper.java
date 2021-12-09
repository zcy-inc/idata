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

package cn.zhengcaiyun.idata.core.sql.dsl.helper;

import cn.zhengcaiyun.idata.core.sql.dsl.expr.col.ColumnModel;
import cn.zhengcaiyun.idata.core.sql.dsl.expr.table.TableModel;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-26 17:49
 **/
public class SqlHelper {
    public static String quotes(String str) {
        return "\"" + str + "\"";
    }

    public static String singleQuote(String str) {
        return "'" + str + "'";
    }

    public static ColumnModel col(String name) {
        return ColumnModel.of(name);
    }

    public static ColumnModel col(String name, TableModel table) {
        return ColumnModel.of(name, table);
    }

    public static TableModel tb(String table) {
        return TableModel.of(table);
    }
}
