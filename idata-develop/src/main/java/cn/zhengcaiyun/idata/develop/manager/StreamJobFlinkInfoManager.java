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

import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.connector.bean.dto.FlinkJobInfoDto;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIStreamJobContent;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.model.opt.stream.StreamJobFlinkInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIStreamJobContentRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.opt.stream.StreamJobFlinkInfoRepo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-08-23 20:23
 **/
@Component
public class StreamJobFlinkInfoManager {

    private final StreamJobFlinkInfoRepo streamJobFlinkInfoRepo;
    private final DIStreamJobContentRepo streamJobContentRepo;

    @Autowired
    public StreamJobFlinkInfoManager(StreamJobFlinkInfoRepo streamJobFlinkInfoRepo,
                                     DIStreamJobContentRepo streamJobContentRepo) {
        this.streamJobFlinkInfoRepo = streamJobFlinkInfoRepo;
        this.streamJobContentRepo = streamJobContentRepo;
    }

    @Transactional
    public void updateFlinkInfo(final JobInfo jobInfo, final EnvEnum envEnum, List<FlinkJobInfoDto> flinkJobInfoDtoList) {
        if (CollectionUtils.isEmpty(flinkJobInfoDtoList)) {
            return;
        }
        if (JobTypeEnum.SQL_FLINK.getCode().equals(jobInfo.getJobType())) {
            updateFlinkSqlInfo(jobInfo.getId(), envEnum, flinkJobInfoDtoList.get(0));
        } else if (JobTypeEnum.DI_STREAM.getCode().equals(jobInfo.getJobType())) {
            updateDIStreamFlinkInfo(jobInfo.getId(), envEnum, flinkJobInfoDtoList);
        }
    }

    @Transactional
    public void updateFlinkSqlInfo(Long jobId, EnvEnum envEnum, FlinkJobInfoDto flinkJobInfoDto) {
        if (StringUtils.isBlank(flinkJobInfoDto.getJid())) {
            return;
        }
        String env = envEnum.name();
        refreshFlinkInfo(jobId, env, null, flinkJobInfoDto.getJid());
    }

    @Transactional
    public void updateDIStreamFlinkInfo(Long jobId, EnvEnum envEnum, List<FlinkJobInfoDto> flinkJobInfoDtoList) {
        String env = envEnum.name();
        Optional<DIStreamJobContent> contentOptional = streamJobContentRepo.queryLatest(jobId);
        if (contentOptional.isEmpty()) {
            return;
        }
        DIStreamJobContent jobContent = contentOptional.get();
        if ((DataSourceTypeEnum.mysql.name().equals(jobContent.getSrcDataSourceType())
                || DataSourceTypeEnum.postgresql.name().equals(jobContent.getSrcDataSourceType()))
                && DataSourceTypeEnum.starrocks.name().equals(jobContent.getDestDataSourceType())) {
            for (FlinkJobInfoDto flinkJobInfoDto : flinkJobInfoDtoList) {
                if (StringUtils.isBlank(flinkJobInfoDto.getJid())) {
                    continue;
                }

                String name = flinkJobInfoDto.getName();
                String sep = "-" + jobId + "-";
                int sep_idx = name.indexOf(sep);
                if (sep_idx > 0) {
                    String destTable = name.substring(sep_idx + sep.length());
                    if (StringUtils.isNotBlank(destTable)) {
                        refreshFlinkInfo(jobId, env, destTable, flinkJobInfoDto.getJid());
                    }
                }
            }
        } else if ((DataSourceTypeEnum.mysql.name().equals(jobContent.getSrcDataSourceType())
                || DataSourceTypeEnum.postgresql.name().equals(jobContent.getSrcDataSourceType()))
                && DataSourceTypeEnum.kafka.name().equals(jobContent.getDestDataSourceType())) {
            refreshFlinkInfo(jobId, env, null, flinkJobInfoDtoList.get(0).getJid());
        }
    }

    private void refreshFlinkInfo(Long jobId, String env, String secondaryId, String flinkJobId) {
        List<StreamJobFlinkInfo> jobFlinkInfoList = streamJobFlinkInfoRepo.queryList(jobId, env, secondaryId);
        if (CollectionUtils.isEmpty(jobFlinkInfoList)) {
            addFlinkInfo(jobId, env, secondaryId, flinkJobId);
        } else {
            StreamJobFlinkInfo existFlinkInfo = jobFlinkInfoList.get(0);
            if (Objects.equals(flinkJobId, existFlinkInfo.getFlinkJobId())) {
                return;
            }

            streamJobFlinkInfoRepo.delete(jobId, env, secondaryId);
            addFlinkInfo(jobId, env, secondaryId, flinkJobId);
        }
    }

    private Long addFlinkInfo(Long jobId, String env, String secondaryId, String flinkJobId) {
        StreamJobFlinkInfo flinkInfo = new StreamJobFlinkInfo();
        flinkInfo.setJobId(jobId);
        flinkInfo.setSecondaryId(StringUtils.defaultString(secondaryId));
        flinkInfo.setEnvironment(env);
        flinkInfo.setFlinkJobId(flinkJobId);
        return streamJobFlinkInfoRepo.save(flinkInfo);
    }
}
