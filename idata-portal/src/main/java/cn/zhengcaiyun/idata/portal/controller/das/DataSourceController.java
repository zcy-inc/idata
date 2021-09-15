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

package cn.zhengcaiyun.idata.portal.controller.das;

import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.datasource.bean.condition.DataSourceCondition;
import cn.zhengcaiyun.idata.datasource.bean.dto.DataSourceDto;
import cn.zhengcaiyun.idata.datasource.bean.dto.DbConfigDto;
import cn.zhengcaiyun.idata.datasource.constant.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.datasource.service.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * data-source-controller
 *
 * @description: 数据库数据源管理
 * @author: yangjianhua
 * @create: 2021-07-12 15:47
 **/
@RestController
@RequestMapping(path = "/p1/das")
public class DataSourceController {

    private final DataSourceService dataSourceService;

    @Autowired
    public DataSourceController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    /**
     * 获取数据源类型
     *
     * @return
     */
    @GetMapping("/datasources/types")
    public RestResult<List<DataSourceTypeEnum>> getDataSourceTypes() {
        return RestResult.success(Arrays.stream(DataSourceTypeEnum.values()).collect(Collectors.toList()));
    }

    /**
     * 查询数据源
     *
     * @param condition 条件
     * @param limit     每页记录数
     * @param offset    偏移量
     * @return
     */
    @GetMapping("/datasources")
    public RestResult<List<DataSourceDto>> pagingDataSource(DataSourceCondition condition,
                                                            @RequestParam(value = "limit") Long limit,
                                                            @RequestParam(value = "offset") Long offset) {
        return RestResult.success();
    }

    /**
     * 创建数据源
     *
     * @param dto 数据源信息
     * @return
     */
    @PostMapping("/datasources")
    public RestResult<DataSourceDto> addDataSource(@RequestBody DataSourceDto dto) {
        return RestResult.success();
    }

    /**
     * 修改数据源
     *
     * @param dto 数据源信息
     * @return
     */
    @PutMapping("/datasources")
    public RestResult<DataSourceDto> editDataSource(@RequestBody DataSourceDto dto) {
        return RestResult.success();
    }

    /**
     * 获取数据源
     *
     * @param id 主键
     * @return
     */
    @GetMapping("/datasources/{id}")
    public RestResult<List<DataSourceDto>> getDataSource(@PathVariable("id") Long id) {
        return RestResult.success();
    }

    /**
     * 删除数据源
     *
     * @param id 主键
     * @return
     */
    @DeleteMapping("/datasources/{id}")
    public RestResult<Boolean> deleteDataSource(@PathVariable("id") Long id) {
        return RestResult.success();
    }

    /**
     * 测试连接
     *
     * @param dto 数据库配置
     * @return
     */
    @PostMapping("/datasources/test")
    public RestResult<Boolean> testDataSource(@RequestBody DbConfigDto dto) {
        return RestResult.success();
    }
}
