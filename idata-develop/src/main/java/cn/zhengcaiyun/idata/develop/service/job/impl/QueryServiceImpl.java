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
import cn.zhengcaiyun.idata.connector.spi.hdfs.HdfsService;
import cn.zhengcaiyun.idata.connector.spi.livy.LivyService;
import cn.zhengcaiyun.idata.connector.spi.livy.enums.LivySessionKindEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.DevColumnInfoDao;
import cn.zhengcaiyun.idata.develop.dal.model.DevColumnInfo;
import cn.zhengcaiyun.idata.develop.dto.job.*;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;
import cn.zhengcaiyun.idata.develop.service.job.*;
import cn.zhengcaiyun.idata.develop.service.table.TableInfoService;
import cn.zhengcaiyun.idata.system.api.SystemConfigApi;
import cn.zhengcaiyun.idata.system.dto.ConfigDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevColumnInfoDynamicSqlSupport.devColumnInfo;
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
    private DevColumnInfoDao devColumnInfoDao;

    @Override
    public QueryStatementDto runQuery(QueryDto queryDto) {
        if (StringUtils.isBlank(queryDto.getQuerySource())) return null;
        checkArgument(!queryDto.getQuerySource().toUpperCase().contains(DROP_QUERY), "不能执行DROP操作");
        checkArgument(queryDto.getSessionKind() != null, "执行类型不能为空");
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
        return queryRunResult;
    }

    @Override
    public AutocompletionTipDto getAutocompletionTipConfigs() {
        AutocompletionTipDto echo = new AutocompletionTipDto();

        ConfigDto baseAutocompletionTimConfig = systemConfigApi.getSystemConfigByKey(AUTOCOMPLETION_KEY);
        String baseAutocompletionTimValue = baseAutocompletionTimConfig.getValueOne().get(AUTOCOMPLETION_KEY).getConfigValue();
        List<String> dbNameList = tableInfoService.getDbNames().stream().map(LabelDto::getLabelParamValue).collect(Collectors.toList());
        List<String> dbTableNameList = new ArrayList<>();
        dbNameList.forEach(dbName -> {
            List<String> tableNameList = tableInfoService.getTablesByDataBase(dbName).stream()
                    .map(table -> dbName + "." + table.getTableName()).collect(Collectors.toList());
            dbTableNameList.addAll(tableNameList);
        });
        Set<String> columnNameList = devColumnInfoDao.select(c -> c.where(devColumnInfo.del, isNotEqualTo(1)))
                .stream().map(DevColumnInfo::getColumnName).collect(Collectors.toSet());
        echo.setBasicAutocompletionTips(Arrays.asList(baseAutocompletionTimValue.split(",")));
        echo.setDbTableNames(dbTableNameList);
        echo.setColumnNames(new ArrayList<>(columnNameList));
        return echo;
    }

}
