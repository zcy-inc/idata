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

import cn.zhengcaiyun.idata.develop.dto.job.sql.SqlJobDto;
import cn.zhengcaiyun.idata.develop.dto.job.sql.SqlQueryDto;
import cn.zhengcaiyun.idata.develop.dto.job.sql.SqlResultDto;
import cn.zhengcaiyun.idata.develop.service.job.SqlJobService;
import org.springframework.stereotype.Service;

/**
 * @author caizhedong
 * @date 2021-11-22 上午10:38
 */

@Service
public class SqlJobServiceImpl implements SqlJobService {

    @Override
    public SqlJobDto save(SqlJobDto sqlJobDto, String operator) {
        return null;
    }

    @Override
    public SqlJobDto find(Long jobId, Integer version) {
        return null;
    }

    @Override
    public SqlResultDto runQuerySql(SqlQueryDto sqlQueryDto) {
        return null;
    }
}
