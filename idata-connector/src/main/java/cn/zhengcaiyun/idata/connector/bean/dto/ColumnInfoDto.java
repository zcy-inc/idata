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
package cn.zhengcaiyun.idata.connector.bean.dto;

import cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum;

/**
 * 字段信息
 */
public class ColumnInfoDto {

    private Long columnId;
    /**
     * 字段名称
     */
    private String columnName;
    /**
     * 字段别名
     */
    private String columnAlias;
    /**
     * 字段类型（数据库具体类型）
     */
    private String columnType;
    /**
     * 字段描述
     */
    private String columnComment;
    /**
     * 数据类型
     */
    private WideDataTypeEnum dataType;

    private Boolean isPartition;

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
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

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public WideDataTypeEnum getDataType() {
        return dataType;
    }

    public void setDataType(WideDataTypeEnum dataType) {
        this.dataType = dataType;
    }

    public Boolean getPartition() {
        return isPartition;
    }

    public void setPartition(Boolean partition) {
        isPartition = partition;
    }
}
