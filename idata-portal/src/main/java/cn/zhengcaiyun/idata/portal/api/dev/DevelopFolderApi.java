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
package cn.zhengcaiyun.idata.portal.api.dev;

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.dto.dev.folder.DevFolderDto;
import cn.zhengcaiyun.idata.dto.dev.folder.FolderTreeNodeDto;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author caizhedong
 * @date 2021-05-18 14:21
 */

@RestController
@RequestMapping(path = "/p1/dev")
public class DevelopFolderApi {

    @GetMapping("devFolderTree")
    public RestResult<FolderTreeNodeDto> getDevFolderTree(@RequestParam(value = "devTreeType", required = false) String devTreeType) {
        return RestResult.success();
    }

    @GetMapping("devFolder/{folderId}")
    public RestResult<DevFolderDto> findById(@PathVariable("folderId") Long folderId) {
        return RestResult.success();
    }


    @PostMapping("devFolder")
    public RestResult<DevFolderDto> addDevFolder(@RequestBody DevFolderDto devFolderDto,
                                                 HttpServletRequest request) {
        return RestResult.success();
    }

    @PutMapping("devFolder")
    public RestResult<DevFolderDto> updateDevFolder(@RequestBody DevFolderDto devFolderDto,
                                                    HttpServletRequest request) {
        return RestResult.success();
    }

    @DeleteMapping("devFolder/{folderId}")
    public RestResult deleteDevFolder(@PathVariable("folderId") Long folderId,
                                      HttpServletRequest request) {
        return RestResult.success();
    }

}
