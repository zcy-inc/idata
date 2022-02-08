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
import cn.zhengcaiyun.idata.develop.dto.measure.MeasureDto;
import cn.zhengcaiyun.idata.develop.service.measure.DimensionService;
import cn.zhengcaiyun.idata.user.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * @author caizhedong
 * @date 2021-05-25 11:10
 */

@RestController
@RequestMapping(path = "/p1/dev")
public class DimensionController {

    @Autowired
    private DimensionService dimensionService;
    @Autowired
    private TokenService tokenService;

    @GetMapping("dimension")
    public RestResult<MeasureDto> findByCode(@RequestParam("dimensionCode") String dimensionCode) {
        return RestResult.success(dimensionService.findDimension(dimensionCode));
    }

    @GetMapping("dimensionValues")
    public RestResult<List<String>> findValuesByCode(@RequestParam("dimensionCode") String dimensionCode) {
        return RestResult.success(dimensionService.findDimensionValues(dimensionCode));
    }

    @PostMapping("dimension")
    public RestResult<MeasureDto> addOrUpdateDimension(@RequestBody MeasureDto dimension,
                                                       HttpServletRequest request) {
        MeasureDto echoDimension;
        if (isEmpty(dimension.getLabelCode())) {
            echoDimension = dimensionService.create(dimension, tokenService.getNickname(request));
        }
        else {
            echoDimension = dimensionService.edit(dimension, tokenService.getNickname(request));
        }
        return RestResult.success(echoDimension);
    }

    @PostMapping("dimension/disableOrAbleDimension")
    public RestResult disableOrAbleDimension(@RequestParam("dimensionCode") String dimensionCode,
                                             @RequestParam("labelTag") String labelTag,
                                             HttpServletRequest request) {
        return RestResult.success(dimensionService.disableOrAble(dimensionCode, labelTag, tokenService.getNickname(request)));
    }

    @DeleteMapping("dimension")
    public RestResult deleteDimension(@RequestParam("dimensionCode") String dimensionCode,
                                      HttpServletRequest request) {
        return RestResult.success(dimensionService.delete(dimensionCode, tokenService.getNickname(request)));
    }
}
