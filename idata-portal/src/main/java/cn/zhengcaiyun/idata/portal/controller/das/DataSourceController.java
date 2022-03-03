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

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.datasource.bean.condition.DataSourceCondition;
import cn.zhengcaiyun.idata.datasource.bean.dto.DataSourceDto;
import cn.zhengcaiyun.idata.datasource.bean.dto.DbConfigDto;
import cn.zhengcaiyun.idata.datasource.service.DataSourceService;
import cn.zhengcaiyun.idata.user.service.UserAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

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
    private final UserAccessService userAccessService;

    @Autowired
    public DataSourceController(DataSourceService dataSourceService, UserAccessService userAccessService) {
        this.dataSourceService = dataSourceService;
        this.userAccessService = userAccessService;
    }

    private final String CONFIG_DATASOURCE_ACCESS_CODE = "F_MENU_DATASOURCE_CENTER";

    /**
     * 加载kafka topic
     */
    @GetMapping("/kafka/topics")
    public RestResult<List<String>> getTopics(@RequestParam("dataSourceId") Long dataSourceId) throws ExecutionException, InterruptedException {
        return RestResult.success(dataSourceService.getTopics(dataSourceId));
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
    public RestResult<Page<DataSourceDto>> pagingDataSource(DataSourceCondition condition,
                                                            @RequestParam(value = "limit") Long limit,
                                                            @RequestParam(value = "offset") Long offset) throws IllegalAccessException {
        if (!userAccessService.checkAccess(OperatorContext.getCurrentOperator().getId(), CONFIG_DATASOURCE_ACCESS_CODE)) {
            throw new IllegalAccessException("没有数据源管理权限");
        }
        return RestResult.success(dataSourceService.pagingDataSource(condition, PageParam.of(limit, offset)));
    }

    /**
     * 创建数据源
     *
     * @param dto 数据源信息
     * @return
     */
    @PostMapping("/datasources")
    public RestResult<DataSourceDto> addDataSource(@RequestBody DataSourceDto dto) {
        Long id = dataSourceService.addDataSource(dto, OperatorContext.getCurrentOperator());
        return getDataSource(id);
    }

    /**
     * 修改数据源
     *
     * @param dto 数据源信息
     * @return
     */
    @PutMapping("/datasources")
    public RestResult<DataSourceDto> editDataSource(@RequestBody DataSourceDto dto) {
        dataSourceService.editDataSource(dto, OperatorContext.getCurrentOperator());
        return getDataSource(dto.getId());
    }

    /**
     * 获取数据源
     *
     * @param id 主键
     * @return
     */
    @GetMapping("/datasources/{id}")
    public RestResult<DataSourceDto> getDataSource(@PathVariable("id") Long id) {
        return RestResult.success(dataSourceService.getDataSource(id));
    }

    /**
     * 删除数据源
     *
     * @param id 主键
     * @return
     */
    @DeleteMapping("/datasources/{id}")
    public RestResult<Boolean> deleteDataSource(@PathVariable("id") Long id) {
        return RestResult.success(dataSourceService.removeDataSource(id, OperatorContext.getCurrentOperator()));
    }

    /**
     * 测试连接
     *
     * @param dto 数据库配置
     * @return
     */
    @PostMapping("/datasources/test")
    public RestResult<Boolean> testConnection(@RequestBody DbConfigDto dto, DataSourceTypeEnum dataSourceType) {
        return RestResult.success(dataSourceService.testConnection(dataSourceType, dto));
    }
}
