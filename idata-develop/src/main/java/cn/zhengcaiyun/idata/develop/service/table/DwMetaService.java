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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author caizhedong
 * @date 2021-08-10 19:20
 */

@Service
public class DwMetaService implements InitializingBean, DisposableBean {

    private HikariDataSource dwMetaDatasource;
    private JdbcTemplate dwMetaJdbcTemplate;

    @Value("${pg.datasource.jdbcUrl}")
    private String jdbcUrl;
    @Value("${pg.datasource.username}")
    private String username;
    @Value("${pg.datasource.password}")
    private String password;
    @Value("${pg.datasource.maximumPoolSize:#{null}}")
    private Integer maximumPoolSize;
    @Value("${pg.datasource.minimumIdle:#{null}}")
    private Integer minimumIdle;
    @Value("${pg.datasource.idleTimeout:#{null}}")
    private Long idleTimeout;

    @Override
    public void destroy() throws Exception {
        if (dwMetaDatasource != null) {
            dwMetaDatasource.close();
            dwMetaDatasource = null;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        if (maximumPoolSize != null) {
            hikariConfig.setMaximumPoolSize(maximumPoolSize);
        }
        if (minimumIdle != null) {
            hikariConfig.setMinimumIdle(minimumIdle);
        }
        if (idleTimeout != null) {
            hikariConfig.setIdleTimeout(idleTimeout);
        }
        this.dwMetaDatasource = new HikariDataSource(hikariConfig);
        this.dwMetaJdbcTemplate = new JdbcTemplate(dwMetaDatasource);
    }

    public List<String> getTableFolders(Long parentFolderId) {
        List<String> tableFolderList = new ArrayList<>();
        String folderSql = "select idata.table_folder.id, idata.table_folder.folder_name " +
                "from idata.table_folder " +
                "where idata.table_folder.del = false and idata.table_folder.parent_id = " + parentFolderId + " order by id";
        dwMetaJdbcTemplate.queryForList(folderSql).stream().forEach(record -> tableFolderList.add((String) record.get("folder_name")));
        return tableFolderList;
    }

    public List<String> getTableByFolderId(Long folderId) {
        List<String> tableNameList = new ArrayList<>();
        String tableSql = "select idata.table_info.tbl_name from idata.table_info " +
                "where idata.table_info.del = false and folder_id = " + folderId;
        dwMetaJdbcTemplate.queryForList(tableSql).stream().forEach(record -> tableNameList.add((String) record.get("tbl_name")));
        return tableNameList;
    }

    public List<Map<String, Object>> getTables(Long tableId) {
        String tableSql;
        if (tableId != null) {
            tableSql = "select idata.table_info.* from idata.table_info " +
                    "where idata.table_info.del = false and id = " + tableId;
        }
        else {
            tableSql = "select idata.table_info.* from idata.table_info " +
                    "where idata.table_info.del = false order by id";
        }
        return dwMetaJdbcTemplate.queryForList(tableSql);
    }

    public List<Map<String, Object>> getColumns() {
        String columnSql = "select  idata.column_info.* " +
                "from idata.column_info " +
                "where idata.column_info.del = false order by id";
        return dwMetaJdbcTemplate.queryForList(columnSql);
    }

    public List<Map<String, Object>> getForeignKeys(Long tableId) {
        String foreignKeySql;
        if (tableId != null) {
            foreignKeySql = "select idata.foreign_key.* " +
                    "from idata.foreign_key " +
                    "where idata.foreign_key.del = false and table_id = " + tableId;
        }
        else {
            foreignKeySql = "select idata.foreign_key.* " +
                    "from idata.foreign_key " +
                    "where idata.foreign_key.del = false order by id";
        }
        return dwMetaJdbcTemplate.queryForList(foreignKeySql);
    }

    public Map<String, String> getUsers() {
        Map<String, String> userMap = new HashMap<>();
        String userSql = "select idata.idata_user.id, idata.idata_user.nick_name " +
                "from idata.idata_user " +
                "where idata.idata_user.del = false order by id";
        dwMetaJdbcTemplate.queryForList(userSql).stream().forEach(record -> {
            userMap.put(record.get("id").toString(), (String) record.get("nick_name")) ;
        });
        return userMap;
    }

    public Map<String, String> getDomains() {
        Map<String, String> domainMap = new HashMap<>();
        String domainSql = "select idata.data_domain.id, idata.data_domain.domain_name " +
                "from idata.data_domain " +
                "where idata.data_domain.del = false order by id";
        dwMetaJdbcTemplate.queryForList(domainSql).stream().forEach(record -> {
            domainMap.put(record.get("id").toString(), (String) record.get("domain_name")) ;
        });
        return domainMap;
    }

    public Map<String, String> getBizProcesses() {
        Map<String, String> bizProcessMap = new HashMap<>();
        String bizProcessSql = "select idata.business_process.id, idata.business_process.cn_name " +
                "from idata.business_process " +
                "where idata.business_process.del = false order by id";
        dwMetaJdbcTemplate.queryForList(bizProcessSql).stream().forEach(record -> {
            bizProcessMap.put(record.get("id").toString(), (String) record.get("cn_name")) ;
        });
        return bizProcessMap;
    }

    public List<String> getColumnName(List<Long> columnIds) {
        String selectSql = "select idata.column_info.col_name " +
                "from idata.column_info " +
                "where idata.column_info.id in " + columnIds + " order by id";
        return dwMetaJdbcTemplate.queryForList(selectSql)
                .stream().map(record -> (String) record.get("col_name")).collect(Collectors.toList());
    }

    public Map<String, String> getBizProcessNames() {
        Map<String, String> bizProcessMap = new HashMap<>();
        String biProcessSql = "select cn_name, en_name from idata.business_process " +
                "where del = false order by id";
        dwMetaJdbcTemplate.queryForList(biProcessSql).stream().forEach(record -> {
            bizProcessMap.put((String) record.get("en_name"), (String) record.get("cn_name")) ;
        });
        return bizProcessMap;
    }

    public List<Map<String, Object>> getDimensions() {
        String dimensionSql = "select idata.dimension.* " +
                "from idata.dimension " +
                "where idata.dimension.del = false order by id";
        return dwMetaJdbcTemplate.queryForList(dimensionSql);
    }

    public List<Map<String, Object>> getModifiers() {
        String modifierSql = "select idata.modifier.* " +
                "from idata.modifier " +
                "where idata.modifier.del = false order by id";
        return dwMetaJdbcTemplate.queryForList(modifierSql);
    }

    public Map<String, List<String>> getModifierTypeIdMap() {
        Map<String, List<String>> modifierTypeIdMap = new HashMap<>();
        String modifierSql = "select idata.modifier.* " +
                "from idata.modifier " +
                "where idata.modifier.del = false order by id";
        List<Map<String, Object>> modifierList = dwMetaJdbcTemplate.queryForList(modifierSql);
        Map<String, List<Map<String, Object>>> modifierMap = modifierList.stream()
                .collect(Collectors.groupingBy(record -> record.get("type_id").toString()));
        modifierMap.forEach((key, value) -> {
            List<String> modifierIdList = value.stream().map(r -> r.get("id").toString()).collect(Collectors.toList());
            modifierTypeIdMap.put(key, modifierIdList);
        });
        return modifierTypeIdMap;
    }

    public List<Map<String, Object>> getModifierTypes() {
        String modifierTypeSql = "select idata.modifier_type.* " +
                "from idata.modifier_type " +
                "where idata.modifier_type.del = false order by id";
        return dwMetaJdbcTemplate.queryForList(modifierTypeSql);
    }

    public List<Map<String, Object>> getMetrics(String metricType) {
        String metricSql = "select idata.metric.* " +
                "from idata.metric " +
                "where idata.metric.del = false and idata.metric.metric_type = '" + metricType + "'" + " order by id";
        return dwMetaJdbcTemplate.queryForList(metricSql);
    }

    public Map<String, List<Map<String, Object>>> getColumnRoles(String roleType) {
        String columnRoleSql = "select idata.column_role.* " +
                "from idata.column_role " +
                "where idata.column_role.del = false and role_type = '" + roleType + "'" + " order by id";
        List<Map<String, Object>> columnRoleList = dwMetaJdbcTemplate.queryForList(columnRoleSql);
        return columnRoleList.stream()
                .collect(Collectors.groupingBy(record -> record.get("role_id").toString()));
    }

}
