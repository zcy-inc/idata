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

import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.connector.bean.dto.TableTechInfoDto;
import cn.zhengcaiyun.idata.develop.dal.dao.DevColumnInfoDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevEnumValueDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevFolderDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDao;
import cn.zhengcaiyun.idata.develop.dal.model.DevColumnInfo;
import cn.zhengcaiyun.idata.develop.dal.model.DevFolder;
import cn.zhengcaiyun.idata.develop.dal.model.DevTableInfo;
import cn.zhengcaiyun.idata.develop.dto.label.EnumValueDto;
import cn.zhengcaiyun.idata.develop.dto.table.*;
import cn.zhengcaiyun.idata.develop.service.label.EnumService;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import cn.zhengcaiyun.idata.develop.service.table.DwMetaService;
import cn.zhengcaiyun.idata.develop.service.table.TableInfoService;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.user.dal.dao.UacUserDao;
import cn.zhengcaiyun.idata.user.dal.model.UacUser;
import cn.zhengcaiyun.idata.user.service.TokenService;
import cn.zhengcaiyun.idata.user.service.UserManagerService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONString;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.postgresql.jdbc.PgArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.commons.pojo.PojoUtil.castType;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevColumnInfoDynamicSqlSupport.devColumnInfo;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevFolderDynamicSqlSupport.devFolder;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDynamicSqlSupport.devTableInfo;
import static cn.zhengcaiyun.idata.user.dal.dao.UacUserDynamicSqlSupport.uacUser;
import static org.mybatis.dynamic.sql.SqlBuilder.isNotEqualTo;

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
    public RestResult<List<ColumnDetailsDto>> getColumns(@PathVariable("tableId") Long tableId) {
        return RestResult.success(columnInfoService.getColumnDetails(tableId));
    }

    @GetMapping("tableDdl/{tableId}")
    public RestResult<String> getTableDDL(@PathVariable("tableId") Long tableId) {
        return RestResult.success(tableInfoService.getTableDDL(tableId));
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
                                  HttpServletRequest request) {
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
    @PostMapping("/syncHiveInfo/{tableId")
    public RestResult<Boolean> syncHiveInfo(@PathVariable("tableId") Long tableId) {
        return RestResult.success(tableInfoService.syncHiveInfo(tableId));
    }

}
