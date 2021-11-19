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

package cn.zhengcaiyun.idata.develop.dto.job;

import cn.zhengcaiyun.idata.develop.dal.model.job.JobConfigCombination;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobDependence;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobExecuteConfig;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobOutput;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-18 14:45
 **/
public class JobConfigCombinationDto {

    /**
     * 作业运行调度配置
     */
    private JobExecuteConfigDto executeConfig;
    /**
     * 作业依赖配置
     */
    private List<JobDependenceDto> dependencies;
    /**
     * 作业输出配置
     */
    private JobOutputDto output;

    public JobExecuteConfigDto getExecuteConfig() {
        return executeConfig;
    }

    public void setExecuteConfig(JobExecuteConfigDto executeConfig) {
        this.executeConfig = executeConfig;
    }

    public List<JobDependenceDto> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<JobDependenceDto> dependencies) {
        this.dependencies = dependencies;
    }

    public JobOutputDto getOutput() {
        return output;
    }

    public void setOutput(JobOutputDto output) {
        this.output = output;
    }

    public static JobConfigCombinationDto from(JobConfigCombination model) {
        JobExecuteConfig executeConfigModel = model.getExecuteConfig();
        List<JobDependence> dependenceList = model.getDependenceList();
        Optional<JobOutput> outputOptional = model.getOutputOptional();

        JobConfigCombinationDto dto = new JobConfigCombinationDto();
        dto.setExecuteConfig(JobExecuteConfigDto.from(executeConfigModel));
        if (!CollectionUtils.isEmpty(dependenceList)) {
            dto.setDependencies(dependenceList.stream()
                    .map(JobDependenceDto::from)
                    .collect(Collectors.toList()));
        }
        if (outputOptional.isPresent()) {
            dto.setOutput(JobOutputDto.from(outputOptional.get()));
        }
        return dto;
    }
}
