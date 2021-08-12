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
import cn.zhengcaiyun.idata.connector.bean.dto.TableTechInfoDto;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import cn.zhengcaiyun.idata.develop.service.table.TableInfoService;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.table.ColumnInfoDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;
import cn.zhengcaiyun.idata.user.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author caizhedong
 * @date 2021-05-19 11:05
 */

@RestController
@RequestMapping(path = "/p1/dev")
public class TableInfoController {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private TableInfoService tableInfoService;
    @Autowired
    private ColumnInfoService columnInfoService;

    @GetMapping("tableInfo/{tableId}")
    public RestResult<TableInfoDto> findById(@PathVariable("tableId") Long tableId) {
        return RestResult.success(tableInfoService.getTableInfo(tableId));
    }

    @GetMapping("referTables")
    public RestResult<List<TableInfoDto>> getReferTables(@RequestParam("labelValue") String labelValue) {
        return RestResult.success(tableInfoService.getTablesByDataBase(labelValue));
    }

    @GetMapping("dbNames")
    public RestResult<List<LabelDto>> getDbNames() {
        return RestResult.success(tableInfoService.getDbNames());
    }

    @GetMapping("columnInfos/{tableId}")
    public RestResult<List<ColumnInfoDto>> getColumns(@PathVariable("tableId") Long tableId) {
        return RestResult.success(columnInfoService.getColumns(tableId));
    }

    @PostMapping("tableInfo")
    public RestResult<TableInfoDto> addOrUpdateTable(@RequestBody TableInfoDto tableInfoDto,
                                                     HttpServletRequest request) {
        TableInfoDto echoTableInfo;
        if (tableInfoDto.getId() != null) {
            echoTableInfo = tableInfoService.edit(tableInfoDto, tokenService.getNickname(request));
        }
        else {
            echoTableInfo = tableInfoService.create(tableInfoDto, tokenService.getNickname(request));
        }
        return RestResult.success(echoTableInfo);
    }

    @DeleteMapping("tableInfo/{tableId}")
    public RestResult deleteTable(@PathVariable("tableId") Long tableId,
                                  HttpServletRequest request) {
        return RestResult.success(tableInfoService.delete(tableId, tokenService.getNickname(request)));
    }

    @GetMapping("table/techInfo/{tableId}")
    public RestResult<TableTechInfoDto> getTableTechInfo(@PathVariable("tableId") Long tableId){
        return RestResult.success(tableInfoService.getTableTechInfo(tableId));
    }

}
