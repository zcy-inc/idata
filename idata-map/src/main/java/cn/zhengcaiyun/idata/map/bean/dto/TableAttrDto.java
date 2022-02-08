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

package cn.zhengcaiyun.idata.map.bean.dto;

import java.util.List;

/**
 * @description: 表属性
 * @author: yangjianhua
 * @create: 2021-07-14 15:05
 **/
public class TableAttrDto {
    /**
     * 字段信息列表
     */
    private List<ColumnAttrDto> columnAttrDtoList;
    /**
     * 安全等级
     */
    private String securityLevelCode;
    /**
     * 安全等级
     */
    private String securityLevelName;
    /**
     * 安全等级映射为高、中、低
     */
    private String securityLevelMapName;

    public List<ColumnAttrDto> getColumnAttrDtoList() {
        return columnAttrDtoList;
    }

    public void setColumnAttrDtoList(List<ColumnAttrDto> columnAttrDtoList) {
        this.columnAttrDtoList = columnAttrDtoList;
    }

    public String getSecurityLevelCode() {
        return securityLevelCode;
    }

    public void setSecurityLevelCode(String securityLevelCode) {
        this.securityLevelCode = securityLevelCode;
    }

    public String getSecurityLevelName() {
        return securityLevelName;
    }

    public void setSecurityLevelName(String securityLevelName) {
        this.securityLevelName = securityLevelName;
    }

    public String getSecurityLevelMapName() {
        return securityLevelMapName;
    }

    public void setSecurityLevelMapName(String securityLevelMapName) {
        this.securityLevelMapName = securityLevelMapName;
    }
}
