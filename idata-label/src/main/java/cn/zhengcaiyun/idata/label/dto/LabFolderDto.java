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

import java.util.Objects;

/**
 * @description: 文件夹dto类
 * @author: yangjianhua
 * @create: 2021-06-21 15:23
 **/
public class LabFolderDto extends BaseDto {
    /**
     * 主键
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 父文件夹编号，第一级文件夹父编号为0
     */
    private Long parentId;
    /**
     * 所属业务标识
     */
    private String belong;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LabFolderDto that = (LabFolderDto) o;
        return id.equals(that.id) && name.equals(that.name) && parentId.equals(that.parentId) && belong.equals(that.belong);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, name, parentId, belong);
    }

    @Override
    public String toString() {
        return "LabFolderDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", belong='" + belong + '\'' +
                "} " + super.toString();
    }
}
