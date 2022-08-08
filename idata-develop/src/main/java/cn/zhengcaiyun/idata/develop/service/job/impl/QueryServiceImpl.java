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
package cn.zhengcaiyun.idata.develop.service.job.impl;

import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.connector.spi.livy.LivyService;
import cn.zhengcaiyun.idata.connector.spi.livy.enums.LivySessionKindEnum;
import cn.zhengcaiyun.idata.connector.util.SparkSqlUtil;
import cn.zhengcaiyun.idata.connector.util.SqlDynamicParamTool;
import cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDao;
import cn.zhengcaiyun.idata.develop.dal.model.DevTableInfo;
import cn.zhengcaiyun.idata.develop.dto.job.AutocompletionTipDto;
import cn.zhengcaiyun.idata.develop.dto.job.QueryDto;
import cn.zhengcaiyun.idata.develop.dto.job.QueryRunResultDto;
import cn.zhengcaiyun.idata.develop.dto.job.QueryStatementDto;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.table.ColumnDetailsDto;
import cn.zhengcaiyun.idata.develop.dto.table.DbTableDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;
import cn.zhengcaiyun.idata.develop.service.job.QueryService;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import cn.zhengcaiyun.idata.develop.service.table.TableInfoService;
import cn.zhengcaiyun.idata.system.api.SystemConfigApi;
import cn.zhengcaiyun.idata.system.dto.ConfigDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDynamicSqlSupport.devTableInfo;
import static com.google.common.base.Preconditions.checkArgument;
import static org.mybatis.dynamic.sql.SqlBuilder.isNotEqualTo;

/**
 * @author caizhedong
 * @date 2021-11-23 下午8:10
 */

@Service
public class QueryServiceImpl implements QueryService {

    private final String DROP_QUERY = "DROP";
    private final String JOB_ENVIRONMENT = "prod";
    private final String AUTOCOMPLETION_KEY = "autocompletion-config";

    @Autowired
    private LivyService livyService;
    @Autowired
    private SystemConfigApi systemConfigApi;
    @Autowired
    private TableInfoService tableInfoService;
    @Autowired
    private ColumnInfoService columnInfoService;
    @Autowired
    private DevTableInfoDao devTableInfoDao;

    @Override
    public QueryStatementDto runQuery(QueryDto queryDto) {
        if (StringUtils.isBlank(queryDto.getQuerySource())) return null;
        checkArgument(!queryDto.getQuerySource().toUpperCase().contains(DROP_QUERY), "不能执行DROP操作");
        checkArgument(queryDto.getSessionKind() != null, "执行类型不能为空");
        if (LivySessionKindEnum.spark.equals(queryDto.getSessionKind())) {
            // sql调试默认参数替换
            queryDto.setQuerySource(SqlDynamicParamTool.replaceParam(queryDto.getQuerySource()));
        }
        return PojoUtil.copyOne(livyService.createStatement(queryDto), QueryStatementDto.class);
    }

    @Override
    public QueryRunResultDto runQueryResult(Integer sessionId, Integer statementId, String sessionKind,
                                            Integer from, Integer size) {
        checkArgument(LivySessionKindEnum.checkSessionKind(sessionKind), "Spark执行代码类型有误");
        QueryRunResultDto queryRunResult = PojoUtil.copyOne(livyService.queryResult(sessionId, statementId,
                LivySessionKindEnum.valueOf(sessionKind)), QueryRunResultDto.class);
        if (LivySessionKindEnum.spark.name().equals(sessionKind)) {
            queryRunResult.setQueryRunLog(livyService.queryLog(sessionId, from, size));
        }

        // 字段为null处理
        List<Map<String, Object>> resultSet = queryRunResult.getResultSet();
        if (resultSet != null && resultSet.size() > 0) {
            queryRunResult.setResultHeader(SparkSqlUtil.getSelectColumns(resultSet));
        }
        return queryRunResult;
    }

