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

package cn.zhengcaiyun.idata.develop.manager;

import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.exception.ExecuteSqlException;
import cn.zhengcaiyun.idata.connector.api.MetadataQueryApi;
import cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto;
import cn.zhengcaiyun.idata.connector.bean.dto.QueryResultDto;
import cn.zhengcaiyun.idata.connector.connection.ConnectionCfg;
import cn.zhengcaiyun.idata.connector.constant.enums.DataSourceEnum;
import cn.zhengcaiyun.idata.connector.service.Query;
import cn.zhengcaiyun.idata.datasource.bean.dto.DbConfigDto;
import cn.zhengcaiyun.idata.develop.dto.query.TableDataQueryDto;
import cn.zhengcaiyun.idata.develop.dto.table.ColumnDetailsDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import cn.zhengcaiyun.idata.develop.service.table.TableInfoService;
import cn.zhengcaiyun.idata.system.dal.dao.SysConfigDao;
import cn.zhengcaiyun.idata.system.dal.model.SysConfig;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.system.dal.dao.SysConfigDynamicSqlSupport.sysConfig;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description: 目前各种写法，待重构连接模块
 * @author: yangjianhua
 * @create: 2021-07-21 17:50
 **/
@Component
public class TableQueryManager {
    private static final Logger logger = LoggerFactory.getLogger(TableQueryManager.class);

    private final SysConfigDao sysConfigDao;
    private final Query query;
    private final MetadataQueryApi metadataQueryApi;
    private final TableInfoService tableInfoService;
    private final ColumnInfoService columnInfoService;

    @Autowired
    public TableQueryManager(SysConfigDao sysConfigDao,
                             Query query,
                             MetadataQueryApi metadataQueryApi,
                             TableInfoService tableInfoService,
                             ColumnInfoService columnInfoService) {
        this.sysConfigDao = sysConfigDao;
        this.query = query;
        this.metadataQueryApi = metadataQueryApi;
        this.tableInfoService = tableInfoService;
        this.columnInfoService = columnInfoService;
    }

    public QueryResultDto queryData(TableDataQueryDto dataQueryDto) {
        ConnectionCfg connectionDto = getConnectionInfo();
        checkState(connectionDto != null, "数据源连接信息不正确");
        checkArgument(isNotEmpty(dataQueryDto.getDimensions()) || isNotEmpty(dataQueryDto.getMeasures()), "维度和度量不能同时为空");

        QueryResultDto resultDto;
        try {
            String selectSql = new PrestoDSL()
                    .select(dataQueryDto.getDimensions(), dataQueryDto.getMeasures(), dataQueryDto.getAggregate())
                    .from(dataQueryDto.getDbSchema() + "." + dataQueryDto.getTableName())
                    .where(dataQueryDto.getFilters())
                    .groupBy(dataQueryDto.getDimensions(), dataQueryDto.getAggregate())
                    .orderBy(dataQueryDto.getOrderBy())
                    .limit(dataQueryDto.getPageParam().getLimit(), dataQueryDto.getPageParam().getOffset()).toString();
            logger.info("selectSql : {}", selectSql);
            resultDto = query.query(connectionDto, selectSql);
        } catch (Exception ex) {
            throw new ExecuteSqlException("SQL执行失败", ex);
        }
        if (Objects.isNull(resultDto)) {
            return null;
        }
        // add columnAlias
        Map<String, Long> tableInfoMap = tableInfoService.getTablesByDataBase(dataQueryDto.getDbSchema().toLowerCase())
                .stream().collect(Collectors.toMap(TableInfoDto::getTableName, TableInfoDto::getId));
        checkArgument(tableInfoMap.containsKey(dataQueryDto.getTableName()), "数仓分层或表名有误");
        Map<String, String> columnNameAliasMap = columnInfoService.getColumnDetails(tableInfoMap.get(dataQueryDto.getTableName()))
                .stream().collect(Collectors.toMap(ColumnDetailsDto::getColumnName,
                        columnDetails -> StringUtils.isNotEmpty(columnDetails.getColumnComment())
                                ? columnDetails.getColumnComment() : columnDetails.getColumnName()));
        resultDto.getMeta().forEach(columnInfoDto ->
                columnInfoDto.setColumnAlias(columnNameAliasMap.containsKey(columnInfoDto.getColumnName())
                        ? columnNameAliasMap.get(columnInfoDto.getColumnName()) : columnInfoDto.getColumnName()));
        return resultDto;
    }

    public Long count(TableDataQueryDto dataQueryDto) {
        ConnectionCfg connectionDto = getConnectionInfo();
        checkState(connectionDto != null, "数据源连接信息不正确");
        checkArgument(isNotEmpty(dataQueryDto.getDimensions()) || isNotEmpty(dataQueryDto.getMeasures()), "维度和度量不能同时为空");
        QueryResultDto resultDto;

        String selectStr = (dataQueryDto.getAggregate() != null && dataQueryDto.getAggregate()) ?
                "select count(1)" : "select 1";
        try {
            StringBuilder countSql = new StringBuilder("select count(1) from (\n")
                    .append(new PrestoDSL().append(selectStr)
                            .from(dataQueryDto.getDbSchema() + "." + dataQueryDto.getTableName())
                            .where(dataQueryDto.getFilters())
                            .groupBy(dataQueryDto.getDimensions(), dataQueryDto.getAggregate()).toString())
                    .append(") t limit 1");
            logger.info("countSql : {}", countSql);
            resultDto = query.query(connectionDto, countSql.toString());
        } catch (Exception ex) {
            throw new ExecuteSqlException("SQL执行失败", ex);
        }
        if (Objects.isNull(resultDto)) {
            return null;
        }
        return Long.parseLong(resultDto.getData().get(0).get(0));
    }

    private ConnectionCfg getConnectionInfo() {
        SysConfig trinoConfig = sysConfigDao.selectOne(dsl -> dsl.where(sysConfig.keyOne, isEqualTo("trino-info")))
                .orElse(null);
        ConnectionCfg cfg = null;
        if (trinoConfig != null) {
            cfg = JSON.parseObject(trinoConfig.getValueOne(), ConnectionCfg.class);
            cfg.setDataSourceEnum(DataSourceEnum.PRESTO);
        }
        return cfg;
    }

    public List<String> getTableNames(DataSourceTypeEnum sourceTypeEnum, DbConfigDto configDto) {
        return metadataQueryApi.getTableNames(sourceTypeEnum, configDto.getHost(), configDto.getPort(),
                configDto.getUsername(), configDto.getPassword(), configDto.getDbName(), configDto.getSchema());
    }

    public List<ColumnInfoDto> getTableColumns(DataSourceTypeEnum sourceTypeEnum, DbConfigDto configDto, String tableName) {
        return metadataQueryApi.getTableColumns(sourceTypeEnum, configDto.getHost(), configDto.getPort(),
                configDto.getUsername(), configDto.getPassword(), configDto.getDbName(), configDto.getSchema(), tableName);
    }

    public List<ColumnInfoDto> getTablePrimaryKeys(DataSourceTypeEnum sourceTypeEnum, DbConfigDto configDto, String tableName) {
        return metadataQueryApi.getTablePrimaryKeys(sourceTypeEnum, configDto.getHost(), configDto.getPort(),
                configDto.getUsername(), configDto.getPassword(), configDto.getDbName(), configDto.getSchema(), tableName);
    }
}
