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
import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.connector.bean.dto.QueryResultDto;
import cn.zhengcaiyun.idata.datasource.bean.condition.DataSourceFileCondition;
import cn.zhengcaiyun.idata.datasource.bean.dto.DataSourceFileDto;
import cn.zhengcaiyun.idata.datasource.service.DataSourceFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * data-source-controller
 *
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-17 10:56
 **/
@RestController
@RequestMapping(path = "/p1/das")
public class DataSourceFileController {

    private final DataSourceFileService dataSourceFileService;

    @Autowired
    public DataSourceFileController(DataSourceFileService dataSourceFileService) {
        this.dataSourceFileService = dataSourceFileService;
    }

    /**
     * 查询文件型数据源
     *
     * @param condition 条件
     * @param limit     每页记录数
     * @param offset    偏移量
     * @return
     */
    @GetMapping("/datasources/files")
    public RestResult<Page<DataSourceFileDto>> pagingDataSourceFile(DataSourceFileCondition condition,
                                                                    @RequestParam(value = "limit") Long limit,
                                                                    @RequestParam(value = "offset") Long offset) {
        return RestResult.success(dataSourceFileService.pagingDataSourceFile(condition, PageParam.of(limit, offset)));
    }

    /**
     * 创建文件型数据源
     *
     * @param dto 数据源信息
     * @return
     */
    @PostMapping("/datasources/files")
    public RestResult<DataSourceFileDto> addDataSourceFile(@RequestBody DataSourceFileDto dto) {
        return RestResult.success(dataSourceFileService.addDataSourceFile(dto, OperatorContext.getCurrentOperator()));
    }

    /**
     * 上传csv文件数据源
     *
     * @param file
     * @param destTableName 表名，格式：库名.表名
     * @param environments  环境，多环境用,号分隔
     * @return
     */
    @PostMapping("/datasources/files/upload")
    public RestResult<String> uploadDataSourceFile(@RequestPart MultipartFile file,
                                                   @RequestParam(value = "destTableName") String destTableName,
                                                   @RequestParam(value = "environments") String environments) {
        checkArgument(!file.isEmpty(), "上传的文件不能为空");
        checkArgument(file.getSize() > 0, "上传的文件大小需要大于0kb");
        String originFileName = file.getOriginalFilename();
        checkArgument(StringUtils.isNotBlank(originFileName), "文件名称不能为空");
        String tableName = null;
        try {
            tableName = dataSourceFileService.importSourceFileData(file.getInputStream(), originFileName, destTableName, environments);
        } catch (IOException ex) {
            throw new GeneralException("上传文件失败", ex);
        }
        return RestResult.success(tableName);
    }

    /**
     * 预览文件数据源数据
     *
     * @param dto
     * @return
     */
    @PostMapping("/datasources/files/preview")
    public RestResult<QueryResultDto> previewDataSourceFileData(@RequestBody DataSourceFileDto dto) {
        return RestResult.success(dataSourceFileService.getDataSourceFileData(dto, PageParam.of(10L, 0L)));
    }

}
