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

package cn.zhengcaiyun.idata.portal.controller.ops;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.operation.bean.dto.ClusterAppMonitorDto;
import cn.zhengcaiyun.idata.operation.service.ClusterService;
import cn.zhengcaiyun.idata.user.service.UserAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * operations-controller
 *
 * @description:
 * @author: yangjianhua
 * @create: 2021-12-10 14:14
 **/
@RestController
@RequestMapping(path = "/p1/ops/clusters")
public class ClusterController {

    private final ClusterService clusterService;
    private final UserAccessService userAccessService;

    @Autowired
    public ClusterController(ClusterService clusterService,
                             UserAccessService userAccessService) {
        this.clusterService = clusterService;
        this.userAccessService = userAccessService;
    }

    private final String CLUSTER_MONITORING_ACCESS_CODE = "F_MENU_CLUSTER_MONITORING";

    /**
     * 查询集群应用
     *
     * @param state RUNNING：查询正在运行的应用
     * @return
     */
    @GetMapping("/apps")
    public RestResult<List<ClusterAppMonitorDto>> fetchClusterApp(@RequestParam(value = "state") String state) throws IllegalAccessException {
        if (!userAccessService.checkAccess(OperatorContext.getCurrentOperator().getId(), CLUSTER_MONITORING_ACCESS_CODE)) {
            throw new IllegalAccessException("没有集群监控权限");
        }
        return RestResult.success(clusterService.fetchClusterApp(state));
    }

    /**
     * 停止集群应用
     *
     * @param appId 应用id
     * @return
     */
    @PutMapping("/apps/{appId}/stop")
    public RestResult<Boolean> stopClusterApp(@PathVariable(value = "appId") String appId) {
        return RestResult.success(clusterService.stopClusterApp(appId));
    }

}
