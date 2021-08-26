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
import cn.zhengcaiyun.idata.develop.api.MeasureApi;
import cn.zhengcaiyun.idata.develop.dal.dao.*;
import cn.zhengcaiyun.idata.develop.dal.model.DevEnumValue;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabel;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabelDefine;
import cn.zhengcaiyun.idata.develop.dto.label.AttributeDto;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDefineDto;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.label.LabelTagEnum;
import cn.zhengcaiyun.idata.develop.dto.measure.MeasureDto;
import cn.zhengcaiyun.idata.develop.service.measure.MetricService;
import kotlin.jvm.internal.PackageReference;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumValueDynamicSqlSupport.devEnumValue;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumValueDynamicSqlSupport.valueCode;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDynamicSqlSupport.devLabelDefine;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDynamicSqlSupport.labelAttributes;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.devLabel;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDynamicSqlSupport.devTableInfo;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author caizhedong
 * @date 2021-06-22 11:37
 */

@Service
public class MeasureApiImpl implements MeasureApi {

    @Autowired
    private DevLabelDefineMyDao devLabelDefineMyDao;
    @Autowired
    private DevLabelDao devLabelDao;
    @Autowired
    private DevTableInfoDao devTableInfoDao;
    @Autowired
    private DevLabelDefineDao devLabelDefineDao;
    @Autowired
    private DevEnumValueDao devEnumValueDao;
    @Autowired
    private MetricService metricService;

    private final String DB_NAME = "dbName:LABEL";
    private final String BIZ_TYPE_ENUM = "bizProcessEnum:ENUM";
    private final String METRIC_DEFINE = "metricDefine";
    private final String BIZ_TYPE_CODE = "bizProcessCode";
    private final String METRIC_ID = "metricId";
    private final String EN_NAME = "enName";
    private final String DIMENSION_DEFINE = "dimensionDefine";
    private final String DIMENSION_ID = "dimensionId";

