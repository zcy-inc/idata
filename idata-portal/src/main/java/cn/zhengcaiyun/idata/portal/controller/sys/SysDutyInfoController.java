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

package cn.zhengcaiyun.idata.portal.controller.sys;

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.system.service.SysDutyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * sys-duty-controller
 *
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-13 14:34
 **/
@RestController
@RequestMapping(path = "/p0/sys/duty")
public class SysDutyInfoController {

    private final SysDutyInfoService sysDutyInfoService;

    @Autowired
    public SysDutyInfoController(SysDutyInfoService sysDutyInfoService) {
        this.sysDutyInfoService = sysDutyInfoService;
    }

    /**
     * 获取值班电话
     *
     * @return
     */
    @GetMapping("/phone")
    public RestResult<String> getDutyPhone() {
        Optional<String> optional = sysDutyInfoService.findDutyPhone(null);
        return RestResult.success(optional.orElse(null));
    }
}
