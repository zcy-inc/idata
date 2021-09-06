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
import cn.zhengcaiyun.idata.develop.dto.folder.DevelopFolderDto;
import cn.zhengcaiyun.idata.develop.dto.label.EnumDto;
import cn.zhengcaiyun.idata.develop.dto.measure.MeasureDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;
import cn.zhengcaiyun.idata.portal.config.MigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-08-16 18:56
 */

@RestController
@RequestMapping(path = "/p1/dev")
public class MigrationController {

    @Autowired
    private MigrationService migrationService;

    @PostMapping("syncBizProcess")
    @Transactional(rollbackFor = Throwable.class)
    public RestResult<EnumDto> syncBizProcess(){
        return RestResult.success(migrationService.syncBizProcess());
    }

    @PostMapping("syncTableFolder")
    @Transactional(rollbackFor = Throwable.class)
    public RestResult<List<DevelopFolderDto>> syncTableFolder(@RequestParam("parentFolderId") Long parentFolderId,
                                                              @RequestParam("parentFolderName") String parentFolderName){
        return RestResult.success(migrationService.syncFolderAndTables(parentFolderId, parentFolderName));
    }

    @PostMapping("syncTable")
    @Transactional(rollbackFor = Throwable.class)
    public RestResult<List<TableInfoDto>> syncTable(@RequestParam(value = "tableId", required = false) Long tableId){
        return RestResult.success(migrationService.syncTableData(tableId));
    }

    @PostMapping("syncForeignKey")
    @Transactional(rollbackFor = Throwable.class)
    public RestResult<Boolean> syncForeignKey(@RequestParam(value = "tableId", required = false) Long tableId){
        return RestResult.success(migrationService.syncForeignKeys(tableId));
    }

    @PostMapping("syncDimensions")
    @Transactional(rollbackFor = Throwable.class)
    public RestResult<List<MeasureDto>> syncDimensions() {
        return RestResult.success(migrationService.syncDimensions());
    }

    @PostMapping("syncModifierEnum")
    @Transactional(rollbackFor = Throwable.class)
    public RestResult<List<EnumDto>> syncModifierEnum() {
        return RestResult.success(migrationService.syncModifierEnum());
    }

    @PostMapping("syncModifiers")
    @Transactional(rollbackFor = Throwable.class)
    public RestResult<List<MeasureDto>> syncModifiers() {
        return RestResult.success(migrationService.syncModifiers());
    }

    @PostMapping("syncMetrics")
    @Transactional(rollbackFor = Throwable.class)
    public RestResult<List<MeasureDto>> syncMetrics(@RequestParam("metricType") String metricType) {
        return RestResult.success(migrationService.syncMetrics(metricType));
    }

}
