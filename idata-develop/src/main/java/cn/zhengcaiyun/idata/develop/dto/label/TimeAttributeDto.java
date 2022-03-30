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
package cn.zhengcaiyun.idata.develop.dto.label;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author caizhedong
 * @date 2022-03-24 下午4:58
 */

public class TimeAttributeDto {
    private Long tableId;
    private Long tableName;
    private Long columnId;
    private String columnName;
    @ApiModelProperty(value = "SEVEN_DAYS | THIRTY_DAS | ONE_MONTH | THREE_MONTHS | SIX_MONTHS |ONE_YEAR")
    private String timeDim;

    // GaS
    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Long getTableName() {
        return tableName;
    }

    public void setTableName(Long tableName) {
        this.tableName = tableName;
    }

    public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTimeDim() {
        return timeDim;
    }

    public void setTimeDim(String timeDim) {
        this.timeDim = timeDim;
    }
}
