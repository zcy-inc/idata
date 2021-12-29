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
package cn.zhengcaiyun.idata.develop.dto.job;

import cn.zhengcaiyun.idata.develop.dto.table.ColumnDetailsDto;
import cn.zhengcaiyun.idata.develop.dto.table.ColumnInfoDto;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-12-22 下午4:55
 */

public class AutocompletionTipDto {
    private List<String> basicAutocompletionTips;
    private List<String> dbTableNames;
    private List<ColumnDetailsDto> columns;

    // GaS
    public List<String> getBasicAutocompletionTips() {
        return basicAutocompletionTips;
    }

    public void setBasicAutocompletionTips(List<String> basicAutocompletionTips) {
        this.basicAutocompletionTips = basicAutocompletionTips;
    }

    public List<String> getDbTableNames() {
        return dbTableNames;
    }

    public void setDbTableNames(List<String> dbTableNames) {
        this.dbTableNames = dbTableNames;
    }

    public List<ColumnDetailsDto> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDetailsDto> columns) {
        this.columns = columns;
    }
}
