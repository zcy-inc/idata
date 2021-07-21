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

import cn.zhengcaiyun.idata.commons.exception.ExecuteSqlException;
import cn.zhengcaiyun.idata.connector.bean.dto.QueryResultDto;
import cn.zhengcaiyun.idata.connector.connection.ConnectionCfg;
import cn.zhengcaiyun.idata.connector.service.Query;
import cn.zhengcaiyun.idata.develop.dto.query.TableDataQueryDto;
import cn.zhengcaiyun.idata.system.dal.dao.SysConfigDao;
import cn.zhengcaiyun.idata.system.dal.model.SysConfig;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static cn.zhengcaiyun.idata.system.dal.dao.SysConfigDynamicSqlSupport.sysConfig;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description: 目前各种写法，待重构连接模块
 * @author: yangjianhua
 * @create: 2021-07-21 17:50
 **/
@Component
public class TableQueryManager {
    private static final Logger logger = LoggerFactory.getLogger(TableQueryManager.class);

    @Autowired
    private SysConfigDao sysConfigDao;
    @Autowired
    private Query query;

    public QueryResultDto queryData(TableDataQueryDto dataQueryDto) {
        ConnectionCfg connectionDto = getConnectionInfo();
        checkState(connectionDto != null, "数据源连接信息不正确");
        checkArgument(isEmpty(dataQueryDto.getDimensions()) && isEmpty(dataQueryDto.getMeasures()), "维度和度量不能同时为空");

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
        return resultDto;
    }

    public Long count(TableDataQueryDto dataQueryDto) {
        ConnectionCfg connectionDto = getConnectionInfo();
        checkState(connectionDto != null, "数据源连接信息不正确");
        checkArgument(isEmpty(dataQueryDto.getDimensions()) && isEmpty(dataQueryDto.getMeasures()), "维度和度量不能同时为空");
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
        if (trinoConfig != null) {
            return JSON.parseObject(trinoConfig.getValueOne(), ConnectionCfg.class);
        }
        return null;
    }
}
