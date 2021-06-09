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

import java.util.Date;

/**
 * @author caizhedong
 * @date 2021-05-18 19:05
 */
public class ForeignKeyDto {
    private Long id;
    private Integer del;
    private String creator;
    private Date createTime;
    private String editor;
    private Date editTime;
    private Long tableId;
    private String columnNames;
    private Long referTableId;
    private String referColumnNames;
    private String referDbName;
    private String erType;
    private String tableName;
    private String referTableName;

    // GaS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String columnNames) {
        this.columnNames = columnNames;
    }

    public String getReferDbName() {
        return referDbName;
    }

    public void setReferDbName(String referDbName) {
        this.referDbName = referDbName;
    }

    public Long getReferTableId() {
        return referTableId;
    }

    public void setReferTableId(Long referTableId) {
        this.referTableId = referTableId;
    }

    public String getReferColumnNames() {
        return referColumnNames;
    }

    public void setReferColumnNames(String referColumnNames) {
        this.referColumnNames = referColumnNames;
    }

    public String getErType() {
        return erType;
    }

    public void setErType(String erType) {
        this.erType = erType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getReferTableName() {
        return referTableName;
    }

    public void setReferTableName(String referTableName) {
        this.referTableName = referTableName;
    }
}
