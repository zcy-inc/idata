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

package cn.zhengcaiyun.idata.develop.dal.repo.job.impl;

import cn.zhengcaiyun.idata.develop.constant.enums.PublishStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.job.JobPublishRecordDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobPublishRecordRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-28 14:56
 **/
@Repository
public class JobPublishRecordRepoImpl implements JobPublishRecordRepo {

    private final JobPublishRecordDao jobPublishRecordDao;

    @Autowired
    public JobPublishRecordRepoImpl(JobPublishRecordDao jobPublishRecordDao) {
        this.jobPublishRecordDao = jobPublishRecordDao;
    }

    @Override
    public Long save(JobPublishRecord record) {
        return null;
    }

    @Override
    public Boolean updatePublishStatus(Long jobId, Integer version, String environment, PublishStatusEnum statusEnum, String operator) {
        return null;
    }

    @Override
    public Optional<JobPublishRecord> query(Long jobId, Integer version, String environment) {
        return Optional.empty();
    }
}
