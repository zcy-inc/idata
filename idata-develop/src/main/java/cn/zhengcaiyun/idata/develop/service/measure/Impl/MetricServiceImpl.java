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

import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineMyDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevLabelMyDao;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabel;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabelDefine;
import cn.zhengcaiyun.idata.develop.dto.label.*;
import cn.zhengcaiyun.idata.develop.dto.measure.MeasureDto;
import cn.zhengcaiyun.idata.develop.dto.measure.MetricDto;
import cn.zhengcaiyun.idata.develop.dto.measure.ModifierDto;
import cn.zhengcaiyun.idata.develop.service.label.EnumService;
import cn.zhengcaiyun.idata.develop.service.label.LabelService;
import cn.zhengcaiyun.idata.develop.service.measure.DimensionService;
import cn.zhengcaiyun.idata.develop.service.measure.MetricService;
import cn.zhengcaiyun.idata.develop.service.measure.ModifierService;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDynamicSqlSupport.devLabelDefine;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.devLabel;
import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author caizhedong
 * @date 2021-06-22 11:40
 */

@Service
public class MetricServiceImpl implements MetricService {

    @Autowired
    private DevLabelDefineDao devLabelDefineDao;
    @Autowired
    private ModifierService modifierService;
    @Autowired
    private DevLabelDefineMyDao devLabelDefineMyDao;
    @Autowired
    private DevLabelDao devLabelDao;
    @Autowired
    private LabelService labelService;
    @Autowired
    private DimensionService dimensionService;
    @Autowired
    private EnumService enumService;
    @Autowired
    private ColumnInfoService columnInfoService;

    private String[] metricInfos = new String[]{"enName", "metricId", "bizTypeCode", "metricDefine"};
    private final String MODIFIER_ENUM = "modifierEnum";
    private final String METRIC_LABEL = "METRIC_LABEL";

    @Override
    public MetricDto findMetric(String metricCode) {
        MetricDto echoMetric = getMetricByCode(metricCode);
        checkArgument(echoMetric != null, "指标不存在");
        return echoMetric;
    }

    @Override
    public List<MeasureDto> findMetrics(String labelTag) {
        LabelTagEnum.valueOf(labelTag);
        return PojoUtil.copyList(labelService.findDefines(null, labelTag), MeasureDto.class);
    }

