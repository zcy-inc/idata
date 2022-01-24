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
package cn.zhengcaiyun.idata.connector.constant.enums;

/**
 * presto类型定义，及和WideADataTypeEnum的映射关系
 */
public enum PrestoDataTypeEnum {

    booleanType("boolean", cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    tinyintType("tinyint", cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Whole),
    smallintType("smallint", cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Whole),
    integerType("integer", cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Whole),
    bigintType("bigint", cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Whole),
    realType("real", cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Decimal),
    doubleType("double", cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Decimal),
    decimalType("decimal", cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Decimal),
    varcharType("varchar", cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    charType("char", cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    varbinaryType("varbinary", cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    jsonType("json", cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    dateType("date", cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Date),
    timeType("time", cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    timeWithTimeZoneType("time with time zone", cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    timestampType("timestamp", cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.DateTime),
    timestampWithTimeZoneType("timestamp with time zone", cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.DateTime),
    arrayType("array", cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    mapType("map", cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    rowType("row", cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String);

    private final String prestoDataType;
    private final WideDataTypeEnum WideDataTypeEnum;

    PrestoDataTypeEnum(String prestoDataType, WideDataTypeEnum WideDataTypeEnum) {
        this.prestoDataType = prestoDataType;
        this.WideDataTypeEnum = WideDataTypeEnum;
    }

    public String getPrestoDataType() {
        return prestoDataType;
    }

    public WideDataTypeEnum getWideADataTypeEnum() {
        return WideDataTypeEnum;
    }

    public static WideDataTypeEnum toDataType(String prestoDataType) {
        String type = prestoDataType;
        if (prestoDataType != null && prestoDataType.indexOf("(") > 0) {
            type = prestoDataType.substring(0, prestoDataType.indexOf("("));
        }
        for (PrestoDataTypeEnum typeEnum : PrestoDataTypeEnum.values()) {
            if (typeEnum.prestoDataType.equalsIgnoreCase(type)) {
                return typeEnum.getWideADataTypeEnum();
            }
        }
        return cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String;
    }
}
