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

package cn.zhengcaiyun.idata.portal.controller.dev.job;

import cn.zhengcaiyun.idata.commons.dto.general.SingleCodePair;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.condition.dag.DAGInfoCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.ExecuteQueueEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dto.dag.DAGInfoDto;
import cn.zhengcaiyun.idata.develop.service.dag.DAGService;
import cn.zhengcaiyun.idata.develop.service.job.DIStreamJobContentService;
import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * job-assistance-controller
 *
 * @description: 作业辅助功能
 * @author: yangjianhua
 * @create: 2022-08-18 15:06
 **/
@RestController
@RequestMapping(path = "/p1/dev/jobs/assistance")
public class JobAssistanceController {

    private final DAGService dagService;
    private final DIStreamJobContentService diStreamJobContentService;

    @Value("stream.job.dag.pattern:Flink-Dummy-")
    private String streamDagPattern;

    @Autowired
    public JobAssistanceController(DAGService dagService,
                                   DIStreamJobContentService diStreamJobContentService) {
        this.dagService = dagService;
        this.diStreamJobContentService = diStreamJobContentService;
    }

    /**
     * 查询作业可用dag
     *
     * @param cond 查询条件
     * @return
     */
    @GetMapping("/dags/list")
    public RestResult<List<DAGInfoDto>> getDAGList(DagCond cond) {
        List<DAGInfoDto> dagInfoDtoList;
        if (Objects.equals(JobTypeEnum.DI_STREAM, cond.jobType)
                || Objects.equals(JobTypeEnum.SQL_FLINK, cond.jobType)) {
            cond.setNamePattern(streamDagPattern);
            dagInfoDtoList = dagService.getDAGInfoList(cond);
        } else {
            dagInfoDtoList = dagService.getDAGInfoList(cond);
            dagInfoDtoList = dagInfoDtoList.stream()
                    .filter(dto -> !dto.getName().startsWith(streamDagPattern))
                    .collect(Collectors.toList());
        }
        return RestResult.success(dagInfoDtoList);
    }

    /**
     * 查询作业运行队列
     *
     * @param jobType 作业类型
     * @return
     */
    @GetMapping("/executeQueues")
    public RestResult<List<SingleCodePair<String>>> getJobExecuteQueue(@RequestParam(value = "jobType", required = false) JobTypeEnum jobType) {
        if (Objects.equals(JobTypeEnum.DI_STREAM, jobType)
                || Objects.equals(JobTypeEnum.SQL_FLINK, jobType)) {
            return RestResult.success(Arrays.stream(ExecuteQueueEnum.values())
                    .filter(queueEnum -> queueEnum.code.contains("realtime"))
                    .map(queueEnum -> new SingleCodePair<String>(queueEnum.code, queueEnum.name))
                    .collect(Collectors.toList()));
        } else {
            return RestResult.success(Arrays.stream(ExecuteQueueEnum.values())
                    .filter(queueEnum -> queueEnum.code.contains("offline"))
                    .map(queueEnum -> new SingleCodePair<String>(queueEnum.code, queueEnum.name))
                    .collect(Collectors.toList()));
        }
    }

    /**
     * 获取转换目标表
     *
     * @param srcDataSourceType  数据来源-数据源类型
     * @param srcDataSourceId    数据来源-数据源id
     * @param destDataSourceType 数据去向-数据源类型
     * @param srcTables          数据来源表，多个表以 , 号分隔
     * @param enableSharding     是否开启分表支持，0：否，1：是
     * @return
     */
    @GetMapping("/DIStreamDestTable")
    public RestResult<List<String>> transformDIStreamDestTable(@RequestParam(value = "srcDataSourceType") String srcDataSourceType,
                                                               @RequestParam(value = "srcDataSourceId") Long srcDataSourceId,
                                                               @RequestParam(value = "destDataSourceType") String destDataSourceType,
                                                               @RequestParam(value = "srcTables") String srcTables,
                                                               @RequestParam(value = "enableSharding") Integer enableSharding) {
        checkArgument(StringUtils.isNotBlank(srcTables), "获取目标表名 - 来源表名为空");
        List<String> srcTableList = Splitter.on(",").omitEmptyStrings().splitToList(srcTables);
        return RestResult.success(diStreamJobContentService.transformDestTable(srcDataSourceType, srcDataSourceId, destDataSourceType, srcTableList, enableSharding));
    }

    public static class DagCond extends DAGInfoCondition {
        /**
         * 作业类型
         */
        private JobTypeEnum jobType;

        public JobTypeEnum getJobType() {
            return jobType;
        }

        public void setJobType(JobTypeEnum jobType) {
            this.jobType = jobType;
        }
    }

}
