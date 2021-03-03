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
package cn.zhengcaiyun.idata.portal.api;

import cn.zhengcaiyun.idata.dto.RestResult;
import cn.zhengcaiyun.idata.dto.system.FeatureTreeNodeDto;
import cn.zhengcaiyun.idata.dto.system.FolderTreeNodeDto;
import cn.zhengcaiyun.idata.dto.system.SystemStateDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author shiyin
 * @date 2021-03-02 11:02
 */
@RestController
@RequestMapping(path = "/p0/sys")
public class SystemApi {

    @GetMapping("systemState")
    public RestResult<SystemStateDto> getSystemState() {
        return RestResult.success();
    }

    @GetMapping("systemFeatureTree")
    public RestResult<List<FeatureTreeNodeDto>> getSystemFeatureTree() {
        return RestResult.success();
    }

    @GetMapping("systemFolderTree")
    public RestResult<List<FolderTreeNodeDto>> getSystemFolderTree() {
        return RestResult.success();
    }
}
