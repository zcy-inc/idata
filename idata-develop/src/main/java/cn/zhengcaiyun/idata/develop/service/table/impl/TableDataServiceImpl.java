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

package cn.zhengcaiyun.idata.develop.service.table.impl;

import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.connector.api.DataQueryApi;
import cn.zhengcaiyun.idata.connector.bean.dto.QueryResultDto;
import cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDao;
import cn.zhengcaiyun.idata.develop.dal.model.DevTableInfo;
import cn.zhengcaiyun.idata.develop.dto.table.ColumnInfoDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;
import cn.zhengcaiyun.idata.develop.service.table.TableDataService;
import cn.zhengcaiyun.idata.develop.service.table.TableInfoService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.devLabel;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDynamicSqlSupport.devTableInfo;
import static com.google.common.base.Preconditions.checkState;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-20 14:04
 **/
@Service
public class TableDataServiceImpl implements TableDataService {

    @Autowired
    private DataQueryApi dataQueryApi;
    @Autowired
    private DevLabelDao devLabelDao;
    @Autowired
    private DevTableInfoDao devTableInfoDao;
    @Autowired
    private TableInfoService tableInfoService;

    private final String DB_NAME_LABEL = "dbName:LABEL";

    @Override
    public QueryResultDto getTableData(Long tableId, PageParam pageParam) {
        DevTableInfo tableInfo = devTableInfoDao.selectOne(c -> c.where(devTableInfo.id, isEqualTo(tableId),
                and(devTableInfo.del, isNotEqualTo(1))))
                .orElseThrow(() -> new IllegalArgumentException("表不存在"));
        TableInfoDto tableInfoDto = tableInfoService.getTableInfo(tableId);
        checkState(Objects.nonNull(tableInfoDto), "表不存在");

        QueryResultDto resultDto = dataQueryApi.queryData(getDbName(tableId), tableInfo.getTableName(), pageParam.getLimit(), pageParam.getOffset());
        Map<String, String> columnAliasMap = extractColumnAlias(tableInfoDto);
        resultDto.setMeta(injectColumnAlias(resultDto.getMeta(), columnAliasMap));
        return resultDto;
    }

    private String getDbName(Long tableId) {
        return devLabelDao.selectOne(c -> c
                .where(devLabel.del, isNotEqualTo(1), and(devLabel.labelCode, isEqualTo(DB_NAME_LABEL)),
                        and(devLabel.tableId, isEqualTo(tableId))))
                .get().getLabelParamValue();
    }

    private Map<String, String> extractColumnAlias(TableInfoDto tableInfoDto) {
        List<ColumnInfoDto> columns = tableInfoDto.getColumnInfos();
        if (ObjectUtils.isEmpty(columns)) return Maps.newHashMap();

        return columns.stream().filter(colInfo -> StringUtils.isNotBlank(colInfo.getColumnName()) && StringUtils.isNotBlank(colInfo.getColumnComment()))
                .collect(Collectors.toMap(ColumnInfoDto::getColumnName, ColumnInfoDto::getColumnComment, (oldVal, newVal) -> newVal));
    }

    private List<cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto> injectColumnAlias(List<cn.zhengcaiyun.idata.connector.bean.dto.ColumnInfoDto> columns,
                                                                                          Map<String, String> columnAliasMap) {
        if (ObjectUtils.isEmpty(columns) || ObjectUtils.isEmpty(columnAliasMap))
            return columns;

        columns.stream().forEach(col -> col.setColumnComment(columnAliasMap.get(col.getColumnName())));
        return columns;
    }
}
