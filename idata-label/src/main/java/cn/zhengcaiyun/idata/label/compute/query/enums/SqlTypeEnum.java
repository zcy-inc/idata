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
package cn.zhengcaiyun.idata.label.compute.query.enums;

import java.sql.Types;

public enum SqlTypeEnum {

	bitType(Types.BIT, DataTypeEnum.String),
	tinyintType(Types.TINYINT, DataTypeEnum.Whole),
	smallintType(Types.SMALLINT, DataTypeEnum.Whole),
	integerType(Types.INTEGER, DataTypeEnum.Whole),
	bigintType(Types.BIGINT, DataTypeEnum.Whole),
	floatType(Types.FLOAT, DataTypeEnum.Decimal),
	realType(Types.REAL, DataTypeEnum.Decimal),
	doubleType(Types.DOUBLE, DataTypeEnum.Decimal),
	numericType(Types.NUMERIC, DataTypeEnum.Decimal),
	decimalType(Types.DECIMAL, DataTypeEnum.Decimal),
	charType(Types.CHAR, DataTypeEnum.String),
	varcharType(Types.VARCHAR, DataTypeEnum.String),
	longVarcharType(Types.LONGVARCHAR, DataTypeEnum.String),
	dateType(Types.DATE, DataTypeEnum.Date),
	timeType(Types.TIME, DataTypeEnum.DateTime),
	timestampType(Types.TIMESTAMP, DataTypeEnum.DateTime),
	binaryType(Types.BINARY, DataTypeEnum.String),
	varbinaryType(Types.VARBINARY, DataTypeEnum.Ignore),
	longVarbinaryType(Types.LONGVARBINARY, DataTypeEnum.Ignore),
	nullType(Types.NULL, DataTypeEnum.String),
	otherType(Types.OTHER, DataTypeEnum.String),
	java_objectType(Types.JAVA_OBJECT, DataTypeEnum.String),
	distinctType(Types.DISTINCT, DataTypeEnum.String),
	structType(Types.STRUCT, DataTypeEnum.String),
	arrayType(Types.ARRAY, DataTypeEnum.String),
	blobType(Types.BLOB, DataTypeEnum.Ignore),
	clobType(Types.CLOB, DataTypeEnum.Ignore),
	refType(Types.REF, DataTypeEnum.Ignore),
	dataLinkType(Types.DATALINK, DataTypeEnum.Ignore),
	booleanType(Types.BOOLEAN, DataTypeEnum.String),
	rowIdType(Types.ROWID, DataTypeEnum.Ignore),
	ncharType(Types.NCHAR, DataTypeEnum.String),
	nvarcharType(Types.NVARCHAR, DataTypeEnum.String),
	longNvarcharType(Types.LONGNVARCHAR, DataTypeEnum.Ignore),
	nclobType(Types.NCLOB, DataTypeEnum.Ignore),
	sqlXmlType(Types.SQLXML, DataTypeEnum.Ignore);
	
	private int sqlType;
	private DataTypeEnum dataType;
	
	SqlTypeEnum(int sqlType, DataTypeEnum dataType) {
		this.sqlType = sqlType;
		this.dataType = dataType;
	}

	public static DataTypeEnum toDataType(int sqlType) {
		for (SqlTypeEnum t: SqlTypeEnum.values()) {
			if (t.sqlType == sqlType) {
				return t.dataType;
			}
		}
		return DataTypeEnum.String;
	}
	
}
