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

package cn.zhengcaiyun.idata.portal.controller.dev.opt.stream;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.condition.opt.stream.StreamJobInstanceCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.StreamJobInstanceStatusEnum;
import cn.zhengcaiyun.idata.develop.dto.opt.stream.StreamJobInstanceDto;
import cn.zhengcaiyun.idata.develop.dto.opt.stream.StreamJobRunParamDto;
import cn.zhengcaiyun.idata.develop.service.opt.stream.StreamJobInstanceService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Stream-Instance-Controller
 *
 * @description: 实时作业运行实例管理
 * @author: yangjianhua
 * @create: 2022-08-25 15:14
 **/
@RestController
@RequestMapping(path = "/p1/opt/stream/instances")
public class StreamJobInstanceController {

    private final StreamJobInstanceService streamJobInstanceService;

    public StreamJobInstanceController(StreamJobInstanceService streamJobInstanceService) {
        this.streamJobInstanceService = streamJobInstanceService;
    }

    /**
     * 分页查询运行实例
     *
     * @param condition 查询条件
     * @param limit
     * @param offset
     * @return
     * @throws IllegalAccessException
     */
    @PostMapping("/page")
    public RestResult<Page<StreamJobInstanceDto>> pagingStreamJobInstance(@RequestBody StreamJobInstanceCondition condition) throws IllegalAccessException {
        // todo 加权限控制
        if (CollectionUtils.isEmpty(condition.getStatusList())) {
            condition.setStatusList(StreamJobInstanceStatusEnum.getNotDestroyedStatusValList());
        }
        return RestResult.success(streamJobInstanceService.paging(condition));
    }

    /**
     * 启动
     *
     * @param id          运行实例id
     * @param runParamDto 启动参数
     * @return
     * @throws IllegalAccessException
     */
    @PostMapping("/{id}/start")
    public RestResult<Boolean> start(@PathVariable Long id,
                                     @RequestBody StreamJobRunParamDto runParamDto) throws IllegalAccessException {
        // todo 加权限控制
        return RestResult.success(streamJobInstanceService.start(id, runParamDto, OperatorContext.getCurrentOperator()));
    }

    /**
     * 停止
     *
     * @param id 运行实例id
     * @return
     * @throws IllegalAccessException
     */
    @PostMapping("/{id}/stop")
    public RestResult<Boolean> stop(@PathVariable Long id) throws IllegalAccessException {
        // todo 加权限控制
        return RestResult.success(streamJobInstanceService.stop(id, OperatorContext.getCurrentOperator()));
    }

    /**
     * 下线
     *
     * @param id 运行实例id
     * @return
     * @throws IllegalAccessException
     */
    @PostMapping("/{id}/destroy")
    public RestResult<Boolean> destroy(@PathVariable Long id) throws IllegalAccessException {
        // todo 加权限控制
        return RestResult.success(streamJobInstanceService.destroy(id, OperatorContext.getCurrentOperator()));
    }

    /**
     * 查询强制初始化表集合
     *
     * @param id 运行实例id
     * @return
     */
    @GetMapping("/{id}/forceInitTables")
    public RestResult<List<String>> getForceInitTable(@PathVariable Long id) {
        return RestResult.success(streamJobInstanceService.getForceInitTable(id));
    }
}
