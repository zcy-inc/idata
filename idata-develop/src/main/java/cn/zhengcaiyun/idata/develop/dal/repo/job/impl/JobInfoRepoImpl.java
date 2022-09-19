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
import cn.zhengcaiyun.idata.develop.dal.dao.job.*;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DIJobContentDynamicSqlSupport.DI_JOB_CONTENT;
import static cn.zhengcaiyun.idata.develop.dal.dao.job.DIStreamJobContentDynamicSqlSupport.DI_STREAM_JOB_CONTENT;
import static cn.zhengcaiyun.idata.develop.dal.dao.job.DIStreamJobTableDynamicSqlSupport.DI_STREAM_JOB_TABLE;
import static cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobContentKylinDynamicSqlSupport.devJobContentKylin;
import static cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobContentScriptDynamicSqlSupport.devJobContentScript;
import static cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobContentSparkDynamicSqlSupport.devJobContentSpark;
import static cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobContentSqlDynamicSqlSupport.devJobContentSql;
import static cn.zhengcaiyun.idata.develop.dal.dao.job.JobDependenceDynamicSqlSupport.JOB_DEPENDENCE;
import static cn.zhengcaiyun.idata.develop.dal.dao.job.JobExecuteConfigDynamicSqlSupport.JOB_EXECUTE_CONFIG;
import static cn.zhengcaiyun.idata.develop.dal.dao.job.JobInfoDynamicSqlSupport.jobInfo;
import static cn.zhengcaiyun.idata.develop.dal.dao.job.JobOutputDynamicSqlSupport.JOB_OUTPUT;
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
    private final DevJobContentSqlDao devJobContentSqlDao;
    private final DevJobContentSparkDao devJobContentSparkDao;
    private final DevJobContentScriptDao devJobContentScriptDao;
    private final DevJobContentKylinDao devJobContentKylinDao;
    private final JobOutputDao jobOutputDao;
    private final JobDependenceDao jobDependenceDao;
    private final DIStreamJobContentDao diStreamJobContentDao;
    private final DIStreamJobTableDao diStreamJobTableDao;


    @Autowired
    public JobInfoRepoImpl(JobInfoDao jobInfoDao,
                           JobExecuteConfigDao jobExecuteConfigDao,
                           JobPublishRecordDao jobPublishRecordDao,
                           DIJobContentDao diJobContentDao,
                           DevJobContentSqlDao devJobContentSqlDao,
                           DevJobContentSparkDao devJobContentSparkDao,
                           DevJobContentScriptDao devJobContentScriptDao,
                           DevJobContentKylinDao devJobContentKylinDao,
                           JobOutputDao jobOutputDao,
                           JobDependenceDao jobDependenceDao,
                           DIStreamJobContentDao diStreamJobContentDao,
                           DIStreamJobTableDao diStreamJobTableDao) {
        this.jobInfoDao = jobInfoDao;
        this.jobExecuteConfigDao = jobExecuteConfigDao;
        this.jobPublishRecordDao = jobPublishRecordDao;
        this.diJobContentDao = diJobContentDao;
        this.devJobContentSqlDao = devJobContentSqlDao;
        this.devJobContentSparkDao = devJobContentSparkDao;
        this.devJobContentScriptDao = devJobContentScriptDao;
        this.devJobContentKylinDao = devJobContentKylinDao;
        this.jobOutputDao = jobOutputDao;
        this.jobDependenceDao = jobDependenceDao;
        this.diStreamJobContentDao = diStreamJobContentDao;
        this.diStreamJobTableDao = diStreamJobTableDao;
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
        final Long jobId = job.getId();
        jobInfoDao.update(dsl -> dsl.set(jobInfo.del).equalTo(DeleteEnum.DEL_YES.val)
                .set(jobInfo.editor).equalTo(operator)
                .where(jobInfo.id, isEqualTo(jobId)));

        jobExecuteConfigDao.update(dsl -> dsl.set(JOB_EXECUTE_CONFIG.del).equalTo(DeleteEnum.DEL_YES.val)
                .set(JOB_EXECUTE_CONFIG.editor).equalTo(operator)
                .where(JOB_EXECUTE_CONFIG.jobId, isEqualTo(jobId)));

        jobPublishRecordDao.update(dsl -> dsl.set(jobPublishRecord.del).equalTo(DeleteEnum.DEL_YES.val)
                .set(jobPublishRecord.editor).equalTo(operator)
                .where(jobPublishRecord.jobId, isEqualTo(jobId)));

        jobDependenceDao.update(dsl -> dsl.set(JOB_DEPENDENCE.del).equalTo(DeleteEnum.DEL_YES.val)
                .where(JOB_DEPENDENCE.jobId, isEqualTo(jobId)));

        // 删除作业输出
        jobOutputDao.update(dsl -> dsl.set(JOB_OUTPUT.del).equalTo(DeleteEnum.DEL_YES.val)
                .where(JOB_OUTPUT.jobId, isEqualTo(jobId)));

        // 不同类型作业删除
        String jobType = job.getJobType();
        if (JobTypeEnum.DI_BATCH.getCode().equals(jobType)
                || JobTypeEnum.BACK_FLOW.getCode().equals(job.getJobType())) {
            diJobContentDao.update(dsl -> dsl.set(DI_JOB_CONTENT.del).equalTo(DeleteEnum.DEL_YES.val)
                    .set(DI_JOB_CONTENT.editor).equalTo(operator)
                    .where(DI_JOB_CONTENT.jobId, isEqualTo(jobId)));
        } else if (JobTypeEnum.DI_STREAM.getCode().equals(job.getJobType())) {
            diStreamJobContentDao.update(dsl -> dsl.set(DI_STREAM_JOB_CONTENT.del).equalTo(DeleteEnum.DEL_YES.val)
                    .set(DI_STREAM_JOB_CONTENT.editor).equalTo(operator)
                    .where(DI_STREAM_JOB_CONTENT.jobId, isEqualTo(jobId)));
            diStreamJobTableDao.delete(dsl -> dsl.where(DI_STREAM_JOB_TABLE.jobId, isEqualTo(jobId)));
        } else if (JobTypeEnum.SQL_SPARK.getCode().equals(jobType)
                || JobTypeEnum.SQL_FLINK.getCode().equals(jobType)) {
            devJobContentSqlDao.update(dsl -> dsl.set(devJobContentSql.del).equalTo(DeleteEnum.DEL_YES.val)
                    .set(devJobContentSql.editor).equalTo(operator)
                    .where(devJobContentSql.jobId, isEqualTo(jobId)));
        } else if (JobTypeEnum.SPARK_PYTHON.getCode().equals(jobType)
                || JobTypeEnum.SPARK_JAR.getCode().equals(jobType)) {
            devJobContentSparkDao.update(dsl -> dsl.set(devJobContentSpark.del).equalTo(DeleteEnum.DEL_YES.val)
                    .set(devJobContentSpark.editor).equalTo(operator)
                    .where(devJobContentSpark.jobId, isEqualTo(jobId)));
        } else if (JobTypeEnum.SCRIPT_PYTHON.getCode().equals(jobType)
                || JobTypeEnum.SCRIPT_SHELL.getCode().equals(jobType)) {
            devJobContentScriptDao.update(dsl -> dsl.set(devJobContentScript.del).equalTo(DeleteEnum.DEL_YES.val)
                    .set(devJobContentScript.editor).equalTo(operator)
                    .where(devJobContentScript.jobId, isEqualTo(jobId)));
        } else if (JobTypeEnum.KYLIN.getCode().equals(jobType)) {
            devJobContentKylinDao.update(dsl -> dsl.set(devJobContentKylin.del).equalTo(DeleteEnum.DEL_YES.val)
                    .set(devJobContentKylin.editor).equalTo(operator)
                    .where(devJobContentKylin.jobId, isEqualTo(jobId)));
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
    public List<JobInfo> queryJobInfo(List<Long> ids) {
        return jobInfoDao.select(dsl -> dsl.where(jobInfo.id, isIn(ids),
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

    @Override
    public List<JobInfo> queryList(JobInfoCondition condition, long limit, long offset) {
        String jobType = Objects.isNull(condition.getJobType()) ? null : condition.getJobType().getCode();
        return jobInfoDao.select(dsl -> dsl.where(jobInfo.folderId, isEqualToWhenPresent(condition.getFolderId()),
                        and(jobInfo.jobType, isEqualToWhenPresent(jobType)),
                        and(jobInfo.jobType, isInWhenPresent(condition.getJobTypeCodes())),
                        and(jobInfo.dwLayerCode, isEqualToWhenPresent(condition.getDwLayerCode())),
                        and(jobInfo.status, isEqualToWhenPresent(condition.getStatus())),
                        and(jobInfo.name, isLikeWhenPresent(condition.getName()).map(MybatisHelper::appendWildCards)),
                        and(jobInfo.del, isEqualTo(DeleteEnum.DEL_NO.val)))
                .orderBy(jobInfo.id.descending())
                .limit(limit).offset(offset));
    }

    @Override
    public Boolean updateJobFolder(List<Long> jobIds, Long destFolderId, String operator) {
        jobInfoDao.update(dsl -> dsl.set(jobInfo.folderId).equalTo(destFolderId)
                .set(jobInfo.editor).equalTo(operator)
                .where(jobInfo.id, isIn(jobIds)));
        return Boolean.TRUE;
    }
}
