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

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dto.job.JobInfoDto;
import cn.zhengcaiyun.idata.develop.dto.job.JobTypeDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * job-basic-controller
 *
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-23 10:24
 **/
@RestController
@RequestMapping(path = "/p1/dev/jobs")
public class JobInfoController {

    /**
     * 获取作业类型
     *
     * @param catalog 作业分类，DI：数据集成作业（获取数据集成分类下的作业类型：离线作业、实时作业），为空时获取所有作业类型
     * @return
     */
    @GetMapping("/types")
    public RestResult<List<JobTypeDto>> getJobType(String catalog) {
        return RestResult.success();
    }

    /**
     * 新增作业
     *
     * @param jobInfoDto 作业基础信息
     * @return
     */
    @PostMapping
    public RestResult<Long> addJobInfo(@RequestBody JobInfoDto jobInfoDto) {
        return RestResult.success();
    }

    /**
     * 编辑作业信息
     *
     * @param jobInfoDto 作业基础信息
     * @return
     */
    @PutMapping
    public RestResult<Boolean> editJobInfo(@RequestBody JobInfoDto jobInfoDto) {
        return RestResult.success();
    }

    /**
     * 获取作业信息
     *
     * @param id 作业id
     * @return
     */
    @GetMapping("/{id}")
    public RestResult<JobInfoDto> getJobInfo(@PathVariable Long id) {
        return RestResult.success();
    }

    /**
     * 删除作业
     *
     * @param id 作业id
     * @return
     */
    @DeleteMapping("/{id}")
    public RestResult<Boolean> removeJobInfo(@PathVariable Long id) {
        return RestResult.success();
    }

    /**
     * 恢复作业
     *
     * @param id 作业id
     * @return
     */
    @PutMapping("/{id}/enable")
    public RestResult<Boolean> enableJobInfo(@PathVariable Long id) {
        return RestResult.success();
    }

    /**
     * 暂停作业
     *
     * @param id 作业id
     * @return
     */
    @PutMapping("/{id}/disable")
    public RestResult<Boolean> disableJobInfo(@PathVariable Long id) {
        return RestResult.success();
    }

}