    @Override
    public List<MeasureDto> getMeasures(List<String> labelCodes) {
        List<DevLabelDefine> measureList = devLabelDefineMyDao.selectLabelDefinesByLabelCodes(String.join(",", labelCodes));
        List<AttributeDto> measureAttributeList = new ArrayList<>();
        List<MeasureDto> echoMeasureList = measureList.stream().map(measure -> {
            MeasureDto echoMeasure;
            if (LabelTagEnum.DERIVE_METRIC_LABEL.name().equals(measure.getLabelTag())) {
                echoMeasure = metricService.findMetric(measure.getLabelCode());
            }
            else {
                List<LabelDto> measureLabelList = PojoUtil.copyList(devLabelDao.selectMany(select(devLabel.allColumns())
                        .from(devLabel)
                        .where(devLabel.del, isNotEqualTo(1), and(devLabel.labelCode, isEqualTo(measure.getLabelCode())))
                        .build().render(RenderingStrategies.MYBATIS3)), LabelDto.class);
                Map<Long, String> dwdTableInfoMap = devLabelDao.select(c -> c.where(devLabel.del, isNotEqualTo(1),
                        and(devLabel.labelCode, isEqualTo(DB_NAME))))
                        .stream().collect(Collectors.toMap(DevLabel::getTableId, DevLabel::getLabelParamValue));
                measureLabelList.forEach(measureLabel -> {
                    measureLabel.setDbTableName(dwdTableInfoMap.get(measureLabel.getTableId()) + "." +
                            devTableInfoDao.selectOne(c -> c.where(devTableInfo.del, isNotEqualTo(1),
                                    and(devTableInfo.id, isEqualTo(measureLabel.getTableId())))).get().getTableName());
                });
                echoMeasure = PojoUtil.copyOne(measure, MeasureDto.class);
                echoMeasure.setMeasureLabels(measureLabelList);
            }
            measureAttributeList.addAll(echoMeasure.getLabelAttributes());
            return echoMeasure;
        }).collect(Collectors.toList());
        Map<String, String> bizTypeMap = devEnumValueDao.select(c -> c.where(devEnumValue.del, isNotEqualTo(1),
                and(devEnumValue.enumCode, isEqualTo(BIZ_TYPE_ENUM))))
                .stream().collect(Collectors.toMap(DevEnumValue::getValueCode, DevEnumValue::getEnumValue));
        Map<String, String> enNameMap = echoMeasureList.stream()
                .collect(Collectors.toMap(LabelDefineDto::getLabelCode,
                        measure -> {
                            AttributeDto attribute = measure.getLabelAttributes().stream()
                                    .filter(labelAttribute -> (measure.getLabelTag().endsWith("_METRIC_LABEL")
                                            || measure.getLabelTag().equals("DIMENSION_LABEL"))
                                            && labelAttribute.getAttributeKey().equals(EN_NAME)).findAny().orElse(null);
                            if (attribute != null) {
                                return attribute.getAttributeValue();
                            }
                            return null;
                        }));
        Map<String, String> measureIdMap = new HashMap<>();
        for (MeasureDto measure : echoMeasureList) {
            AttributeDto attribute = null;
            if (measure.getLabelTag().endsWith("_METRIC_LABEL")) {
                attribute = measure.getLabelAttributes().stream()
                        .filter(labelAttribute -> labelAttribute.getAttributeKey().equals(METRIC_ID)).findAny().orElse(null);
            }
            else if (measure.getLabelTag().equals("DIMENSION_LABEL")) {
                attribute = measure.getLabelAttributes().stream()
                        .filter(labelAttribute -> labelAttribute.getAttributeKey().equals(DIMENSION_ID)).findAny().orElse(null);
            }
            if (attribute != null) {
                measureIdMap.put(measure.getLabelCode(), attribute.getAttributeValue());
            }
        }
//                echoMeasureList.stream()
//                .collect(Collectors.toMap(LabelDefineDto::getLabelCode,
//                        measure -> {
//                            AttributeDto attribute = measure.getLabelAttributes().stream()
//                                    .filter(labelAttribute -> measure.getLabelTag().endsWith("_METRIC_LABEL")
//                                            && labelAttribute.getAttributeKey().equals(METRIC_ID)).findAny().orElse(null);
//                            if (attribute != null) {
//                                return attribute.getAttributeValue();
//                            }
//                            return null;
//                        }));
//        Map<String, String> dimensionIdMap = echoMeasureList.stream()
//                .collect(Collectors.toMap(LabelDefineDto::getLabelCode,
//                        measure -> {
//                            AttributeDto attribute = measure.getLabelAttributes().stream()
//                                    .filter(labelAttribute -> measure.getLabelTag().equals("DIMENSION_LABEL")
//                                            && labelAttribute.getAttributeKey().equals(DIMENSION_ID)).findAny().orElse(null);
//                            if (attribute != null) {
//                                return attribute.getAttributeValue();
//                            }
//                            return null;
//                        }));
        Map<String, String> measureDefineMap = new HashMap<>();
        for (MeasureDto measure : echoMeasureList) {
            AttributeDto attribute = null;
            if (measure.getLabelTag().endsWith("_METRIC_LABEL")) {
                attribute = measure.getLabelAttributes().stream()
                        .filter(labelAttribute -> labelAttribute.getAttributeKey().equals(METRIC_DEFINE)).findAny().orElse(null);
            }
            else if (measure.getLabelTag().equals("DIMENSION_LABEL")) {
                attribute = measure.getLabelAttributes().stream()
                        .filter(labelAttribute -> labelAttribute.getAttributeKey().equals(DIMENSION_DEFINE)).findAny().orElse(null);
            }
            if (attribute != null) {
                measureDefineMap.put(measure.getLabelCode(), attribute.getAttributeValue());
            }
        }
//        Map<String, String> metricDefineMap = echoMeasureList.stream()
//                .collect(Collectors.toMap(LabelDefineDto::getLabelCode,
//                        measure -> {
//                            AttributeDto attribute = measure.getLabelAttributes().stream()
//                                    .filter(labelAttribute -> measure.getLabelTag().endsWith("_METRIC_LABEL")
//                                            && labelAttribute.getAttributeKey().equals(METRIC_DEFINE)).findAny().orElse(null);
//                            if (attribute != null) {
//                                return attribute.getAttributeValue();
//                            }
//                            return null;
//                        }));
//        Map<String, String> dimensionDefineMap = echoMeasureList.stream()
//                .collect(Collectors.toMap(LabelDefineDto::getLabelCode,
//                        measure -> {
//                            AttributeDto attribute = measure.getLabelAttributes().stream()
//                                    .filter(labelAttribute -> measure.getLabelTag().equals("DIMENSION_LABEL")
//                                            && labelAttribute.getAttributeKey().equals(DIMENSION_DEFINE)).findAny().orElse(null);
//                            if (attribute != null) {
//                                return attribute.getAttributeValue();
//                            }
//                            return null;
//                        }));
        Map<String, String> metricBizTypeCodeMap = new HashMap<>();
        for (MeasureDto measure : echoMeasureList) {
            AttributeDto attribute = null;
            if (measure.getLabelTag().endsWith("_METRIC_LABEL")) {
                attribute = measure.getLabelAttributes().stream()
                        .filter(labelAttribute -> labelAttribute.getAttributeKey().equals(BIZ_TYPE_CODE)).findAny().orElse(null);
            }
            if (attribute != null) {
                metricBizTypeCodeMap.put(measure.getLabelCode(), attribute.getAttributeValue());
            }
        }
//        Map<String, String> metricBizTypeCodeMap = echoMeasureList.stream()
//                .collect(Collectors.toMap(LabelDefineDto::getLabelCode,
//                        measure -> {
//                            AttributeDto attribute = measure.getLabelAttributes().stream()
//                                    .filter(labelAttribute -> measure.getLabelTag().endsWith("_METRIC_LABEL")
//                                            && labelAttribute.getAttributeKey().equals(BIZ_TYPE_CODE)).findAny().orElse(null);
//                            if (attribute != null) {
//                                return attribute.getAttributeValue();
//                            }
//                            return null;
//                        }));
        echoMeasureList.forEach(measure -> {
            measure.setEnName(enNameMap.get(measure.getLabelCode()));
            measure.setMeasureId(measureIdMap.get(measure.getLabelCode()));
            measure.setMeasureDefine(measureDefineMap.get(measure.getLabelCode()));
            if (measure.getLabelTag().endsWith("METRIC_LABEL")) {
                measure.setBizTypeValue(bizTypeMap.get(metricBizTypeCodeMap.get(measure.getLabelCode())));
            }
        });
        return echoMeasureList;
    }

