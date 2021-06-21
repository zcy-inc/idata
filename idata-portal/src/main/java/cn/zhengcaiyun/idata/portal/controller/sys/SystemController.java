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
import cn.zhengcaiyun.idata.system.dto.FeatureTreeNodeDto;
import cn.zhengcaiyun.idata.system.dto.FolderTreeNodeDto;
import cn.zhengcaiyun.idata.system.dto.SystemStateDto;
import cn.zhengcaiyun.idata.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @author shiyin
 * @date 2021-03-02 11:02
 */
@RestController
public class SystemController {

    @Autowired
    private SystemService systemService;

    @GetMapping("/p0/sys/state")
    public RestResult<SystemStateDto> getSystemState() {
        return RestResult.success(systemService.getSystemState());
    }

    @GetMapping("/p1/sys/featureTree")
    public RestResult<List<FeatureTreeNodeDto>> getSystemFeatureTree() {
        return RestResult.success(systemService.getFeatureTree(SystemService.FeatureTreeMode.FULL, null));
    }

    @GetMapping("/p1/sys/folderTree")
    public RestResult<List<FolderTreeNodeDto>> getSystemFolderTree() {
        return RestResult.success(systemService.getFolderTree(new HashMap<>()));
    }
}
