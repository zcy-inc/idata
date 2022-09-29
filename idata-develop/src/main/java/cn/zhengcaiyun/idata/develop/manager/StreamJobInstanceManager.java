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

import cn.zhengcaiyun.idata.commons.dto.Tuple3;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.connector.bean.dto.ClusterAppDto;
import cn.zhengcaiyun.idata.connector.bean.dto.FlinkJobInfoDto;
import cn.zhengcaiyun.idata.develop.condition.opt.stream.StreamJobInstanceCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.StreamJobInstanceStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.model.opt.stream.StreamJobInstance;
import cn.zhengcaiyun.idata.develop.dal.repo.opt.stream.StreamJobInstanceRepo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-08-22 17:44
 **/
@Component
public class StreamJobInstanceManager {

    private final StreamJobInstanceRepo streamJobInstanceRepo;

    @Autowired
    public StreamJobInstanceManager(StreamJobInstanceRepo streamJobInstanceRepo) {
        this.streamJobInstanceRepo = streamJobInstanceRepo;
    }

    public List<StreamJobInstance> queryWaitSyncStatusJobInstance(EnvEnum envEnum) {
        StreamJobInstanceCondition condition = new StreamJobInstanceCondition();
        condition.setEnvironment(envEnum.name());
        condition.setStatusList(Arrays.asList(StreamJobInstanceStatusEnum.STARTING.val, StreamJobInstanceStatusEnum.RUNNING.val, StreamJobInstanceStatusEnum.FAILED.val));
        return streamJobInstanceRepo.queryList(condition);
    }

    public void updateJobInstanceStatus(StreamJobInstance jobInstance, Tuple3<ClusterAppDto, String, List<FlinkJobInfoDto>> flinkAppTuple) {
        if (jobInstance.getStatus().equals(StreamJobInstanceStatusEnum.STARTING.val)) {
            updateJobInstanceStatusFromStarting(jobInstance, flinkAppTuple);
        } else if (jobInstance.getStatus().equals(StreamJobInstanceStatusEnum.RUNNING.val)) {
            updateJobInstanceStatusFromRunning(jobInstance, flinkAppTuple);
        } else if (jobInstance.getStatus().equals(StreamJobInstanceStatusEnum.FAILED.val)) {
            updateJobInstanceStatusFromFailed(jobInstance, flinkAppTuple);
        }
    }

    public void updateJobInstanceStatusFromStarting(StreamJobInstance jobInstance, Tuple3<ClusterAppDto, String, List<FlinkJobInfoDto>> flinkAppTuple) {
        LocalDateTime startTime = LocalDateTime.ofInstant(jobInstance.getEditTime().toInstant(), ZoneId.systemDefault());
        if (Objects.isNull(flinkAppTuple)) {
            LocalDateTime secondsBefore = LocalDateTime.now().minusSeconds(300);
            if (startTime.isBefore(secondsBefore)) {
                // 300 秒后没有获取到 running yarn app，设置为失败状态
                streamJobInstanceRepo.updateStatus(jobInstance.getId(), StreamJobInstanceStatusEnum.FAILED,
                        jobInstance.getStatus());
            }
        } else {
            List<FlinkJobInfoDto> flinkJobInfoDtoList = flinkAppTuple._f3;
            StreamJobInstanceStatusEnum flinkJobStatus = parseStatusFromStarting(startTime, flinkJobInfoDtoList);
            if (StreamJobInstanceStatusEnum.STARTING != flinkJobStatus) {
                StreamJobInstance updateInstance = new StreamJobInstance();
                updateInstance.setId(jobInstance.getId());
                updateInstance.setExternalUrl(StringUtils.defaultString(flinkAppTuple._f2));
                if (StreamJobInstanceStatusEnum.RUNNING == flinkJobStatus) {
                    updateInstance.setRunStartTime(parseJobStartTime(flinkAppTuple));
                }
                streamJobInstanceRepo.update(updateInstance);
                streamJobInstanceRepo.updateStatus(jobInstance.getId(), flinkJobStatus, jobInstance.getStatus());
            }
        }
    }

    public void updateJobInstanceStatusFromRunning(StreamJobInstance jobInstance, Tuple3<ClusterAppDto, String, List<FlinkJobInfoDto>> flinkAppTuple) {
        LocalDateTime runningTime = LocalDateTime.ofInstant(jobInstance.getEditTime().toInstant(), ZoneId.systemDefault());
        if (Objects.isNull(flinkAppTuple)) {
            LocalDateTime secondsBefore = LocalDateTime.now().minusSeconds(60);
            if (runningTime.isBefore(secondsBefore)) {
                // 60秒后没有获取到 running yarn app，设置为失败状态
                streamJobInstanceRepo.updateStatus(jobInstance.getId(), StreamJobInstanceStatusEnum.FAILED, jobInstance.getStatus());
            }
        } else {
            List<FlinkJobInfoDto> flinkJobInfoDtoList = flinkAppTuple._f3;
            StreamJobInstanceStatusEnum flinkJobStatus = parseStatusFromRunning(runningTime, flinkJobInfoDtoList);
            if (StreamJobInstanceStatusEnum.RUNNING != flinkJobStatus) {
                streamJobInstanceRepo.updateStatus(jobInstance.getId(), flinkJobStatus, jobInstance.getStatus());
            }
        }
    }

