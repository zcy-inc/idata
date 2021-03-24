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

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.dto.system.FeatureTreeNodeDto;
import cn.zhengcaiyun.idata.dto.user.SignInDto;
import cn.zhengcaiyun.idata.dto.user.UserInfoDto;
import cn.zhengcaiyun.idata.user.service.TokenService;
import cn.zhengcaiyun.idata.user.service.UserManagerService;
import cn.zhengcaiyun.idata.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author shiyin
 * @date 2021-03-01 22:12
 */
@RestController
public class UserApi {

    @Autowired
    private UserService userService;
    @Autowired
    private UserManagerService userManagerService;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/p0/uac/register")
    public RestResult<UserInfoDto> register(@RequestBody UserInfoDto userInfoDto,
                                            HttpServletResponse response) {
        UserInfoDto userInfo = userService.register(userInfoDto, false, null);
        Cookie cookie = new Cookie("Authorization", userInfo.getToken());
        cookie.setPath("/");
        response.addCookie(cookie);
        return RestResult.success(userInfo);
    }

    @PostMapping("/p0/uac/signIn")
    public RestResult<UserInfoDto> signIn(@RequestBody SignInDto signInDto,
                                          HttpServletResponse response) {
        UserInfoDto userInfo = userService.signIn(signInDto);
        Cookie cookie = new Cookie("Authorization", userInfo.getToken());
        cookie.setPath("/");
        response.addCookie(cookie);
        return RestResult.success(userInfo);
    }

    @GetMapping("/p1/uac/currentUser")
    public RestResult<UserInfoDto> getCurrentUser(HttpServletRequest request) {
        return RestResult.success(userManagerService.getUserInfo(tokenService.getUserId(request)));
    }

    @GetMapping("/p1/uac/currentFeatureTree")
    public RestResult<List<FeatureTreeNodeDto>> getCurrentFeatureTree(HttpServletRequest request) {
        return RestResult.success(userManagerService.getUserFeatureTree(tokenService.getUserId(request)));
    }

    @GetMapping("/p1/uac/checkCurrentAccess")
    public RestResult<Boolean> checkCurrentAccess(@RequestParam(value = "accessCode", required = false) String accessCode,
                                                  @RequestParam(value = "accessTypes", required = false) List<String> accessTypes,
                                                  @RequestParam(value = "accessKey", required = false) String accessKey,
                                                  HttpServletRequest request) {
        if (accessCode != null) {
            return RestResult.success(userManagerService.checkAccess(tokenService.getUserId(request), accessCode));
        }
        else {
            return RestResult.success(userManagerService.checkAccess(tokenService.getUserId(request), accessTypes, accessKey));
        }
    }

    @GetMapping("/p1/uac/currentAccessKeys")
    public RestResult<List<String>> findCurrentAccessKeys(@RequestParam("accessType") String accessType,
                                                          HttpServletRequest request) {
        return RestResult.success(userManagerService.getAccessKeys(tokenService.getUserId(request), accessType));
    }

    @PostMapping("/p1/uac/signOut")
    public RestResult signOut(HttpServletRequest request) {
        userService.signOut(tokenService.getToken(request));
        return RestResult.success();
    }
}