    @Override
    public AutocompletionTipDto getAutocompletionTipConfigs(String autocompletionType) {
        AutocompletionTipDto echo = new AutocompletionTipDto();

        ConfigDto baseAutocompletionTimConfig = systemConfigApi.getSystemConfigByKeyAndType(AUTOCOMPLETION_KEY, autocompletionType);
        if (baseAutocompletionTimConfig.getValueOne() == null
                || baseAutocompletionTimConfig.getValueOne().get(AUTOCOMPLETION_KEY) == null
                || baseAutocompletionTimConfig.getValueOne().get(AUTOCOMPLETION_KEY).getConfigValue() == null) {
            return echo;
        }
        String baseAutocompletionTimValue = baseAutocompletionTimConfig.getValueOne().get(AUTOCOMPLETION_KEY).getConfigValue();
        List<String> dbNameList = tableInfoService.getDbNames().stream().map(LabelDto::getLabelParamValue).collect(Collectors.toList());
        List<TableInfoDto> tableList = new ArrayList<>();
        dbNameList.forEach(dbName -> tableList.addAll(tableInfoService.getTablesByDataBase(dbName)));
        Map<Long, String> dbTableMap = tableList.stream().collect(Collectors.toMap(TableInfoDto::getId,
                tableInfo -> tableInfo.getDbName() + "." + tableInfo.getTableName()));
        List<DbTableDto> dbTableList = dbNameList.stream().map(dbName -> {
            DbTableDto dbTableDto = new DbTableDto();
            dbTableDto.setDbName(dbName);
            List<String> tableNameList = tableInfoService.getTablesByDataBase(dbName).stream().map(TableInfoDto::getTableName)
                    .collect(Collectors.toList());
            dbTableDto.setTableNames(tableNameList);
            return dbTableDto;
        }).collect(Collectors.toList());
        echo.setDbTableNames(dbTableList);
        List<Long> tableIdList = devTableInfoDao.select(c -> c.where(devTableInfo.del, isNotEqualTo(1)))
                .stream().map(DevTableInfo::getId).collect(Collectors.toList());
        List<ColumnDetailsDto> columnDetailsList = new ArrayList<>();
        tableIdList.forEach(tableId -> {
            columnDetailsList.addAll(PojoUtil.copyList(columnInfoService.getColumnDetails(tableId),
                    ColumnDetailsDto.class, "columnName", "columnType", "tableId"));
        });
        columnDetailsList.forEach(columnDetails -> {
            if (dbTableMap.containsKey(columnDetails.getTableId())) {
                columnDetails.setDbName(dbTableMap.get(columnDetails.getTableId()).split("\\.")[0]);
                columnDetails.setTableName(dbTableMap.get(columnDetails.getTableId()).split("\\.")[1]);
            }
        });
        echo.setColumns(columnDetailsList);
        echo.setBasicAutocompletionTips(Arrays.asList(baseAutocompletionTimValue.split(",")));
        return echo;
    }

    private String changeBizParseSql(String sourceSql) {
        String[] bizs = {"bizdate", "bizmonth", "bizquarter", "bizyear", "day", "dt"};
        List<String> bizList = Arrays.asList(bizs);
        Map<String, String> paramMap = getParamMap();
        sourceSql = sourceSql.replace("{0}", paramMap.get("day"));
        String[] sourceSqls = sourceSql.split("[${}]");
        List<String> sourceSqlList = Arrays.asList(sourceSqls);
        List<String> sqlList = new ArrayList<>();
        Map<Integer, String> bizChangeMap = new HashMap<>();
        for (int i = 0; i < sourceSqlList.size(); i++) {
            String sql = sourceSqlList.get(i);
            if ("".equals(sql)) {
                if (i < sourceSqlList.size() - 1) {
                    String nextSql = sourceSqlList.get(i + 1);
                    if (bizList.contains(nextSql)) {
                        bizChangeMap.put(i + 1, paramMap.get(nextSql));
                    }
                    else if (nextSql.contains("+") || nextSql.contains("-")) {
                        bizChangeMap.put(i + 1, getParsedDate(nextSql, paramMap));
                    }
                }
            }
        }
        for (int i = 0; i < sourceSqlList.size(); i++) {
            if (!"".equals(sourceSqlList.get(i))) {
                if (bizChangeMap.containsKey(i)) {
                    sqlList.add(bizChangeMap.get(i));
                }
                else {
                    sqlList.add(sourceSqlList.get(i));
                }
            }
        }
        return String.join("", sqlList);
    }

    private Map<String, String> getParamMap() {
        Map<String, String> paramMap = new HashMap<>();
        LocalDate localDate = LocalDate.now().plusDays(-1L);
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        String yesterday = yyyyMMdd.format(localDate);
        DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");
        String month = yyyyMM.format(localDate);
        DateTimeFormatter yyyy = DateTimeFormatter.ofPattern("yyyy");
        String year = yyyy.format(localDate);
        paramMap.put("bizdate", yesterday);
        paramMap.put("bizmonth", month);
        paramMap.put("bizquarter", yearQuarter(localDate));
        paramMap.put("bizyear", year);
        paramMap.put("day", yesterday);
        return paramMap;
    }

    private String yearQuarter(LocalDate localDate) {
        int month = localDate.getMonth().getValue();
        int quarter = (month + 2) / 3;
        int year = localDate.getYear();
        return year + "0" + quarter;
    }

    private static String getParsedDate(String exp, Map<String, String> param) {
        char unit = exp.charAt(exp.length() - 1);
        int index = StringUtils.indexOfAny(exp, "+", "-");
        int offset = NumberUtils.toInt(exp.substring(index, exp.length() - 1));
        String rKey = exp.substring(0, index);
        String dateValue = param.get(rKey);
        String format = dateValue.length() == 2 ? "HH" :
                dateValue.length() == 4 ? "HHmm" :
                        dateValue.length() == 8 ? "yyyyMMdd" : "yyyy-MM-dd";
        try {
            Date date = DateUtils.parseDate(dateValue, format);
            if (unit == 'Y' || unit == 'y') {
                date = DateUtils.addYears(date, offset);
            } else if (unit == 'M' || unit == 'm') {
                date = DateUtils.addMonths(date, offset);
            } else if (unit == 'D' || unit == 'd') {
                date = DateUtils.addDays(date, offset);
            } else if (unit == 'H' || unit == 'h') {
                date = DateUtils.addHours(date, offset);
            } else {
                date = DateUtils.addMinutes(date, offset);
            }
            return DateFormatUtils.format(date, format);
        } catch (Exception e) {
            throw new IllegalArgumentException("The timeExp " + exp + " is not support expression.");
        }
    }

}
