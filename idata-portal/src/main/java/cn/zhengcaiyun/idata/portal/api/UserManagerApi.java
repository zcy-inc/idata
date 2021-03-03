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

import cn.zhengcaiyun.idata.dto.Page;
import cn.zhengcaiyun.idata.dto.RestResult;
import cn.zhengcaiyun.idata.dto.system.FeatureTreeNodeDto;
import cn.zhengcaiyun.idata.dto.system.FolderTreeNodeDto;
import cn.zhengcaiyun.idata.dto.user.UserInfoDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shiyin
 * @date 2021-03-02 09:51
 */
@RestController
@RequestMapping(path = "/p1/uac")
public class UserManagerApi {

    @GetMapping("currentUser")
    public RestResult<UserInfoDto> getCurrentUser() {
        return RestResult.success();
    }

    @GetMapping("users")
    public RestResult<Page<UserInfoDto>> findUsers(@RequestParam("name") String name,
                                                   @RequestParam(value = "limit", required = false) Integer limit,
                                                   @RequestParam(value = "offset", required = false) Integer offset) {
        return RestResult.success();
    }

    @GetMapping("userFeatureTree/{userId}")
    public RestResult<List<FeatureTreeNodeDto>> getUserFeatureTree(@PathVariable("userId") Long userId) {
        return RestResult.success();
    }

    @GetMapping("userFolderTree/{userId}")
    public RestResult<List<FolderTreeNodeDto>> getUserFolderTree(@PathVariable("userId") Long userId) {
        return RestResult.success();
    }

    @PostMapping("user")
    public RestResult<UserInfoDto> create(@RequestBody UserInfoDto userInfoDto) {
        return RestResult.success();
    }

    @PutMapping("user")
    public RestResult<UserInfoDto> edit(@RequestBody UserInfoDto userInfoDto) {
        return RestResult.success();
    }

    @PutMapping("resetUserPassword/{userId}")
    public RestResult resetUserPassword(@PathVariable("userId") Long userId) {
        return RestResult.success();
    }

    @DeleteMapping("user/{userId}")
    public RestResult delete(@PathVariable("userId") Long userId) {
        return RestResult.success();
    }

}
