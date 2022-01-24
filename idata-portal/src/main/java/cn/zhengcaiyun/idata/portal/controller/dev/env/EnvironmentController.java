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

package cn.zhengcaiyun.idata.portal.controller.dev.env;

import cn.zhengcaiyun.idata.commons.dto.general.SingleCodePair;
import cn.zhengcaiyun.idata.commons.dto.general.SingleValuePair;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.constant.enums.ExecuteQueueEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.JobPriorityEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTimeOutStrategyEnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * environment-controller
 *
 * @description: 环境信息
 * @author: yangjianhua
 * @create: 2021-07-12 15:47
 **/
@RestController
@RequestMapping(path = "/p1/dev")
public class EnvironmentController {

    @GetMapping("/environments")
    public RestResult<List<EnvEnum>> getEnvironments() {
        return RestResult.success(Arrays.stream(EnvEnum.values()).collect(Collectors.toList()));
    }

    /**
     * 获取运行队列
     *
     * @return
     */
    @GetMapping("/executeQueues")
    public RestResult<List<SingleCodePair<String>>> getJobExecuteQueue() {
        return RestResult.success(Arrays.stream(ExecuteQueueEnum.values())
                .map(queueEnum -> new SingleCodePair<String>(queueEnum.code, queueEnum.name))
                .collect(Collectors.toList()));
    }

    /**
     * 获取作业优先级
     *
     * @return
     */
    @GetMapping("/JobPriorities")
    public RestResult<List<SingleValuePair<Integer>>> getJobPriorities() {
        return RestResult.success(Arrays.stream(JobPriorityEnum.values())
                .map(queueEnum -> new SingleValuePair<Integer>(queueEnum.val, queueEnum.desc))
                .collect(Collectors.toList()));
    }

    /**
     * 获取作业超时策略
     *
     * @return
     */
    @GetMapping("/JobTimeOutStrategies")
    public RestResult<List<SingleCodePair<String>>> getJobTimeOutStrategies() {
        return RestResult.success(Arrays.stream(JobTimeOutStrategyEnum.values())
                .map(queueEnum -> new SingleCodePair<String>(queueEnum.code, queueEnum.desc))
                .collect(Collectors.toList()));
    }

}
