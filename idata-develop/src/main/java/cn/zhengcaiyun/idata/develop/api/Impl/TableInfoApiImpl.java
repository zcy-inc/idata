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
package cn.zhengcaiyun.idata.develop.api.Impl;

import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.develop.api.TableInfoApi;
import cn.zhengcaiyun.idata.develop.dal.dao.*;
import cn.zhengcaiyun.idata.develop.dal.model.DevEnum;
import cn.zhengcaiyun.idata.develop.dal.model.DevEnumValue;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabel;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.table.ColumnInfoDto;
import cn.zhengcaiyun.idata.develop.dto.table.SecurityLevelEnum;
import cn.zhengcaiyun.idata.develop.dto.table.TableDetailDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;
import cn.zhengcaiyun.idata.develop.service.label.LabelService;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import com.sun.xml.bind.v2.util.CollisionCheckStack;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevColumnInfoDynamicSqlSupport.devColumnInfo;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumValueDynamicSqlSupport.devEnumValue;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumValueDynamicSqlSupport.valueCode;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.devLabel;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDynamicSqlSupport.devTableInfo;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author caizhedong
 * @date 2021-07-23 16:36
 */

@Service
public class TableInfoApiImpl implements TableInfoApi {

    @Autowired
    private DevTableInfoDao devTableInfoDao;
    @Autowired
    private DevLabelDao devLabelDao;
    @Autowired
    private DevEnumValueDao devEnumValueDao;
    @Autowired
    private DevTableInfoMyDao devTableInfoMyDao;
    @Autowired
    private DevColumnInfoDao devColumnInfoDao;

    private final String ASSET_CATALOGUE_LABEL = "assetCatalogue:LABEL";
    private final String ASSET_CATALOGUE_ENUM = "assetCatalogueEnum:ENUM";
    private final String DW_LAYER_LABEL = "dwLayer:LABEL";
    private final String METABASE_URL_LABEL = "metabaseUrl:LABEL";
    private final String TABLE_COMMENT_LABEL = "tblComment:LABEL";
    private final String SECURITY_LEVEL_LABEL = "securityLevel:LABEL";
    private final String COLUMN_COMMENT_LABEL = "columnComment:LABEL";
    private final String[] tableDetails = {TABLE_COMMENT_LABEL, ASSET_CATALOGUE_LABEL, SECURITY_LEVEL_LABEL,
            METABASE_URL_LABEL, COLUMN_COMMENT_LABEL};

    @Override
    public List<Long> getTableIds(List<String> searchTexts, String assetCatalogueCode, String dwLayerCode,
                                        String searchType) {
        List<Long> tableInfoIdList ;
        var builder = select(devTableInfo.id).from(devTableInfo);
        if (searchTexts == null || searchTexts.size() == 0) {
            tableInfoIdList = PojoUtil.copyList(devTableInfoDao.selectMany(
                        builder.where(devTableInfo.del, isNotEqualTo(1))
                                .build().render(RenderingStrategies.MYBATIS3)), TableInfoDto.class)
                    .stream().map(TableInfoDto::getId).collect(Collectors.toList());
        }
        else {
            tableInfoIdList = devTableInfoMyDao.getSearchTableIds(searchType, searchTexts);
        }
        Set<Long> tableIds = new HashSet<>(tableInfoIdList);
        // 资产目录查询(本级及可能的下级)
        if (isNotEmpty(assetCatalogueCode)) {
            List<String> assetCatalogueCodeList = devEnumValueDao.select(c -> c.where(devEnumValue.del, isNotEqualTo(1))
                    .and(devEnumValue.valueCode, isEqualTo(assetCatalogueCode), or(devEnumValue.parentCode, isEqualTo(assetCatalogueCode))))
                    .stream().map(DevEnumValue::getValueCode).collect(Collectors.toList());
            Set<Long> removeAssetCatalogueTableIds = devLabelDao.select(c ->
                    c.where(devLabel.del, isNotEqualTo(1)).and(devLabel.labelParamValue, isNotIn(assetCatalogueCodeList))
                            .and(devLabel.labelCode, isEqualTo(ASSET_CATALOGUE_LABEL)))
                    .stream().map(DevLabel::getTableId).collect(Collectors.toSet());
            tableIds.removeAll(removeAssetCatalogueTableIds);
        }
        // 数仓分层查询
        if (isNotEmpty(dwLayerCode)) {
            Set<Long> removeDwLayerTableIds = devLabelDao.select(c ->
                    c.where(devLabel.del, isNotEqualTo(1)).and(devLabel.labelParamValue, isNotEqualTo(dwLayerCode))
                            .and(devLabel.labelCode, isEqualTo(DW_LAYER_LABEL)))
                    .stream().map(DevLabel::getTableId).collect(Collectors.toSet());
            tableIds.removeAll(removeDwLayerTableIds);
        }
        return new ArrayList<>(tableIds);
    }

