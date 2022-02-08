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
import cn.zhengcaiyun.idata.connector.bean.dto.SingleColumnResultDto;
import cn.zhengcaiyun.idata.develop.constant.enums.LogicOperatorEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.StringMatchTypeEnum;
import cn.zhengcaiyun.idata.develop.dto.query.*;
import cn.zhengcaiyun.idata.develop.manager.TableQueryManager;
import cn.zhengcaiyun.idata.develop.service.measure.DimensionDataService;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-21 18:36
 **/
@Service
public class DimensionDataServiceImpl implements DimensionDataService {

    @Autowired
    private TableQueryManager tableQueryManager;

    @Override
    public SingleColumnResultDto queryDimensionData(DimDataQueryDto dimDataQueryDto) {
        checkArgument(StringUtils.isNotEmpty(dimDataQueryDto.getDbSchema()), "数仓层级为空");
        checkArgument(StringUtils.isNotEmpty(dimDataQueryDto.getTableName()), "表名为空");
        checkArgument(StringUtils.isNotEmpty(dimDataQueryDto.getColumnName()), "字段名为空");

        TableDataQueryDto dataQueryDto = new TableDataQueryDto();
        dataQueryDto.setPageParam(PageParam.of(dimDataQueryDto.getLimit()));

        dataQueryDto.setDbSchema(dimDataQueryDto.getDbSchema());
        dataQueryDto.setTableName(dimDataQueryDto.getTableName());
        dataQueryDto.setAggregate(true);
        List<DimColumnDto> dimensions = new ArrayList<>();
        DimColumnDto dimColumnDto = new DimColumnDto();
        dimColumnDto.setColumnName(dimDataQueryDto.getColumnName());
        dimensions.add(dimColumnDto);
        dataQueryDto.setDimensions(dimensions);
        if (StringUtils.isNotEmpty(dimDataQueryDto.getMatchStr())) {
            FilterDto filter = new FilterDto();
            StringMatchDto stringMatch = new StringMatchDto();
            stringMatch.setMatchStr(dimDataQueryDto.getMatchStr());
            stringMatch.setMatchType(StringMatchTypeEnum.contains);
            stringMatch.setLogicOp(LogicOperatorEnum.and);
            filter.setColumnName(dimDataQueryDto.getColumnName());
            filter.setMatch(Collections.singletonList(stringMatch));
            dataQueryDto.setFilters(Collections.singletonList(filter));
        }
        QueryResultDto resultDto = tableQueryManager.queryData(dataQueryDto);
        Long total = tableQueryManager.count(dataQueryDto);

        SingleColumnResultDto singleColumnResultDto = new SingleColumnResultDto();
        singleColumnResultDto.setTotal(MoreObjects.firstNonNull(total, 0L));
        singleColumnResultDto.setData(resultDto.getData().stream().map(dataList ->
                dataList.get(0)).collect(Collectors.toList()));
        return singleColumnResultDto;
    }
}
