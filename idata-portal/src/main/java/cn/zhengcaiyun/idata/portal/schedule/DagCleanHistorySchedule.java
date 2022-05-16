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

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.develop.condition.dag.DAGInfoCondition;
import cn.zhengcaiyun.idata.develop.dto.dag.DAGInfoDto;
import cn.zhengcaiyun.idata.develop.service.dag.DAGService;
import com.google.common.base.Throwables;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-05-16 16:22
 **/
@Component
public class DagCleanHistorySchedule {

    private static final Logger LOGGER = LoggerFactory.getLogger(DagCleanHistorySchedule.class);

    private final DAGService dagService;

    public DagCleanHistorySchedule(DAGService dagService) {
        this.dagService = dagService;
    }

    @Scheduled(cron = "0 10 23 * * ?")
    public void cleanDagHistory() {
        LOGGER.info("Start to cleanDagHistory... ...");
        DAGInfoCondition condition = new DAGInfoCondition();
        List<DAGInfoDto> dagInfoDtoList = dagService.getDAGInfoList(condition);
        if (CollectionUtils.isEmpty(dagInfoDtoList)) {
            return;
        }

        Operator operator = new Operator.Builder(0L).nickname("系统定时").build();
        for (DAGInfoDto dagInfoDto : dagInfoDtoList) {
            try {
                boolean suc = dagService.cleanDagHistory(dagInfoDto.getId(), operator);
                if (suc) {
                    LOGGER.info("Clean Dag {} successfully.", dagInfoDto.getId());
                } else {
                    LOGGER.info("Clean Dag {} failed.", dagInfoDto.getId());
                }
            } catch (Exception ex) {
                LOGGER.warn("Clean Dag {} exception. ex: {}", dagInfoDto.getId(), Throwables.getStackTraceAsString(ex));
            }
        }
        LOGGER.info("End to cleanDagHistory... ...");
    }
}
