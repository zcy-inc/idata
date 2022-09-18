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

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.user.dto.AppInfoDto;
import cn.zhengcaiyun.idata.user.dto.RoleDto;
import cn.zhengcaiyun.idata.user.service.AppFeatureService;
import com.sun.xml.bind.v2.schemagen.xmlschema.Appinfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author caizhedong
 * @date 2022-09-18 下午1:47
 */

@RestController
@RequestMapping("p1/uac")
public class AppFeatureController {

    @Autowired
    private AppFeatureService appFeatureService;

    @GetMapping("/apps")
    public RestResult<Page<AppInfoDto>> findApps(@RequestParam(value = "limit", required = false) Integer limit,
                                                 @RequestParam(value = "offset", required = false) Integer offset) {
        limit = limit != null ? limit : (int) PageParam.DEFAULT_LIMIT;
        offset = offset != null ? offset : 0;
        return RestResult.success(appFeatureService.findApps(limit, offset));
    }

    @PostMapping("/app")
    public RestResult<AppInfoDto> add(@RequestBody AppInfoDto appInfoDto) {
        checkArgument(StringUtils.isNotEmpty(appInfoDto.getAppName()), "ID不能为空");
        checkArgument(StringUtils.isNotEmpty(appInfoDto.getFeatureCodes()), "ID不能为空");

        Operator operator = OperatorContext.getCurrentOperator();
        appInfoDto.setCreator(operator.getNickname());
        return RestResult.success(appFeatureService.add(appInfoDto));
    }

    @PutMapping("/app")
    public RestResult<AppInfoDto> update(@RequestBody AppInfoDto appInfoDto) {
        checkArgument(appInfoDto.getId() != null, "ID不能为空");

        Operator operator = OperatorContext.getCurrentOperator();
        appInfoDto.setEditor(operator.getNickname());
        return RestResult.success(appFeatureService.update(appInfoDto));
    }
}
