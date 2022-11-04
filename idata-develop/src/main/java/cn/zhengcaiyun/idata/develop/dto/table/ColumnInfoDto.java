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
package cn.zhengcaiyun.idata.develop.dto.table;

import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;

import java.util.Date;
import java.util.List;

/**
 * @author caizhedong
 * @date 2021-05-18 18:44
 */

public class ColumnInfoDto {
    private Long id;
    private Integer del;
    private String creator;
    private Date createTime;
    private String editor;
    private Date editTime;
    private String columnName;
    private Long tableId;
    private Integer columnIndex;
    private List<LabelDto> columnLabels;
    private Boolean pk;
    private String columnComment;
    private String columnType;
    private String partitionedColumn;
    private boolean enableCompare;// 当此字段为ture时，isDiff字段有效
    private boolean isHiveDiff; // 字段和hive表是否存在不同
    private String columnAttributeCode; // 字段属性(维度或指标)

    // GaS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

    public List<LabelDto> getColumnLabels() {
        return columnLabels;
    }

    public void setColumnLabels(List<LabelDto> columnLabels) {
        this.columnLabels = columnLabels;
    }

    public Boolean getPk() {
        return pk;
    }

    public void setPk(Boolean pk) {
        this.pk = pk;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getPartitionedColumn() {
        return partitionedColumn;
    }

    public void setPartitionedColumn(String partitionedColumn) {
        this.partitionedColumn = partitionedColumn;
    }

    public boolean isEnableCompare() {
        return enableCompare;
    }

    public void setEnableCompare(boolean enableCompare) {
        this.enableCompare = enableCompare;
    }

    public boolean isHiveDiff() {
        return isHiveDiff;
    }

    public void setHiveDiff(boolean hiveDiff) {
        isHiveDiff = hiveDiff;
    }

    public String getColumnAttributeCode() {
        return columnAttributeCode;
    }

    public void setColumnAttributeCode(String columnAttributeCode) {
        this.columnAttributeCode = columnAttributeCode;
    }
}
