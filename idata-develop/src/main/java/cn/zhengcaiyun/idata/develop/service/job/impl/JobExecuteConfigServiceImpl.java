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

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.UsingStatusEnum;
import cn.zhengcaiyun.idata.develop.condition.job.JobExecuteConfigCondition;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGInfo;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobExecuteConfig;
import cn.zhengcaiyun.idata.develop.dal.repo.dag.DAGRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobExecuteConfigRepo;
import cn.zhengcaiyun.idata.develop.dto.job.JobExecuteConfigDto;
import cn.zhengcaiyun.idata.develop.service.job.JobExecuteConfigService;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-27 16:27
 **/
@Service
public class JobExecuteConfigServiceImpl implements JobExecuteConfigService {

    private final JobExecuteConfigRepo jobExecuteConfigRepo;
    private final DAGRepo dagRepo;

    @Autowired
    public JobExecuteConfigServiceImpl(JobExecuteConfigRepo jobExecuteConfigRepo,
                                       DAGRepo dagRepo) {
        this.jobExecuteConfigRepo = jobExecuteConfigRepo;
        this.dagRepo = dagRepo;
    }

    @Override
    public JobExecuteConfigDto save(Long jobId, JobExecuteConfigDto dto, Operator operator) {
        checkArgument(Objects.nonNull(jobId), "作业编号不存在");
        checkArgument(StringUtils.isNotBlank(dto.getEnvironment()), "环境参数为空");
        checkArgument(Objects.nonNull(dto.getSchDagId()), "DAG参数为空");
        checkArgument(StringUtils.isNotBlank(dto.getSchRerunMode()), "重跑属性为空");
        Optional<DAGInfo> dagInfoOptional = dagRepo.queryDAGInfo(dto.getSchDagId());
        checkArgument(dagInfoOptional.isPresent(), "DAG不存在或已删除");
        checkState(dagInfoOptional.get().getStatus().equals(UsingStatusEnum.ENABLE.val), "DAG已停用");

        Optional<JobExecuteConfig> optional = jobExecuteConfigRepo.query(jobId, dto.getEnvironment());
        if (optional.isEmpty()) {
            dto.setOperator(operator);
            JobExecuteConfig config = dto.toModel();
            config.setId(null);
            config.setJobId(jobId);
            setDefaultValue(config);
            jobExecuteConfigRepo.save(config);
        } else {
            JobExecuteConfig existConfig = optional.get();
            dto.resetEditor(operator);
            JobExecuteConfig config = dto.toModel();
            config.setId(existConfig.getId());
            config.setJobId(jobId);
            setDefaultValue(config);
            jobExecuteConfigRepo.update(config);
        }

        //todo 同步ds

        optional = jobExecuteConfigRepo.query(jobId, dto.getEnvironment());
        if (optional.isPresent()) {
            return JobExecuteConfigDto.from(optional.get());
        }
        return null;
    }

    @Override
    public List<JobExecuteConfigDto> getList(Long jobId, JobExecuteConfigCondition condition) {
        checkArgument(Objects.nonNull(jobId), "作业编号不存在");
        checkArgument(StringUtils.isNotBlank(condition.getEnvironment()), "环境参数为空");
        List<JobExecuteConfig> configList = jobExecuteConfigRepo.queryList(jobId, condition);
        if (ObjectUtils.isEmpty(configList)) return Lists.newArrayList();

        return configList.stream()
                .map(JobExecuteConfigDto::from)
                .collect(Collectors.toList());
    }

    private void setDefaultValue(JobExecuteConfig config) {
        if (Objects.isNull(config.getSchTimeOut())) config.setSchTimeOut(0);
        if (Objects.isNull(config.getSchDryRun())) config.setSchDryRun(0);
        config.setExecQueue(Strings.nullToEmpty(config.getExecQueue()));
        if (Objects.isNull(config.getExecMaxParallelism())) config.setExecMaxParallelism(0);
        config.setExecWarnLevel(Strings.nullToEmpty(config.getExecWarnLevel()));
    }
}
