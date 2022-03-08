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

package cn.zhengcaiyun.idata.datasource.service.impl;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto;
import cn.zhengcaiyun.idata.connector.clients.hive.ConnectInfo;
import cn.zhengcaiyun.idata.connector.clients.hive.Jive;
import cn.zhengcaiyun.idata.connector.clients.hive.pool.HivePool;
import cn.zhengcaiyun.idata.connector.common.factory.JdbcTemplateFactory;
import cn.zhengcaiyun.idata.datasource.api.DataSourceApi;
import cn.zhengcaiyun.idata.datasource.bean.condition.DataSourceCondition;
import cn.zhengcaiyun.idata.datasource.bean.dto.DataSourceDto;
import cn.zhengcaiyun.idata.datasource.bean.dto.DbConfigDto;
import cn.zhengcaiyun.idata.datasource.dal.model.DataSource;
import cn.zhengcaiyun.idata.datasource.dal.repo.DataSourceRepo;
import cn.zhengcaiyun.idata.datasource.manager.DataSourceManager;
import cn.zhengcaiyun.idata.datasource.service.DataSourceService;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.nonNull;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-15 16:48
 **/
@Service
public class DataSourceServiceImpl implements DataSourceService {

    private final DataSourceRepo dataSourceRepo;
    private final DataSourceManager dataSourceManager;

    @Autowired
    private DataSourceApi dataSourceApi;


    @Autowired
    public DataSourceServiceImpl(DataSourceRepo dataSourceRepo,
                                 DataSourceManager dataSourceManager) {
        this.dataSourceRepo = dataSourceRepo;
        this.dataSourceManager = dataSourceManager;
    }

    @Override
    public Page<DataSourceDto> pagingDataSource(DataSourceCondition condition, PageParam pageParam) {
        Page<DataSource> page = dataSourceRepo.pagingDataSource(condition, pageParam);
        if (ObjectUtils.isEmpty(page.getContent())) {
            return Page.empty();
        }
        return Page.newOne(
                page.getContent().stream()
                        .map(DataSourceDto::from)
                        .collect(Collectors.toList()),
                page.getTotal());
    }

    @Override
    public DataSourceDto getDataSource(Long id) {
        checkArgument(nonNull(id), "数据源编号为空");
        Optional<DataSource> optional = dataSourceRepo.queryDataSource(id);
        checkArgument(optional.isPresent(), "数据源不存在");
        DataSource source = optional.get();
        checkState(Objects.equals(DeleteEnum.DEL_NO.val, source.getDel()), "数据源已删除");
        return DataSourceDto.from(source);
    }

    @Override
    public Long addDataSource(DataSourceDto dto, Operator operator) {
        checkDataSource(dto);
        List<DataSource> dupNameDataSources = dataSourceRepo.queryDataSource(dto.getName());
        checkArgument(ObjectUtils.isEmpty(dupNameDataSources), "数据源名称已存在");

        dto.setDel(DeleteEnum.DEL_NO.val);
        dto.setOperator(operator);
        Long id = dataSourceRepo.createDataSource(dto.toModel());
        checkState(nonNull(id), "创建数据源失败");
        return id;
    }

    @Override
    public Boolean editDataSource(DataSourceDto dto, Operator operator) {
        checkArgument(nonNull(dto.getId()), "数据源编号为空");
        checkDataSource(dto);

        Optional<DataSource> optional = dataSourceRepo.queryDataSource(dto.getId());
        checkArgument(optional.isPresent(), "数据源不存在");
        DataSource source = optional.get();
        checkState(Objects.equals(DeleteEnum.DEL_NO.val, source.getDel()), "数据源已删除");
        if (!Objects.equals(source.getName(), dto.getName())) {
            List<DataSource> dupNameDataSources = dataSourceRepo.queryDataSource(dto.getName());
            checkArgument(ObjectUtils.isEmpty(dupNameDataSources), "数据源名称已存在");
        }

        dto.setEditor(operator.getNickname());
        dataSourceRepo.updateDataSource(dto.toModel());
        return Boolean.TRUE;
    }

