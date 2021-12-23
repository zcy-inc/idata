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
package cn.zhengcaiyun.idata.commons.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-12-17 上午9:06
 */

public class BaseTreeNodeDto {
    private Long id;
    private String name;
    private Long parentId;
    @ApiModelProperty(value = "F_MENU | R_DATA_DEVELOP_DW_DIR | R_DATA_DEVELOP_DAG_DIR | " +
            "R_DATA_DEVELOP_DI_DIR | R_DATA_DEVELOP_DD_DIR")
    private String type;
    @ApiModelProperty(value = "F_MENU | FOLDER | RECORD")
    private String belong;
    private List<BaseTreeNodeDto> children;

    // GaS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<BaseTreeNodeDto> getChildren() {
        return children;
    }

    public void setChildren(List<BaseTreeNodeDto> children) {
        this.children = children;
    }
}
