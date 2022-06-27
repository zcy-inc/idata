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
import cn.zhengcaiyun.idata.connector.spi.hive.dto.CompareInfoNewDTO;
import cn.zhengcaiyun.idata.develop.dal.model.DevTableInfo;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.table.ColumnDetailsDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableDdlDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;
import cn.zhengcaiyun.idata.connector.spi.hive.dto.CompareInfoDTO;
import cn.zhengcaiyun.idata.connector.spi.hive.dto.SyncHiveDTO;
import cn.zhengcaiyun.idata.develop.dto.table.*;
import cn.zhengcaiyun.idata.develop.facade.ColumnFacade;
import cn.zhengcaiyun.idata.develop.facade.MetadataFacade;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import cn.zhengcaiyun.idata.develop.service.table.TableInfoService;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.portal.model.request.dev.PullHiveInfoRequest;
import cn.zhengcaiyun.idata.portal.model.response.dev.CompareInfoResponse;
import cn.zhengcaiyun.idata.portal.model.response.dev.PullHiveResponse;
import cn.zhengcaiyun.idata.user.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
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

    @Autowired
    private MetadataFacade metadataFacade;

    @Autowired
    private ColumnFacade columnFacade;

    @GetMapping("tableInfo/{tableId}")
    public RestResult<TableInfoDto> findById(@PathVariable("tableId") Long tableId) {
        TableInfoDto tableInfo = tableInfoService.getTableInfo(tableId);
//        metadataFacade.markDiff(tableInfo);
        return RestResult.success(tableInfo);
    }

    @GetMapping("tableInfo/tableName")
    public RestResult<TableInfoDto> findByName(@RequestParam("tableName") String tableName) {
        TableInfoDto tableInfo = tableInfoService.getTableInfoByName(tableName);
        return RestResult.success(tableInfo);
    }

    @GetMapping("referTables")
    public RestResult<List<TableInfoDto>> getReferTables(@RequestParam("labelValue") String labelValue) {
        return RestResult.success(tableInfoService.getTablesByDataBase(labelValue));
    }

    @GetMapping("dbNames")
    public RestResult<List<LabelDto>> getDbNames() {
        return RestResult.success(tableInfoService.getDbNames());
    }

    @GetMapping("foreignKeyTables")
    public RestResult<List<DevTableInfo>> getForeignKeyTables(@RequestParam("tableId") Long tableId) {
        return RestResult.success(tableInfoService.getForeignKeyTables(tableId));
    }

    @GetMapping("columnInfos/{tableId}")
    public RestResult<List<ColumnDetailsDto>> getColumns(@PathVariable("tableId") Long tableId) {
        return RestResult.success(columnInfoService.getColumnDetails(tableId));
    }

    @GetMapping("dimensionColumnInfos/{tableId}")
    public RestResult<List<ColumnInfoDto>> getDimensionColumns(@PathVariable("tableId") Long tableId,
                                                               @RequestParam("metricCode") String metricCode) {
        return RestResult.success(columnInfoService.getDimensionColumns(metricCode, tableId));
    }

    @GetMapping("tables")
    public RestResult<List<DevTableInfo>> getTables(@RequestParam(value = "tableName", required = false) String tableName) {
        return RestResult.success(tableInfoService.getTablesByCondition(tableName));
    }

    @GetMapping("tableDdl/{tableId}")
    public RestResult<String> getTableDDL(@PathVariable("tableId") Long tableId) {
        return RestResult.success(tableInfoService.getTableDDL(tableId));
    }

    @PostMapping("tableInfo")
    public RestResult<TableInfoDto> addOrUpdateTable(@RequestBody TableInfoDto tableInfoDto,
                                                     HttpServletRequest request) throws IllegalAccessException {
        TableInfoDto echoTableInfo;
        if (tableInfoDto.getId() != null) {
            echoTableInfo = tableInfoService.edit(tableInfoDto, tokenService.getNickname(request));
        }
        else {
            echoTableInfo = tableInfoService.create(tableInfoDto, tokenService.getNickname(request));
        }
        return RestResult.success(echoTableInfo);
    }

    /**
     * DDL模式生成表结构
     * @param tableDdlDto
     * @return
     */
    @PostMapping("ddl/syncTableInfo")
    public RestResult<TableInfoDto> syncTableInfo(@RequestBody TableDdlDto tableDdlDto) {
        return RestResult.success(tableInfoService.syncTableInfoByDDL(tableDdlDto));
    }

    @PostMapping("syncMetabaseInfo/{tableId}")
    public RestResult<String> syncMetabaseInfo(@PathVariable("tableId") Long tableId,
                                               HttpServletRequest request) {
        return RestResult.success(tableInfoService.syncMetabaseInfo(tableId, tokenService.getNickname(request)));
    }

    @DeleteMapping("tableInfo/{tableId}")
    public RestResult deleteTable(@PathVariable("tableId") Long tableId,
                                  HttpServletRequest request) throws IllegalAccessException {
        return RestResult.success(tableInfoService.delete(tableId, tokenService.getNickname(request)));
    }

    @GetMapping("table/techInfo/{tableId}")
    public RestResult<TableTechInfoDto> getTableTechInfo(@PathVariable("tableId") Long tableId){
        return RestResult.success(tableInfoService.getTableTechInfo(tableId));
    }

    /**
     * 将表元数据信息同步至HIVE，若是修改则进行增量操作，删减字段不做修改。
     * 删减字段不做修改原因：删除字段会删除hive表中的数据，风险大
     */
    @PostMapping("/syncHiveInfo/{tableId}")
    public RestResult<SyncHiveDTO> syncHiveInfo(HttpServletRequest request, @PathVariable("tableId") Long tableId) {
        String nickname = tokenService.getNickname(request);
        return RestResult.success(metadataFacade.syncMetadataToHive(tableId, nickname));
    }

    /**
     * 比较本地表和远端hive表的区别
     * @param tableId
     * @return
     */
    @PostMapping("/compareHiveInfo/{tableId}")
    public RestResult<CompareInfoDTO> compareHiveInfo(@PathVariable("tableId") Long tableId) {
        return RestResult.success(metadataFacade.compareHive(tableId));
    }

    /**
     * 比较hive不同的相关信息提示
     * @param tableId
     * @return
     */
    @GetMapping("/pull/hive/info/{tableId}")
    public RestResult<CompareInfoResponse> pullHiveInfo(@PathVariable("tableId") Long tableId) {
        TableInfoDto tableInfo = tableInfoService.getTableInfo(tableId);
        CompareInfoNewDTO compareInfoNewDTO = columnFacade.compare(tableInfo.getDbName(), tableInfo.getTableName(), tableInfo.getColumnInfos());

        CompareInfoResponse response = new CompareInfoResponse();
        List<CompareInfoResponse.ChangeContentInfo> changeContentInfoList = new ArrayList<>();
        response.setChangeContentInfoList(changeContentInfoList);

        compareInfoNewDTO.getLessList().forEach(e -> {
            CompareInfoResponse.ChangeContentInfo changeContentInfo = new CompareInfoResponse.ChangeContentInfo();
            changeContentInfo.setColumnNameBefore("-");
            changeContentInfo.setColumnNameAfter(e.getHiveColumnName());
            changeContentInfo.setChangeDescription("字段新增");
            changeContentInfo.setChangeType(1);

            changeContentInfoList.add(changeContentInfo);
        });

        compareInfoNewDTO.getMoreList().forEach(e -> {
            CompareInfoResponse.ChangeContentInfo changeContentInfo = new CompareInfoResponse.ChangeContentInfo();
            changeContentInfo.setColumnNameBefore(e.getColumnName());
            changeContentInfo.setColumnNameAfter("-");
            changeContentInfo.setChangeDescription("字段删除");
            changeContentInfo.setChangeType(2);

            changeContentInfoList.add(changeContentInfo);
        });

        compareInfoNewDTO.getDifferentList().forEach(e -> {
            CompareInfoResponse.ChangeContentInfo changeContentInfo = new CompareInfoResponse.ChangeContentInfo();
            changeContentInfo.setColumnNameBefore(e.getColumnName());
            changeContentInfo.setColumnNameAfter(e.getHiveColumnName());
            changeContentInfo.setChangeType(3);

            StringBuilder stringBuilder = new StringBuilder("字段修改\n");
            if (e.getHiveColumnIndex() != e.getColumnIndex()) {
                stringBuilder.append("排序变更：" + e.getColumnIndex() + " 改为 " + e.getHiveColumnIndex() + "\n");
            }
            if (!StringUtils.equalsIgnoreCase(e.getColumnType(), e.getHiveColumnType())) {
                stringBuilder.append("字段类型：" + e.getColumnType() + " 改为 " + e.getHiveColumnType() + "\n");
            }
            if (!StringUtils.equalsIgnoreCase(e.getColumnComment(), e.getHiveColumnComment())) {
                stringBuilder.append("描述：" + e.getColumnComment() + " 改为 " + e.getHiveColumnComment() + "\n");
            }
            if (e.isPartition() != e.isHivePartition()) {
                stringBuilder.append("是否分区字段：" + e.isPartition() + " 改为 " + e.isHivePartition() + "\n");
            }
            changeContentInfo.setChangeDescription(stringBuilder.toString());

            changeContentInfoList.add(changeContentInfo);
        });

        return RestResult.success(response);
    }

    /**
     * 拉取hive信息
     * @param tableId
     * @return
     */
    @PostMapping("/pull/hive/{tableId}")
    public RestResult<Boolean> pullHive(@PathVariable("tableId") Long tableId) {
        TableInfoDto tableInfo = tableInfoService.getTableInfo(tableId);

        CompareInfoNewDTO compareInfoNewDTO = columnFacade.compare(tableInfo.getDbName(), tableInfo.getTableName(), tableInfo.getColumnInfos());
        Boolean overwrite = columnFacade.overwriteList(tableInfo, compareInfoNewDTO);
        return RestResult.success(overwrite);
    }

}