    @Override
    public List<TableDetailDto> getTablesByIds(List<Long> tableIds) {
        if (tableIds.size() == 0) {
            return new ArrayList<>();
        }
        List<TableDetailDto> tableInfoList = PojoUtil.copyList(devTableInfoDao.select(c ->
                c.where(devTableInfo.id, isIn(tableIds)).and(devTableInfo.del, isNotEqualTo(1))),
                TableDetailDto.class, "id", "tableName");
        Map<Long, List<LabelDto>> tableInfoMap = PojoUtil.copyList(devLabelDao.select(c ->
                c.where(devLabel.del, isNotEqualTo(1)).and(devLabel.tableId, isIn(tableIds))
                        .and(devLabel.labelCode, isIn(Arrays.asList(tableDetails)))), LabelDto.class)
                .stream().collect(Collectors.groupingBy(LabelDto::getTableId));
        List<ColumnInfoDto> columnInfoList = PojoUtil.copyList(devColumnInfoDao.select(c ->
                c.where(devColumnInfo.del, isNotEqualTo(1)).and(devColumnInfo.tableId, isIn(tableIds))),
                ColumnInfoDto.class, "id", "columnName", "tableId");
        columnInfoList.forEach(columnInfo -> {
            List<LabelDto> columnLabelList = tableInfoMap.get(columnInfo.getTableId());
            columnLabelList.stream().filter(columnLabel ->
                    columnLabel.getLabelCode().equals(COLUMN_COMMENT_LABEL)).findFirst().ifPresent(commentLabel ->
                    columnInfo.setColumnComment(commentLabel.getLabelParamValue()));
        });
        Map<Long, List<ColumnInfoDto>> columnInfoMap = columnInfoList.stream().collect(Collectors.groupingBy(ColumnInfoDto::getTableId));
        tableInfoList.forEach(tableDetail -> {
            if (tableInfoMap.containsKey(tableDetail.getId())) {
                List<LabelDto> tableLabelList = tableInfoMap.get(tableDetail.getId());
                tableDetail.setTableComment(tableLabelList.stream().filter(tableLabel ->
                        tableLabel.getLabelCode().equals(TABLE_COMMENT_LABEL)).findFirst().get().getLabelParamValue());
                tableDetail.setAssetCatalogues(getAssetCatalogues(tableLabelList.stream().filter(tableLabel ->
                        tableLabel.getLabelCode().equals(ASSET_CATALOGUE_LABEL)).findFirst().get().getLabelParamValue()));
                tableLabelList.stream().filter(tableLabel ->
                        tableLabel.getLabelCode().equals(SECURITY_LEVEL_LABEL)).findFirst().ifPresent(securityLabel ->
                        tableDetail.setSecurityLevel(convertSecurityLevel(securityLabel.getLabelParamValue())));
                tableLabelList.stream().filter(tableLabel ->
                        tableLabel.getLabelCode().equals(METABASE_URL_LABEL)).findFirst().ifPresent(metabaseLabel ->
                        tableDetail.setMetabaseUrl(metabaseLabel.getLabelParamValue()));
                tableDetail.setColumnInfos(columnInfoMap.get(tableDetail.getId()));
            }
        });
        return tableInfoList;
    }

    private List<String> getAssetCatalogues(String assetCatalogueCode) {
        List<DevEnumValue> assetCatalogueList = devEnumValueDao.select(c ->
                c.where(devEnumValue.del, isNotEqualTo(1)).and(devEnumValue.enumCode, isEqualTo(ASSET_CATALOGUE_ENUM)));
        Map<String, DevEnumValue> assetCatalogueMap = assetCatalogueList.stream()
                .collect(Collectors.toMap(DevEnumValue::getValueCode, Function.identity()));
        List<DevEnumValue> echo = new ArrayList<>();
        echo.add(assetCatalogueMap.get(assetCatalogueCode));
        return getTree(echo, assetCatalogueMap).stream().map(DevEnumValue::getEnumValue).collect(Collectors.toList());
    }

    private List<DevEnumValue> getTree(List<DevEnumValue> assetCatalogues, Map<String, DevEnumValue> assetCatalogueMap) {
        if (isNotEmpty(assetCatalogues.get(0).getParentCode())) {
            assetCatalogues.add(0, assetCatalogueMap.get(assetCatalogues.get(0).getParentCode()));
            getTree(assetCatalogues, assetCatalogueMap);
        }
        return assetCatalogues;
    }

    private SecurityLevelEnum convertSecurityLevel(String securityLevel) {
        String changeSecurityLevel = securityLevel.split(":")[0];
        if (changeSecurityLevel.endsWith("C1") || changeSecurityLevel.endsWith("C2")
                || changeSecurityLevel.endsWith("S1") || changeSecurityLevel.endsWith("B1")) {
            return SecurityLevelEnum.LOW;
        }
        else if (changeSecurityLevel.endsWith("C3") || changeSecurityLevel.endsWith("S2") || changeSecurityLevel.endsWith("B2")) {
            return SecurityLevelEnum.MEDIUM;
        }
        else {
            return SecurityLevelEnum.HIGH;
        }
    }
}
