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
import cn.zhengcaiyun.idata.develop.service.label.EnumService;
import cn.zhengcaiyun.idata.develop.dto.label.EnumDto;
import cn.zhengcaiyun.idata.develop.dto.label.EnumValueDto;
import cn.zhengcaiyun.idata.user.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author caizhedong
 * @date 2021-05-19 10:41
 */

@RestController
@RequestMapping(path = "/p1/dev")
public class EnumController {

    @Autowired
    private EnumService enumService;
    @Autowired
    private TokenService tokenService;

    @GetMapping("enum")
    public RestResult<EnumDto> findEnum(@RequestParam("enumCode") String enumCode) {
        return RestResult.success(enumService.findEnum(enumCode));
    }

    @GetMapping("enumNames")
    public RestResult<List<EnumDto>> getEnumNames() {
        return RestResult.success(enumService.getEnumNames());
    }

    @GetMapping("enumValues")
    public RestResult<List<EnumValueDto>> getEnumValues(@RequestParam("enumCode") String enumCode) {
        return RestResult.success(enumService.getEnumValues(enumCode));
    }

    @PostMapping("enum")
    public RestResult<EnumDto> createOrEdit(@RequestBody EnumDto enumDto,
                                            HttpServletRequest request) {
        return RestResult.success(enumService.createOrEdit(enumDto, tokenService.getNickname(request)));
    }

    @DeleteMapping("enum")
    public RestResult delete(@RequestParam("enumCode") String enumCode,
                             HttpServletRequest request) {
        return RestResult.success(enumService.delete(enumCode, tokenService.getNickname(request)));
    }
}
