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
package cn.zhengcaiyun.idata.dto.dev.label;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

/**
 * @author caizhedong
 * @date 2021-05-18 18:46
 */

public class LabelDefineDto {
    private Long id;
    private Integer del;
    private String creator;
    private Date createTime;
    private String editor;
    private Date editTime;
    private String labelCode;
    private String labelName;
    @ApiModelProperty(value = "STRING_LABEL | BOOLEAN_LABEL | USER_LABEL | ENUM_LABEL | ENUM_VALUE_LABEL " +
            "| ATTRIBUTE_LABEL " +
            "| DIMENSION_LABEL | ATOMIC_METRIC_LABEL | DERIVE_METRIC_LABEL | COMPLEX_METRIC_LABEL | MODIFIER_LABEL " +
            "| DIMENSION_LABEL_DISABLE | ATOMIC_METRIC_LABEL_DISABLE | DERIVE_METRIC_LABEL_DISABLE " +
            "| COMPLEX_METRIC_LABEL_DISABLE | MODIFIER_LABEL_DISABLE")
    private LabelTagEnum labelTag;
    @ApiModelProperty(value = "BOOLEAN | WHOLE | STRING | ENUM:enum_code(动态) | ENUM")
    private String labelParamType;
    private List<LabelSpecialAttributeDto> specialParams;
    private List<LabelAttributeDto> labelAttributes;
    @ApiModelProperty(value = "TABLE | COLUMN")
    private String subjectType;
    private Integer labelIndex;
    private Boolean labelRequired;
    private Long labelScope;
    private Long folderId;

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

    public LabelTagEnum getLabelTag() {
        return labelTag;
    }

    public void setLabelTag(LabelTagEnum labelTag) {
        this.labelTag = labelTag;
    }

    public String getLabelParamType() {
        return labelParamType;
    }

    public void setLabelParamType(String labelParamType) {
        this.labelParamType = labelParamType;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public Integer getLabelIndex() {
        return labelIndex;
    }

    public void setLabelIndex(Integer labelIndex) {
        this.labelIndex = labelIndex;
    }

    public Boolean getLabelRequired() {
        return labelRequired;
    }

    public void setLabelRequired(Boolean labelRequired) {
        this.labelRequired = labelRequired;
    }

    public Long getLabelScope() {
        return labelScope;
    }

    public void setLabelScope(Long labelScope) {
        this.labelScope = labelScope;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public List<LabelSpecialAttributeDto> getSpecialParams() {
        return specialParams;
    }

    public void setSpecialParams(List<LabelSpecialAttributeDto> specialParams) {
        this.specialParams = specialParams;
    }

    public List<LabelAttributeDto> getLabelAttributes() {
        return labelAttributes;
    }

    public void setLabelAttributes(List<LabelAttributeDto> labelAttributes) {
        this.labelAttributes = labelAttributes;
    }
}
