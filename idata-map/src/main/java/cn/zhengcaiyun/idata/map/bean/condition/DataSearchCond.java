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

package cn.zhengcaiyun.idata.map.bean.condition;

import cn.zhengcaiyun.idata.map.constant.enums.EntitySourceEnum;

import java.util.List;

/**
 * 数据地图查询condition
 *
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-14 13:50
 **/
public class DataSearchCond {
    /**
     * 数据源：数仓表（table） or 数据指标（indicator）
     */
    private String source;
    /**
     * 数据所属类别唯一标识：资产目录的id or 业务过程的id
     */
    private String categoryId;
    /**
     * 类别类型：资产目录（property_dir） or 业务过程（biz_process）
     */
    private String categoryType;
    /**
     * 关键词，空格时认为存在多个关键字，最多1000个
     */
    private String keyWord;
    /**
     * 关键词
     */
    private List<String> keyWords;
    /**
     * 数仓分层，ADS，DWS，DWD，DIM，ODS，仅数仓表搜索使用
     */
    private String tableLayer;
    /**
     * 搜索范围选项，全部(all)、表(table)、字段(column)，仅数仓表搜索使用
     */
    private String tableSearchRange;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getTableLayer() {
        return tableLayer;
    }

    public void setTableLayer(String tableLayer) {
        this.tableLayer = tableLayer;
    }

    public String getTableSearchRange() {
        return tableSearchRange;
    }

    public void setTableSearchRange(String tableSearchRange) {
        this.tableSearchRange = tableSearchRange;
    }

    public List<String> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }

    public boolean searchFromTable() {
        return EntitySourceEnum.TABLE.getCode().equals(this.source);
    }

    public boolean searchFromIndicator() {
        return EntitySourceEnum.INDICATOR.getCode().equals(this.source);
    }
}
