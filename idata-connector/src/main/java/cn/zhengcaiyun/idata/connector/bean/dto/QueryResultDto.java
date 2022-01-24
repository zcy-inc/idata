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
package cn.zhengcaiyun.idata.connector.bean.dto;


import java.util.List;

/**
 * 查询数据结果dto
 */
public class QueryResultDto {
    /**
     * 查询结果字段信息
     */
    private List<ColumnInfoDto> meta;
    /**
     * 查询结果数据
     */
    private List<List<String>> data;
    /**
     * 总数
     */
    private Long total;

    public List<ColumnInfoDto> getMeta() {
        return meta;
    }

    public void setMeta(List<ColumnInfoDto> meta) {
        this.meta = meta;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