    @Override
    public Boolean removeDataSource(Long id, Operator operator) {
        checkArgument(nonNull(id), "数据源编号为空");
        Optional<DataSource> optional = dataSourceRepo.queryDataSource(id);
        checkArgument(optional.isPresent(), "数据源不存在");
        DataSource source = optional.get();
        if (Objects.equals(DeleteEnum.DEL_YES.val, source.getDel())) return true;

        boolean isUsing = dataSourceManager.checkInUsing(DataSourceTypeEnum.getEnum(source.getType()).orElse(null),
                id);
        checkArgument(!isUsing, "数据源正在被使用，不能删除");
        dataSourceRepo.deleteDataSource(id, operator.getNickname());
        return true;
    }

    @Override
    public Boolean testConnection(DataSourceTypeEnum dataSourceType, DbConfigDto dto) {
        checkArgument(nonNull(dataSourceType), "数据源类型为空");
        if (!supportTestConnection(dataSourceType)) return false;

        checkArgument(nonNull(dto.getEnv()), "数据库所属环境为空");
        checkArgument(StringUtils.isNotBlank(dto.getHost()), "数据库地址为空");
        checkArgument(nonNull(dto.getPort()), "数据库端口为空");
        checkArgument(StringUtils.isNotBlank(dto.getDbName()), "数据库名称为空");

        if (DataSourceTypeEnum.mysql == dataSourceType
                || DataSourceTypeEnum.postgresql == dataSourceType
                || DataSourceTypeEnum.presto == dataSourceType) {
            checkArgument(StringUtils.isNotBlank(dto.getUsername()), "数据库用户名为空");
        }
        if (DataSourceTypeEnum.mysql == dataSourceType
                || DataSourceTypeEnum.postgresql == dataSourceType) {
            checkArgument(StringUtils.isNotBlank(dto.getPassword()), "数据库密码为空");
        }
        return dataSourceManager.testConnectionWithJDBC(dataSourceType, dto);
    }

    @Override
    public String[] getDBTableColumns(Long id, String tableName) {
        cn.zhengcaiyun.idata.datasource.api.dto.DataSourceDto dataSource = dataSourceApi.getDataSource(id);
        List<DbConfigDto> dbConfigList = dataSource.getDbConfigList();
        checkArgument(CollectionUtils.isNotEmpty(dbConfigList));

        DataSourceTypeEnum dataSourceType = dataSource.getType();
        DbConfigDto dbConfigDto = dbConfigList.get(0);
        String dbName = dbConfigDto.getDbName();
        String jdbc = String.format("jdbc:%s://%s:%s/%s", dataSourceType.name(), dbConfigDto.getHost(),
                dbConfigDto.getPort(), dbName);
        JdbcTemplate jdbcTemplate = JdbcTemplateFactory.getJdbcTemplate(jdbc, dbConfigDto.getUsername(), dbConfigDto.getPassword());

        String mysqlSchemaSQL = "SELECT a.table_comment,b.column_name,b.data_type,b.column_comment,b.column_key,b.extra,"
                + "b.numeric_precision,b.numeric_scale FROM information_schema.TABLES a join information_schema.COLUMNS b "
                + "on a.TABLE_NAME=b.table_name and a.TABLE_SCHEMA=b.TABLE_SCHEMA WHERE LOWER(a.TABLE_SCHEMA)='%s' and LOWER(a.table_name) = '%s'";
        String pgSchemaSQL ="SELECT a.attname as column_name, t.typname as data_type, b.description as column_comment, '' as column_key,'' as extra "
                + "FROM (select c.relname,c.oid,n.nspname from pg_class c left join pg_catalog.pg_namespace n on c.relnamespace = n.oid) u, pg_attribute a "
                + "LEFT JOIN pg_description b ON a.attrelid = b.objoid AND a.attnum = b.objsubid, pg_type t WHERE u.nspname = '%s' AND u.relname = '%s' "
                + "AND a.attnum > 0 AND a.attrelid = u.oid AND a.atttypid = t.oid ORDER BY a.attnum";
        String columnKey = "";
        String extra = "";
        StringBuilder columns = new StringBuilder();
        List<Map<String, Object>> schemaMapList;
        if (dataSourceType == DataSourceTypeEnum.mysql) {
            schemaMapList = jdbcTemplate.queryForList(String.format(mysqlSchemaSQL, dbName.toLowerCase(), tableName.toLowerCase()));
        } else {
            schemaMapList = jdbcTemplate.queryForList(String.format(pgSchemaSQL,
                    tableName.split("\\.")[0].toLowerCase(), tableName.split("\\.")[1].toLowerCase()));
        }
        for(Map<String, Object> schemaMap : schemaMapList) {
            columns.append(schemaMap.get("column_name")).append(",");
            if ("PRI".equalsIgnoreCase((String) schemaMap.get("column_key"))) {
                columnKey = (String) schemaMap.get("column_name");
            }
            if ("DEFAULT_GENERATED on update CURRENT_TIMESTAMP".equalsIgnoreCase((String) schemaMap.get("extra"))) {
                extra = (String) schemaMap.get("column_name");
            }
        }
        return new String[]{columns.substring(0, columns.length() - 1), columnKey, extra};
    }

