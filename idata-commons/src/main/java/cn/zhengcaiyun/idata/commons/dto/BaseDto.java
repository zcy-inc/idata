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

import java.util.Date;
import java.util.Objects;

/**
 * @description: 基础dto类，放置基本属性
 * @author: yangjianhua
 * @create: 2021-06-21 15:19
 **/
public class BaseDto {
    /**
     * 是否删除，0否，1是
     */
    private Integer del;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改者
     */
    private String editor;
    /**
     * 修改时间
     */
    private Date editTime;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseDto baseDto = (BaseDto) o;
        return Objects.equals(del, baseDto.del) && Objects.equals(creator, baseDto.creator) && Objects.equals(createTime, baseDto.createTime) && Objects.equals(editor, baseDto.editor) && Objects.equals(editTime, baseDto.editTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(del, creator, createTime, editor, editTime);
    }

    @Override
    public String toString() {
        return "BaseDto{" +
                "del=" + del +
                ", creator='" + creator + '\'' +
                ", createTime=" + createTime +
                ", editor='" + editor + '\'' +
                ", editTime=" + editTime +
                '}';
    }
}
