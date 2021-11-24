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
import cn.zhengcaiyun.idata.connector.spi.livy.dto.LivyStatementDto;
import cn.zhengcaiyun.idata.develop.dto.job.QueryRunResultDto;
import cn.zhengcaiyun.idata.develop.dto.job.QueryDto;
import cn.zhengcaiyun.idata.develop.service.job.QueryRunService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author caizhedong
 * @date 2021-11-23 下午8:10
 */

@Service
public class QueryRunServiceImpl implements QueryRunService {

    private final String DROP_QUERY = "DROP";

    @Autowired
    private LivyService livyService;

    @Override
    public LivyStatementDto runQuery(QueryDto queryDto) {
        if (StringUtils.isBlank(queryDto.getSourceQuery())) return null;
        checkArgument(!queryDto.getSourceQuery().toUpperCase().contains(DROP_QUERY), "不能执行DROP操作");
        checkArgument(queryDto.getSessionKind() != null, "执行类型不能为空");
        return livyService.createStatement(queryDto);
    }

    @Override
    public QueryRunResultDto runQueryResult(Integer sessionId, Integer statementId) {
        return PojoUtil.copyOne(livyService.queryResult(sessionId, statementId), QueryRunResultDto.class);
    }
}
