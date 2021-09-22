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

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dto.dag.DAGDto;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 创建DAG
     *
     * @param dagDto
     * @return
     */
    @PostMapping
    public RestResult<Long> addDAG(@RequestBody DAGDto dagDto) {
        return RestResult.success();
    }

    /**
     * 编辑DAG
     *
     * @param dagDto
     * @return
     */
    @PutMapping
    public RestResult<Boolean> editDAG(@RequestBody DAGDto dagDto) {
        return RestResult.success();
    }

    /**
     * 获取DAG
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public RestResult<DAGDto> getDAG(@PathVariable Long id) {
        return RestResult.success();
    }

    /**
     * 删除DAG
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public RestResult<Boolean> removeDAG(@PathVariable("id") Long id) {
        return RestResult.success();
    }

    /**
     * 启用DAG
     *
     * @param id
     * @return
     */
    @PutMapping("/{id}/enable")
    public RestResult<Boolean> enableDAG(@PathVariable("id") Long id) {
        return RestResult.success();
    }

    /**
     * 停用DAG
     *
     * @param id
     * @return
     */
    @PutMapping("/{id}/disable")
    public RestResult<Boolean> disableDAG(@PathVariable("id") Long id) {
        return RestResult.success();
    }
}
