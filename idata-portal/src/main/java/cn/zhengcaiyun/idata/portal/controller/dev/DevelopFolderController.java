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
package cn.zhengcaiyun.idata.portal.controller.dev;

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.service.folder.DevFolderService;
import cn.zhengcaiyun.idata.develop.dto.folder.DevelopFolderDto;
import cn.zhengcaiyun.idata.develop.dto.folder.DevelopFolderTreeNodeDto;
import cn.zhengcaiyun.idata.user.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author caizhedong
 * @date 2021-05-18 14:21
 */

@RestController
@RequestMapping(path = "/p1/dev")
public class DevelopFolderController {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private DevFolderService devFolderService;

    @GetMapping("devFolderTree")
    public RestResult<List<DevelopFolderTreeNodeDto>> getDevFolderTree(@RequestParam(value = "devTreeType", required = false) String devTreeType,
                                                                       @RequestParam(value = "treeNodeName", required = false) String treeNodeName) {
        return RestResult.success(devFolderService.getDevelopFolderTree(devTreeType, treeNodeName));
    }

    @GetMapping("devFolders")
    public RestResult<List<DevelopFolderDto>> getDevFolders(@RequestParam(value = "folderName", required = false) String folderName) {
        return RestResult.success(devFolderService.getDevelopFolders(folderName));
    }

    @PostMapping("devFolder")
    public RestResult<DevelopFolderDto> addDevFolder(@RequestBody DevelopFolderDto developFolderDto,
                                                     HttpServletRequest request) {
        return RestResult.success(devFolderService.create(developFolderDto, tokenService.getNickname(request)));
    }

    @PutMapping("devFolder")
    public RestResult<DevelopFolderDto> updateDevFolder(@RequestBody DevelopFolderDto developFolderDto,
                                                        HttpServletRequest request) {
        return RestResult.success(devFolderService.edit(developFolderDto, tokenService.getNickname(request)));
    }

    @DeleteMapping("devFolder/{folderId}")
    public RestResult deleteDevFolder(@PathVariable("folderId") Long folderId,
                                      HttpServletRequest request) {
        return RestResult.success(devFolderService.delete(folderId, tokenService.getNickname(request)));
    }

}
