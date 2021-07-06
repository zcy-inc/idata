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
package cn.zhengcaiyun.idata.develop.dto.measure;

import cn.zhengcaiyun.idata.develop.dto.label.AttributeDto;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-05-26 10:52
 */
public class ModifierDto {
    private String modifierCode;
    private String modifierName;
    private String tableName;
    private String columnName;
    private List<String> enumValueCodes;
    private List<String> enumValues;
    private AttributeDto modifierAttribute;

    // GaS
    public String getModifierCode() {
        return modifierCode;
    }

    public void setModifierCode(String modifierCode) {
        this.modifierCode = modifierCode;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public List<String> getEnumValueCodes() {
        return enumValueCodes;
    }

    public void setEnumValueCodes(List<String> enumValueCodes) {
        this.enumValueCodes = enumValueCodes;
    }

    public List<String> getEnumValues() {
        return enumValues;
    }

    public void setEnumValues(List<String> enumValues) {
        this.enumValues = enumValues;
    }

    public AttributeDto getModifierAttribute() {
        return modifierAttribute;
    }

    public void setModifierAttribute(AttributeDto modifierAttribute) {
        this.modifierAttribute = modifierAttribute;
    }
}
