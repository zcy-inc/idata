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

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-03-22 17:00
 **/
public class DITableFashionConfig {

    /**
     * 下拉选择：S、文本编辑：E
     */
    private String inputMode;
    /**
     * 表名
     */
    private String rawTable;
    /**
     * 分表区间开始
     */
    private Integer tableIdxBegin;
    /**
     * 分表区间结束
     */
    private Integer tableIdxEnd;

    public String getInputMode() {
        return inputMode;
    }

    public void setInputMode(String inputMode) {
        this.inputMode = inputMode;
    }

    public Integer getTableIdxBegin() {
        return tableIdxBegin;
    }

    public void setTableIdxBegin(Integer tableIdxBegin) {
        this.tableIdxBegin = tableIdxBegin;
    }

    public Integer getTableIdxEnd() {
        return tableIdxEnd;
    }

    public void setTableIdxEnd(Integer tableIdxEnd) {
        this.tableIdxEnd = tableIdxEnd;
    }

    public String getRawTable() {
        return rawTable;
    }

    public void setRawTable(String rawTable) {
        this.rawTable = rawTable;
    }

}
