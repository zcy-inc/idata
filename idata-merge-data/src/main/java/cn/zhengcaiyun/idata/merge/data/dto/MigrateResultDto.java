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

package cn.zhengcaiyun.idata.merge.data.dto;

import cn.zhengcaiyun.idata.merge.data.dal.model.MigrateResult;
import org.springframework.beans.BeanUtils;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-06 16:26
 **/
public class MigrateResultDto {

    private String migrateType;
    private String reason;
    private String data;

    public MigrateResultDto(String migrateType, String reason, String data) {
        this.migrateType = migrateType;
        this.reason = reason;
        this.data = data;
    }

    public String getMigrateType() {
        return migrateType;
    }

    public void setMigrateType(String migrateType) {
        this.migrateType = migrateType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public MigrateResult toModel() {
        MigrateResult result = new MigrateResult();
        BeanUtils.copyProperties(this, result);
        return result;
    }
}