    @Override
    public List<String> getTopics(Long dataSourceId) throws ExecutionException, InterruptedException {
        DataSourceDto dataSource = getDataSource(dataSourceId);
        String host = dataSource.getDbConfigList().get(0).getHost();
        Integer port = dataSource.getDbConfigList().get(0).getPort();

        Properties prop = new Properties();
        prop.put("bootstrap.servers", host + ":" + port);
        ListTopicsResult result = KafkaAdminClient.create(prop).listTopics();
        KafkaFuture<Set<String>> set = result.names();
        return new ArrayList(set.get());

//        return Arrays.asList(new String[]{"topic1", "topic2"});
    }

    @Override
    public List<String> getDbNames(Long id) {
        Jive jive = null;
        String jdbcUrl = getJdbcUrl(id);
        try {
            ConnectInfo connectInfo = new ConnectInfo();
            connectInfo.setJdbc(jdbcUrl);
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setTestOnBorrow(true);
            HivePool hivePool = new HivePool(config, connectInfo);
            jive = hivePool.getResource();
            return jive.getDbNameList();
        } finally {
            jive.close();
        }
    }

    @Override
    public List<String> getTableNames(Long id, String dbName) {
        Jive jive = null;
        String jdbcUrl = getJdbcUrl(id, dbName);
        try {
            ConnectInfo connectInfo = new ConnectInfo();
            connectInfo.setJdbc(jdbcUrl);
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setTestOnBorrow(true);
            HivePool hivePool = new HivePool(config, connectInfo);
            jive = hivePool.getResource();
            return jive.getTableNameList();
        } finally {
            jive.close();
        }
    }

    @Override
    public List<ColumnInfoDto> getTableColumns(Long id, String dbName, String tableName) {
        Jive jive = null;
        HivePool hivePool = null;
        String jdbcUrl = getJdbcUrl(id, dbName);
        try {
            ConnectInfo connectInfo = new ConnectInfo();
            connectInfo.setJdbc(jdbcUrl);
            GenericObjectPoolConfig config = new GenericObjectPoolConfig();
            config.setTestOnBorrow(true);
            hivePool = new HivePool(config, connectInfo);
            jive = hivePool.getResource();
            return jive.getColumnMetaInfo(dbName, tableName);
        } finally {
            jive.close();
            hivePool.close();
        }
    }

    private boolean supportTestConnection(DataSourceTypeEnum dataSourceType) {
        return DataSourceTypeEnum.mysql == dataSourceType
                || DataSourceTypeEnum.postgresql == dataSourceType
                || DataSourceTypeEnum.presto == dataSourceType
                || DataSourceTypeEnum.hive == dataSourceType;
    }

