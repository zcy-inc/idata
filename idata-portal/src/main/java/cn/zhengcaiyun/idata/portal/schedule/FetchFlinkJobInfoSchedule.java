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

package cn.zhengcaiyun.idata.portal.schedule;

import cn.zhengcaiyun.idata.develop.service.job.FlinkJobCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-05-16 16:22
 **/
@Component
public class FetchFlinkJobInfoSchedule {

    private static final Logger LOGGER = LoggerFactory.getLogger(FetchFlinkJobInfoSchedule.class);

    private final FlinkJobCommonService flinkJobCommonService;

    @Autowired
    public FetchFlinkJobInfoSchedule(FlinkJobCommonService flinkJobCommonService) {
        this.flinkJobCommonService = flinkJobCommonService;
    }

    //    @Scheduled(cron = "0 0/5 * * * ?")
    public void fetchFlinkJobInfo() {
        LOGGER.info("Start to fetchFlinkJobInfo... ...");
        flinkJobCommonService.fetchFlinkJobId();
        LOGGER.info("End to flinkJobCommonService... ...");
    }
}
