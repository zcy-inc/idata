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

package cn.zhengcaiyun.idata.portal.controller.map;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.map.bean.dto.ViewCountDto;
import cn.zhengcaiyun.idata.map.service.ViewCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * data-map-controller
 *
 * @description: 浏览热度rest接口
 * @author: yangjianhua
 * @create: 2021-07-12 15:47
 **/
@RestController
@RequestMapping(path = "/p1/map/view")
public class ViewCountController {

    private final ViewCountService viewCountService;

    @Autowired
    public ViewCountController(ViewCountService viewCountService) {
        this.viewCountService = viewCountService;
    }

    /**
     * 记录用户浏览日志
     *
     * @param entitySource 数据源：数仓表（table） or 数据指标（indicator）
     * @param entityCode   数据实体（表或指标）的唯一标识
     * @return
     */
    @PostMapping("/log")
    public RestResult<String> logUserView(@RequestParam(value = "entitySource") String entitySource,
                                          @RequestParam(value = "entityCode") String entityCode) {
        Operator operator = OperatorContext.getCurrentOperator();
        viewCountService.increase(0L, entitySource, entityCode, operator.getNickname());
        viewCountService.increase(operator.getId(), entitySource, entityCode, operator.getNickname());
        return RestResult.success();
    }

    /**
     * 全局热门排行
     *
     * @param entitySource 数据源：数仓表（table） or 数据指标（indicator）
     * @param topNum       查询排行记录数
     * @return 热门排行数据
     */
    @GetMapping("/hot")
    public RestResult<List<ViewCountDto>> hotRanking(@RequestParam(value = "entitySource") String entitySource,
                                                     @RequestParam(value = "topNum") Integer topNum) {
        return RestResult.success(viewCountService.getTopCountEntity(entitySource, topNum));
    }

    /**
     * 当前用户热门排行
     *
     * @param entitySource 数据源：数仓表（table） or 数据指标（indicator）
     * @param topNum       查询排行记录数
     * @return 热门排行数据
     */
    @GetMapping("/user/hot")
    public RestResult<List<ViewCountDto>> hotRankingOfUser(@RequestParam(value = "entitySource") String entitySource,
                                                           @RequestParam(value = "topNum") Integer topNum) {
        return RestResult.success(viewCountService.getUserTopCountEntity(entitySource, topNum, OperatorContext.getCurrentOperator()));
    }
}
