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
package cn.zhengcaiyun.idata.develop.dto.label;

import java.util.Date;

/**
 * @author caizhedong
 * @date 2021-05-18 18:53
 */

public class LabelDto {
    private Long id;
    private Integer del;
    private String creator;
    private Date createTime;
    private String editor;
    private Date editTime;
    private String labelTag;
    private String labelCode;
    private String labelName;
    private Long tableId;
    private String columnName;
    private String labelParamType;
    private String labelParamValue;
    // 枚举名称或者枚举值字面值
    private String enumNameOrValue;

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

    public String getLabelTag() {
        return labelTag;
    }

    public void setLabelTag(String labelTag) {
        this.labelTag = labelTag;
    }

    public String getLabelCode() {
        return labelCode;
    }

    public void setLabelCode(String labelCode) {
        this.labelCode = labelCode;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getLabelParamType() {
        return labelParamType;
    }

    public void setLabelParamType(String labelParamType) {
        this.labelParamType = labelParamType;
    }

    public String getLabelParamValue() {
        return labelParamValue;
    }

    public void setLabelParamValue(String labelParamValue) {
        this.labelParamValue = labelParamValue;
    }

    public String getEnumNameOrValue() {
        return enumNameOrValue;
    }

    public void setEnumNameOrValue(String enumNameOrValue) {
        this.enumNameOrValue = enumNameOrValue;
    }
}
