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
import cn.zhengcaiyun.idata.develop.dto.job.di.DIJobContentContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.MappingColumnDto;
import cn.zhengcaiyun.idata.develop.service.job.DIJobContentService;
import cn.zhengcaiyun.idata.portal.model.request.job.DIJobContentRequest;
import cn.zhengcaiyun.idata.portal.model.response.job.DIJobContentResponse;
import org.springframework.beans.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public RestResult<DIJobContentResponse> saveContent(@PathVariable("jobId") Long jobId,
                                                          @RequestBody @Valid DIJobContentRequest contentRequest) {
        DIJobContentContentDto contentDto = new DIJobContentContentDto();
        BeanUtils.copyProperties(contentRequest, contentDto);

        // 前端的两个字段合并成一个
        if (StringUtils.isNotEmpty(contentRequest.getSrcDbName())) {
            contentDto.setSrcTables(contentRequest.getSrcDbName() + "." + contentRequest.getSrcTables());
        }

        // mapping_sql添加AS columnName
        List<MappingColumnDto> destCols = contentDto.getDestCols();
        if (CollectionUtils.isNotEmpty(destCols)) {
            destCols.stream()
                    .filter(e -> StringUtils.isNotBlank(e.getMappingSql())
                            && !StringUtils.containsIgnoreCase(e.getMappingSql(), " AS "))
                    .forEach(e -> e.setMappingSql(e.getMappingSql() + " AS " + e.getName()));
        }
        List<MappingColumnDto> srcCols = contentDto.getSrcCols();
        if (CollectionUtils.isNotEmpty(srcCols)) {
            srcCols.stream()
                    .filter(e -> StringUtils.isNotBlank(e.getMappingSql())
                            && !StringUtils.containsIgnoreCase(e.getMappingSql(), " AS "))
                    .forEach(e -> e.setMappingSql(e.getMappingSql() + " AS " + e.getName()));
        }
        DIJobContentContentDto dto = diJobContentService.save(jobId, contentDto, OperatorContext.getCurrentOperator());
        DIJobContentResponse response = new DIJobContentResponse();
        BeanUtils.copyProperties(dto, response);

        // 1个字段拆成2个返回
        if (StringUtils.contains(dto.getSrcTables(), ".")) {
            response.setSrcDbName(dto.getSrcTables().split("\\.")[0]);
            response.setSrcTables(dto.getSrcTables().split("\\.")[1]);
        }
        return RestResult.success(response);
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
        DIJobContentContentDto dto = diJobContentService.get(jobId, version);

        DIJobContentResponse response = new DIJobContentResponse();
        BeanUtils.copyProperties(dto, response);

        // 1个字段拆成2个返回
        if (StringUtils.contains(dto.getSrcTables(), ".")) {
            response.setSrcDbName(dto.getSrcTables().split("\\.")[0]);
            response.setSrcTables(dto.getSrcTables().split("\\.")[1]);
        }

        return RestResult.success(response);
    }


}
