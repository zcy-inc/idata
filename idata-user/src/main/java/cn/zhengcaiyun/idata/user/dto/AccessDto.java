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
package cn.zhengcaiyun.idata.user.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author shiyin
 * @date 2021-03-24 13:49
 */
public class AccessDto {
    @ApiModelProperty(value = "R_DW_DESIGN_DIR | R_JOB_MANAGE_DIR | " +
            "R_RESOURCE_MANAGE_DIR | R_FUNCTION_MANAGE_DIR | " +
            "R_API_DEVELOP_DIR")
    private String resourceType;
    private String accessKey;
    // GaS
    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
}
