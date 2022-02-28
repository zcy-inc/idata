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

package cn.zhengcaiyun.idata.portal.controller.dev.job.di;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIJobContentContentDto;
import cn.zhengcaiyun.idata.develop.service.job.DIJobContentService;
import cn.zhengcaiyun.idata.portal.model.request.job.DIJobContentRequest;
import cn.zhengcaiyun.idata.portal.model.response.job.DIJobContentResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * job-content-controller
 *
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 11:46
 **/
@RestController
@RequestMapping(path = "/p1/dev/jobs/{jobId}/di")
public class DIJobContentController {

    private final DIJobContentService diJobContentService;

    @Autowired
    public DIJobContentController(DIJobContentService diJobContentService) {
        this.diJobContentService = diJobContentService;
    }

    /**
     * 保存DI作业内容
     *
     * @param jobId      作业id
     * @param contentRequest 作业内容信息
     * @return
     */
    @PostMapping("/contents")
    public RestResult<DIJobContentContentDto> saveContent(@PathVariable("jobId") Long jobId,
                                                          @RequestBody DIJobContentRequest contentRequest) {
        DIJobContentContentDto contentDto = new DIJobContentContentDto();
        BeanUtils.copyProperties(contentRequest, contentDto);
        return RestResult.success(diJobContentService.save(jobId, contentDto, OperatorContext.getCurrentOperator()));
    }

    /**
     * 适配获取DI作业内容
     *
     * @param jobId   作业id
     * @param version 作业版本号
     * @return
     */
    @GetMapping("/adapt/contents/{version}")
    public RestResult<DIJobContentResponse> getAdaptContent(@PathVariable("jobId") Long jobId,
                                                       @PathVariable("version") Integer version) {
        DIJobContentContentDto diJobContentContentDto = diJobContentService.get(jobId, version);

        DIJobContentResponse response = new DIJobContentResponse();
        BeanUtils.copyProperties(diJobContentContentDto, response);

        //此处硬编码，原始数据是一个字段存两种信息，目前无法扩展，后续需要梳理枚举整合进去，目前无法融入到 JobTypeEnum
        JobTypeEnum jobType = diJobContentContentDto.getJobType();
        switch (jobType) {
            case DI_STREAM:
                response.setJobTypeDesc("集成");
                response.setSyncModeDesc("实时");
                break;
            case DI_BATCH:
                response.setJobTypeDesc("集成");
                response.setSyncModeDesc("离线");
                break;
            case BACK_FLOW_STREAM:
                response.setJobTypeDesc("回流");
                response.setSyncModeDesc("实时");
                break;
            case BACK_FLOW_BATCH:
                response.setJobTypeDesc("回流");
                response.setSyncModeDesc("离线");
                break;
        }

        // 映射转换
        return RestResult.success(response);
    }


}
