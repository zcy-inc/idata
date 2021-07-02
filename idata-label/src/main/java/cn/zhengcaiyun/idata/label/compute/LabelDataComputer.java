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
package cn.zhengcaiyun.idata.label.compute;

import cn.zhengcaiyun.idata.commons.exception.ExecuteSqlException;
import cn.zhengcaiyun.idata.label.compute.query.Query;
import cn.zhengcaiyun.idata.label.compute.query.dto.ConnectionDto;
import cn.zhengcaiyun.idata.label.compute.query.dto.WideTableDataDto;
import cn.zhengcaiyun.idata.label.compute.sql.transform.SqlTranslator;
import cn.zhengcaiyun.idata.label.dto.LabelQueryDataDto;
import cn.zhengcaiyun.idata.label.dto.label.rule.LabelRuleDto;
import cn.zhengcaiyun.idata.system.dal.dao.SysConfigDao;
import cn.zhengcaiyun.idata.system.dal.model.SysConfig;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static cn.zhengcaiyun.idata.system.dal.dao.SysConfigDynamicSqlSupport.sysConfig;
import static com.google.common.base.Preconditions.checkState;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 14:36
 **/
@Component
public class LabelDataComputer {
    private static final Logger logger = LoggerFactory.getLogger(LabelDataComputer.class);

    private final Query query;
    private final SqlTranslator sqlTranslator;
    private final SysConfigDao sysConfigDao;

    @Autowired
    public LabelDataComputer(Query query, SqlTranslator sqlTranslator, SysConfigDao sysConfigDao) {
        this.query = query;
        this.sqlTranslator = sqlTranslator;
        this.sysConfigDao = sysConfigDao;
    }

    public Optional<LabelQueryDataDto> compute(LabelRuleDto ruleDto, String objectType, Long limit, Long offset) {
        ConnectionDto connectionDto = getConnectionInfo();
        checkState(connectionDto != null, "数据源连接信息不正确");
        String sql = sqlTranslator.translate(ruleDto, objectType, limit, offset);
//        String sql = sqlTranslator.mockSQL();
        logger.info("translate to sql: {}.", sql);
        WideTableDataDto tableDataDto;
        try {
            tableDataDto = query.query(connectionDto, sql);
        } catch (Exception ex) {
            throw new ExecuteSqlException("SQL执行失败", ex);
        }
        if (Objects.isNull(tableDataDto)) {
            return Optional.empty();
        }
        LabelQueryDataDto dataDto = new LabelQueryDataDto();
        dataDto.setColumns(tableDataDto.getMeta());
        dataDto.setData(tableDataDto.getData());
        return Optional.of(dataDto);
    }

    private ConnectionDto getConnectionInfo() {
        SysConfig trinoConfig = sysConfigDao.selectOne(dsl -> dsl.where(sysConfig.keyOne, isEqualTo("trino-info")))
                .orElse(null);
        if (trinoConfig != null) {
            return JSON.parseObject(trinoConfig.getValueOne(), ConnectionDto.class);
        }
        return null;
    }

}
