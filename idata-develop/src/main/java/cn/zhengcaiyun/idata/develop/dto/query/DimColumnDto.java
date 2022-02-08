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

package cn.zhengcaiyun.idata.develop.dto.query;

import cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum;

/**
 * @description: 维度列
 * @author: yangjianhua
 * @create: 2021-07-21 16:29
 **/
public class DimColumnDto {
    /**
     * 表名
     */
    private String tableName;
    /**
     * 表别名
     */
    private String tableAlias;
    /**
     * 字段名
     */
    private String columnName;
    /**
     * 字段别名
     */
    private String columnAlias;
    /**
     * 数据类型
     */
    private WideDataTypeEnum dataType;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnAlias() {
        return columnAlias;
    }

    public void setColumnAlias(String columnAlias) {
        this.columnAlias = columnAlias;
    }

    public WideDataTypeEnum getDataType() {
        return dataType;
    }

    public void setDataType(WideDataTypeEnum dataType) {
        this.dataType = dataType;
    }
}
