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

import cn.zhengcaiyun.idata.commons.dto.Tuple3;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.connector.bean.dto.ClusterAppDto;
import cn.zhengcaiyun.idata.connector.bean.dto.FlinkJobInfoDto;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.model.opt.stream.StreamJobInstance;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.manager.FlinkJobManager;
import cn.zhengcaiyun.idata.develop.manager.StreamJobFlinkInfoManager;
import cn.zhengcaiyun.idata.develop.manager.StreamJobInstanceManager;
import cn.zhengcaiyun.idata.develop.service.job.FlinkJobCommonService;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-07-12 17:02
 **/
@Service
public class FlinkJobCommonServiceImpl implements FlinkJobCommonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlinkJobCommonServiceImpl.class);

    private final FlinkJobManager flinkJobManager;
    private final StreamJobInstanceManager streamJobInstanceManager;
    private final StreamJobFlinkInfoManager streamJobFlinkInfoManager;
    private final JobInfoRepo jobInfoRepo;

    @Autowired
    public FlinkJobCommonServiceImpl(FlinkJobManager flinkJobManager,
                                     StreamJobInstanceManager streamJobInstanceManager,
                                     StreamJobFlinkInfoManager streamJobFlinkInfoManager,
                                     JobInfoRepo jobInfoRepo) {
        this.flinkJobManager = flinkJobManager;
        this.streamJobInstanceManager = streamJobInstanceManager;
        this.streamJobFlinkInfoManager = streamJobFlinkInfoManager;
        this.jobInfoRepo = jobInfoRepo;
    }

    @Override
    public void fetchAndSetFlinkJobRunningInfo(EnvEnum envEnum) {
        // 获取flink app信息
        List<ClusterAppDto> clusterAppDtoList = flinkJobManager.fetchRunningFlinkApp(envEnum);

        Map<Long, Tuple3<ClusterAppDto, String, List<FlinkJobInfoDto>>> flinkAppMap = Maps.newHashMap();
        for (ClusterAppDto appDto : clusterAppDtoList) {
            List<FlinkJobInfoDto> flinkJobInfoDtoList = null;
            String flinkAppBaseUrl = flinkJobManager.getFlinkAppBaseUrl(appDto);
            String flinkWebUrl = null;
            String flinkApiUrl;
            if (StringUtils.isNotBlank(flinkAppBaseUrl)) {
                try {
                    flinkApiUrl = flinkAppBaseUrl + "jobs/overview";
                    flinkWebUrl = flinkAppBaseUrl + "#/overview";
                    // 获取 flink job 信息
                    flinkJobInfoDtoList = flinkJobManager.fetchFlinkJobInfo(flinkApiUrl);
                } catch (Exception ex) {
                    LOGGER.warn("Fetch flink job info exception. ClusterAppDto: {}, ex: {}", appDto, Throwables.getStackTraceAsString(ex));
                }
            }

            flinkAppMap.put(appDto.getJobId(), new Tuple3<>(appDto, StringUtils.defaultString(flinkWebUrl), flinkJobInfoDtoList));
        }

        // 更新运行实例信息
        List<StreamJobInstance> jobInstanceList = streamJobInstanceManager.queryWaitSyncStatusJobInstance(envEnum);
        if (CollectionUtils.isNotEmpty(jobInstanceList)) {
            for (StreamJobInstance jobInstance : jobInstanceList) {
                Tuple3<ClusterAppDto, String, List<FlinkJobInfoDto>> flinkAppTuple = flinkAppMap.get(jobInstance.getJobId());
                streamJobInstanceManager.updateJobInstanceStatus(jobInstance, flinkAppTuple);
            }
        }

        // 更新flink id 信息
        flinkAppMap.entrySet().stream()
                .filter(entry -> CollectionUtils.isNotEmpty(entry.getValue()._f3))
                .forEach(entry -> {
                    Long jobId = entry.getKey();
                    List<FlinkJobInfoDto> flinkJobInfoDtoList = entry.getValue()._f3;
                    Optional<JobInfo> jobInfoOptional = jobInfoRepo.queryJobInfo(jobId);
                    if (jobInfoOptional.isEmpty()) {
                        return;
                    }
                    JobInfo jobInfo = jobInfoOptional.get();
                    streamJobFlinkInfoManager.updateFlinkInfo(jobInfo, envEnum, flinkJobInfoDtoList);
                });
    }

}
