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

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.condition.tree.DevTreeCondition;
import cn.zhengcaiyun.idata.develop.dto.folder.CompositeFolderDto;
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
@RequestMapping(path = "/p1/dev/compositeFolders")
public class CompositeFolderController {

    private final CompositeFolderService compositeFolderService;

    @Autowired
    public CompositeFolderController(CompositeFolderService compositeFolderService) {
        this.compositeFolderService = compositeFolderService;
    }

    /**
     * 搜索文件树
     *
     * @param condition
     * @return
     */
    @PostMapping("/tree")
    RestResult<List<DevTreeNodeDto>> searchDevTree(@RequestBody DevTreeCondition condition) {
        return RestResult.success(compositeFolderService.searchDevTree(condition));
    }

    /**
     * 获取功能型文件树
     *
     * @return
     */
    @GetMapping("/functions/tree")
    RestResult<List<DevTreeNodeDto>> getFunctionTree() {
        return RestResult.success(compositeFolderService.getFunctionTree());
    }

    /**
     * 新建文件夹
     *
     * @param folderDto
     * @return
     */
    @PostMapping("")
    public RestResult<Long> addFolder(@RequestBody CompositeFolderDto folderDto) {
        return RestResult.success(compositeFolderService.addFolder(folderDto, OperatorContext.getCurrentOperator()));
    }

    /**
     * 编辑文件夹
     *
     * @param folderDto
     * @return
     */
    @PutMapping("")
    public RestResult<Boolean> editFolder(@RequestBody CompositeFolderDto folderDto) {
        return RestResult.success(compositeFolderService.editFolder(folderDto, OperatorContext.getCurrentOperator()));
    }

    /**
     * 查询文件夹
     *
     * @param id 文件夹id
     * @return {@link RestResult}
     */
    @GetMapping("/{id}")
    public RestResult<CompositeFolderDto> getFolder(@PathVariable Long id) {
        return RestResult.success(compositeFolderService.getFolder(id));
    }

    /**
     * 删除文件夹
     *
     * @param id 文件夹id
     * @return {@link RestResult}
     */
    @DeleteMapping("/{id}")
    public RestResult<Boolean> removeFolder(@PathVariable("id") Long id) {
        return RestResult.success(compositeFolderService.removeFolder(id, OperatorContext.getCurrentOperator()));
    }

    /**
     * 查询文件夹列表
     *
     * @param belong 所属功能模块标识
     * @return {@link RestResult}
     */
    @GetMapping("")
    public RestResult<List<CompositeFolderDto>> getFolders(@RequestParam(value = "belong") String belong) {
        return RestResult.success(compositeFolderService.getFolders(belong));
    }

}
