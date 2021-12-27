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
import cn.zhengcaiyun.idata.system.dto.ResourceTypeEnum;
import cn.zhengcaiyun.idata.user.service.UserAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

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
    private final UserAccessService userAccessService;

    @Autowired
    public CompositeFolderController(CompositeFolderService compositeFolderService,
                                     UserAccessService userAccessService) {
        this.compositeFolderService = compositeFolderService;
        this.userAccessService = userAccessService;
    }

    private final String DATA_DEVELOP_ROOT_DIR_ACCESS_CODE = "F_ICON_DATA_DEVELOP_ROOT_DIR";
    private final String CONFIG_DATA_DEVELOP_ACCESS_CODE = "F_MENU_DATA_DEVELOP";

    /**
     * 搜索文件树
     *
     * @param condition
     * @return
     */
    @PostMapping("/tree")
    public RestResult<List<DevTreeNodeDto>> searchDevTree(@RequestBody DevTreeCondition condition) {
        return RestResult.success(compositeFolderService.searchDevTree(condition, OperatorContext.getCurrentOperator().getId()));
    }

    /**
     * 获取功能型文件树
     *
     * @return
     */
    @GetMapping("/functions/tree")
    public RestResult<List<DevTreeNodeDto>> getFunctionTree() {
        checkArgument(userAccessService.checkAccess(OperatorContext.getCurrentOperator().getId(), CONFIG_DATA_DEVELOP_ACCESS_CODE),
                "没有数据开发权限");
        return RestResult.success(compositeFolderService.getFunctionTree());
    }

    /**
     * 新建文件夹
     *
     * @param folderDto
     * @return
     */
    @PostMapping("")
    public RestResult<CompositeFolderDto> addFolder(@RequestBody CompositeFolderDto folderDto) {
        checkArgument(userAccessService.checkAccess(OperatorContext.getCurrentOperator().getId(), DATA_DEVELOP_ROOT_DIR_ACCESS_CODE),
                "没有根目录新增权限");
        Long id = compositeFolderService.addFolder(folderDto, OperatorContext.getCurrentOperator());
        if (Objects.isNull(id)) return RestResult.error("新建文件夹失败", "");
        return getFolder(id);
    }

    /**
     * 编辑文件夹
     *
     * @param folderDto
     * @return
     */
    @PutMapping("")
    public RestResult<CompositeFolderDto> editFolder(@RequestBody CompositeFolderDto folderDto) {
        compositeFolderService.editFolder(folderDto, OperatorContext.getCurrentOperator());
        return getFolder(folderDto.getId());
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
        checkArgument(userAccessService.checkDeleteAccess(OperatorContext.getCurrentOperator().getId(), id,
                ResourceTypeEnum.R_DATA_DEVELOP_DIR.name()), "无权限，请联系管理员");
        return RestResult.success(compositeFolderService.removeFolder(id, OperatorContext.getCurrentOperator()));
    }

    /**
     * 查询文件夹列表
     *
     * @param belong 所属功能模块标识
     * @return {@link RestResult}
     */
    @GetMapping("/folders")
    public RestResult<List<CompositeFolderDto>> getFolders(@RequestParam(value = "belong") String belong) {
        return RestResult.success(compositeFolderService.getFolders(belong));
    }

}
