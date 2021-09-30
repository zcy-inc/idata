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

import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${dev.env.executeQueues}")
    private String executeQueues;

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
    public RestResult<List<String>> getJobExecuteQueue() {
        List<String> queues = null;
        if (StringUtils.isNotBlank(executeQueues)) {
            queues = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(executeQueues);
        }
        return RestResult.success(queues);
    }

}
