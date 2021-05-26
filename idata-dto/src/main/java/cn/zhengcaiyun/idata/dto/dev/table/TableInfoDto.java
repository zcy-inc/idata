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
package cn.zhengcaiyun.idata.dto.dev.table;

import cn.zhengcaiyun.idata.dto.dev.label.LabelDto;

import java.util.Date;
import java.util.List;

/**
 * @author caizhedong
 * @date 2021-05-18 18:39
 */

public class TableInfoDto {
    private Long id;
    private Integer del;
    private String creator;
    private Date createTime;
    private String editor;
    private Date editTime;
    private String tableName;
    private Long folderId;
    private List<LabelDto> tableLabels;
    private List<ColumnInfoDto> columnInfos;
    private List<ForeignKeyDto> foreignKeys;

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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public List<LabelDto> getTableLabels() {
        return tableLabels;
    }

    public void setTableLabels(List<LabelDto> tableLabels) {
        this.tableLabels = tableLabels;
    }

    public List<ColumnInfoDto> getColumnInfos() {
        return columnInfos;
    }

    public void setColumnInfos(List<ColumnInfoDto> columnInfos) {
        this.columnInfos = columnInfos;
    }

    public List<ForeignKeyDto> getForeignKeys() {
        return foreignKeys;
    }

    public void setForeignKeys(List<ForeignKeyDto> foreignKeys) {
        this.foreignKeys = foreignKeys;
    }
}
