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
package cn.zhengcaiyun.idata.label.dto;

import java.util.List;
import java.util.Objects;

/**
 * @description: 文件树
 * @author: yangjianhua
 * @create: 2021-06-21 16:08
 **/
public class LabFolderTreeNodeDto {
    /**
     * 文件夹或者文件id，可能会重复，通过cid做唯一标识
     */
    private Long id;
    /**
     * 文件夹或文件名称
     */
    private String name;
    /**
     * 树节点类型，文件夹：FOLDER，数据标签：LABEL
     *
     * @see cn.zhengcaiyun.idata.label.enums.FolderTreeNodeTypeEnum
     */
    private String type;
    /**
     * 节点唯一标识，生成方式为：type+id
     */
    private String cid;
    /**
     * 父节点id
     */
    private Long parentId;
    /**
     * 所属业务模块标识，数据标签为：lab
     */
    private String belong;
    /**
     * 子节点集合
     */
    private List<LabFolderTreeNodeDto> children;

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

    public List<LabFolderTreeNodeDto> getChildren() {
        return children;
    }

    public void setChildren(List<LabFolderTreeNodeDto> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LabFolderTreeNodeDto that = (LabFolderTreeNodeDto) o;
        return id.equals(that.id) && name.equals(that.name) && type.equals(that.type) && cid.equals(that.cid) && parentId.equals(that.parentId) && Objects.equals(belong, that.belong) && Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, cid, parentId, belong, children);
    }

    @Override
    public String toString() {
        return "LabFolderTreeNodeDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", cid='" + cid + '\'' +
                ", parentId=" + parentId +
                ", belong='" + belong + '\'' +
                ", children=" + children +
                '}';
    }
}
