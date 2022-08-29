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

package cn.zhengcaiyun.idata.develop.dto.job.di;

import cn.zhengcaiyun.idata.develop.dal.model.job.DIStreamJobContent;
import cn.zhengcaiyun.idata.develop.dto.job.JobContentBaseDto;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-08-16 17:49
 **/
public class DIStreamJobContentDto extends JobContentBaseDto {
    /**
     * 数据来源-数据源类型
     */
    private String srcDataSourceType;
    /**
     * 数据来源-数据源id
     */
    private Long srcDataSourceId;
    /**
     * 数据去向-数据源类型
     */
    private String destDataSourceType;
    /**
     * 数据去向-数据源id
     */
    private Long destDataSourceId;
    /**
     * 是否开启分表支持，0：否，1：是
     */
    private Integer enableSharding;

    private List<DIStreamJobTableDto> tableDtoList;

    public String getSrcDataSourceType() {
        return srcDataSourceType;
    }

    public void setSrcDataSourceType(String srcDataSourceType) {
        this.srcDataSourceType = srcDataSourceType;
    }

    public Long getSrcDataSourceId() {
        return srcDataSourceId;
    }

    public void setSrcDataSourceId(Long srcDataSourceId) {
        this.srcDataSourceId = srcDataSourceId;
    }

    public String getDestDataSourceType() {
        return destDataSourceType;
    }

    public void setDestDataSourceType(String destDataSourceType) {
        this.destDataSourceType = destDataSourceType;
    }

    public Long getDestDataSourceId() {
        return destDataSourceId;
    }

    public void setDestDataSourceId(Long destDataSourceId) {
        this.destDataSourceId = destDataSourceId;
    }

    public Integer getEnableSharding() {
        return enableSharding;
    }

    public void setEnableSharding(Integer enableSharding) {
        this.enableSharding = enableSharding;
    }

    public List<DIStreamJobTableDto> getTableDtoList() {
        return tableDtoList;
    }

    public void setTableDtoList(List<DIStreamJobTableDto> tableDtoList) {
        this.tableDtoList = tableDtoList;
    }

    public static DIStreamJobContentDto from(DIStreamJobContent content) {
        DIStreamJobContentDto dto = new DIStreamJobContentDto();
        BeanUtils.copyProperties(content, dto);
        return dto;
    }

    public DIStreamJobContent toModel() {
        DIStreamJobContent model = new DIStreamJobContent();
        BeanUtils.copyProperties(this, model);
        model.setCdcConfig("");
        return model;
    }
}
