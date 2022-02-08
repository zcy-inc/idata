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
import cn.zhengcaiyun.idata.connector.bean.dto.SingleColumnResultDto;
import cn.zhengcaiyun.idata.develop.dto.query.DimDataQueryDto;
import cn.zhengcaiyun.idata.develop.service.measure.DimensionDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-21 18:58
 **/
@RestController
@RequestMapping(path = "/p1/dev/dimension")
public class DimensionDataController {
    @Autowired
    private DimensionDataService dimensionDataService;

    @PostMapping("/dataQuery")
    RestResult<SingleColumnResultDto> queryDimensionData(@RequestBody DimDataQueryDto dataQueryDto) {
        return RestResult.success(dimensionDataService.queryDimensionData(dataQueryDto));
    }
}
