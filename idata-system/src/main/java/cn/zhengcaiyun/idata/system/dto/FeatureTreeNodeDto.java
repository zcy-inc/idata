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
package cn.zhengcaiyun.idata.system.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author shiyin
 * @date 2021-03-08 09:30
 */
public class FeatureTreeNodeDto {
    @ApiModelProperty(value = "F_MENU | F_ICON")
    private String type;
    private String name;
    private String featureCode;
    private String parentCode;
    private Boolean enable;
    private List<FeatureTreeNodeDto> children;

    // GaS
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public List<FeatureTreeNodeDto> getChildren() {
        return children;
    }

    public void setChildren(List<FeatureTreeNodeDto> children) {
        this.children = children;
    }
}
