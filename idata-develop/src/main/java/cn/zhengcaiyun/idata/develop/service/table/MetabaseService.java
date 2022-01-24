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
package cn.zhengcaiyun.idata.develop.service.table;

import cn.zhengcaiyun.idata.develop.dto.table.ColumnDetailsDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableDetailDto;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * @author caizhedong
 * @date 2021-09-02 下午1:35
 */

@Service
public class MetabaseService implements InitializingBean, DisposableBean {

    private HikariDataSource metabaseDatasource;

    private JdbcTemplate metabaseJdbcTemplate;
    @Value("${metabase.datasource.jdbcUrl:#{null}}")
    private String jdbcUrl;
    @Value("${metabase.datasource.username:#{null}}")
    private String username;
    @Value("${metabase.datasource.password:#{null}}")
    private String password;
    @Value("${metabase.datasource.databaseName:#{null}}")
    private String databaseName;

    @Override
    public void destroy() throws Exception {
        if (metabaseDatasource != null) {
            metabaseDatasource.close();
            metabaseDatasource = null;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final HikariConfig hikariConfig = new HikariConfig();
        if (jdbcUrl != null && username != null && password != null) {
            hikariConfig.setJdbcUrl(jdbcUrl);
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);
            this.metabaseDatasource = new HikariDataSource(hikariConfig);
            this.metabaseJdbcTemplate = new JdbcTemplate(metabaseDatasource);
        }
    }

    /**
     * idata元数据同步metabase逻辑：
     * 表的schema和名称完全一致才会进行后续表或字段同步，字段名称完全一致才会进行字段中文名和描述的同步
     * 同步失败跳过当前表或字段并记录失败表名字段名，不影响其他字段同步
     */
    public Map<String, List<String>> syncMetabaseTableInfo(List<TableDetailDto> tablesList,
                                                           Map<String, List<ColumnDetailsDto>> syncMetabaseColMap) {
        Map<String, List<String>> syncTblsMap = new HashMap<>();
        List<String> syncSuccessColTblsList = new ArrayList<>();
        List<String> syncMissColTblsList = new ArrayList<>();
        List<String> syncMissColsList = new ArrayList<>();
        List<String> syncSuccessTblsList = new ArrayList<>();
        List<String> syncMissTblsList = new ArrayList<>();
        String tableId;
        String dbTblName;
        String dbName;
        String tblName;
        String colComment;
        List<Map<String, Object>> colTblQueryList;
        List<Map<String, Object>> colQueryList;
        List<Map<String, Object>> tblQueryList;
        List<ColumnDetailsDto> columnsList;
        String selectTblIdSql = "select id from metabase_table " +
                "where (db_id = (select id from metabase_database where name = '" + databaseName + "') " +
                "and schema = '%s' and name = '%s')";
        String selectColNameSql = "select name from metabase_field where (name = '%s' and table_id = %s)";
        String updateColInfoSql = "update metabase_field " +
                "set (updated_at, display_name, description) = (now(), '%s', '%s') " +
                "where (name = '%s' and table_id = %s)";
        String updateColDescriptionNullSql = "update metabase_field " +
                "set (updated_at, display_name, description) = (now(), '%s', null) " +
                "where (name = '%s' and table_id = %s)";
        String updateTblInfoSql = "update metabase_table " +
                "set (updated_at, display_name) = (now(), '%s')  " +
                "where (id = %s)";
        // 同步字段信息
        for (Map.Entry<String, List<ColumnDetailsDto>> entry : syncMetabaseColMap.entrySet()) {
            dbTblName = entry.getKey();
            dbName = dbTblName.split("\\.")[0];
            tblName = dbTblName.split("\\.")[1];
            columnsList = entry.getValue();
            boolean isColError = false;
            String getTblIdSql = String.format(selectTblIdSql, dbName, tblName);
            colTblQueryList = metabaseJdbcTemplate.queryForList(getTblIdSql);
            if (colTblQueryList.size() == 0) {
                isColError = true;
            }
            else {
                tableId = String.valueOf(colTblQueryList.get(0).get("id"));
                for (ColumnDetailsDto columnDetail : columnsList) {
                    String getColNameSql = String.format(selectColNameSql, columnDetail.getColumnName(), tableId);
                    colQueryList = metabaseJdbcTemplate.queryForList(getColNameSql);
                    if (colQueryList.size() == 0) {
                        isColError = true;
                        syncMissColsList.add(columnDetail.getColumnName());
                    }
                    else {
                        colComment = isNotEmpty(columnDetail.getColumnComment()) ? columnDetail.getColumnComment() : columnDetail.getColumnName();
                        String syncColInfoSql;
                        if (isEmpty(columnDetail.getColumnDescription())) {
                            syncColInfoSql = String.format(updateColDescriptionNullSql, colComment, columnDetail.getColumnName(), tableId);
                        }
                        else {
                            syncColInfoSql = String.format(updateColInfoSql, colComment, columnDetail.getColumnDescription(),
                                    columnDetail.getColumnName(), tableId);
                        }
                        metabaseJdbcTemplate.update(syncColInfoSql);
                    }
                }
            }
            if (isColError) {
                syncMissColTblsList.add(dbTblName);
            }
            else {
                syncSuccessColTblsList.add(dbTblName);
            }
        }

        // 同步表信息
        for (TableDetailDto tableDetail : tablesList) {
            boolean isTblError = false;
            String getTblIdSql = String.format(selectTblIdSql, tableDetail.getDbName(), tableDetail.getTableName());
            tblQueryList = metabaseJdbcTemplate.queryForList(getTblIdSql);
            if (tblQueryList.size() == 0) {
                isTblError = true;
            }
            else {
                String syncTblInfoSql = String.format(updateTblInfoSql, tableDetail.getTableComment(),
                        tblQueryList.get(0).get("id"));
                metabaseJdbcTemplate.update(syncTblInfoSql);
            }
            if (isTblError) {
                syncMissTblsList.add(tableDetail.getTableName());
            }
            else {
                syncSuccessTblsList.add(tableDetail.getTableName());
            }
        }

        syncTblsMap.put("syncSuccessColTbls", syncSuccessColTblsList);
        syncTblsMap.put("syncMissColTbls", syncMissColTblsList);
        syncTblsMap.put("syncMissCols", syncMissColsList);
        syncTblsMap.put("syncSuccessTbls", syncSuccessTblsList);
        syncTblsMap.put("syncMissTbls", syncMissTblsList);
        return syncTblsMap;
    }
}
