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
import cn.zhengcaiyun.idata.develop.constant.enums.AggregatorEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.*;
import cn.zhengcaiyun.idata.develop.dal.model.DevEnumValue;
import cn.zhengcaiyun.idata.develop.dal.model.DevForeignKey;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabel;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabelDefine;
import cn.zhengcaiyun.idata.develop.dto.label.*;
import cn.zhengcaiyun.idata.develop.dto.measure.MeasureDto;
import cn.zhengcaiyun.idata.develop.dto.measure.MetricDto;
import cn.zhengcaiyun.idata.develop.dto.measure.ModifierDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;
import cn.zhengcaiyun.idata.develop.service.label.EnumService;
import cn.zhengcaiyun.idata.develop.service.label.LabelService;
import cn.zhengcaiyun.idata.develop.service.measure.DimensionService;
import cn.zhengcaiyun.idata.develop.service.measure.MetricService;
import cn.zhengcaiyun.idata.develop.service.measure.ModifierService;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import cn.zhengcaiyun.idata.develop.service.table.TableInfoService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumValueDynamicSqlSupport.devEnumValue;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevForeignKeyDynamicSqlSupport.devForeignKey;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDynamicSqlSupport.*;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.devLabel;
import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isAlphanumeric;
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
    private DevEnumValueDao devEnumValueDao;
    @Autowired
    private DevForeignKeyDao devForeignKeyDao;
    @Autowired
    private LabelService labelService;
    @Autowired
    private DimensionService dimensionService;
    @Autowired
    private EnumService enumService;
    @Autowired
    private ColumnInfoService columnInfoService;
    @Autowired
    private TableInfoService tableInfoService;

    private String[] metricInfos = new String[]{"enName", "metricId", "bizProcessCode", "metricDefine", "domainCode", "metricDeadline"};
    private final String MODIFIER_ENUM = "modifierEnum";
    private final String METRIC_LABEL = "METRIC_LABEL";
    private final String METRIC_BIZ_TYPE = "bizProcessCode";

    @Override
    public MetricDto findMetric(String metricCode) {
        MetricDto echoMetric = getMetricByCode(metricCode);
        checkArgument(echoMetric != null, "指标不存在");
        return echoMetric;
    }

    @Override
    public List<MeasureDto> findMetrics(String labelTag) {
        LabelTagEnum.valueOf(labelTag);
        return PojoUtil.copyList(labelService.findDefines(SubjectTypeEnum.COLUMN.name(), labelTag), MeasureDto.class);
    }

    // 标签系统查询指标或维度，根据输入的labelCodes缩小范围
    @Override
    public List<MeasureDto> findMetricsOrDimensions(List<String> labelCodes, String labelTag) {
        List<MeasureDto> echoMeasureList = new ArrayList<>();
        List<DevLabelDefine> labelCodeList = devLabelDefineDao.select(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isIn(labelCodes))));
        List<String> metricCodeList = labelCodeList.stream().filter(measure -> measure.getLabelTag().endsWith(METRIC_LABEL))
                .collect(Collectors.toList()).stream().map(DevLabelDefine::getLabelCode).collect(Collectors.toList());
        List<String> dimensionCodeList = labelCodeList.stream().filter(measure -> LabelTagEnum.DIMENSION_LABEL.name().equals(measure.getLabelTag()))
                .collect(Collectors.toList()).stream().map(DevLabelDefine::getLabelCode).collect(Collectors.toList());
        if (METRIC_LABEL.equals(labelTag)) {
            List<DevLabel> measureLabelList = devLabelDao.select(c -> c.where(devLabel.del, isNotEqualTo(1),
                    and(devLabel.labelCode, isIn(dimensionCodeList))));
            List<DevLabel> devMeasureLabelList = new ArrayList<>();
            for (DevLabel measureLabel : measureLabelList) {
                var builder = select(devLabel.allColumns())
                        .from(devLabel)
                        .leftJoin(devLabelDefine).on(devLabel.labelCode, equalTo(devLabelDefine.labelCode))
                        .where(devLabel.del, isNotEqualTo(1), and(devLabel.tableId, isEqualTo(measureLabel.getTableId())),
                                and(devLabelDefine.labelTag, isLike("%" + METRIC_LABEL)));
                List<DevLabel> devMeasureLabels = devLabelDao.selectMany(builder.build().render(RenderingStrategies.MYBATIS3));
                devMeasureLabelList.addAll(devMeasureLabels);
            }
            List<LabelDefineDto> deriveMetricList = PojoUtil.copyList(devLabelDefineDao.select(c ->
                            c.where(devLabelDefine.del, isNotEqualTo(1),
                                    and(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.DERIVE_METRIC_LABEL.name())))),
                    LabelDefineDto.class);
            List<String> measureCodeList = devMeasureLabelList.stream().map(DevLabel::getLabelCode).collect(Collectors.toList());
            deriveMetricList.forEach(deriveMetric -> {
                if (isNotEmpty(deriveMetric.getSpecialAttribute().getAtomicMetricCode())
                        && measureCodeList.contains(deriveMetric.getSpecialAttribute().getAtomicMetricCode())) {
                    measureCodeList.add(deriveMetric.getLabelCode());
                }
            });
            Set<String> echoMeasureCodeList = new HashSet<>(measureCodeList);
            echoMeasureList = PojoUtil.copyList(devLabelDefineMyDao.selectLabelDefinesByLabelCodes(String.join(",",
                    echoMeasureCodeList)), MeasureDto.class);
        }
        else if (labelTag.equals(LabelTagEnum.DIMENSION_LABEL.name())) {
            List<DevLabel> dimensionLabel = devLabelDao.select(c -> c.where(devLabel.del, isNotEqualTo(1),
                    and(devLabel.labelCode, isIn(dimensionCodeList))));
            Set<Long> dimensionTblIdList = dimensionLabel.stream().map(DevLabel::getTableId).collect(Collectors.toSet());
            List<MeasureDto> dimensionList = new ArrayList<>();
            for (String labelCode : metricCodeList) {
                dimensionList.addAll(dimensionService.findDimensionsByMetricCode(labelCode));
            }
            Set<String> dimensionCodes = dimensionList.stream().map(MeasureDto::getLabelCode).collect(Collectors.toSet());
            Map<String, MeasureDto> dimensionMap = dimensionList.stream().collect(Collectors.toMap(
                    MeasureDto::getLabelCode, Function.identity(), (value1, value2) -> value1));
            List<MeasureDto> useDimensionList = dimensionCodes.stream().map(dimensionMap::get).collect(Collectors.toList());
            for (MeasureDto dimension : useDimensionList) {
                if (dimensionTblIdList.contains(dimension.getMeasureLabels().get(0).getTableId())
                        && !dimensionCodeList.contains(dimension.getLabelCode())) {
                    echoMeasureList.add(dimension);
                }
            }
        }
        return echoMeasureList;
    }

    @Override
    public String getMetricSql(String metricCode) {
        DevLabelDefine metric = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(metricCode))))
                .orElse(null);
        if (metric == null) {
            return null;
        }

        String metricBaseSql = "SELECT %s AS %s FROM %s.%s";
        String metricSql;
        List<LabelDto> metricLabelList = labelService.findLabelsByCode(metricCode);
        // 暂只提供原子指标和派生指标，修饰词只能取到定义值
        if (metric.getLabelTag().contains(LabelTagEnum.ATOMIC_METRIC_LABEL.name())) {
            if (ObjectUtils.isEmpty(metricLabelList)) {
                return null;
            }
            String selectColSql = StringUtils.isEmpty(metric.getSpecialAttribute().getAggregatorCode())
                    ? metricLabelList.get(0).getColumnName()
                    : changeAggregate(metricLabelList.get(0).getColumnName(),
                    metric.getSpecialAttribute().getAggregatorCode().split(":")[0].split("_")[1]);
            metricSql = String.format(metricBaseSql, selectColSql, metric.getLabelName(),
                    metricLabelList.get(0).getDbName(), metricLabelList.get(0).getTableName());
        }
        else if (metric.getLabelTag().contains(LabelTagEnum.DERIVE_METRIC_LABEL.name())) {
            DevLabelDefine atomicMetric = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                    and(devLabelDefine.labelCode, isEqualTo(metric.getSpecialAttribute().getAtomicMetricCode())))).get();
            List<LabelDto> atomicLabelList = labelService.findLabelsByCode(metric.getSpecialAttribute().getAtomicMetricCode());
            List<ModifierDto> modifierList = modifierService.findModifiers(metricCode, atomicLabelList.get(0).getTableId());
            String selectColSql = StringUtils.isEmpty(atomicMetric.getSpecialAttribute().getAggregatorCode())
                    ? atomicLabelList.get(0).getColumnName()
                    : changeAggregate(atomicLabelList.get(0).getColumnName(),
                    atomicMetric.getSpecialAttribute().getAggregatorCode().split(":")[0].split("_")[1]);
            metricSql = String.format(metricBaseSql, selectColSql, metric.getLabelName(),
                    atomicLabelList.get(0).getDbName(), atomicLabelList.get(0).getTableName());
            if (modifierList != null && modifierList.size() > 0) {
                StringBuilder modifierSql = new StringBuilder(String.format(" WHERE %s IS IN (%s)",
                        modifierList.get(0).getColumnName(),
                        String.join(",", modifierList.get(0).getEnumValues())));
                if (modifierList.size() > 1) {
                    for (int i = 1; i < modifierList.size(); i++) {
                        modifierSql.append(String.format(" AND %s IS IN (%s)", modifierList.get(i).getColumnName(),
                                String.join(",", modifierList.get(i).getEnumValues())));
                    }
                }
                metricSql += modifierSql;
            }
        }
        else {
            metricSql = metric.getSpecialAttribute().getComplexMetricFormula();
        }
        return metricSql;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public MeasureDto create(MeasureDto metric, String operator) {
        checkArgument(isNotEmpty(operator), "创建者不能为空");
        checkArgument(isNotEmpty(metric.getLabelName()), "指标名称不能为空");
        checkArgument(isNotEmpty(metric.getLabelTag()), "类型不能为空");
        DevLabelDefine checkModifier = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelName, isEqualTo(metric.getLabelName())),
                and(devLabelDefine.labelTag, isEqualTo(metric.getLabelTag()))))
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
        checkArgument(metric.getSpecialAttribute() != null && metric.getSpecialAttribute().getCalculable() != null,
                "是否可累加不能为空");
        // 校验指标来源
        checkArgument(metric.getMeasureLabels() != null && metric.getMeasureLabels().size() > 0, "指标来源不能为空");
        List<LabelDto> metricLabelList = metric.getMeasureLabels();
        // 校验指标来源来自dws
        List<Long> dwsTableIdList = tableInfoService.getTablesByDataBase("dws").stream().map(TableInfoDto::getId).collect(Collectors.toList());
        checkArgument(dwsTableIdList.contains(metricLabelList.get(0).getTableId()), "指标来源有误，请选择dws层表");

        if (LabelTagEnum.DERIVE_METRIC_LABEL.name().equals(metric.getLabelTag())) {
            checkArgument(checkMetricBaseInfo(metric), "指标新建失败");
        }
        if (LabelTagEnum.ATOMIC_METRIC_LABEL.name().equals(metric.getLabelTag())) {
            // 校验可计算方式
            checkArgument(isNotEmpty(metric.getSpecialAttribute().getAggregatorCode()), "可计算方式不能为空");
            DevEnumValue checkAggregator = devEnumValueDao.selectOne(c -> c.where(devEnumValue.del, isNotEqualTo(1),
                    and(devEnumValue.valueCode, isEqualTo(metric.getSpecialAttribute().getAtomicMetricCode()))))
                    .orElseThrow(() -> new IllegalArgumentException("可计算方式不存在"));
            // 校验关联信息
            metricLabelList.forEach(metricLabel ->
                    checkArgument(columnInfoService.checkColumn(metricLabel.getColumnName(), metricLabel.getTableId()),
                            String.format("表%s不含%s字段", metricLabel.getTableId(), metricLabel.getColumnName())));
        }

        MeasureDto echoMetric = PojoUtil.copyOne(labelService.defineLabel(metric, operator), MeasureDto.class);
        List<LabelDto> echoMetricLabelList = metricLabelList.stream().map(metricLabel -> {
            metricLabel.setLabelCode(echoMetric.getLabelCode());
            return labelService.label(metricLabel, operator);
        }).collect(Collectors.toList());
        echoMetric.setMeasureLabels(echoMetricLabelList);
        return echoMetric;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public MeasureDto edit(MeasureDto metric, String operator) {
        checkArgument(isNotEmpty(operator), "修改者不能为空");
        checkArgument(isNotEmpty(metric.getLabelCode()), "指标Code不能为空");
        DevLabelDefine existMetric = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(metric.getLabelCode())), and(devLabelDefine.labelTag,
                        isLike("%_METRIC_LABEL%"))))
                .orElseThrow(() -> new IllegalArgumentException("指标不存在"));
        MeasureDto existMetricDto = PojoUtil.copyOne(existMetric, MeasureDto.class, "labelAttributes", "specialAttribute");
        AttributeDto existMetricIdAttribute = existMetricDto.getLabelAttributes()
                .stream().filter(t -> "metricId".equals(t.getAttributeKey())).findFirst().get();
        List<AttributeDto> labelAttributeList = metric.getLabelAttributes().stream()
                .filter(t -> !"metricId".equals(t.getAttributeKey())).collect(Collectors.toList());
        labelAttributeList.add(existMetricIdAttribute);

        metric.setLabelAttributes(labelAttributeList);
        metric.setLabelTag(existMetric.getLabelTag());
        if (LabelTagEnum.DERIVE_METRIC_LABEL.name().equals(metric.getLabelTag())) {
            checkArgument(checkMetricBaseInfo(metric), "指标更新失败");
        }
        MeasureDto echoMetric = PojoUtil.copyOne(labelService.defineLabel(metric, operator), MeasureDto.class);
        if (LabelTagEnum.ATOMIC_METRIC_LABEL.name().equals(metric.getLabelTag()) && metric.getMeasureLabels() != null
                && metric.getMeasureLabels().size() > 0) {
            // 校验可计算方式是否修改
            if (isNotEmpty(metric.getSpecialAttribute().getAggregatorCode())) {
                DevEnumValue checkAggregator = devEnumValueDao.selectOne(c -> c.where(devEnumValue.del, isNotEqualTo(1),
                        and(devEnumValue.valueCode, isEqualTo(metric.getSpecialAttribute().getAtomicMetricCode()))))
                        .orElseThrow(() -> new IllegalArgumentException("可计算方式不存在"));
            }
        }
        echoMetric.setMeasureLabels(metric.getMeasureLabels());
        return echoMetric;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public MeasureDto disableOrAble(String metricCode, String labelTag, String operator) {
        checkArgument(isNotEmpty(operator), "修改者不能为空");
        checkArgument(isNotEmpty(metricCode), "指标Code不能为空");
        String existLabelTag = labelTag.endsWith("_METRIC_LABEL_DISABLE") ?
                labelTag.substring(0, labelTag.length() - 8) : labelTag + "_DISABLE";
        DevLabelDefine existMetric = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(metricCode)),
                and(devLabelDefine.labelTag, isEqualTo(existLabelTag))))
                .orElse(null);
        checkArgument(existMetric != null, "指标不存在");

        // 原子指标校验是否被派生指标依赖
        if (LabelTagEnum.ATOMIC_METRIC_LABEL.name().equals(existMetric.getLabelTag())
                && labelTag.endsWith("_METRIC_LABEL_DISABLE")) {
            checkArgument(getRelyDeriveMetricNames(metricCode) == null,
                    getRelyDeriveMetricNames(metricCode) + "依赖该原子指标，不能停用");
        }
        devLabelDefineDao.update(c -> c.set(devLabelDefine.labelTag).equalTo(labelTag).set(devLabelDefine.editor).equalTo(operator)
                .where(devLabelDefine.del, isNotEqualTo(1), and(devLabelDefine.labelCode, isEqualTo(metricCode)),
                        and(devLabelDefine.labelTag, isEqualTo(existLabelTag))));
        return getMetricByCode(metricCode);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean delete(String metricCode, String operator) {
        checkArgument(isNotEmpty(operator), "修改者不能为空");
        checkArgument(isNotEmpty(metricCode), "指标Code不能为空");
        DevLabelDefine existModifier = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(metricCode)), and(devLabelDefine.labelTag, isLike("%_METRIC_LABEL_DISABLE"))))
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
        deriveMetricList.stream().peek(deriveMetric -> {
            AttributeDto metricLabelAttribute = deriveMetric.getLabelAttributes().stream()
                    .filter(labelAttribute -> labelAttribute.getAttributeKey().equals(METRIC_BIZ_TYPE))
                    .findAny().get();
            deriveMetric.setBizProcessValue(enumService.getEnumValue(metricLabelAttribute.getAttributeValue()));
        }).collect(Collectors.toList());
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

    // 查询指标
    private MetricDto getMetricByCode(String metricCode) {
        DevLabelDefine metric = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(metricCode))))
                .orElse(null);
        if (metric == null) {
            return null;
        }

        MetricDto echoMetric = PojoUtil.copyOne(metric, MetricDto.class);
        echoMetric.getLabelAttributes().stream()
                .peek(labelAttribute -> {
                    if (METRIC_BIZ_TYPE.equals(labelAttribute.getAttributeKey())) {
                        labelAttribute.setEnumValue(enumService.getEnumValue(labelAttribute.getAttributeValue()));
                    }
                }).collect(Collectors.toList());
        echoMetric.setMeasureLabels(labelService.findLabelsByCode(metricCode));
        // 反查维度和派生指标
        if (metric.getLabelTag().contains(LabelTagEnum.ATOMIC_METRIC_LABEL.name())
                || metric.getLabelTag().contains(LabelTagEnum.DERIVE_METRIC_LABEL.name())) {
            List<MeasureDto> dimensionList = dimensionService.findDimensionsByMetricCode(metricCode);
            echoMetric.setDimensions(dimensionList);
            SpecialAttributeDto metricSpecialAttribute = metric.getSpecialAttribute();
            String aggregate = StringUtils.isNotBlank(metricSpecialAttribute.getAggregatorCode())
                    ? metricSpecialAttribute.getAggregatorCode().split(":")[0].split("_")[1] : null;
            metricSpecialAttribute.setAggregate(aggregate);
            metric.setSpecialAttribute(metricSpecialAttribute);
            if (metric.getLabelTag().contains(LabelTagEnum.ATOMIC_METRIC_LABEL.name())) {
                List<MeasureDto> deriveMetricList = getMetricByAtomic(metricCode);
                echoMetric.setDeriveMetrics(deriveMetricList);
            }
            else {
                MeasureDto atomicMetric = getMetricByCode(echoMetric.getSpecialAttribute().getAtomicMetricCode());
                // 派生指标表名和字段名
                echoMetric.setMeasureLabels(labelService.findLabelsByCode(atomicMetric.getLabelCode()));
                SpecialAttributeDto echoSpecialAttribute = atomicMetric.getSpecialAttribute();
                echoSpecialAttribute.setAtomicMetricCode(atomicMetric.getLabelCode());
                echoSpecialAttribute.setAtomicMetricName(atomicMetric.getLabelName());
                echoMetric.setSpecialAttribute(echoSpecialAttribute);
                List<LabelDto> atomicLabelList = labelService.findLabelsByCode(atomicMetric.getLabelCode());
                if (atomicLabelList.size() > 0) {
                    echoMetric.setModifiers(modifierService.findModifiers(metricCode, atomicLabelList.get(0).getTableId()));
                }
            }
        }
        return echoMetric;
    }

    private String changeAggregate(String columnName, String aggregator) {
        AggregatorEnum aggregatorEnum = AggregatorEnum.valueOf(aggregator);
        String aggregateSql;
        if (AggregatorEnum.SUM.equals(aggregatorEnum)) {
            aggregateSql = String.format("SUM(%s)", columnName);
        }
        else if (AggregatorEnum.AVG.equals(aggregatorEnum)) {
            aggregateSql = String.format("AVG(%s)", columnName);
        }
        else if (AggregatorEnum.MAX.equals(aggregatorEnum)) {
            aggregateSql = String.format("MAX(%s)", columnName);
        }
        else if (AggregatorEnum.MIN.equals(aggregatorEnum)) {
            aggregateSql = String.format("MIN(%s)", columnName);
        }
        else if (AggregatorEnum.CNT.equals(aggregatorEnum)) {
            aggregateSql = String.format("COUNT(%s)", columnName);
        }
        else {
            aggregateSql = String.format("COUNT(DISTINCT(%s))", columnName);
        }
        return aggregateSql;
    }

    private Boolean checkMetricBaseInfo(MeasureDto metric) {
        String metricType = metric.getLabelTag();
        LabelTagEnum.valueOf(metricType);
        checkArgument(metricType.contains("_METRIC_LABEL"), "指标类型有误");

        List<LabelDto> metricLabelList = metric.getMeasureLabels();
        if (LabelTagEnum.DERIVE_METRIC_LABEL.name().equals(metric.getLabelTag())) {
            checkArgument(isNotEmpty(metric.getSpecialAttribute().getAtomicMetricCode()), "原子指标不能为空");
            DevLabelDefine checkAtomicMetric = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                    and(devLabelDefine.labelCode, isEqualTo(metric.getSpecialAttribute().getAtomicMetricCode())),
                    and(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.ATOMIC_METRIC_LABEL.name()))))
                    .orElseThrow(() -> new IllegalArgumentException("原子指标不存在或已停用"));
            // 校验维度
            checkArgument(ObjectUtils.isNotEmpty(metric.getSpecialAttribute().getDimTableIds()), "维度不能为空");
            List<Long> existForeignKeyTblIdList = devForeignKeyDao.select(c -> c.where(devForeignKey.del, isNotEqualTo(1),
                    and(devForeignKey.tableId, isEqualTo(metricLabelList.get(0).getTableId()))))
                    .stream().map(DevForeignKey::getReferTableId).sorted().collect(Collectors.toList());
            checkArgument(existForeignKeyTblIdList.size() == metric.getSpecialAttribute().getDimTableIds().size() &&
                    existForeignKeyTblIdList.containsAll(metric.getSpecialAttribute().getDimTableIds()), "请选择正确的维表");
            // 校验修饰词
            checkArgument(ObjectUtils.isNotEmpty(metric.getSpecialAttribute().getModifiers()), "修饰词不能为空");
            List<ModifierDto> relatedModifierList = metric.getSpecialAttribute().getModifiers();
            Set<String> relatedModifierCodes = relatedModifierList.stream().map(ModifierDto::getModifierCode).collect(Collectors.toSet());
            checkArgument(relatedModifierList.size() == relatedModifierCodes.size(), "修饰词不能重复");
            List<DevLabelDefine> existModifierList = devLabelDefineDao.select(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                    and(devLabelDefine.labelCode, isIn(relatedModifierCodes)),
                    and(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.MODIFIER_LABEL.name()))));
            checkArgument(relatedModifierCodes.size() == existModifierList.size(), "修饰词有误，部分修饰词不存在");
            // 校验修饰词底表属于指标来源表或维表
            Set<Long> modifierTableIds = devLabelDao.select(c -> c.where(devLabel.del, isNotEqualTo(1),
                    and(devLabel.labelCode, isIn(relatedModifierCodes))))
                    .stream().map(DevLabel::getTableId).collect(Collectors.toSet());
            Set<Long> allConfigTblIds = new HashSet<>(metric.getSpecialAttribute().getDimTableIds());
            allConfigTblIds.add(metricLabelList.get(0).getTableId());
            checkArgument(allConfigTblIds.containsAll(modifierTableIds), "修饰词有误，请选择来源于指标来源表或维表的修饰词");
        }
        return true;
    }
}
