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

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.commons.enums.UsingStatusEnum;
import cn.zhengcaiyun.idata.commons.exception.BizProcessException;
import cn.zhengcaiyun.idata.datasource.api.DataSourceApi;
import cn.zhengcaiyun.idata.datasource.api.dto.DataSourceDto;
import cn.zhengcaiyun.idata.develop.constant.enums.PublishStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGInfo;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIJobContent;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobExecuteConfig;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobPublishRecord;
import cn.zhengcaiyun.idata.develop.dal.repo.dag.DAGRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIJobContentRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobExecuteConfigRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobPublishRecordRepo;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-29 11:39
 **/
@Component
public class JobPublishManager {

    @Value("${dev.job.publish.whitelist}")
    public String publishWhitelist;

    private final DIJobContentRepo diJobContentRepo;
    private final JobInfoRepo jobInfoRepo;
    private final JobPublishRecordRepo jobPublishRecordRepo;
    private final JobExecuteConfigRepo jobExecuteConfigRepo;
    private final DAGRepo dagRepo;
    private final DataSourceApi dataSourceApi;

    @Autowired
    public JobPublishManager(DIJobContentRepo diJobContentRepo,
                             JobInfoRepo jobInfoRepo,
                             JobPublishRecordRepo jobPublishRecordRepo,
                             JobExecuteConfigRepo jobExecuteConfigRepo,
                             DAGRepo dagRepo,
                             DataSourceApi dataSourceApi) {
        this.diJobContentRepo = diJobContentRepo;
        this.jobInfoRepo = jobInfoRepo;
        this.jobPublishRecordRepo = jobPublishRecordRepo;
        this.jobExecuteConfigRepo = jobExecuteConfigRepo;
        this.dagRepo = dagRepo;
        this.dataSourceApi = dataSourceApi;
    }

    public boolean submit(DIJobContent content, EnvEnum envEnum, String remark, Operator operator) {
        Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(content.getJobId());
        checkArgument(jobInfoOptional.isPresent(), "作业不存在或已删除");

        // 查询是否存在已提交记录
        Optional<JobPublishRecord> publishRecordOptional = jobPublishRecordRepo.query(content.getJobId(), content.getVersion(), envEnum.name());
        JobPublishRecord publishRecord = null;
        if (publishRecordOptional.isPresent()) {
            publishRecord = publishRecordOptional.get();
            PublishStatusEnum statusEnum = PublishStatusEnum.getEnum(publishRecord.getPublishStatus()).orElse(null);
            String statusDesc = statusEnum == null ? "" : statusEnum.desc;
            checkArgument(PublishStatusEnum.ARCHIVED == statusEnum, statusDesc + "版本不能多次提交");
        }
        // 未提交或已归档版本才可以再次提交
        preSubmit(content, envEnum);

        if (publishRecord == null) {
            // 第一次提交
            publishRecord = getJobPublishRecord(jobInfoOptional.get(), content, envEnum, remark, operator);
        } else {
            publishRecord.setSubmitRemark(remark);
        }
        diJobContentRepo.submit(content, publishRecord, operator.getNickname());

        postSubmit(publishRecord, operator);
        return true;
    }

    private JobPublishRecord getJobPublishRecord(JobInfo jobInfo, DIJobContent content, EnvEnum envEnum, String remark, Operator operator) {
        JobPublishRecord record = new JobPublishRecord();
        record.setJobId(content.getJobId());
        record.setJobContentId(content.getId());
        record.setJobContentVersion(content.getVersion());
        record.setJobTypeCode(jobInfo.getJobType());
        record.setDwLayerCode(jobInfo.getDwLayerCode());
        record.setEnvironment(envEnum.name());
        record.setPublishStatus(PublishStatusEnum.SUBMITTED.val);
        record.setCreator(operator.getNickname());
        record.setEditor(operator.getNickname());
        record.setSubmitRemark(remark);
        return record;
    }

    private void preSubmit(DIJobContent content, EnvEnum envEnum) {
        checkDataSource(content, envEnum);
        checkExecuteConfig(content, envEnum);
    }

    private void postSubmit(JobPublishRecord publishRecord, Operator operator) {
        // 预发环境直接发布
        if (EnvEnum.stag.name().equals(publishRecord.getEnvironment())) {
            //更新状态为发布
            toPublish(publishRecord, operator);
        }

        // todo 同步ds
    }

    private void toPublish(JobPublishRecord publishRecord, Operator operator) {
        jobPublishRecordRepo.publish(publishRecord, operator.getNickname());
    }

    public boolean publish(JobPublishRecord publishRecord, Operator operator) {
        //更新状态为发布
        toPublish(publishRecord, operator);
        postPublish(publishRecord, operator);
        return true;
    }

    private void postPublish(JobPublishRecord publishRecord, Operator operator) {
        // todo 同步ds
    }

    private void checkDataSource(DIJobContent content, EnvEnum envEnum) {
        DataSourceDto dataSourceDto = dataSourceApi.getDataSource(content.getSrcDataSourceId());
        if (ObjectUtils.isEmpty(dataSourceDto.getEnvList())
                || !dataSourceDto.getEnvList().contains(envEnum)) {
            throw new BizProcessException("来源数据源未配置" + envEnum.name() + "数据源");
        }

        dataSourceDto = dataSourceApi.getDataSource(content.getDestDataSourceId());
        if (ObjectUtils.isEmpty(dataSourceDto.getEnvList())
                || !dataSourceDto.getEnvList().contains(envEnum)) {
            throw new BizProcessException("目标数据源未配置" + envEnum.name() + "数据源");
        }
    }

    private void checkExecuteConfig(DIJobContent content, EnvEnum envEnum) {
        Optional<JobExecuteConfig> optional = jobExecuteConfigRepo.query(content.getJobId(), envEnum.name());
        checkArgument(optional.isPresent(), envEnum.name() + "环境未配置作业运行配置");

        Optional<DAGInfo> dagInfoOptional = dagRepo.queryDAGInfo(optional.get().getSchDagId());
        checkArgument(dagInfoOptional.isPresent(), "DAG不存在或已删除");
        checkArgument(Objects.equals(UsingStatusEnum.ONLINE.val, dagInfoOptional.get().getStatus()), "DAG已停用");
    }

    public boolean hasPublishPermission(Operator operator) {
        // 暂用白名单实现
        return Objects.nonNull(publishWhitelist) && publishWhitelist.indexOf(operator.getNickname()) >= 0;
    }
}
