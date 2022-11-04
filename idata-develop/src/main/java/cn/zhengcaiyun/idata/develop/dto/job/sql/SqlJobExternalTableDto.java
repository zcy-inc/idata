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

package cn.zhengcaiyun.idata.develop.dto.job.sql;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-07-07 16:30
 **/
public class SqlJobExternalTableDto {

    /**
     * 数据源类型
     */
    private String dataSourceType;
    /**
     * 数据源id
     */
    private Long dataSourceId;

    /**
     * 表配置
     */
    private List<ExternalTableInfo> tables;

    public String getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(String dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public Long getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(Long dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public List<ExternalTableInfo> getTables() {
        return tables;
    }

    public void setTables(List<ExternalTableInfo> tables) {
        this.tables = tables;
    }

    public static class ExternalTableInfo {
        /**
         * 表名
         */
        private String tableName;
        /**
         * 表别名
         */
        private String tableAlias;

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
        }

        public String getTableAlias() {
            return tableAlias;
        }

        public void setTableAlias(String tableAlias) {
            this.tableAlias = tableAlias;
        }
    }
}
