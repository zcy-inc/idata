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
import cn.zhengcaiyun.idata.dto.user.UserInfoDto;
import cn.zhengcaiyun.idata.user.service.TokenService;
import cn.zhengcaiyun.idata.user.service.UserAccessService;
import cn.zhengcaiyun.idata.user.service.UserManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author shiyin
 * @date 2021-03-30 15:31
 */
@RestController
@RequestMapping(path = "/p1/uac")
public class UserAccessApi {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserManagerService userManagerService;
    @Autowired
    private UserAccessService userAccessService;

    @GetMapping("/currentUser")
    public RestResult<UserInfoDto> getCurrentUser(HttpServletRequest request) {
        return RestResult.success(userManagerService.getUserInfo(tokenService.getUserId(request)));
    }

    @GetMapping("/currentFeatureTree")
    public RestResult<List<FeatureTreeNodeDto>> getCurrentFeatureTree(HttpServletRequest request) {
        return RestResult.success(userAccessService.getUserFeatureTree(tokenService.getUserId(request)));
    }

    @GetMapping("/checkCurrentAccess")
    public RestResult<Boolean> checkCurrentAccess(@RequestParam(value = "accessCode", required = false) String accessCode,
                                                  @RequestParam(value = "accessTypes", required = false) List<String> accessTypes,
                                                  @RequestParam(value = "accessKey", required = false) String accessKey,
                                                  HttpServletRequest request) {
        if (accessCode != null) {
            return RestResult.success(userAccessService.checkAccess(tokenService.getUserId(request), accessCode));
        }
        else {
            return RestResult.success(userAccessService.checkAccess(tokenService.getUserId(request), accessTypes, accessKey));
        }
    }

    @GetMapping("/currentAccessKeys")
    public RestResult<List<String>> findCurrentAccessKeys(@RequestParam("accessType") String accessType,
                                                          HttpServletRequest request) {
        return RestResult.success(userAccessService.getAccessKeys(tokenService.getUserId(request), accessType));
    }

}
