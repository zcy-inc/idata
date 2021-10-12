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

import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.develop.condition.job.JobPublishRecordCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.PublishStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.job.JobPublishRecordDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobPublishRecordRepo;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.JobPublishRecordDynamicSqlSupport.jobPublishRecord;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

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
    public Page<JobPublishRecord> paging(JobPublishRecordCondition condition, PageParam pageParam) {
        long total = count(condition);
        List<JobPublishRecord> publishRecordList = null;
        if (total > 0) {
            publishRecordList = queryList(condition, pageParam.getLimit(), pageParam.getOffset());
        }
        return Page.newOne(publishRecordList, total);
    }

    @Override
    public List<JobPublishRecord> queryList(JobPublishRecordCondition condition, long limit, long offset) {
        return jobPublishRecordDao.select(dsl -> dsl.where(
                                jobPublishRecord.jobId, isEqualToWhenPresent(condition.getJobId()),
                                and(jobPublishRecord.jobContentVersion, isEqualToWhenPresent(condition.getJobContentVersion())),
                                and(jobPublishRecord.jobContentId, isEqualToWhenPresent(condition.getJobContentId())),
                                and(jobPublishRecord.environment, isEqualToWhenPresent(condition.getEnvironment())),
                                and(jobPublishRecord.publishStatus, isEqualToWhenPresent(condition.getPublishStatus())),
                                and(jobPublishRecord.jobTypeCode, isEqualToWhenPresent(condition.getJobTypeCode())),
                                and(jobPublishRecord.dwLayerCode, isEqualToWhenPresent(condition.getDwLayerCode())),
                                and(jobPublishRecord.creator, isEqualToWhenPresent(condition.getSubmitOperator()))
                        ).orderBy(jobPublishRecord.id.descending())
                        .limit(limit).offset(offset)
        );
    }

    @Override
    public long count(JobPublishRecordCondition condition) {
        return jobPublishRecordDao.count(dsl -> dsl.where(
                jobPublishRecord.jobId, isEqualToWhenPresent(condition.getJobId()),
                and(jobPublishRecord.jobContentVersion, isEqualToWhenPresent(condition.getJobContentVersion())),
                and(jobPublishRecord.jobContentId, isEqualToWhenPresent(condition.getJobContentId())),
                and(jobPublishRecord.environment, isEqualToWhenPresent(condition.getEnvironment())),
                and(jobPublishRecord.publishStatus, isEqualToWhenPresent(condition.getPublishStatus())),
                and(jobPublishRecord.jobTypeCode, isEqualToWhenPresent(condition.getJobTypeCode())),
                and(jobPublishRecord.dwLayerCode, isEqualToWhenPresent(condition.getDwLayerCode())),
                and(jobPublishRecord.creator, isEqualToWhenPresent(condition.getSubmitOperator()))
        ));
    }

    @Override
    public Long save(JobPublishRecord record) {
        int ret = jobPublishRecordDao.insertSelective(record);
        if (ret <= 0) return null;
        return record.getId();
    }

    @Override
    public Boolean update(JobPublishRecord record) {
        jobPublishRecordDao.updateByPrimaryKeySelective(record);
        return Boolean.TRUE;
    }

    @Override
    public Optional<JobPublishRecord> query(Long jobId, Integer version, String environment) {
        return jobPublishRecordDao.selectOne(dsl -> dsl.where(jobPublishRecord.jobId, isEqualTo(jobId),
                and(jobPublishRecord.jobContentVersion, isEqualTo(version)),
                and(jobPublishRecord.environment, isEqualTo(environment)),
                and(jobPublishRecord.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }

    @Override
    public Optional<JobPublishRecord> query(Long id) {
        Optional<JobPublishRecord> optional = jobPublishRecordDao.selectByPrimaryKey(id);
        if (optional.isPresent()) {
            if (optional.get().getDel().equals(DeleteEnum.DEL_YES.val)) {
                return Optional.empty();
            }
        }
        return optional;
    }

    @Override
    public List<JobPublishRecord> queryList(Long jobId) {
        return jobPublishRecordDao.select(dsl -> dsl.where(jobPublishRecord.jobId, isEqualTo(jobId),
                        and(jobPublishRecord.del, isEqualTo(DeleteEnum.DEL_NO.val)))
                .orderBy(jobPublishRecord.jobContentVersion.descending(),
                        jobPublishRecord.id.descending()));
    }

    @Override
    @Transactional
    public Boolean publish(JobPublishRecord record, String operator) {
        jobPublishRecordDao.update(dsl -> dsl.set(jobPublishRecord.publishStatus).equalTo(PublishStatusEnum.ARCHIVED.val)
                .where(jobPublishRecord.jobId, isEqualTo(record.getJobId()),
                        and(jobPublishRecord.environment, isEqualTo(record.getEnvironment())),
                        and(jobPublishRecord.publishStatus, isEqualTo(PublishStatusEnum.PUBLISHED.val))));
        //更新状态为发布
        JobPublishRecord publishedRecord = new JobPublishRecord();
        publishedRecord.setId(record.getId());
        publishedRecord.setPublishStatus(PublishStatusEnum.PUBLISHED.val);
        publishedRecord.setApproveOperator(operator);
        publishedRecord.setApproveTime(new Date());
        publishedRecord.setApproveRemark(Strings.nullToEmpty(record.getApproveRemark()));
        update(publishedRecord);
        return Boolean.TRUE;
    }
}
