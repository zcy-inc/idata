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
package cn.zhengcaiyun.idata.portal.controller.uac;

import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.system.dto.FeatureTreeNodeDto;
import cn.zhengcaiyun.idata.system.dto.FolderTreeNodeDto;
import cn.zhengcaiyun.idata.user.dto.UserInfoDto;
import cn.zhengcaiyun.idata.user.service.TokenService;
import cn.zhengcaiyun.idata.user.service.UserAccessService;
import cn.zhengcaiyun.idata.user.service.UserManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author shiyin
 * @date 2021-03-02 09:51
 */
@RestController
@RequestMapping(path = "/p1/uac")
public class UserManagerController {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserAccessService userAccessService;
    @Autowired
    private UserManagerService userManagerService;

    private final String accessCode = "F_MENU_USER_FEATURE";

    @GetMapping("users")
    public RestResult<Page<UserInfoDto>> findUsers(@RequestParam(value = "name", required = false) String name,
                                                   @RequestParam(value = "limit", required = false) Integer limit,
                                                   @RequestParam(value = "offset", required = false) Integer offset,
                                                   HttpServletRequest request) throws IllegalAccessException {
        if (!userAccessService.checkAccess(tokenService.getUserId(request), accessCode)) {
            throw new IllegalAccessException("没有用户管理权限");
        }
        return RestResult.success(userManagerService.findUsers(name, limit, offset));
    }

    @GetMapping("userFeatureTree/{userId}")
    public RestResult<List<FeatureTreeNodeDto>> getUserFeatureTree(@PathVariable("userId") Long userId,
                                                                   HttpServletRequest request) throws IllegalAccessException {
        if (!userAccessService.checkAccess(tokenService.getUserId(request), accessCode)) {
            throw new IllegalAccessException("没有用户管理权限");
        }
        return RestResult.success(userAccessService.getUserFeatureTree(userId));
    }

    @GetMapping("userFolderTree/{userId}")
    public RestResult<List<FolderTreeNodeDto>> getUserFolderTree(@PathVariable("userId") Long userId,
                                                                 HttpServletRequest request) throws IllegalAccessException {
        // 浑仪接入的数据权限依赖此接口，线上IDATA激活了此校验无法跳过下面的鉴权判断，故注释，后面考虑迁移
//        if (!userAccessService.checkAccess(tokenService.getUserId(request), accessCode)) {
//            throw new IllegalAccessException("没有用户管理权限");
//        }
        return RestResult.success(userAccessService.getUserFolderTree(userId));
    }

    @PostMapping("user")
    public RestResult<UserInfoDto> create(@RequestBody UserInfoDto userInfoDto,
                                          HttpServletRequest request) throws IllegalAccessException {
        if (!userAccessService.checkAccess(tokenService.getUserId(request), accessCode)) {
            throw new IllegalAccessException("没有用户管理权限");
        }
        return RestResult.success(userManagerService.create(userInfoDto, tokenService.getNickname(request)));
    }

    @PutMapping("user")
    public RestResult<UserInfoDto> edit(@RequestBody UserInfoDto userInfoDto,
                                        HttpServletRequest request) throws IllegalAccessException {
        if (!userAccessService.checkAccess(tokenService.getUserId(request), accessCode)) {
            throw new IllegalAccessException("没有用户管理权限");
        }
        return RestResult.success(userManagerService.edit(userInfoDto, tokenService.getNickname(request)));
    }

    @PutMapping("resetUserPassword/{userId}")
    public RestResult resetUserPassword(@PathVariable("userId") Long userId,
                                        HttpServletRequest request) throws IllegalAccessException {
        if (!userAccessService.checkAccess(tokenService.getUserId(request), accessCode)) {
            throw new IllegalAccessException("没有用户管理权限");
        }
        userManagerService.resetUserPassword(userId, tokenService.getNickname(request));
        return RestResult.success();
    }

    @DeleteMapping("user/{userId}")
    public RestResult delete(@PathVariable("userId") Long userId,
                             HttpServletRequest request) throws IllegalAccessException {
        if (!userAccessService.checkAccess(tokenService.getUserId(request), accessCode)) {
            throw new IllegalAccessException("没有用户管理权限");
        }
        userManagerService.delete(userId, tokenService.getNickname(request));
        return RestResult.success();
    }

}
