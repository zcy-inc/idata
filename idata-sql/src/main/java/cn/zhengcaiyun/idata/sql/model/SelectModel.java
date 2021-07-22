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

import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 14:49
 **/
public class SelectModel implements ModelRender {
    private final List<BaseColumn> columnList;

    private SelectModel() {
        columnList = Lists.newArrayList();
    }

    public static SelectModel of() {
        return new SelectModel();
    }

    public SelectModel addColumn(BaseColumn... columns) {
        if (columns != null && columns.length > 0) {
            for (BaseColumn column : columns) {
                columnList.add(column);
            }
        }
        return this;
    }

    @Override
    public String renderSql() {
        checkArgument(!CollectionUtils.isEmpty(this.columnList), "columnList is empty.");
        StringBuilder builder = new StringBuilder();
        builder.append("select ");
        int count = 0;
        for (BaseColumn column : this.columnList) {
            if (count > 0) {
                builder.append(", ");
            }
            builder.append(column.renderSqlWithAlias());
            count++;
        }
        return builder.toString();
    }
}
