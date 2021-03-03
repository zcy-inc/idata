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
import cn.zhengcaiyun.idata.dto.user.SignInDto;
import cn.zhengcaiyun.idata.dto.user.UserInfoDto;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author shiyin
 * @date 2021-03-01 22:12
 */
@RestController
@RequestMapping(path = "/p0/uac")
public class UserApi {

    @PostMapping("signIn")
    public RestResult<UserInfoDto> signIn(@RequestBody SignInDto signInDto,
                                          HttpServletResponse response) {
        return RestResult.success();
    }

    @PostMapping("signOut")
    public RestResult signOut(HttpServletRequest request) {
        return RestResult.success();
    }

    @PostMapping("register")
    public RestResult<UserInfoDto> register(@RequestBody UserInfoDto userInfoDto) {
        return RestResult.success();
    }
}
