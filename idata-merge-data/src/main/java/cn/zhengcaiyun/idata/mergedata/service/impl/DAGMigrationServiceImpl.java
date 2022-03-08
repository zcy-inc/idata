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

package cn.zhengcaiyun.idata.mergedata.service.impl;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.FunctionModuleEnum;
import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGInfo;
import cn.zhengcaiyun.idata.develop.dal.model.folder.CompositeFolder;
import cn.zhengcaiyun.idata.develop.dal.repo.dag.DAGRepo;
import cn.zhengcaiyun.idata.develop.dal.repo.folder.CompositeFolderRepo;
import cn.zhengcaiyun.idata.develop.dto.dag.DAGDto;
import cn.zhengcaiyun.idata.develop.dto.dag.DAGInfoDto;
import cn.zhengcaiyun.idata.develop.dto.dag.DAGScheduleDto;
import cn.zhengcaiyun.idata.develop.dto.folder.CompositeFolderDto;
import cn.zhengcaiyun.idata.develop.service.dag.DAGService;
import cn.zhengcaiyun.idata.develop.service.folder.CompositeFolderService;
import cn.zhengcaiyun.idata.mergedata.dal.old.OldIDataDao;
import cn.zhengcaiyun.idata.mergedata.dto.MigrateResultDto;
import cn.zhengcaiyun.idata.mergedata.manager.DAGMigrateManager;
import cn.zhengcaiyun.idata.mergedata.service.DAGMigrationService;
import cn.zhengcaiyun.idata.mergedata.util.DWLayerCodeMapTool;
import cn.zhengcaiyun.idata.mergedata.util.IdPadTool;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-07 10:05
 **/
@Component
public class DAGMigrationServiceImpl implements DAGMigrationService {

    @Autowired
    private OldIDataDao oldIDataDao;
    @Autowired
    private DAGService dagService;
    @Autowired
    private CompositeFolderService compositeFolderService;
    @Autowired
    private CompositeFolderRepo compositeFolderRepo;
    @Autowired
    private DAGMigrateManager dagMigrateManager;
    @Autowired
    private DAGRepo dagRepo;

