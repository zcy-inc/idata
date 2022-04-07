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

package cn.zhengcaiyun.idata.develop.service.measure.Impl;

import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.connector.bean.dto.QueryResultDto;
import cn.zhengcaiyun.idata.connector.connection.ConnectionCfg;
import cn.zhengcaiyun.idata.connector.service.Query;
import cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDao;
import cn.zhengcaiyun.idata.develop.dal.model.DevTableInfo;
import cn.zhengcaiyun.idata.develop.dto.query.MeasureDataQueryDto;
import cn.zhengcaiyun.idata.develop.dto.query.TableDataQueryDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;
import cn.zhengcaiyun.idata.develop.manager.TableQueryManager;
import cn.zhengcaiyun.idata.develop.service.measure.MeasureDataService;
import cn.zhengcaiyun.idata.develop.service.table.TableInfoService;
import com.google.common.base.MoreObjects;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.devLabel;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-21 18:24
 **/
@Service
public class MeasureDataServiceImpl implements MeasureDataService {

    @Autowired
    private TableQueryManager tableQueryManager;
    @Autowired
    private Query query;
    @Autowired
    private DevTableInfoDao devTableInfoDao;
    @Autowired
    private DevLabelDao devLabelDao;

    private final String DB_NAME_LABEL = "dbName:LABEL";

    @Override
    public QueryResultDto queryMeasureData(MeasureDataQueryDto measureDataQueryDto) {
        if (isEmpty(measureDataQueryDto.getDimensions()) && isEmpty(measureDataQueryDto.getMeasures())) {
            return null;
        }
        TableDataQueryDto dataQueryDto = new TableDataQueryDto();
        BeanUtils.copyProperties(measureDataQueryDto, dataQueryDto);
        dataQueryDto.setPageParam(PageParam.of(measureDataQueryDto.getPageNo(), measureDataQueryDto.getPageSize()));
        dataQueryDto.setAggregate(true);
        QueryResultDto resultDto = tableQueryManager.queryData(dataQueryDto);
        Long total = tableQueryManager.count(dataQueryDto);
        resultDto.setTotal(MoreObjects.firstNonNull(total, 0L));
        return resultDto;
    }

    @Override
    public List<String> queryModifierValues(Long tableId, String columnName) throws SQLException {
        String selectSql = "SELECT %s FROM %s.%s GROUP BY %s";
        DevTableInfo tableInfo = devTableInfoDao.selectByPrimaryKey(tableId).get();
        String tableDbName = devLabelDao.selectOne(c -> c.where(devLabel.del, isNotEqualTo(1),
                and(devLabel.tableId, isEqualTo(tableId)), and(devLabel.labelCode, isEqualTo(DB_NAME_LABEL))))
                .get().getLabelParamValue();
        ConnectionCfg connectionDto = tableQueryManager.getConnectionInfo();
        QueryResultDto resultDto = query.query(connectionDto, String.format(selectSql, columnName,
                tableDbName, tableInfo.getTableName(), columnName));
        return resultDto.getData().stream().map(data -> data.get(0)).collect(Collectors.toList());
    }
}
