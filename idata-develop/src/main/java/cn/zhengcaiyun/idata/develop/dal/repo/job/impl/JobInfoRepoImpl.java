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
import cn.zhengcaiyun.idata.commons.util.MybatisHelper;
import cn.zhengcaiyun.idata.develop.condition.job.JobInfoCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.job.DIJobContentDao;
import cn.zhengcaiyun.idata.develop.dal.dao.job.JobExecuteConfigDao;
import cn.zhengcaiyun.idata.develop.dal.dao.job.JobInfoDao;
import cn.zhengcaiyun.idata.develop.dal.dao.job.JobPublishRecordDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DIJobContentDynamicSqlSupport.DI_JOB_CONTENT;
import static cn.zhengcaiyun.idata.develop.dal.dao.job.JobExecuteConfigDynamicSqlSupport.jobExecuteConfig;
import static cn.zhengcaiyun.idata.develop.dal.dao.job.JobInfoDynamicSqlSupport.jobInfo;
import static cn.zhengcaiyun.idata.develop.dal.dao.job.JobPublishRecordDynamicSqlSupport.jobPublishRecord;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-26 16:56
 **/
@Repository
public class JobInfoRepoImpl implements JobInfoRepo {

    private final JobInfoDao jobInfoDao;
    private final JobExecuteConfigDao jobExecuteConfigDao;
    private final JobPublishRecordDao jobPublishRecordDao;
    private final DIJobContentDao diJobContentDao;


    @Autowired
    public JobInfoRepoImpl(JobInfoDao jobInfoDao,
                           JobExecuteConfigDao jobExecuteConfigDao,
                           JobPublishRecordDao jobPublishRecordDao,
                           DIJobContentDao diJobContentDao) {
        this.jobInfoDao = jobInfoDao;
        this.jobExecuteConfigDao = jobExecuteConfigDao;
        this.jobPublishRecordDao = jobPublishRecordDao;
        this.diJobContentDao = diJobContentDao;
    }

    @Override
    public Long saveJobInfo(JobInfo info) {
        int ret = jobInfoDao.insertSelective(info);
        if (ret <= 0) return null;
        return info.getId();
    }

    @Override
    public Boolean updateJobInfo(JobInfo info) {
        jobInfoDao.updateByPrimaryKeySelective(info);
        return Boolean.TRUE;
    }

    @Override
    public Optional<JobInfo> queryJobInfo(Long id) {
        Optional<JobInfo> optional = jobInfoDao.selectByPrimaryKey(id);
        if (optional.isEmpty()) return optional;

        if (DeleteEnum.DEL_YES.val == optional.get().getDel()) return Optional.empty();
        return optional;
    }

    @Override
    public Boolean deleteJobInfo(Long id, String operator) {
        jobInfoDao.update(dsl -> dsl.set(jobInfo.del).equalTo(DeleteEnum.DEL_YES.val)
                .set(jobInfo.editor).equalTo(operator)
                .where(jobInfo.id, isEqualTo(id)));
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean deleteJobAndSubInfo(JobInfo job, String operator) {
        jobInfoDao.update(dsl -> dsl.set(jobInfo.del).equalTo(DeleteEnum.DEL_YES.val)
                .set(jobInfo.editor).equalTo(operator)
                .where(jobInfo.id, isEqualTo(job.getId())));

        jobExecuteConfigDao.update(dsl -> dsl.set(jobExecuteConfig.del).equalTo(DeleteEnum.DEL_YES.val)
                .set(jobExecuteConfig.editor).equalTo(operator)
                .where(jobExecuteConfig.jobId, isEqualTo(job.getId())));

        jobPublishRecordDao.update(dsl -> dsl.set(jobPublishRecord.del).equalTo(DeleteEnum.DEL_YES.val)
                .set(jobPublishRecord.editor).equalTo(operator)
                .where(jobPublishRecord.jobId, isEqualTo(job.getId())));

        if (JobTypeEnum.DI_BATCH.getCode().equals(job.getJobType())
                || JobTypeEnum.DI_STREAM.getCode().equals(job.getJobType())) {
            diJobContentDao.update(dsl -> dsl.set(DI_JOB_CONTENT.del).equalTo(DeleteEnum.DEL_YES.val)
                    .set(DI_JOB_CONTENT.editor).equalTo(operator)
                    .where(DI_JOB_CONTENT.jobId, isEqualTo(job.getId())));
        }
        return Boolean.TRUE;
    }

    @Override
    public List<JobInfo> queryJobInfoByName(String name) {
        return jobInfoDao.select(dsl -> dsl.where(jobInfo.name, isEqualTo(name),
                and(jobInfo.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }

    @Override
    public Long countJobInfo(JobInfoCondition condition) {
        String jobType = Objects.isNull(condition.getJobType()) ? null : condition.getJobType().getCode();
        return jobInfoDao.count(dsl -> dsl.where(jobInfo.folderId, isEqualToWhenPresent(condition.getFolderId()),
                and(jobInfo.jobType, isEqualToWhenPresent(jobType)),
                and(jobInfo.dwLayerCode, isEqualToWhenPresent(condition.getDwLayerCode())),
                and(jobInfo.status, isEqualToWhenPresent(condition.getStatus())),
                and(jobInfo.name, isLikeWhenPresent(condition.getName()).map(MybatisHelper::appendWildCards)),
                and(jobInfo.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }

    @Override
    public List<JobInfo> queryJobInfo(JobInfoCondition condition) {
        String jobType = Objects.isNull(condition.getJobType()) ? null : condition.getJobType().getCode();
        return jobInfoDao.select(dsl -> dsl.where(jobInfo.folderId, isEqualToWhenPresent(condition.getFolderId()),
                and(jobInfo.jobType, isEqualToWhenPresent(jobType)),
                and(jobInfo.jobType, isInWhenPresent(condition.getJobTypeCodes())),
                and(jobInfo.dwLayerCode, isEqualToWhenPresent(condition.getDwLayerCode())),
                and(jobInfo.status, isEqualToWhenPresent(condition.getStatus())),
                and(jobInfo.name, isLikeWhenPresent(condition.getName()).map(MybatisHelper::appendWildCards)),
                and(jobInfo.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }

    @Override
    public long count(JobInfoCondition condition) {
        String jobType = Objects.isNull(condition.getJobType()) ? null : condition.getJobType().getCode();
        return jobInfoDao.count(dsl -> dsl.where(jobInfo.folderId, isEqualToWhenPresent(condition.getFolderId()),
                and(jobInfo.jobType, isEqualToWhenPresent(jobType)),
                and(jobInfo.jobType, isInWhenPresent(condition.getJobTypeCodes())),
                and(jobInfo.dwLayerCode, isEqualToWhenPresent(condition.getDwLayerCode())),
                and(jobInfo.status, isEqualToWhenPresent(condition.getStatus())),
                and(jobInfo.name, isLikeWhenPresent(condition.getName()).map(MybatisHelper::appendWildCards)),
                and(jobInfo.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }
}
