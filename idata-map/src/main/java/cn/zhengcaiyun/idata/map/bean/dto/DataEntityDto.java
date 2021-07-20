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

package cn.zhengcaiyun.idata.map.bean.dto;

import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @description: 数据实体dto
 * @author: yangjianhua
 * @create: 2021-07-14 13:55
 **/
public class DataEntityDto implements Comparable<DataEntityDto> {
    /**
     * 数据实体code，唯一编号
     */
    private String entityCode;
    /**
     * 数据实体名称
     */
    private String entityName;
    /**
     * 数据实体英文名称
     */
    private String entityNameEn;
    /**
     * 数据实体所属类别：资产目录的code or 业务过程的code
     */
    private String categoryCode;
    /**
     * 数据实体所属类别：资产目录名称 or 业务过程名称
     */
    private String categoryName;
    /**
     * 数据实体 metabase url
     */
    private String metabaseUrl;
    /**
     * 浏览次数
     */
    private Long viewCount = 0L;
    /**
     * 更多属性
     */
    private final Map<String, Object> moreAttrs = Maps.newHashMap();

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityNameEn() {
        return entityNameEn;
    }

    public void setEntityNameEn(String entityNameEn) {
        this.entityNameEn = entityNameEn;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getMetabaseUrl() {
        return metabaseUrl;
    }

    public void setMetabaseUrl(String metabaseUrl) {
        this.metabaseUrl = metabaseUrl;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount == null ? 0L : viewCount;
    }

    public Map<String, Object> getMoreAttrs() {
        return moreAttrs;
    }

    public DataEntityDto putMoreAttr(String attrKey, Object attrVal) {
        this.moreAttrs.put(attrKey, attrVal);
        return this;
    }

    @Override
    public int compareTo(@NotNull DataEntityDto other) {
        int result = Long.compare(this.getViewCount(), other.getViewCount());
        if (result == 0)
            result = this.getEntityName().compareTo(other.getEntityName());
        if (result == 0)
            result = this.getEntityCode().compareTo(other.getEntityCode());
        return result;
    }
}
