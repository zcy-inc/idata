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
package cn.zhengcaiyun.idata.develop.service.measure.Impl;

import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.develop.dal.dao.*;
import cn.zhengcaiyun.idata.develop.dal.model.DevFolder;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabel;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabelDefine;
import cn.zhengcaiyun.idata.develop.dal.model.DevTableInfo;
import cn.zhengcaiyun.idata.develop.dto.label.AttributeDto;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.label.LabelTagEnum;
import cn.zhengcaiyun.idata.develop.dto.measure.MeasureDto;
import cn.zhengcaiyun.idata.develop.service.folder.DevFolderService;
import cn.zhengcaiyun.idata.develop.service.label.EnumService;
import cn.zhengcaiyun.idata.develop.service.measure.MeasureService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevFolderDynamicSqlSupport.devFolder;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDynamicSqlSupport.devLabelDefine;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.devLabel;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDynamicSqlSupport.devTableInfo;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author caizhedong
 * @date 2022-03-22 下午3:04
 */

@Service
public class MeasureServiceImpl implements MeasureService {

    @Autowired
    private DevLabelDefineDao devLabelDefineDao;
    @Autowired
    private DevFolderDao devFolderDao;
    @Autowired
    private DevLabelDao devLabelDao;
    @Autowired
    private DevTableInfoDao devTableInfoDao;
    @Autowired
    private DevLabelDefineMyDao devLabelDefineMyDao;
    @Autowired
    private EnumService enumService;
    @Autowired
    private DevFolderService devFolderService;

    @Override
    public List<MeasureDto> getMeasures(Long folderId, String measureType, String metricType, String measureId,
                                        String measureName, String bizProcess, Boolean enable, String creator, Date measureDeadline,
                                        String domain, String belongTblName, Long limit, Integer offset) {
        offset = offset != null ? offset : 0;
        limit = limit != null ? limit : PageParam.MAX_LIMIT;
        String isEnable = null;
        String measureDeadlineStr = null;
        if (enable != null) {
            if (enable) {
                isEnable = "_METRIC_LABEL";
            }
            else {
                isEnable = "_METRIC_LABEL_DISABLE";
            }
        }
        if (measureDeadline != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            measureDeadlineStr = sdf.format(measureDeadline);
        }
        String folderIdsStr = null;
        if (folderId != null) {
            Set<Long> folderIds = new HashSet<>(Collections.singletonList(folderId));
            folderIds = devFolderService.getChildFolderIds(folderIds, measureType.split("_")[0]);
            List<String> folderIdStrList = folderIds.stream().map(String::valueOf).collect(Collectors.toList());
            folderIdsStr = String.join(",", folderIdStrList);
        }
        List<String> measureCodeList = devLabelDefineMyDao.selectLabelDefineCodesByCondition(
                folderIdsStr, measureType, metricType, measureId, measureName,
                bizProcess, isEnable, creator, measureDeadlineStr, domain, belongTblName, limit, offset)
                .stream().map(DevLabelDefine::getLabelCode).collect(Collectors.toList());
        return getMeasureDetails(measureCodeList, measureType);
    }

    private List<MeasureDto> getMeasureDetails(List<String> labelCodes, String measureType) {
        List<MeasureDto> measureList = PojoUtil.copyList(devLabelDefineDao.select(c -> c.where(devLabelDefine.del,
                isNotEqualTo(1),
                and(devLabelDefine.labelCode, isIn(labelCodes)))), MeasureDto.class);
        List<Long> folderIdList = measureList.stream().map(MeasureDto::getFolderId).collect(Collectors.toList());
        Map<Long, String> folderMap = devFolderDao.select(c -> c.where(devFolder.id, isIn(folderIdList))).stream()
                .collect(Collectors.toMap(DevFolder::getId, DevFolder::getFolderName));
        measureList = measureList.stream().peek(measure -> measure.setFolderName(folderMap.get(measure.getFolderId())))
                .collect(Collectors.toList());
        if (LabelTagEnum.MODIFIER_LABEL.name().equals(measureType)) {
            return getModifierDetails(measureList, labelCodes);
        }
        else {
            return getMetricDetails(measureList);
        }
    }

    private List<MeasureDto> getMetricDetails(List<MeasureDto> metrics) {
        List<MeasureDto> echoMetricList =  metrics.stream().map(metric -> {
                    MeasureDto echo = PojoUtil.copyOne(metric,
                    "id", "createTime", "editTime", "creator", "editor", "labelName", "labelTag", "folderName");
                    List<AttributeDto> labelAttributeList = metric.getLabelAttributes();
                    labelAttributeList.forEach(labelAttribute -> {
                        if ("metricId".equals(labelAttribute.getAttributeKey())) {
                            echo.setMetricId(labelAttribute.getAttributeValue());
                        }
                        if ("metricDefine".equals(labelAttribute.getAttributeKey())) {
                            echo.setMeasureDefine(labelAttribute.getAttributeValue());
                        }
                        if ("domainCode".equals(labelAttribute.getAttributeKey())) {
                            echo.setDomain(enumService.getEnumValue(labelAttribute.getAttributeValue()));
                        }
                        if ("bizProcessCode".equals(labelAttribute.getAttributeKey())) {
                            echo.setBizProcessValue(enumService.getEnumValue(labelAttribute.getAttributeValue()));
                        }
                        if ("metricDeadline".equals(labelAttribute.getAttributeKey())) {
                            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                echo.setMetricDeadline(fmt.parse(labelAttribute.getAttributeValue()));
                            } catch (ParseException ignore) {}
                        }
                    });
            return echo;
        }).collect(Collectors.toList());
        return echoMetricList;
    }

    private List<MeasureDto> getModifierDetails(List<MeasureDto> modifiers, List<String> labelCodes) {
        List<DevLabel> modifierLabelList = devLabelDao.select(c -> c.where(devLabel.labelCode, isIn(labelCodes)));
        Map<String, List<DevLabel>> modifierLabelMap = modifierLabelList.stream().collect(Collectors.groupingBy(DevLabel::getLabelCode));
        Set<Long> tableIdList = modifierLabelList.stream().map(DevLabel::getTableId).collect(Collectors.toSet());
        Map<Long, String> tableMap = devTableInfoDao.select(c -> c.where(devTableInfo.id, isIn(tableIdList))).stream()
                .collect(Collectors.toMap(DevTableInfo::getId, DevTableInfo::getTableName));
        List<MeasureDto> echoModifierList =  modifiers.stream().map(modifier -> {
            MeasureDto echo = PojoUtil.copyOne(modifier,
                    "id", "creatTime", "editTime", "creator", "editor", "labelName", "labelTag", "folderName");
            List<AttributeDto> labelAttributeList = modifier.getLabelAttributes();
            labelAttributeList.forEach(labelAttribute -> {
                if ("modifierId".equals(labelAttribute.getAttributeKey())) {
                    echo.setModifierId(labelAttribute.getAttributeValue());
                }
                if ("modifierDefine".equals(labelAttribute.getAttributeKey())) {
                    echo.setMeasureDefine(labelAttribute.getAttributeValue());
                }
            });
            echo.setBelongTblName(tableMap.get(modifierLabelMap.get(modifier.getLabelCode()).get(0).getTableId()));
            echo.setColumnName(modifierLabelMap.get(modifier.getLabelCode()).get(0).getColumnName());
            return echo;
        }).collect(Collectors.toList());
        return echoModifierList;
    }
}
