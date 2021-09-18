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

package cn.zhengcaiyun.idata.portal.controller.dev.folder;

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.condition.tree.DevTreeCondition;
import cn.zhengcaiyun.idata.develop.dto.tree.DevTreeNodeDto;
import cn.zhengcaiyun.idata.develop.service.folder.CompositeFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * composite-folder-controller
 *
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-17 17:07
 **/
@RestController
@RequestMapping(path = "/p1/dev/folders")
public class CompositeFolderController {

    private final CompositeFolderService compositeFolderService;

    @Autowired
    public CompositeFolderController(CompositeFolderService compositeFolderService) {
        this.compositeFolderService = compositeFolderService;
    }

    @PostMapping("/tree")
    RestResult<List<DevTreeNodeDto>> searchDevTree(@RequestBody DevTreeCondition condition) {
        return RestResult.success(compositeFolderService.searchDevTree(condition));
    }

    @GetMapping("/functions/tree")
    RestResult<List<DevTreeNodeDto>> getFunctionTree() {
        return RestResult.success(compositeFolderService.getFunctionTree());
    }

}
