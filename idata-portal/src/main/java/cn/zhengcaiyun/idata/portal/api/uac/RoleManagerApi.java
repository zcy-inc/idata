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
package cn.zhengcaiyun.idata.portal.api.uac;

import cn.zhengcaiyun.idata.commons.encrypt.RandomUtil;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.system.dto.FeatureTreeNodeDto;
import cn.zhengcaiyun.idata.system.dto.FolderTreeNodeDto;
import cn.zhengcaiyun.idata.user.dto.RoleDto;
import cn.zhengcaiyun.idata.user.service.RoleService;
import cn.zhengcaiyun.idata.user.service.TokenService;
import cn.zhengcaiyun.idata.user.service.UserAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author shiyin
 * @date 2021-03-03 00:51
 */
@RestController
@RequestMapping("p1/uac")
public class RoleManagerApi {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserAccessService userAccessService;

    private final String accessCode = "F_MENU_ROLE_MANAGE";

    @GetMapping("roles")
    public RestResult<Page<RoleDto>> findRoles(@RequestParam(value = "limit", required = false) Integer limit,
                                               @RequestParam(value = "offset", required = false) Integer offset,
                                               HttpServletRequest request) {
        checkArgument(userAccessService.checkAccess(tokenService.getUserId(request), accessCode),
                "没有角色管理权限");
        return RestResult.success(roleService.findRoles(limit, offset));
    }

    @GetMapping("roleFeatureTree/{roleId}")
    public RestResult<List<FeatureTreeNodeDto>> getRoleFeatureTree(@PathVariable("roleId") Long roleId,
                                                                   HttpServletRequest request) {
        checkArgument(userAccessService.checkAccess(tokenService.getUserId(request), accessCode),
                "没有角色管理权限");
        return RestResult.success(roleService.getRoleFeatureTree(roleId));
    }

    @GetMapping("roleFolderTree/{roleId}")
    public RestResult<List<FolderTreeNodeDto>> getRoleFolderTree(@PathVariable("roleId") Long roleId,
                                                                 HttpServletRequest request) {
        checkArgument(userAccessService.checkAccess(tokenService.getUserId(request), accessCode),
                "没有角色管理权限");
        return RestResult.success(roleService.getRoleFolderTree(roleId));
    }

    @PostMapping("role")
    public RestResult<RoleDto> create(@RequestBody RoleDto roleDto,
                                      HttpServletRequest request) {
        checkArgument(userAccessService.checkAccess(tokenService.getUserId(request), accessCode),
                "没有角色管理权限");
        roleDto.setRoleCode(RandomUtil.randomStr(10));
        return RestResult.success(roleService.create(roleDto, tokenService.getNickname(request)));
    }

    @PutMapping("role")
    public RestResult<RoleDto> edit(@RequestBody RoleDto roleDto,
                                    HttpServletRequest request) {
        checkArgument(userAccessService.checkAccess(tokenService.getUserId(request), accessCode),
                "没有角色管理权限");
        return RestResult.success(roleService.edit(roleDto, tokenService.getNickname(request)));
    }

    @DeleteMapping("role/{roleId}")
    public RestResult delete(@PathVariable("roleId") Long roleId,
                             HttpServletRequest request) {
        checkArgument(userAccessService.checkAccess(tokenService.getUserId(request), accessCode),
                "没有角色管理权限");
        roleService.delete(roleId, tokenService.getNickname(request));
        return RestResult.success();
    }

}
