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

package cn.zhengcaiyun.idata.mergedata.dto;

import com.alibaba.fastjson.JSONObject;

import java.util.Objects;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-10 15:43
 **/
public class JobMigrationDto {

    private Long oldJobId;
    private JSONObject oldJobInfo;
    private JSONObject oldJobConfig;
    private JSONObject oldJobContent;

    public Long getOldJobId() {
        return oldJobId;
    }

    public void setOldJobId(Long oldJobId) {
        this.oldJobId = oldJobId;
    }

    public JSONObject getOldJobInfo() {
        return oldJobInfo;
    }

    public void setOldJobInfo(JSONObject oldJobInfo) {
        this.oldJobInfo = oldJobInfo;
    }

    public JSONObject getOldJobConfig() {
        return oldJobConfig;
    }

    public void setOldJobConfig(JSONObject oldJobConfig) {
        this.oldJobConfig = oldJobConfig;
    }

    public JSONObject getOldJobContent() {
        return oldJobContent;
    }

    public void setOldJobContent(JSONObject oldJobContent) {
        this.oldJobContent = oldJobContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobMigrationDto that = (JobMigrationDto) o;
        return oldJobId.equals(that.oldJobId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldJobId);
    }
}