    @Override
    public List<MigrateResultDto> migrateFolder() {
        List<MigrateResultDto> resultDtoList = Lists.newArrayList();
        final CompositeFolder dagFolder = getDAGFunctionFolder();
        Operator operator = new Operator.Builder(0L).nickname("系统迁移").build();
        try {
            CompositeFolderDto folderDto = new CompositeFolderDto();
            folderDto.setName("ODS");
            folderDto.setType("FOLDER");
            folderDto.setBelong(dagFolder.getBelong());
            folderDto.setParentId(dagFolder.getId());
            Optional<CompositeFolder> existFolderOptional = compositeFolderRepo.queryFolder("ODS", dagFolder.getId());
            if (existFolderOptional.isEmpty()) {
                compositeFolderService.addFolder(folderDto, operator);
            }

            existFolderOptional = compositeFolderRepo.queryFolder("DIM", dagFolder.getId());
            if (existFolderOptional.isEmpty()) {
                folderDto.setId(null);
                folderDto.setName("DIM");
                compositeFolderService.addFolder(folderDto, operator);
            }

            existFolderOptional = compositeFolderRepo.queryFolder("DWD", dagFolder.getId());
            if (existFolderOptional.isEmpty()) {
                folderDto.setId(null);
                folderDto.setName("DWD");
                compositeFolderService.addFolder(folderDto, operator);
            }

            existFolderOptional = compositeFolderRepo.queryFolder("DWS", dagFolder.getId());
            if (existFolderOptional.isEmpty()) {
                folderDto.setId(null);
                folderDto.setName("DWS");
                compositeFolderService.addFolder(folderDto, operator);
            }

            existFolderOptional = compositeFolderRepo.queryFolder("ADS", dagFolder.getId());
            if (existFolderOptional.isEmpty()) {
                folderDto.setId(null);
                folderDto.setName("ADS");
                compositeFolderService.addFolder(folderDto, operator);
            }

            existFolderOptional = compositeFolderRepo.queryFolder("OTHER", dagFolder.getId());
            if (existFolderOptional.isEmpty()) {
                folderDto.setId(null);
                folderDto.setName("OTHER");
                compositeFolderService.addFolder(folderDto, operator);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("无权限新增文件夹");
        }
        return resultDtoList;
    }

    @Override
    public List<MigrateResultDto> migrateDAG(String cluster) {
        List<MigrateResultDto> resultDtoList = Lists.newArrayList();
        resultDtoList.addAll(migrateOriginDAG());
        resultDtoList.addAll(createStandardDAG(cluster, EnvEnum.prod));
        return resultDtoList;
    }

    public List<MigrateResultDto> migrateOriginDAG() {
        List<MigrateResultDto> resultDtoList = Lists.newArrayList();
        // 查询旧版IData数据
        List<JSONObject> dataJsonList = fetchOldData();
        // 处理旧版数据，组装新版IData数据
        Map<String, List<CompositeFolder>> folderMap = queryFolderMap();
        List<DAGDto> dagDtoList = dataJsonList.stream()
                .map(jsonObject -> buildDAGFromOrigin(jsonObject, folderMap))
                .collect(Collectors.toList());

        // 调用新版server接口，新增数据
        Operator operator = new Operator.Builder(0L).nickname("系统迁移").build();
        dagMigrateManager.createDAG(dagDtoList, operator);

        // 返回迁移失败的数据 MigrateResultDto
        return resultDtoList;
    }

    public List<MigrateResultDto> createStandardDAG(String cluster, EnvEnum env) {
        List<MigrateResultDto> resultDtoList = Lists.newArrayList();
        // 处理旧版数据，组装新版IData数据
        Map<String, List<CompositeFolder>> folderMap = queryFolderMap();
        List<String> tags = Lists.newArrayList("ODS", "DIM", "DWD", "DWS", "ADS");
        List<DAGInfo> dagInfoList = dagRepo.queryDAGInfo();
        List<DAGDto> dagDtoList = tags.stream()
                .filter(tag -> !isExistDag(tag, dagInfoList))
                .map(tag -> buildStandardDAG(cluster, env, tag, folderMap))
                .collect(Collectors.toList());

        // 调用新版server接口，新增数据
        Operator operator = new Operator.Builder(0L).nickname("系统迁移").build();
        dagMigrateManager.createDAG(dagDtoList, operator);

        // 返回迁移失败的数据 MigrateResultDto
        return resultDtoList;
    }

    private boolean isExistDag(String tag, List<DAGInfo> dagInfoList) {
        return dagInfoList.stream().anyMatch(dagInfo -> dagInfo.getName().indexOf(tag) > 0);
    }

    private CompositeFolder getDAGFunctionFolder() {
        List<CompositeFolder> folders = compositeFolderRepo.queryFunctionFolder();
        Optional<CompositeFolder> folderOptional = folders.stream()
                .filter(compositeFolder -> compositeFolder.getName().equals("DAG"))
                .findFirst();
        return folderOptional.get();
    }

    private List<JSONObject> fetchOldData() {
        List<String> columns = Lists.newArrayList("id", "dag_name", "dag_interval", "operator", "status");
        String filter = "is_del = false";
        return oldIDataDao.queryList("metadata.dag_info", columns, filter);
    }

    private Map<String, List<CompositeFolder>> queryFolderMap() {
        List<CompositeFolder> folders = compositeFolderRepo.queryFolder(FunctionModuleEnum.DAG);
        return folders.stream().collect(Collectors.groupingBy(CompositeFolder::getName));
    }

    private DAGDto buildDAGFromOrigin(JSONObject jsonObject, Map<String, List<CompositeFolder>> folderMap) {
        DAGDto dto = new DAGDto();
        DAGInfoDto dagInfoDto = new DAGInfoDto();
        DAGScheduleDto dagScheduleDto = new DAGScheduleDto();
        String newName = IdPadTool.padId(jsonObject.getString("id"))
                + "#_"
                + jsonObject.getString("dag_name");
        dagInfoDto.setName(newName);
        String oldLayer = resolveLayerCode(newName);
        dagInfoDto.setDwLayerCode(DWLayerCodeMapTool.getCodeEnum(oldLayer));
        dagInfoDto.setStatus(0);
        dagInfoDto.setRemark("");
        dagInfoDto.setFolderId(resolveFolder(oldLayer, folderMap));
        dagInfoDto.setEnvironment(resolveEnvironment(jsonObject.getString("status")));

        LocalDate localDate = LocalDate.now();
        dagScheduleDto.setBeginTime(Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        dagScheduleDto.setEndTime(Date.from(localDate.plusYears(5).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        dagScheduleDto.setPeriodRange("day");
        dagScheduleDto.setTriggerMode("specified");
        dagScheduleDto.setCronExpression("0 11 11 * * ? *");

        dto.setDagInfoDto(dagInfoDto);
        dto.setDagScheduleDto(dagScheduleDto);
        return dto;
    }

    private DAGDto buildStandardDAG(String cluster, EnvEnum envEnum, String tag, Map<String, List<CompositeFolder>> folderMap) {
        DAGDto dto = new DAGDto();
        DAGInfoDto dagInfoDto = new DAGInfoDto();
        DAGScheduleDto dagScheduleDto = new DAGScheduleDto();
        String newName = cluster + "-" + envEnum.name() + "-" + tag + "-" + "dag";
        newName = newName.toUpperCase();
        dagInfoDto.setName(newName);
        String oldLayer = resolveLayerCode(newName);
        dagInfoDto.setDwLayerCode(DWLayerCodeMapTool.getCodeEnum(oldLayer));
        dagInfoDto.setStatus(0);
        dagInfoDto.setRemark("");
        dagInfoDto.setFolderId(resolveFolder(tag, folderMap));
        dagInfoDto.setEnvironment(envEnum.name());

        LocalDate localDate = LocalDate.now();
        dagScheduleDto.setBeginTime(Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        dagScheduleDto.setEndTime(Date.from(localDate.plusYears(5).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        dagScheduleDto.setPeriodRange("day");
        dagScheduleDto.setTriggerMode("specified");
        dagScheduleDto.setCronExpression("0 11 11 * * ? *");

        dto.setDagInfoDto(dagInfoDto);
        dto.setDagScheduleDto(dagScheduleDto);
        return dto;
    }

    private String resolveLayerCode(String dagName) {
        if (dagName.indexOf("ODS") > 0)
            return "ODS";
        if (dagName.indexOf("DIM") > 0)
            return "DIM";
        if (dagName.indexOf("DWD") > 0)
            return "DWD";
        if (dagName.indexOf("DWS") > 0)
            return "DWS";
        return "ADS";
    }

    private String resolveEnvironment(String oldEnv) {
        if (oldEnv.startsWith("prod"))
            return "prod";
        return "stag";
    }

    private Long resolveFolder(String oldLayer, Map<String, List<CompositeFolder>> folderMap) {
        List<CompositeFolder> folders = folderMap.get(oldLayer);
        if (CollectionUtils.isEmpty(folders)) {
            folders = folderMap.get("OTHER");
        }
        return folders.get(0).getId();
    }

}