    @Override
    public List<String> getMetricCodes(List<String> metricTexts, String bizTypeCode) {
        List<LabelDefineDto> metricList = PojoUtil.copyList(devLabelDefineDao.select(c ->
                c.where(devLabelDefine.del,
                isNotEqualTo(1), and(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.ATOMIC_METRIC_LABEL.name()),
                                or(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.DERIVE_METRIC_LABEL.name())),
                                or(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.COMPLEX_METRIC_LABEL.name()))))),
                LabelDefineDto.class);
        Set<String> metricCodes = metricList.stream().map(LabelDefineDto::getLabelCode).collect(Collectors.toSet());
        if (metricTexts != null && metricTexts.size() > 0) {
            Map<String, String> enNameMetricMap = metricList.stream()
                    .collect(Collectors.toMap(LabelDefineDto::getLabelCode,
                            metric -> {
                        AttributeDto attribute = metric.getLabelAttributes().stream()
                                .filter(labelAttribute -> labelAttribute.getAttributeKey().equals(EN_NAME)).findAny().get();
                        return attribute.getAttributeValue();
                    }));
            Map<String, String> metricIdMap = metricList.stream()
                    .collect(Collectors.toMap(LabelDefineDto::getLabelCode,
                            metric -> {
                                AttributeDto attribute = metric.getLabelAttributes().stream()
                                        .filter(labelAttribute -> labelAttribute.getAttributeKey().equals(METRIC_ID)).findAny().get();
                                return attribute.getAttributeValue();
                            }));
            List<MeasureDto> measureList = metricList.stream().map(metric -> {
                MeasureDto measure = PojoUtil.copyOne(metric, MeasureDto.class,
                        "id", "labelName", "labelCode", "labelTag");
                measure.setEnName(enNameMetricMap.get(metric.getLabelCode()));
                measure.setMeasureId(metricIdMap.get(metric.getLabelCode()));
                return measure;
            }).collect(Collectors.toList());
            List<String> removeSearchMetricCodeList = new ArrayList<>();
            metricTexts.forEach(text -> {
                List<String> removeSearchCodeList = measureList.stream()
                        .filter(measure -> !measure.getLabelName().contains(text)
                                && !measure.getEnName().contains(text) && !measure.getMeasureId().contains(text))
                        .collect(Collectors.toList())
                        .stream().map(MeasureDto::getLabelCode).collect(Collectors.toList());
                removeSearchMetricCodeList.addAll(removeSearchCodeList);
            });
            Set<String> removeSearchMetricCodes = new HashSet<>(removeSearchMetricCodeList);
            metricCodes.removeAll(removeSearchMetricCodes);
        }
        if (isNotEmpty(bizTypeCode)) {
            Map<String, String> bizTypeMetricMap = metricList.stream()
                    .collect(Collectors.toMap(LabelDefineDto::getLabelCode,
                            metric -> {
                        AttributeDto attribute = metric.getLabelAttributes().stream()
                                .filter(labelAttribute -> labelAttribute.getAttributeKey().equals(BIZ_TYPE_CODE)).findAny().get();
                        return attribute.getAttributeValue();
                    }));
            List<String> bizTypeCodeList = devEnumValueDao.select(c -> c.where(devEnumValue.del, isNotEqualTo(1))
                    .and(devEnumValue.valueCode, isEqualTo(bizTypeCode), or(devEnumValue.parentCode, isEqualTo(bizTypeCode))))
                    .stream().map(DevEnumValue::getValueCode).collect(Collectors.toList());
            Set<String> removeBizTypeMetricCodes = bizTypeMetricMap.entrySet().stream()
                    .filter(metric -> !bizTypeCodeList.contains(metric.getValue()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());
            metricCodes.removeAll(removeBizTypeMetricCodes);
        }
        return new ArrayList<>(metricCodes);
    }
}
