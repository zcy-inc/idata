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

import java.sql.Types;

public enum SqlDataTypeEnum {

    bitType(Types.BIT, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    tinyintType(Types.TINYINT, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Whole),
    smallintType(Types.SMALLINT, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Whole),
    integerType(Types.INTEGER, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Whole),
    bigintType(Types.BIGINT, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Whole),
    floatType(Types.FLOAT, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Decimal),
    realType(Types.REAL, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Decimal),
    doubleType(Types.DOUBLE, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Decimal),
    numericType(Types.NUMERIC, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Decimal),
    decimalType(Types.DECIMAL, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Decimal),
    charType(Types.CHAR, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    varcharType(Types.VARCHAR, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    longVarcharType(Types.LONGVARCHAR, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    dateType(Types.DATE, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Date),
    timeType(Types.TIME, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.DateTime),
    timestampType(Types.TIMESTAMP, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.DateTime),
    binaryType(Types.BINARY, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    varbinaryType(Types.VARBINARY, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Ignore),
    longVarbinaryType(Types.LONGVARBINARY, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Ignore),
    nullType(Types.NULL, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    otherType(Types.OTHER, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    java_objectType(Types.JAVA_OBJECT, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    distinctType(Types.DISTINCT, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    structType(Types.STRUCT, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    arrayType(Types.ARRAY, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    blobType(Types.BLOB, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Ignore),
    clobType(Types.CLOB, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Ignore),
    refType(Types.REF, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Ignore),
    dataLinkType(Types.DATALINK, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Ignore),
    booleanType(Types.BOOLEAN, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    rowIdType(Types.ROWID, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Ignore),
    ncharType(Types.NCHAR, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    nvarcharType(Types.NVARCHAR, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String),
    longNvarcharType(Types.LONGNVARCHAR, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Ignore),
    nclobType(Types.NCLOB, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Ignore),
    sqlXmlType(Types.SQLXML, cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.Ignore);

    private int sqlDataType;
    private final WideDataTypeEnum WideDataTypeEnum;

    SqlDataTypeEnum(int sqlType, WideDataTypeEnum WideDataTypeEnum) {
        this.sqlDataType = sqlDataType;
        this.WideDataTypeEnum = WideDataTypeEnum;
    }

    public int getSqlDataType() {
        return sqlDataType;
    }

    public WideDataTypeEnum getWideADataTypeEnum() {
        return WideDataTypeEnum;
    }

    public static WideDataTypeEnum toDataType(int sqlDataType) {
        for (SqlDataTypeEnum typeEnum : SqlDataTypeEnum.values()) {
            if (typeEnum.getSqlDataType() == sqlDataType) {
                return typeEnum.getWideADataTypeEnum();
            }
        }
        return cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum.String;
    }

}