    // 标签系统查询指标或维度，根据输入的labelCodes缩小范围
    @Override
    public List<MeasureDto> findMetricsOrDimensions(List<String> labelCodes, String labelTag) {
//        List<DevLabel> measureLabelList = devLabelMyDao.selectLabelsByLabelCodes(String.join(",", labelCodes));
        List<DevLabel> measureLabelList = devLabelDao.select(c -> c.where(devLabel.del, isNotEqualTo(1),
                and(devLabel.labelCode, isIn(labelCodes))));
        List<DevLabel> devMeasureLabelList = new ArrayList<>();
        for (DevLabel measureLabel : measureLabelList) {
            var builder = select(devLabel.allColumns())
                    .from(devLabel)
                    .leftJoin(devLabelDefine).on(devLabel.labelCode, equalTo(devLabelDefine.labelCode))
                    .where(devLabel.del, isNotEqualTo(1), and(devLabel.tableId, isEqualTo(measureLabel.getTableId()),
                            and(devLabel.columnName, isEqualTo(measureLabel.getColumnName()))));
            if (METRIC_LABEL.equals(labelTag)) {
                builder.and(devLabelDefine.labelTag, isLike("%" + METRIC_LABEL));
            }
            if (LabelTagEnum.DIMENSION_LABEL.name().equals(labelTag)) {
                builder.and(devLabelDefine.labelTag, isEqualTo(labelTag));
            }
            List<DevLabel> devMeasureLabels = devLabelDao.selectMany(builder.build().render(RenderingStrategies.MYBATIS3));
            devMeasureLabelList.addAll(devMeasureLabels);
        }
        Set<String> measureCodeList = devMeasureLabelList.stream().map(DevLabel::getLabelCode).collect(Collectors.toSet());
        return PojoUtil.copyList(devLabelDefineMyDao.selectLabelDefinesByLabelCodes(String.join(",", measureCodeList)),
                MeasureDto.class);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public MeasureDto create(MeasureDto metric, String operator) {
        checkArgument(isNotEmpty(operator), "创建者不能为空");
        checkArgument(isNotEmpty(metric.getLabelName()), "指标名称不能为空");
        checkArgument(isNotEmpty(metric.getLabelTag()), "类型不能为空");
        DevLabelDefine checkModifier = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelName, isEqualTo(metric.getLabelName()))))
                .orElse(null);
        checkArgument(checkModifier == null, "指标已存在");
        checkArgument(metric.getLabelAttributes() != null && metric.getLabelAttributes().size() > 0, "基本信息不能为空");
        List<String> metricInfoList = new ArrayList<>(Arrays.asList(metricInfos));
        List<String> metricAttributeKeyList = metric.getLabelAttributes().stream().map(AttributeDto::getAttributeKey)
                .collect(Collectors.toList());
        if (!metricAttributeKeyList.containsAll(metricInfoList)) {
            metricInfoList.removeAll(metricAttributeKeyList);
            throw new IllegalArgumentException(String.join(",", metricInfoList) + "不能为空");
        }

        // 校验修饰词枚举值是否属于修饰词
        if (LabelTagEnum.DERIVE_METRIC_LABEL.name().equals(metric.getLabelTag())) {
            checkArgument(isNotEmpty(metric.getSpecialAttribute().getAtomicMetricCode()), "原子指标不能为空");
            if (metric.getSpecialAttribute().getModifiers() != null && metric.getSpecialAttribute().getModifiers().size() > 0) {
                List<ModifierDto> relatedModifierList = metric.getSpecialAttribute().getModifiers();
                checkArgument(findErrorModifierNames(relatedModifierList) == null,
                        findErrorModifierNames(relatedModifierList) + "修饰词有误");
            }
        }
        MeasureDto echoMetric = PojoUtil.copyOne(labelService.defineLabel(metric, operator), MeasureDto.class);
        if (LabelTagEnum.ATOMIC_METRIC_LABEL.name().equals(metric.getLabelTag())) {
            checkArgument(metric.getMeasureLabels() != null && metric.getMeasureLabels().size() > 0, "关联信息不能为空");
            checkArgument(isNotEmpty(metric.getSpecialAttribute().getAggregatorCode()), "聚合方式不能为空");
            List<LabelDto> metricLabelList = metric.getMeasureLabels();
            // 校验关联信息
            metricLabelList.forEach(metricLabel ->
                    checkArgument(columnInfoService.checkColumn(metricLabel.getColumnName(), metricLabel.getTableId()),
                    String.format("表%s不含%s字段", metricLabel.getTableId(), metricLabel.getColumnName())));

            List<LabelDto> echoMetricLabelList = metricLabelList.stream().map(metricLabel -> {
                metricLabel.setLabelCode(echoMetric.getLabelCode());
                return labelService.label(metricLabel, operator);
            }).collect(Collectors.toList());
            echoMetric.setMeasureLabels(echoMetricLabelList);
        }
        return echoMetric;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public MeasureDto edit(MeasureDto metric, String operator) {
        checkArgument(isNotEmpty(operator), "修改者不能为空");
        checkArgument(isNotEmpty(metric.getLabelCode()), "指标Code不能为空");
        DevLabelDefine existMetric = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(metric.getLabelCode())), and(devLabelDefine.labelTag,
                        isLike("%_METRIC_DISABLE"))))
                .orElse(null);
        checkArgument(existMetric != null, "指标不存在或未停用");

        metric.setLabelTag(existMetric.getLabelTag());
        // 校验修饰词枚举值是否属于修饰词
        if (LabelTagEnum.DERIVE_METRIC_LABEL.name().equals(metric.getLabelTag())
                && metric.getSpecialAttribute().getModifiers() != null && metric.getSpecialAttribute().getModifiers().size() > 0) {
            List<ModifierDto> relatedModifierList = metric.getSpecialAttribute().getModifiers();
            checkArgument(findErrorModifierNames(relatedModifierList) == null,
                    findErrorModifierNames(relatedModifierList) + "修饰词有误");
        }
        MeasureDto echoMetric = PojoUtil.copyOne(labelService.defineLabel(metric, operator), MeasureDto.class);
        if (LabelTagEnum.ATOMIC_METRIC_LABEL.name().equals(metric.getLabelTag()) && metric.getMeasureLabels() != null
                && metric.getMeasureLabels().size() > 0) {
            List<LabelDto> metricLabelList = metric.getMeasureLabels();
            // 校验关联信息
            metricLabelList.forEach(metricLabel ->
                    checkArgument(columnInfoService.checkColumn(metricLabel.getColumnName(), metricLabel.getTableId()),
                            String.format("表%s不含%s字段", metricLabel.getTableId(), metricLabel.getColumnName())));
            List<LabelDto> echoMetricLabelList = metricLabelList.stream().map(metricLabel -> {
                metricLabel.setLabelCode(echoMetric.getLabelCode());
                return labelService.label(metricLabel, operator);
            }).collect(Collectors.toList());
            echoMetric.setMeasureLabels(echoMetricLabelList);
        }
        return echoMetric;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public MeasureDto disable(String metricCode, String operator) {
        checkArgument(isNotEmpty(operator), "修改者不能为空");
        checkArgument(isNotEmpty(metricCode), "指标Code不能为空");
        DevLabelDefine existMetric = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(metricCode)), and(devLabelDefine.labelTag,
                        isLike("%_METRIC_%")), and(devLabelDefine.labelTag, isNotLike("%DISABLE"))))
                .orElse(null);
        checkArgument(existMetric != null, "指标不存在或已停用");

        // 原子指标校验是否被派生指标依赖
        if (LabelTagEnum.ATOMIC_METRIC_LABEL.name().equals(existMetric.getLabelTag())) {
            checkArgument(getRelyDeriveMetricNames(metricCode) == null,
                    getRelyDeriveMetricNames(metricCode) + "依赖该原子指标，不能停用");
        }
        String labelTag = existMetric.getLabelTag() + "_DISABLE";
        devLabelDefineDao.update(c -> c.set(devLabelDefine.labelTag).equalTo(labelTag).set(devLabelDefine.editor).equalTo(operator)
                .where(devLabelDefine.del, isNotEqualTo(1), and(devLabelDefine.labelCode, isEqualTo(metricCode)),
                        and(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.MODIFIER_LABEL.name()))));
        return getMetricByCode(metricCode);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean delete(String metricCode, String operator) {
        checkArgument(isNotEmpty(operator), "修改者不能为空");
        checkArgument(isNotEmpty(metricCode), "指标Code不能为空");
        DevLabelDefine existModifier = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(metricCode)), and(devLabelDefine.labelTag, isLike("%_METRIC_DISABLE"))))
                .orElse(null);
        checkArgument(existModifier != null, "指标不存在或未停用或已删除");

        // 原子指标校验是否被派生指标依赖
        if (LabelTagEnum.ATOMIC_METRIC_LABEL.name().equals(existModifier.getLabelTag())) {
            checkArgument(getRelyDeriveMetricNames(metricCode) == null,
                    getRelyDeriveMetricNames(metricCode) + "等派生指标依赖该原子指标，不能删除");
        }

        return labelService.deleteDefine(metricCode, operator);
    }

    // 查询被原子指标依赖的派生指标
    private List<MeasureDto> getMetricByAtomic(String atomicMetricCode) {
        List<MeasureDto> deriveMetricList = PojoUtil.copyList(devLabelDefineDao.selectMany(
                select(devLabelDefine.allColumns())
                        .from(devLabelDefine)
                        .where(devLabelDefine.del, isNotEqualTo(1), and(devLabelDefine.labelTag,
                                isEqualTo(LabelTagEnum.DERIVE_METRIC_LABEL.name())))
                        .build().render(RenderingStrategies.MYBATIS3)), MeasureDto.class);
        Map<String, List<MeasureDto>> relyDriveAndAtomicMap = new HashMap<>();
        deriveMetricList.forEach(metricLabel -> {
            List<MeasureDto> metricLabelList;
            if (!relyDriveAndAtomicMap.containsKey(metricLabel.getSpecialAttribute().getAtomicMetricCode())) {
                metricLabelList = new ArrayList<>();
                metricLabelList.add(metricLabel);
                relyDriveAndAtomicMap.put(metricLabel.getSpecialAttribute().getAtomicMetricCode(), metricLabelList);
            }
            else {
                metricLabelList = relyDriveAndAtomicMap.get(metricLabel.getSpecialAttribute().getAtomicMetricCode());
                metricLabelList.add(metricLabel);
                relyDriveAndAtomicMap.replace(metricLabel.getSpecialAttribute().getAtomicMetricCode(), metricLabelList);
            }
        });
        return relyDriveAndAtomicMap.getOrDefault(atomicMetricCode, null);
    }

    // 校验修饰词是否含枚举值，返回错误修饰词名称
    private String findErrorModifierNames(List<ModifierDto> relatedModifiers) {
        Map<String, List<String>> relatedModifierMap = relatedModifiers.stream()
                .collect(Collectors.toMap(ModifierDto::getModifierCode, ModifierDto::getEnumValueCodes));
        List<LabelDefineDto> modifierList = labelService.findDefines(SubjectTypeEnum.COLUMN.name(), LabelTagEnum.MODIFIER_LABEL.name());
        Map<String, List<String>> modifierMap = modifierList.stream().collect(Collectors.toMap(LabelDefineDto::getLabelCode,
                modifier -> enumService.getEnumValues(modifier.getLabelAttributes().stream()
                        .filter(labelAttribute -> MODIFIER_ENUM.equals(labelAttribute.getAttributeKey())).findFirst()
                        .get().getEnumValue()).stream().map(EnumValueDto::getValueCode)
                        .collect(Collectors.toList())));
        List<String> errorModifierCodeList = relatedModifierMap.entrySet().stream().filter(relatedModifier ->
                modifierMap.get(relatedModifier.getKey()) != null && modifierMap.get(relatedModifier.getKey()).size() > 0
                        && modifierMap.get(relatedModifier.getKey()).containsAll(relatedModifier.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return errorModifierCodeList.size() == 0 ? null : errorModifierCodeList.stream().map(errorModifierCode ->
                labelService.findDefine(errorModifierCode).getLabelName()).collect(Collectors.joining(","));
    }

    // 查询被原子指标依赖的派生指标名
    private String getRelyDeriveMetricNames(String autoMetricCode) {
        return getMetricByAtomic(autoMetricCode) != null ?
                getMetricByAtomic(autoMetricCode).stream().map(MeasureDto::getLabelName).collect(Collectors.joining(",")) : null;
    }

    private MetricDto getMetricByCode(String metricCode) {
        DevLabelDefine metric = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(metricCode))))
                .orElse(null);
        if (metric == null) {
            return null;
        }

        MetricDto echoMetric = PojoUtil.copyOne(metric, MetricDto.class);
        echoMetric.setMeasureLabels(labelService.findLabelsByCode(metricCode));
        // 反查维度和派生指标
        if (metric.getLabelTag().contains(LabelTagEnum.ATOMIC_METRIC_LABEL.name())
                || metric.getLabelTag().contains(LabelTagEnum.DERIVE_METRIC_LABEL.name())) {
            List<MeasureDto> dimensionList = dimensionService.findDimensionsByLabelCode(metricCode);
            echoMetric.setDimensions(dimensionList);
            if (metric.getLabelTag().contains(LabelTagEnum.ATOMIC_METRIC_LABEL.name())) {
                List<MeasureDto> deriveMetricList = getMetricByAtomic(metricCode);
                echoMetric.setDeriveMetrics(deriveMetricList);
            }
            else {
                MeasureDto atomicMetric = getMetricByCode(echoMetric.getSpecialAttribute().getAtomicMetricCode());
                echoMetric.setMeasureLabels(labelService.findLabelsByCode(atomicMetric.getLabelCode()));
                Long atomicTableId = labelService.findLabelsByCode(atomicMetric.getLabelCode()).get(0).getTableId();
                echoMetric.setModifiers(modifierService.findModifiers(metricCode, atomicTableId));
            }
        }
        return echoMetric;
    }
}
