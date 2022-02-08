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
package cn.zhengcaiyun.idata.label.dto.label.rule;

import java.util.Arrays;
import java.util.Objects;

/**
 * @description: 维度规则定义dto
 * @author: yangjianhua
 * @create: 2021-06-23 09:48
 **/
public class DimensionDefDto {
    /**
     * 维度code，取自指标系统
     */
    private String dimensionCode;

    /**
     * 维度名称
     */
    private String dimensionName;
    /**
     * 维度值
     */
    private String[] params;

    public String getDimensionCode() {
        return dimensionCode;
    }

    public void setDimensionCode(String dimensionCode) {
        this.dimensionCode = dimensionCode;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public String getDimensionName() {
        return dimensionName;
    }

    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DimensionDefDto that = (DimensionDefDto) o;
        return Objects.equals(dimensionCode, that.dimensionCode) && Arrays.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(dimensionCode);
        result = 31 * result + Arrays.hashCode(params);
        return result;
    }

    @Override
    public String toString() {
        return "DimensionDefDto{" +
                "dimensionCode='" + dimensionCode + '\'' +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}
