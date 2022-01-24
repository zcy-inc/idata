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
package cn.zhengcaiyun.idata.develop.dto.table;

/**
 * @author caizhedong
 * @date 2021-08-26 下午3:03
 */

public enum ColumnTypeEnum {

    booleanType("BOOLEAN", DataTypeEnum.String),
    bigintType("BIGINT", DataTypeEnum.Whole),
    varcharType("STRING", DataTypeEnum.String),
    dateType("DATE", DataTypeEnum.Date),
    timestampType("TIMESTAMP", DataTypeEnum.DateTime),
    arrayBigintType("ARRAY<BIGINT>", DataTypeEnum.String),
    arrayDoubleType("ARRAY<DOUBLE>", DataTypeEnum.String),
    arrayTimestampType("ARRAY<TIMESTAMP>", DataTypeEnum.String),
    arrayDateType("ARRAY<DATE>", DataTypeEnum.String),
    arrayStringType("ARRAY<STRING>", DataTypeEnum.String);

    private String hiveColumnType;
    private DataTypeEnum dataType;

    ColumnTypeEnum(String hiveColumnType, DataTypeEnum dataType) {
        this.hiveColumnType = hiveColumnType;
        this.dataType = dataType;
    }

    public static DataTypeEnum toDataType(String prestoType) {
        if (prestoType != null && prestoType.indexOf("(") > 0) {
            prestoType = prestoType.substring(0, prestoType.indexOf("("));
        }
        for (ColumnTypeEnum t: ColumnTypeEnum.values()) {
            if (t.hiveColumnType.equalsIgnoreCase(prestoType)) {
                return t.dataType;
            }
        }
        return DataTypeEnum.String;
    }
}
