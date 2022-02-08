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
 * @description: 指标规则定义dto
 * @author: yangjianhua
 * @create: 2021-06-23 09:43
 **/
public class IndicatorDefDto {
    /**
     * 指标code，从指标数据中取
     */
    private String indicatorCode;

    /**
     * 指标名称
     */
    private String indicatorName;

    /**
     * 条件:
     * 等于：equal，
     * 大于：greater，
     * 小于：less，
     * 大于等于：greaterOrEqual，
     * 小于等于：lessOrEqual，
     * 介于两个值之间：between
     */
    private String condition;
    /**
     * 如果condition是between，params大小为2，其他条件时params大小为1
     */
    private Long[] params;

    public String getIndicatorCode() {
        return indicatorCode;
    }

    public void setIndicatorCode(String indicatorCode) {
        this.indicatorCode = indicatorCode;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Long[] getParams() {
        return params;
    }

    public void setParams(Long[] params) {
        this.params = params;
    }

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndicatorDefDto that = (IndicatorDefDto) o;
        return indicatorCode.equals(that.indicatorCode) && condition.equals(that.condition) && Arrays.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(indicatorCode, condition);
        result = 31 * result + Arrays.hashCode(params);
        return result;
    }

    @Override
    public String toString() {
        return "IndicatorDefDto{" +
                "indicatorCode='" + indicatorCode + '\'' +
                ", condition='" + condition + '\'' +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}
