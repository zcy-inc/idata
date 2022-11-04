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

import cn.zhengcaiyun.idata.commons.dto.general.KeyValuePair;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIStreamJobTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-08-16 17:48
 **/
public class DIStreamJobTableDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 作业id
     */
    private Long jobId;

    /**
     * 作业内容id
     */
    private Long jobContentId;

    /**
     * 作业内容版本号
     */
    private Integer jobContentVersion;

    /**
     * 数据来源-表
     */
    private String srcTable;

    /**
     * 数据去向-表
     */
    private String destTable;

    /**
     * 是否分表
     */
    private Integer sharding;

    /**
     * 是否强制初始化，0：否，1：是
     */
    private Integer forceInit;

    /**
     * 表cdc配置
     */
    private List<KeyValuePair<String, String>> tableCdcPropList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getJobContentId() {
        return jobContentId;
    }

    public void setJobContentId(Long jobContentId) {
        this.jobContentId = jobContentId;
    }

    public Integer getJobContentVersion() {
        return jobContentVersion;
    }

    public void setJobContentVersion(Integer jobContentVersion) {
        this.jobContentVersion = jobContentVersion;
    }

    public String getSrcTable() {
        return srcTable;
    }

    public void setSrcTable(String srcTable) {
        this.srcTable = srcTable;
    }

    public String getDestTable() {
        return destTable;
    }

    public void setDestTable(String destTable) {
        this.destTable = destTable;
    }

    public Integer getSharding() {
        return sharding;
    }

    public void setSharding(Integer sharding) {
        this.sharding = sharding;
    }

    public Integer getForceInit() {
        return forceInit;
    }

    public void setForceInit(Integer forceInit) {
        this.forceInit = forceInit;
    }

    public List<KeyValuePair<String, String>> getTableCdcPropList() {
        return tableCdcPropList;
    }

    public void setTableCdcPropList(List<KeyValuePair<String, String>> tableCdcPropList) {
        this.tableCdcPropList = tableCdcPropList;
    }

    public static DIStreamJobTableDto from(DIStreamJobTable streamJobTable) {
        DIStreamJobTableDto dto = new DIStreamJobTableDto();
        BeanUtils.copyProperties(streamJobTable, dto);

        String tableCdcProps = streamJobTable.getTableCdcProps();
        if (StringUtils.isNotBlank(tableCdcProps)) {
            List<KeyValuePair<String, String>> cdcPropList = new Gson().fromJson(tableCdcProps, new TypeToken<List<KeyValuePair<String, String>>>() {
            }.getType());
            dto.setTableCdcPropList(cdcPropList);
        }
        return dto;
    }

    public DIStreamJobTable toModel() {
        DIStreamJobTable model = new DIStreamJobTable();
        BeanUtils.copyProperties(this, model);

        if (CollectionUtils.isNotEmpty(this.tableCdcPropList)) {
            model.setTableCdcProps(new Gson().toJson(this.tableCdcPropList));
        } else {
            model.setTableCdcProps("");
        }
        // 暂无用，先设置为0
        model.setForceInit(0);
        return model;
    }
}
