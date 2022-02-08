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

package cn.zhengcaiyun.idata.label.compute.metadata;

import java.util.List;

/**
 * 指标信息
 *
 * @description: 数据来自指标系统
 * @author: yangjianhua
 * @create: 2021-06-25 09:35
 **/
public class IndicatorMetadata {
    private String code;
    private String name;
    private String table;
    private String column;
    private String function;
    private List<DecorateWordMetadata> decorateWords;

    /**
     * 条件:
     * 等于：equal，
     * 大于：greater，
     * 小于：less，
     * 大于等于：greaterOrEqual，
     * 小于等于：lessOrEqual，
     * 介于两个值之间：between
     */
    private String indicatorCondition;
    private Long[] indicatorParams;

    public static class DecorateWordMetadata {
        private String column;
        private List<String> params;

        public String getColumn() {
            return column;
        }

        public void setColumn(String column) {
            this.column = column;
        }

        public List<String> getParams() {
            return params;
        }

        public void setParams(List<String> params) {
            this.params = params;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getIndicatorCondition() {
        return indicatorCondition;
    }

    public void setIndicatorCondition(String indicatorCondition) {
        this.indicatorCondition = indicatorCondition;
    }

    public Long[] getIndicatorParams() {
        return indicatorParams;
    }

    public void setIndicatorParams(Long[] indicatorParams) {
        this.indicatorParams = indicatorParams;
    }

    public List<DecorateWordMetadata> getDecorateWords() {
        return decorateWords;
    }

    public void setDecorateWords(List<DecorateWordMetadata> decorateWords) {
        this.decorateWords = decorateWords;
    }
}
