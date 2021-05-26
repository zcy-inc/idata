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
package cn.zhengcaiyun.idata.dto.dev.label;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author caizhedong
 * @date 2021-05-20 10:12
 */

public class EnumAttributeDto {
    private String enumAttrKey;
    @ApiModelProperty(value = "BOOLEAN | WHOLE | STRING | ENUM:enum_code(动态)")
    private String enumAttrType;
    private String enumAttrValue;

    // GaS
    public String getEnumAttrKey() {
        return enumAttrKey;
    }

    public void setEnumAttrKey(String enumAttrKey) {
        this.enumAttrKey = enumAttrKey;
    }

    public String getEnumAttrType() {
        return enumAttrType;
    }

    public void setEnumAttrType(String enumAttrType) {
        this.enumAttrType = enumAttrType;
    }

    public String getEnumAttrValue() {
        return enumAttrValue;
    }

    public void setEnumAttrValue(String enumAttrValue) {
        this.enumAttrValue = enumAttrValue;
    }
}
