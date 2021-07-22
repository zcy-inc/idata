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
public class OrderModel implements ModelRender {

    private final List<SortableColumn> columnList;

    private OrderModel() {
        columnList = Lists.newArrayList();
    }

    public static OrderModel of() {
        return new OrderModel();
    }

    public OrderModel addAscColumn(BaseColumn column) {
        columnList.add(SortableColumn.asc(column));
        return this;
    }

    public OrderModel addDescColumn(BaseColumn column) {
        columnList.add(SortableColumn.desc(column));
        return this;
    }

    @Override
    public String renderSql() {
        checkArgument(!CollectionUtils.isEmpty(this.columnList), "columnList is empty.");
        StringBuilder builder = new StringBuilder();
        builder.append("order by ");
        int count = 0;
        for (SortableColumn column : this.columnList) {
            if (count > 0) {
                builder.append(", ");
            }
            builder.append(column.getColumn().renderSql());
            builder.append(" ");
            builder.append(column.getSort());
            count++;
        }
        return builder.toString();
    }

    private static class SortableColumn {
        private BaseColumn column;
        private String sort;

        private SortableColumn(BaseColumn column, String sort) {
            this.column = column;
            this.sort = sort;
        }

        public static SortableColumn asc(BaseColumn column) {
            return new SortableColumn(column, "asc");
        }

        public static SortableColumn desc(BaseColumn column) {
            return new SortableColumn(column, "desc");
        }

        public BaseColumn getColumn() {
            return column;
        }

        public String getSort() {
            return sort;
        }
    }
}
