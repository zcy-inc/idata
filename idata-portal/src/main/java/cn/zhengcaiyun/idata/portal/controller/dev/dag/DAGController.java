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

package cn.zhengcaiyun.idata.portal.controller.dev.dag;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.condition.dag.DAGInfoCondition;
import cn.zhengcaiyun.idata.develop.dto.dag.DAGDto;
import cn.zhengcaiyun.idata.develop.dto.dag.DAGInfoDto;
import cn.zhengcaiyun.idata.develop.service.dag.DAGService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * dag-controller
 *
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-22 14:51
 **/
@RestController
@RequestMapping(path = "/p1/dev/dags")
public class DAGController {

    private final DAGService dagService;

    @Autowired
    public DAGController(DAGService dagService) {
        this.dagService = dagService;
    }

    /**
     * 创建DAG
     *
     * @param dagDto
     * @return
     */
    @PostMapping
    public RestResult<DAGDto> addDAG(@RequestBody DAGDto dagDto) {
        Long id = dagService.addDAG(dagDto, OperatorContext.getCurrentOperator());
        if (Objects.isNull(id)) return RestResult.error("创建DAG失败", "");

        return getDAG(id);
    }

    /**
     * 编辑DAG
     *
     * @param dagDto
     * @return
     */
    @PutMapping
    public RestResult<DAGDto> editDAG(@RequestBody DAGDto dagDto) {
        Boolean ret = dagService.editDAG(dagDto, OperatorContext.getCurrentOperator());
        if (BooleanUtils.isFalse(ret)) return RestResult.error("编辑DAG失败", "");

        return getDAG(dagDto.getDagInfoDto().getId());
    }

    /**
     * 获取DAG
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public RestResult<DAGDto> getDAG(@PathVariable Long id) {
        return RestResult.success(dagService.getDag(id));
    }

    /**
     * 删除DAG
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RestResult<Boolean> removeDAG(@PathVariable("id") Long id) {
        return RestResult.success(dagService.removeDag(id, OperatorContext.getCurrentOperator()));
    }

    /**
     * 上线DAG
     *
     * @param id
     * @return
     */
    @PutMapping("/{id}/online")
    public RestResult<Boolean> online(@PathVariable("id") Long id) {
        return RestResult.success(dagService.online(id, OperatorContext.getCurrentOperator()));
    }

    /**
     * 下线DAG
     *
     * @param id
     * @return
     */
    @PutMapping("/{id}/offline")
    public RestResult<Boolean> offline(@PathVariable("id") Long id) {
        return RestResult.success(dagService.offline(id, OperatorContext.getCurrentOperator()));
    }

    /**
     * 查询dag列表
     *
     * @param condition 查询条件
     * @return
     */
    @GetMapping("/info")
    public RestResult<List<DAGInfoDto>> getDAGInfoList(DAGInfoCondition condition) {
        return RestResult.success(dagService.getDAGInfoList(condition));
    }

    /**
     * 保存DAG依赖
     *
     * @param id
     * @param param
     * @return
     */
    @PostMapping("/{id}/dependencies")
    @Deprecated
    public RestResult<Boolean> saveDependence(@PathVariable("id") Long id,
                                              @RequestBody DependenceParam param) {
        return RestResult.success(dagService.saveDependence(id, param.getDependenceIds(), OperatorContext.getCurrentOperator()));
    }

    public static class DependenceParam {
        /**
         * 依赖DAG id
         */
        private List<Long> dependenceIds;

        public List<Long> getDependenceIds() {
            return dependenceIds;
        }

        public void setDependenceIds(List<Long> dependenceIds) {
            this.dependenceIds = dependenceIds;
        }
    }

}
