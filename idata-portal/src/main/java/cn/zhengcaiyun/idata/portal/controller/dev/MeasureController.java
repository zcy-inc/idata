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

import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dto.folder.DevelopFolderTreeNodeDto;
import cn.zhengcaiyun.idata.develop.dto.measure.MeasureDto;
import cn.zhengcaiyun.idata.develop.service.measure.MeasureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author caizhedong
 * @date 2022-03-21 下午9:20
 */

@RestController
@RequestMapping(path = "/p1/dev")
public class MeasureController {

    @Autowired
    private MeasureService measureService;

    @GetMapping("measures")
    public RestResult<Page<MeasureDto>> getMetrics(@RequestParam("measureType") String measureType,
                                                   @RequestParam(value = "folderId", required = false) Long folderId,
                                                   @RequestParam(value = "metricType", required = false) String metricType,
                                                   @RequestParam(value = "measureId", required = false) String measureId,
                                                   @RequestParam(value = "measureName", required = false) String measureName,
                                                   @RequestParam(value = "bizProcess", required = false) String bizProcess,
                                                   @RequestParam(value = "enable", required = false) Boolean enable,
                                                   @RequestParam(value = "creator", required = false) String creator,
                                                   @RequestParam(value = "measureDeadline", required = false) String measureDeadline,
                                                   @RequestParam(value = "domain", required = false) String domain,
                                                   @RequestParam(value = "belongTblName", required = false) String belongTblName,
                                                   @RequestParam(value = "limit", required = false) Long limit,
                                                   @RequestParam(value = "offset", required = false) Integer offset) {
        return RestResult.success(measureService.getMeasures(folderId, measureType, metricType, measureId, measureName,
                bizProcess, enable, creator, measureDeadline, domain, belongTblName, limit, offset));
    }
}
