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
import cn.zhengcaiyun.idata.commons.enums.DriverTypeEnum;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.constant.enums.DestWriteModeEnum;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIJobContentContentDto;
import cn.zhengcaiyun.idata.develop.service.job.DIJobContentService;
import cn.zhengcaiyun.idata.portal.model.request.job.DIJobContentContentRequest;
import cn.zhengcaiyun.idata.portal.model.request.job.GenerateMergeSqlRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
                                                          @RequestBody DIJobContentContentRequest contentRequest) {
        DIJobContentContentDto contentDto = new DIJobContentContentDto();
        BeanUtils.copyProperties(contentRequest, contentDto);
        return RestResult.success(diJobContentService.save(jobId, contentDto, OperatorContext.getCurrentOperator()));
    }

    /**
     * 获取DI作业内容
     *
     * @param jobId   作业id
     * @param version 作业版本号
     * @return
     */
    @GetMapping("/contents/{version}")
    public RestResult<DIJobContentContentDto> getContent(@PathVariable("jobId") Long jobId,
                                                         @PathVariable("version") Integer version) {
        return RestResult.success(diJobContentService.get(jobId, version));
    }

    /**
     * 自动生成mergeSql
     * @param jobId
     * @return
     */
    @PostMapping("/generate/merge-sql")
    public RestResult<String> defaultMergeSql(@PathVariable("jobId") Long jobId,
                                              @RequestBody @Valid GenerateMergeSqlRequest request) {
        String destWriteMode = request.getDestWriteMode();
        DestWriteModeEnum diMode = DestWriteModeEnum.valueOf(destWriteMode);
        if (diMode != DestWriteModeEnum.append) {
            return RestResult.success("");
        }
        String destTable = request.getDestTable();
        String[] hiveTableSplit = destTable.split("\\.");
        if (hiveTableSplit.length != 2) {
            throw new IllegalArgumentException("The hive table must have db name");
        }

        // 格式化输入列，为后续处理做准备
        request.formatColumns();

        String driverType = request.getDriverType();
        String sourceTable = request.getSourceTable();
        String keyColumns = request.getKeyColumns();
        return RestResult.success(diJobContentService.generateMergeSql(request.getSelectColumnList(), keyColumns, sourceTable, destTable, diMode, DriverTypeEnum.of(driverType)));
    }



}
