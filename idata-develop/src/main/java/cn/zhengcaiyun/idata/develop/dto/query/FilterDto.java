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

package cn.zhengcaiyun.idata.develop.dto.query;

import cn.zhengcaiyun.idata.connector.constant.enums.WideDataTypeEnum;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-21 17:00
 **/
public class FilterDto extends ContinuousFilterDto {
    /**
     * 字段名称
     */
    private String columnName;
    /**
     * 数据类型
     */
    private WideDataTypeEnum dataType;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public WideDataTypeEnum getDataType() {
        return dataType;
    }

    public void setDataType(WideDataTypeEnum dataType) {
        this.dataType = dataType;
    }
}
