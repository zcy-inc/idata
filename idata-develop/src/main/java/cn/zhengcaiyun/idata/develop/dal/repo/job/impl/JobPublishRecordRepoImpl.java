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
import cn.zhengcaiyun.idata.develop.constant.enums.EditableEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.PublishStatusEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.RunningStateEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.job.JobExecuteConfigDao;
import cn.zhengcaiyun.idata.develop.dal.dao.job.JobPublishRecordDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import cn.zhengcaiyun.idata.develop.dal.repo.job.*;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.JobExecuteConfigDynamicSqlSupport.JOB_EXECUTE_CONFIG;
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
    private final JobExecuteConfigDao jobExecuteConfigDao;
    private final DIJobContentRepo diJobContentRepo;
    private final JobInfoRepo jobInfoRepo;
    private final SqlJobRepo sqlJobRepo;
    private final SparkJobRepo sparkJobRepo;
    private final ScriptJobRepo scriptJobRepo;
    private final KylinJobRepo kylinJobRepo;
    private final DIStreamJobContentRepo diStreamJobContentRepo;

    @Autowired
    public JobPublishRecordRepoImpl(JobPublishRecordDao jobPublishRecordDao,
                                    JobExecuteConfigDao jobExecuteConfigDao,
                                    DIJobContentRepo diJobContentRepo,
                                    JobInfoRepo jobInfoRepo,
                                    SqlJobRepo sqlJobRepo,
                                    SparkJobRepo sparkJobRepo,
                                    ScriptJobRepo scriptJobRepo,
                                    KylinJobRepo kylinJobRepo,
                                    DIStreamJobContentRepo diStreamJobContentRepo) {
        this.jobPublishRecordDao = jobPublishRecordDao;
        this.jobExecuteConfigDao = jobExecuteConfigDao;
        this.diJobContentRepo = diJobContentRepo;
        this.jobInfoRepo = jobInfoRepo;
        this.sqlJobRepo = sqlJobRepo;
        this.sparkJobRepo = sparkJobRepo;
        this.scriptJobRepo = scriptJobRepo;
        this.kylinJobRepo = kylinJobRepo;
        this.diStreamJobContentRepo = diStreamJobContentRepo;
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
                                and(jobPublishRecord.publishStatus, isInWhenPresent(condition.getPublishStatusList())),
                                and(jobPublishRecord.jobTypeCode, isEqualToWhenPresent(condition.getJobTypeCode())),
                                and(jobPublishRecord.dwLayerCode, isEqualToWhenPresent(condition.getDwLayerCode())),
                                and(jobPublishRecord.creator, isEqualToWhenPresent(condition.getSubmitOperator())),
                                and(jobPublishRecord.del, isEqualTo(DeleteEnum.DEL_NO.val))
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
                and(jobPublishRecord.publishStatus, isInWhenPresent(condition.getPublishStatusList())),
                and(jobPublishRecord.jobTypeCode, isEqualToWhenPresent(condition.getJobTypeCode())),
                and(jobPublishRecord.dwLayerCode, isEqualToWhenPresent(condition.getDwLayerCode())),
                and(jobPublishRecord.creator, isEqualToWhenPresent(condition.getSubmitOperator())),
                and(jobPublishRecord.del, isEqualTo(DeleteEnum.DEL_NO.val))
        ));
    }

    @Override
    public List<JobPublishRecord> queryList(JobPublishRecordCondition condition) {
        return jobPublishRecordDao.select(dsl -> dsl.where(
                jobPublishRecord.jobId, isEqualToWhenPresent(condition.getJobId()),
                and(jobPublishRecord.jobId, isInWhenPresent(condition.getJobIds())),
                and(jobPublishRecord.jobContentVersion, isEqualToWhenPresent(condition.getJobContentVersion())),
                and(jobPublishRecord.jobContentId, isEqualToWhenPresent(condition.getJobContentId())),
                and(jobPublishRecord.environment, isEqualToWhenPresent(condition.getEnvironment())),
                and(jobPublishRecord.publishStatus, isEqualToWhenPresent(condition.getPublishStatus())),
                and(jobPublishRecord.publishStatus, isInWhenPresent(condition.getPublishStatusList())),
                and(jobPublishRecord.jobTypeCode, isEqualToWhenPresent(condition.getJobTypeCode())),
                and(jobPublishRecord.dwLayerCode, isEqualToWhenPresent(condition.getDwLayerCode())),
                and(jobPublishRecord.creator, isEqualToWhenPresent(condition.getSubmitOperator())),
                and(jobPublishRecord.del, isEqualTo(DeleteEnum.DEL_NO.val))
        ).orderBy(jobPublishRecord.id.descending()));
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
    public Boolean submit(JobPublishRecord record, String operator) {
        if (record.getId() == null) {
            String jobType = record.getJobTypeCode();
            // 第一次提交
            // 更新不同作业的可编辑状态
            if (JobTypeEnum.DI_BATCH.getCode().equals(jobType)
                    || JobTypeEnum.BACK_FLOW.getCode().equals(jobType)) {
                diJobContentRepo.updateEditable(record.getJobContentId(), EditableEnum.NO, operator);
            } else if (JobTypeEnum.DI_STREAM.getCode().equals(jobType)) {
                diStreamJobContentRepo.updateEditable(record.getJobContentId(), EditableEnum.NO, operator);
            } else if (JobTypeEnum.SQL_SPARK.getCode().equals(jobType)
                    || JobTypeEnum.SQL_FLINK.getCode().equals(jobType)) {
                sqlJobRepo.updateEditable(record.getJobContentId(), EditableEnum.NO, operator);
            } else if (JobTypeEnum.SPARK_PYTHON.getCode().equals(jobType)
                    || JobTypeEnum.SPARK_JAR.getCode().equals(jobType)) {
                sparkJobRepo.updateEditable(record.getJobContentId(), EditableEnum.NO, operator);
            } else if (JobTypeEnum.SCRIPT_PYTHON.getCode().equals(jobType)
                    || JobTypeEnum.SCRIPT_SHELL.getCode().equals(jobType)) {
                scriptJobRepo.updateEditable(record.getJobContentId(), EditableEnum.NO, operator);
            } else {
                kylinJobRepo.updateEditable(record.getJobContentId(), EditableEnum.NO, operator);
            }
            record.setPublishStatus(PublishStatusEnum.SUBMITTED.val);
            save(record);
        } else {
            // 归档后再次提交
            JobPublishRecord submitStatus = new JobPublishRecord();
            submitStatus.setId(record.getId());
            submitStatus.setPublishStatus(PublishStatusEnum.SUBMITTED.val);
            submitStatus.setEditor(operator);
            submitStatus.setSubmitRemark(record.getSubmitRemark());
            update(submitStatus);
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional
    public Boolean publish(JobPublishRecord record, String operator) {
        jobPublishRecordDao.update(dsl -> dsl.set(jobPublishRecord.publishStatus).equalTo(PublishStatusEnum.ARCHIVED.val)
                .where(jobPublishRecord.jobId, isEqualTo(record.getJobId()),
                        and(jobPublishRecord.environment, isEqualTo(record.getEnvironment())),
                        and(jobPublishRecord.publishStatus, isEqualTo(PublishStatusEnum.PUBLISHED.val))));

        // 修改配置运行状态
        jobExecuteConfigDao.update(dsl -> dsl.set(JOB_EXECUTE_CONFIG.runningState).equalTo(RunningStateEnum.resume.val)
                .where(JOB_EXECUTE_CONFIG.jobId, isEqualTo(record.getJobId()),
                        and(JOB_EXECUTE_CONFIG.environment, isEqualTo(record.getEnvironment()))));

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