    private void checkDataSource(DataSourceDto dto) {
        checkArgument(nonNull(dto.getType()), "数据源类型为空");
        checkArgument(StringUtils.isNotBlank(dto.getName()), "数据源名称为空");
        checkArgument(ObjectUtils.isNotEmpty(dto.getEnvList()), "数据源环境为空");
        checkArgument(ObjectUtils.isNotEmpty(dto.getDbConfigList()), "数据库配置为空");
        checkArgument(Objects.equals(dto.getEnvList().size(), dto.getDbConfigList().size()), "数据库配置与环境不匹配");

        Map<EnvEnum, DbConfigDto> cfgMap = Maps.newHashMap();
        dto.getDbConfigList().forEach(cfg -> {
            checkArgument(nonNull(cfg.getEnv()), "数据库所属环境为空");
            if (DataSourceTypeEnum.hive != dto.getType()) {
                checkArgument(StringUtils.isNotBlank(cfg.getHost()), "数据库地址为空");
                checkArgument(nonNull(cfg.getPort()), "数据库端口为空");
            }

            if (DataSourceTypeEnum.mysql == dto.getType()
                    || DataSourceTypeEnum.postgresql == dto.getType()) {
                checkArgument(StringUtils.isNotBlank(cfg.getDbName()), "数据库名称为空");
                checkArgument(StringUtils.isNotBlank(cfg.getUsername()), "数据库用户名为空");
                checkArgument(StringUtils.isNotBlank(cfg.getPassword()), "数据库密码为空");
            }

            cfgMap.put(cfg.getEnv(), cfg);
        });
        dto.getEnvList().forEach(env -> {
            checkArgument(nonNull(cfgMap.get(env)), "数据库所属环境不正确");
        });
    }


    public String getJdbcUrl(Long id) {
        return getJdbcUrl(id, null);
    }

    public String getJdbcUrl(Long id, String dbName) {
        checkArgument(nonNull(id), "数据源编号为空");
        Optional<DataSource> optional = dataSourceRepo.queryDataSource(id);
        checkArgument(optional.isPresent(), "数据源不存在");
        DataSource source = optional.get();
        checkState(Objects.equals(DeleteEnum.DEL_NO.val, source.getDel()), "数据源已删除");
        cn.zhengcaiyun.idata.datasource.api.dto.DataSourceDto dataSource = cn.zhengcaiyun.idata.datasource.api.dto.DataSourceDto.from(source);

        checkArgument(Objects.nonNull(dataSource), String.format("数据源不存在, DataSourceId:%d", id));
        checkArgument(CollectionUtils.isNotEmpty(dataSource.getDbConfigList()), String.format("数据源配置缺失, DataSourceId:%d", id));
        DbConfigDto dbConfigDto = dataSource.getDbConfigList().get(0);

        DataSourceTypeEnum sourceTypeEnum = dataSource.getType();
        String host = dbConfigDto.getHost();
        Integer port = dbConfigDto.getPort();
        String schema = dbConfigDto.getSchema();
        String protocol = null;
        if (DataSourceTypeEnum.mysql == sourceTypeEnum) {
            protocol = "mysql";
        } else if (DataSourceTypeEnum.postgresql == sourceTypeEnum) {
            protocol = "postgresql";
        } else if (DataSourceTypeEnum.presto == sourceTypeEnum) {
            protocol = "presto";
        } else if (DataSourceTypeEnum.hive == sourceTypeEnum) {
            protocol = "hive2";
        }
        if (StringUtils.isEmpty(protocol)) return null;

        if (StringUtils.isNotEmpty(dbName)) {
            return String.format("jdbc:%s://%s:%d/%s", protocol, host, port, dbName);
        }
        return String.format("jdbc:%s://%s:%d/%s", protocol, host, port, schema);
    }
}
