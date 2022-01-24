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
package cn.zhengcaiyun.idata.develop.dto.folder;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-05-18 19:20
 */

public class DevelopFolderTreeNodeDto {
    @ApiModelProperty(value = "TABLE | LABEL | ENUM")
    private String type;
    @ApiModelProperty(value = "T_table_id | E_enum_code | L_label_code | F_folder_id")
    private String cid;
    private String name;

    private Long folderId;
    private String fileCode;
    private String folderType;

    private Long parentId;
    private List<DevelopFolderTreeNodeDto> children;

    // GaS
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public String getFolderType() {
        return folderType;
    }

    public void setFolderType(String folderType) {
        this.folderType = folderType;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<DevelopFolderTreeNodeDto> getChildren() {
        return children;
    }

    public void setChildren(List<DevelopFolderTreeNodeDto> children) {
        this.children = children;
    }
}
