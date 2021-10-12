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

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.condition.job.JobPublishRecordCondition;
import cn.zhengcaiyun.idata.develop.dto.job.JobPublishRecordDto;
import cn.zhengcaiyun.idata.develop.service.job.JobPublishRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * job-publish-controller
 *
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:51
 **/
@RestController
@RequestMapping(path = "/p1/dev/jobs/publishRecords")
public class JobPublishRecordController {

    private final JobPublishRecordService jobPublishRecordService;

    @Autowired
    public JobPublishRecordController(JobPublishRecordService jobPublishRecordService) {
        this.jobPublishRecordService = jobPublishRecordService;
    }

    /**
     * 分页查询版本发布记录
     *
     * @param condition
     * @return
     */
    @GetMapping("/page")
    public RestResult<Page<JobPublishRecordDto>> pagingJobPublishRecord(JobPublishRecordCondition condition,
                                                                        @RequestParam(value = "limit") Long limit,
                                                                        @RequestParam(value = "offset") Long offset) {
        return RestResult.success(jobPublishRecordService.paging(condition, PageParam.of(limit, offset)));
    }

    /**
     * 发布
     *
     * @param param
     * @return
     */
    @PostMapping("/approve")
    public RestResult<Boolean> approve(@RequestBody JobApproveParam param) {
        return RestResult.success(jobPublishRecordService.approve(param.getRecordIds(), param.getRemark(), OperatorContext.getCurrentOperator()));
    }

    /**
     * 驳回
     *
     * @param param
     * @return
     */
    @PostMapping("/reject")
    public RestResult<Boolean> reject(@RequestBody JobApproveParam param) {
        return RestResult.success(jobPublishRecordService.reject(param.getRecordIds(), param.getRemark(), OperatorContext.getCurrentOperator()));
    }

    public static class JobApproveParam {
        /**
         * 版本记录id
         */
        private List<Long> recordIds;
        /**
         * 发布备注
         */
        private String remark;

        public List<Long> getRecordIds() {
            return recordIds;
        }

        public void setRecordIds(List<Long> recordIds) {
            this.recordIds = recordIds;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
