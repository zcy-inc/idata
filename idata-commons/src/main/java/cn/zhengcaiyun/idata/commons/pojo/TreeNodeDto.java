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

package cn.zhengcaiyun.idata.commons.pojo;

import cn.zhengcaiyun.idata.commons.util.TreeNodeGenerator;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 通用树节点类
 *
 * @description: 通用树节点类，业务节点可以继承该类，并给id，parentId赋值；使用TreeNodeHelper类生成树
 * 泛型 T 表示 id和parentId的类型，如Long or String 类型
 * 泛型 N 表示节点类型
 * @author: yangjianhua
 * @create: 2021-07-15 17:08
 * @see TreeNodeGenerator
 **/
public abstract class TreeNodeDto<T, N extends TreeNodeDto<T, N>> {
    /**
     * 节点唯一标识，生成方式为：type + belong + id
     */
    private String cid;
    /**
     * 父节点cid
     */
    private String parentCid;
    /**
     * 节点业务标识，不同类型节点的id可重复，相同类型节点id不可重复
     */
    private T id;
    /**
     * 节点名称
     */
    private String name;
    /**
     * 节点类型，如所有节点类型都相同，type可以为空
     */
    private String type;
    /**
     * 文件夹所属业务功能
     */
    private String belong;
    /**
     * 父节点标识
     */
    private T parentId;
    /**
     * 子节点列表
     */
    private List<N> children;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public T getParentId() {
        return parentId;
    }

    public void setParentId(T parentId) {
        this.parentId = parentId;
    }

    public List<N> getChildren() {
        return children;
    }

    public void setChildren(List<N> children) {
        if (canHasChildren())
            this.children = children;
    }

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

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public String getCid() {
        if (StringUtils.isEmpty(this.cid)) {
            this.cid = assembleCid(this.type, this.belong, this.id.toString());
        }
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getParentCid() {
        return parentCid;
    }

    public void setParentCid(String parentCid) {
        this.parentCid = parentCid;
    }

    /**
     * 部分类型的节点不能有子节点，如文件夹节点可以有子节点，但文件夹下的具体业务节点（如：数据标签节点）不能有子节点
     * 由具体业务节点实现
     *
     * @return
     */
    public abstract boolean canHasChildren();

    public static final String assembleCid(String type, String belong, String id) {
        // 生成方式为：type + belong + id
        String cidPrefix = "";
        if (StringUtils.isNotEmpty(type)) {
            cidPrefix += type + "_";
        }
        if (StringUtils.isNotEmpty(type)) {
            cidPrefix += belong + "_";
        }
        return cidPrefix + id;
    }
}
