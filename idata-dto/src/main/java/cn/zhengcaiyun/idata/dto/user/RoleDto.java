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
package cn.zhengcaiyun.idata.dto.user;

import cn.zhengcaiyun.idata.dto.system.FeatureTreeNodeDto;
import cn.zhengcaiyun.idata.dto.system.FolderTreeNodeDto;

import java.util.Date;
import java.util.List;

/**
 * @author shiyin
 * @date 2021-03-03 00:55
 */
public class RoleDto {
    private Long id;
    private Short del;
    private String creator;
    private Date createTime;
    private String editor;
    private Date editTime;
    private String roleCode;
    private String roleName;
    private List<FeatureTreeNodeDto> featureTree;
    private List<FolderTreeNodeDto> FolderTree;

    // GaS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getDel() {
        return del;
    }

    public void setDel(Short del) {
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

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<FeatureTreeNodeDto> getFeatureTree() {
        return featureTree;
    }

    public void setFeatureTree(List<FeatureTreeNodeDto> featureTree) {
        this.featureTree = featureTree;
    }

    public List<FolderTreeNodeDto> getFolderTree() {
        return FolderTree;
    }

    public void setFolderTree(List<FolderTreeNodeDto> folderTree) {
        FolderTree = folderTree;
    }
}
