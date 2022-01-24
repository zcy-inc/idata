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

package cn.zhengcaiyun.idata.develop.dto.query;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-21 16:59
 **/
public class ContinuousFilterDto extends DiscreteFilterDto {
    /**
     * 最小边界值
     */
    private String minBound;
    /**
     * 最大边界值
     */
    private String maxBound;
    /**
     * 不包含最小值
     */
    private Boolean excludeMin;
    /**
     * 不包含最大值
     */
    private Boolean excludeMax;

    public String getMinBound() {
        return minBound;
    }

    public void setMinBound(String minBound) {
        this.minBound = minBound;
    }

    public String getMaxBound() {
        return maxBound;
    }

    public void setMaxBound(String maxBound) {
        this.maxBound = maxBound;
    }

    public Boolean getExcludeMin() {
        return excludeMin;
    }

    public void setExcludeMin(Boolean excludeMin) {
        this.excludeMin = excludeMin;
    }

    public Boolean getExcludeMax() {
        return excludeMax;
    }

    public void setExcludeMax(Boolean excludeMax) {
        this.excludeMax = excludeMax;
    }
}