    public void updateJobInstanceStatusFromFailed(StreamJobInstance jobInstance, Tuple3<ClusterAppDto, String, List<FlinkJobInfoDto>> flinkAppTuple) {
        LocalDateTime runningTime = LocalDateTime.ofInstant(jobInstance.getEditTime().toInstant(), ZoneId.systemDefault());
        if (Objects.isNull(flinkAppTuple)) {
            LocalDateTime secondsBefore = LocalDateTime.now().minusSeconds(60);
            if (runningTime.isBefore(secondsBefore)) {
                // 60 秒后没有获取到 running yarn app，设置为失败状态
                streamJobInstanceRepo.updateStatus(jobInstance.getId(), StreamJobInstanceStatusEnum.FAILED, jobInstance.getStatus());
            }
        } else {
            List<FlinkJobInfoDto> flinkJobInfoDtoList = flinkAppTuple._f3;
            StreamJobInstanceStatusEnum flinkJobStatus = parseStatusFromRunning(runningTime, flinkJobInfoDtoList);
            if (StreamJobInstanceStatusEnum.RUNNING != flinkJobStatus) {
                streamJobInstanceRepo.updateStatus(jobInstance.getId(), flinkJobStatus, jobInstance.getStatus());
            }
        }
    }

    public StreamJobInstanceStatusEnum parseStatusFromStarting(LocalDateTime startTime, List<FlinkJobInfoDto> flinkJobInfoDtoList) {
        if (CollectionUtils.isEmpty(flinkJobInfoDtoList)) {
            LocalDateTime secondsBefore = LocalDateTime.now().minusSeconds(300);
            if (startTime.isBefore(secondsBefore)) {
                // 300 秒后没有获取到 flink job，设置为失败状态
                return StreamJobInstanceStatusEnum.FAILED;
            } else {
                return StreamJobInstanceStatusEnum.STARTING;
            }
        }

        int failed_count = 0;
        int starting_count = 0;
        int running_count = 0;
        for (FlinkJobInfoDto flinkJobInfoDto : flinkJobInfoDtoList) {
            StreamJobInstanceStatusEnum flinkJobStatus = parseStatus(flinkJobInfoDto);
            if (StreamJobInstanceStatusEnum.FAILED == flinkJobStatus) {
                failed_count++;
            } else if (StreamJobInstanceStatusEnum.STARTING == flinkJobStatus) {
                starting_count++;
            } else if (StreamJobInstanceStatusEnum.RUNNING == flinkJobStatus) {
                running_count++;
            }
        }

        if (failed_count > 0) {
            return StreamJobInstanceStatusEnum.FAILED;
        }
        if (starting_count > 0) {
            return StreamJobInstanceStatusEnum.STARTING;
        }
        if (running_count > 0) {
            return StreamJobInstanceStatusEnum.RUNNING;
        }
        return StreamJobInstanceStatusEnum.STARTING;
    }

    public StreamJobInstanceStatusEnum parseStatusFromRunning(LocalDateTime runningTime, List<FlinkJobInfoDto> flinkJobInfoDtoList) {
        if (CollectionUtils.isEmpty(flinkJobInfoDtoList)) {
            LocalDateTime secondsBefore = LocalDateTime.now().minusSeconds(60);
            if (runningTime.isBefore(secondsBefore)) {
                // 60 秒后没有获取到 flink job，设置为失败状态
                return StreamJobInstanceStatusEnum.FAILED;
            } else {
                return StreamJobInstanceStatusEnum.RUNNING;
            }
        }

        int failed_count = 0;
        int running_count = 0;
        for (FlinkJobInfoDto flinkJobInfoDto : flinkJobInfoDtoList) {
            StreamJobInstanceStatusEnum flinkJobStatus = parseStatus(flinkJobInfoDto);
            if (StreamJobInstanceStatusEnum.RUNNING == flinkJobStatus) {
                running_count++;
            } else {
                failed_count++;
            }
        }

        if (failed_count > 0) {
            return StreamJobInstanceStatusEnum.FAILED;
        }
        return StreamJobInstanceStatusEnum.RUNNING;
    }

    public StreamJobInstanceStatusEnum parseStatus(FlinkJobInfoDto flinkJobInfoDto) {
        /**
         * Created
         * Running
         * Finished
         * Failed
         * Canceled
         * Failing
         * Canceling
         * Restarting
         * Suspended
         */
        String state = flinkJobInfoDto.getState();
        if ("created".equalsIgnoreCase(state)) {
            return StreamJobInstanceStatusEnum.STARTING;
        } else if ("Running".equalsIgnoreCase(state) || "Finished".equalsIgnoreCase(state)) {
            return StreamJobInstanceStatusEnum.RUNNING;
        } else {
            return StreamJobInstanceStatusEnum.FAILED;
        }
    }

    private Date parseJobStartTime(Tuple3<ClusterAppDto, String, List<FlinkJobInfoDto>> flinkAppTuple) {
        if (Objects.isNull(flinkAppTuple)) return null;

        List<FlinkJobInfoDto> jobInfoDtoList = flinkAppTuple._f3;
        if (CollectionUtils.isEmpty(jobInfoDtoList)) return null;

        Date startTime = null;
        for (FlinkJobInfoDto jobInfoDto : jobInfoDtoList) {
            Long startTimestamp = jobInfoDto.getStartTime();
            if (Objects.nonNull(startTimestamp)) {
                try {
                    startTime = Date.from(Instant.ofEpochMilli(startTimestamp));
                } catch (Exception ex) {
                }
            }
            if (Objects.nonNull(startTime)) break;
        }
        return startTime;
    }
}
