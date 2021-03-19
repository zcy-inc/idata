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
package cn.zhengcaiyun.idata.dto.system;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author shiyin
 * @date 2021-03-08 09:39
 */
public class FolderTreeNodeDto {
    @ApiModelProperty(value = "MENU | DW_DEVELOP_DIR | JOB_DIR | FILE_RESOURCE_DIR | SPARK_UDF_DIR | DATAPI_DIR")
    private String type;
    private String name;
    private String featureCode;
    private String parentCode;
    private String folderId;
    private String parentId;
    private String accessCode;
    @ApiModelProperty(value = "二进制数字位表示：001 读，010 写，100 删，可以组合加和；十进制读写")
    private Short filePermission;
    private List<FolderTreeNodeDto> children;

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

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public List<FolderTreeNodeDto> getChildren() {
        return children;
    }

    public void setChildren(List<FolderTreeNodeDto> children) {
        this.children = children;
    }

    public Short getFilePermission() {
        return filePermission;
    }

    public void setFilePermission(Short filePermission) {
        this.filePermission = filePermission;
    }
}
