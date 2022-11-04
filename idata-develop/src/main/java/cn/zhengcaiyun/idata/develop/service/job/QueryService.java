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
package cn.zhengcaiyun.idata.develop.service.job;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.develop.dto.job.AutocompletionTipDto;
import cn.zhengcaiyun.idata.develop.dto.job.QueryDto;
import cn.zhengcaiyun.idata.develop.dto.job.QueryRunResultDto;
import cn.zhengcaiyun.idata.develop.dto.job.QueryStatementDto;

/**
 * @author caizhedong
 * @date 2021-11-23 下午7:55
 */

public interface QueryService {
    QueryStatementDto runQuery(QueryDto queryDto, Operator operator);

    QueryRunResultDto runQueryResult(Integer sessionId, Integer statementId, String sessionKind, Integer from, Integer size);

    AutocompletionTipDto getAutocompletionTipConfigs(String autocompletionType);

    Boolean cancelStatement(QueryStatementDto queryStatementDto);
}
