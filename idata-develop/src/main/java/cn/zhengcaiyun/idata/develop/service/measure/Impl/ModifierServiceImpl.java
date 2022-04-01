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
import cn.zhengcaiyun.idata.develop.dal.dao.DevEnumValueDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDao;
import cn.zhengcaiyun.idata.develop.dal.model.*;
import cn.zhengcaiyun.idata.develop.dto.label.*;
import cn.zhengcaiyun.idata.develop.dto.measure.MeasureDto;
import cn.zhengcaiyun.idata.develop.dto.measure.ModifierDto;
import cn.zhengcaiyun.idata.develop.service.label.EnumService;
import cn.zhengcaiyun.idata.develop.service.label.LabelService;
import cn.zhengcaiyun.idata.develop.service.measure.ModifierService;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scala.reflect.internal.Trees;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumValueDynamicSqlSupport.devEnumValue;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDynamicSqlSupport.devLabelDefine;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.devLabel;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDynamicSqlSupport.devTableInfo;
import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author caizhedong
 * @date 2021-06-23 09:46
 */

@Service
public class ModifierServiceImpl implements ModifierService {

    @Autowired
    private DevLabelDefineDao devLabelDefineDao;
    @Autowired
    private DevLabelDao devLabelDao;
    @Autowired
    private DevTableInfoDao devTableInfoDao;
    @Autowired
    private DevEnumValueDao devEnumValueDao;
    @Autowired
    private LabelService labelService;
    @Autowired
    private ColumnInfoService columnInfoService;
    @Autowired
    private EnumService enumService;

    private String[] modifierInfos = new String[]{"modifierDefine"};
    private final String MODIFIER_ENUM = "modifierEnum";

    @Override
    public MeasureDto findModifier(String modifierCode) {
        return getModifierByCode(modifierCode);
    }

    // 依靠派生指标code查询修饰词
    @Override
    public List<ModifierDto> findModifiers(String metricCode, Long atomicTableId) {
        DevLabelDefine metric = devLabelDefineDao.selectOne(c ->
                c.where(devLabelDefine.del, isNotEqualTo(1), and(devLabelDefine.labelCode, isEqualTo(metricCode))))
                .orElse(null);
        checkArgument(metric != null, "指标不存在");
        Map<String, List<String>> metricModifierMap = metric.getSpecialAttribute().getModifiers().stream()
                .collect(Collectors.toMap(ModifierDto::getModifierCode, ModifierDto::getEnumValueCodes));
        if (metricModifierMap.size() == 0) {
            return new ArrayList<>();
        }
        List<String> modifierCodeList = PojoUtil.copyOne(metric, MeasureDto.class).getSpecialAttribute().getModifiers()
                .stream().map(ModifierDto::getModifierCode).collect(Collectors.toList());
        List<MeasureDto> modifierList = PojoUtil.copyList(devLabelDefineDao.select(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isIn(modifierCodeList)))), MeasureDto.class);
        List<ModifierDto> echoModifierList = modifierList.stream().map(modifier -> {
            ModifierDto echoModifier = new ModifierDto();
            echoModifier.setModifierCode(modifier.getLabelCode());
            echoModifier.setModifierName(modifier.getLabelName());
            AttributeDto modifierAttribute = modifier.getLabelAttributes()
                    .stream().filter(labelAttribute -> labelAttribute.getAttributeKey().equals(MODIFIER_ENUM))
                    .findAny().orElse(null);
            if (modifierAttribute != null) {
                echoModifier.setModifierAttribute(modifierAttribute);
            }
            if (metricModifierMap.containsKey(modifier.getLabelCode())) {
                List<String> modifierEnumValueList = enumService.getEnumValues(metricModifierMap.get(modifier.getLabelCode()))
                        .stream().map(DevEnumValue::getEnumValue).collect(Collectors.toList());
                echoModifier.setEnumValues(modifierEnumValueList);
                echoModifier.setEnumValueCodes(metricModifierMap.get(modifier.getLabelCode()));
            }
            List<LabelDto> modifierLabelList = labelService.findLabelsByCode(modifier.getLabelCode());
            if (modifierLabelList.size() > 0) {
                echoModifier.setColumnName(modifierLabelList.get(0).getColumnName());
                echoModifier.setColumnComment(modifierLabelList.get(0).getColumnComment());
                echoModifier.setColumnDataType(modifierLabelList.get(0).getColumnDataType());
            }
            return echoModifier;
        }).collect(Collectors.toList());
        return echoModifierList;
    }

    @Override
    public List<MeasureDto> findModifiersByAtomicCode(String atomicMetricCode) {
        DevLabel atomicLabel = devLabelDao.selectOne(select(devLabel.allColumns())
                .from(devLabel)
                .leftJoin(devLabelDefine).on(devLabel.labelCode, equalTo(devLabelDefine.labelCode))
                .where(devLabel.labelCode, isEqualTo(atomicMetricCode), and(devLabel.del, isNotEqualTo(1)),
                        and(devLabelDefine.labelCode, isEqualTo(atomicMetricCode)), and(devLabelDefine.del, isNotEqualTo(1)),
                        and(devLabelDefine.labelTag, isNotEqualTo(LabelTagEnum.ATOMIC_METRIC_LABEL_DISABLE.name())))
                .build().render(RenderingStrategies.MYBATIS3))
                .orElse(null);
        if (atomicLabel == null) { return null; }
        Set<String> modifierCodes = devLabelDefineDao.selectMany(select(devLabelDefine.allColumns())
                .from(devLabelDefine)
                .leftJoin(devLabel).on(devLabelDefine.labelCode, equalTo(devLabel.labelCode))
                .where(devLabelDefine.del, isNotEqualTo(1), and(devLabel.del, isNotEqualTo(1)),
                        and(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.MODIFIER_LABEL.name())),
                        and(devLabel.tableId, isEqualTo(atomicLabel.getTableId())))
                .build().render(RenderingStrategies.MYBATIS3)).stream().map(DevLabelDefine::getLabelCode).collect(Collectors.toSet());
        return PojoUtil.copyList(devLabelDefineDao.selectMany(select(devLabelDefine.allColumns())
                .from(devLabelDefine)
                .where(devLabelDefine.labelCode, isIn(modifierCodes))
                .build().render(RenderingStrategies.MYBATIS3)), MeasureDto.class);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public MeasureDto create(MeasureDto modifier, String operator) {
        checkArgument(isNotEmpty(operator), "创建者不能为空");
        checkArgument(isNotEmpty(modifier.getLabelName()), "修饰词名称不能为空");
        modifier.setLabelTag(LabelTagEnum.MODIFIER_LABEL.name());
        modifier.setSubjectType(SubjectTypeEnum.COLUMN.name());
        modifier.setFolderId(0L);
        DevLabelDefine checkModifier = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelName, isEqualTo(modifier.getLabelName())),
                and(devLabelDefine.labelTag, isLike(LabelTagEnum.MODIFIER_LABEL.name()))))
                .orElse(null);
        checkArgument(checkModifier == null, "修饰词已存在");
        checkArgument(modifier.getLabelAttributes() != null && modifier.getLabelAttributes().size() > 0, "基本信息不能为空");

        List<String> modifierInfoList = new ArrayList<>(Arrays.asList(modifierInfos));
        List<String> modifierAttributeKeyList = modifier.getLabelAttributes().stream().map(AttributeDto::getAttributeKey)
                .collect(Collectors.toList());
        if (!modifierAttributeKeyList.containsAll(modifierInfoList)) {
            modifierInfoList.removeAll(modifierAttributeKeyList);
            throw new IllegalArgumentException(String.join(",", modifierInfoList) + "不能为空");
        }
        checkArgument(modifier.getMeasureLabels() != null && modifier.getMeasureLabels().size() == 1, "修饰词来源不能为空");
        checkArgument(StringUtils.isNotEmpty(modifier.getMeasureLabels().get(0).getLabelParamValue()), "枚举值不能为空");

        List<LabelDto> modifierLabelList = modifier.getMeasureLabels();
        // 校验关联信息
        modifierLabelList.forEach(modifierLabel -> {
            checkArgument(columnInfoService.checkColumn(modifierLabel.getColumnName(), modifierLabel.getTableId()),
                    String.format("表%s不含%s字段", modifierLabel.getTableId(), modifierLabel.getColumnName()));
        });

        MeasureDto echoModifier = PojoUtil.copyOne(labelService.defineLabel(modifier, operator), MeasureDto.class);
        List<LabelDto> echoDimensionLabelList = modifierLabelList.stream().map(modifierLabel -> {
            modifierLabel.setLabelCode(echoModifier.getLabelCode());
            return labelService.label(modifierLabel, operator);
        }).collect(Collectors.toList());
        echoModifier.setMeasureLabels(echoDimensionLabelList);
        return echoModifier;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public MeasureDto edit(MeasureDto modifier, String operator) {
        checkArgument(isNotEmpty(operator), "修改者不能为空");
        checkArgument(isNotEmpty(modifier.getLabelCode()), "修饰词Code不能为空");
        DevLabelDefine existModifier = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(modifier.getLabelCode()))))
                .orElse(null);
        checkArgument(existModifier != null, "修饰词不存在");

        List<LabelDto> modifierLabelList = modifier.getMeasureLabels() != null && modifier.getMeasureLabels().size() > 0
                ? modifier.getMeasureLabels() : null;
        PojoUtil.copyTo(modifier, existModifier, "labelName", "labelAttributes");
        MeasureDto echoModifier = PojoUtil.copyOne(labelService.defineLabel(PojoUtil.copyOne(existModifier, LabelDefineDto.class), operator),
                MeasureDto.class);
        if (modifierLabelList != null) {
            LabelDto modifierLabel = modifierLabelList.get(0);
            LabelDto existModifierLabel = PojoUtil.copyList(devLabelDao.selectMany(select(devLabel.allColumns())
                            .from(devLabel)
                            .where(devLabel.del, isNotEqualTo(1), and(devLabel.labelCode, isEqualTo(modifier.getLabelCode())))
                            .build().render(RenderingStrategies.MYBATIS3)),
                    LabelDto.class).get(0);
            modifierLabel.setLabelCode(modifier.getLabelCode());
            labelService.label(modifierLabel, operator);
            echoModifier.setMeasureLabels(labelService.findLabelsByCode(modifier.getLabelCode()));
        }
        return echoModifier;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public MeasureDto disableOrAble(String modifierCode, String labelTag, String operator) {
        checkArgument(isNotEmpty(operator), "修改者不能为空");
        checkArgument(isNotEmpty(modifierCode), "修饰词Code不能为空");
        String existLabelTag = LabelTagEnum.MODIFIER_LABEL_DISABLE.name().equals(labelTag) ?
                LabelTagEnum.MODIFIER_LABEL.name() : LabelTagEnum.MODIFIER_LABEL_DISABLE.name();
        DevLabelDefine checkModifier = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(modifierCode)), and(devLabelDefine.labelTag,
                        isEqualTo(existLabelTag))))
                .orElse(null);
        checkArgument(checkModifier != null, "修饰词不存");

        // 派生指标依赖校验
        if (LabelTagEnum.MODIFIER_LABEL_DISABLE.name().equals(labelTag)) {
            List<String> relyDeriveMetricNameList = getRelyDeriveMetricName(modifierCode);
            checkArgument(relyDeriveMetricNameList.size() == 0,
                    String.join(",", relyDeriveMetricNameList) + "依赖该修饰词，不能停用");
        }
        devLabelDefineDao.update(c -> c.set(devLabelDefine.labelTag).equalTo(labelTag)
                .set(devLabelDefine.editor).equalTo(operator)
                .where(devLabelDefine.del, isNotEqualTo(1), and(devLabelDefine.labelCode, isEqualTo(modifierCode)),
                        and(devLabelDefine.labelTag, isEqualTo(existLabelTag))));
        return getModifierByCode(modifierCode);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean delete(String modifierCode, String operator) {
        checkArgument(isNotEmpty(operator), "删除者不能为空");
        checkArgument(isNotEmpty(modifierCode), "修饰词Code不能为空");
        DevLabelDefine existDimension = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(modifierCode))))
                .orElse(null);
        checkArgument(existDimension != null, "修饰词不存在");
        // 派生指标依赖校验
        List<String> relyDeriveMetricNameList = getRelyDeriveMetricName(modifierCode);
        checkArgument(relyDeriveMetricNameList.size() == 0,
                String.join(",", relyDeriveMetricNameList) + "依赖该修饰词，不能删除");
        return labelService.deleteDefine(modifierCode, operator);
    }

    @Override
    public int mergeOldModifiers() {
        // 默认修饰词仅有一张事实表，冗余数据数仓已处理，将存在labelAttribute中的枚举存到label中的labelParam
        List<LabelDefineDto> existModifierList = PojoUtil.copyList(devLabelDefineDao.select(c ->
                c.where(devLabelDefine.del, isNotEqualTo(1), and(devLabelDefine.labelTag, isLike("MODIFIER")))),
                LabelDefineDto.class, "labelCode", "labelAttributes");
        List<String> modifierCodeList = existModifierList.stream().map(LabelDefineDto::getLabelCode).collect(Collectors.toList());
        List<String> enumCodeList = enumService.getEnums().stream().map(EnumDto::getEnumCode).collect(Collectors.toList());
        Map<String, List<DevEnumValue>> enumMap = devEnumValueDao.select(c -> c.where(devEnumValue.del, isNotEqualTo(1),
                and(devEnumValue.enumCode, isIn(enumCodeList))))
                .stream().collect(Collectors.groupingBy(DevEnumValue::getEnumCode));
        Map<String, String> modifierEnumValueMap = modifierCodeList.stream()
                .map(this::getModifierByCode)
                .collect(Collectors.toMap(MeasureDto::getLabelCode, measure -> {
                    AttributeDto attributeDto = measure.getLabelAttributes().stream()
                            .filter(labelAttribute -> labelAttribute.getAttributeKey().equals(MODIFIER_ENUM))
                            .findAny().orElse(null);
                    if (attributeDto != null) {
                        List<String> enumValueList = enumMap.get(attributeDto.getEnumValue())
                                .stream().map(DevEnumValue::getEnumValue).collect(Collectors.toList());
                        return String.join(",", enumValueList);
                    }
                    return "";
                }));
        int total = 0;
        for (String modifierCode : modifierCodeList) {
            LabelDto modifierLabel = labelService.findLabelsByCode(modifierCode).get(0);
            modifierLabel.setLabelParamValue(modifierEnumValueMap.getOrDefault(modifierCode, ""));
            total += devLabelDao.updateByPrimaryKeySelective(PojoUtil.copyOne(modifierCode, DevLabel.class,
                    "id", "labelParamValue"));
        }
        return total;
    }

    @Override
    public List<DevLabelDefine> findModifiersByTableIds(String tableIds) {
        List<Long> tableIdList = new ArrayList<>(Arrays.asList(tableIds.split(",")))
                .stream().map(Long::valueOf).collect(Collectors.toList());
        List<DevLabel> modifierLabelList = devLabelDao.select(c -> c.where(devLabel.del, isNotEqualTo(1),
                and(devLabel.tableId, isIn(tableIdList))));
        if (modifierLabelList.size() == 0) return new ArrayList<>();
        List<String> modifierCodeList = modifierLabelList.stream().map(DevLabel::getLabelCode).collect(Collectors.toList());
        return devLabelDefineDao.select(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isIn(modifierCodeList)),
                and(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.MODIFIER_LABEL.name()))));
    }

    private MeasureDto getModifierByCode(String modifierCode) {
        DevLabelDefine modifier = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(modifierCode))))
                .orElse(null);
        checkArgument(modifier != null, "修饰词不存在");

        MeasureDto echoModifier = PojoUtil.copyOne(modifier, MeasureDto.class);
        echoModifier.getLabelAttributes().stream()
                .peek(labelAttribute -> {
                    if (MODIFIER_ENUM.equals(labelAttribute.getAttributeKey())) {
                        labelAttribute.setEnumName(enumService.getEnumName(labelAttribute.getAttributeValue()));
                        List<String> enumValueList = enumService.getEnumValues(labelAttribute.getAttributeValue())
                                .stream().map(EnumValueDto::getEnumValue).collect(Collectors.toList());
                        labelAttribute.setEnumValue(String.join(",", enumValueList));
                    }
                }).collect(Collectors.toList());
        echoModifier.setMeasureLabels(labelService.findLabelsByCode(modifierCode));
        return echoModifier;
    }

    private List<String> getRelyDeriveMetricName(String modifierCode) {
        List<LabelDefineDto> deriveMetricList = PojoUtil.copyList(devLabelDefineDao.selectMany(select(devLabelDefine.allColumns())
                .from(devLabelDefine)
                .where(devLabelDefine.del, isNotEqualTo(1),
                        and(devLabelDefine.labelTag, isLike(LabelTagEnum.DERIVE_METRIC_LABEL.name() + "%")))
                .build().render(RenderingStrategies.MYBATIS3)), LabelDefineDto.class);
        Map<String, List<String>> relyDeriveMetricMap = deriveMetricList.stream().collect(Collectors.toMap(LabelDefineDto::getLabelName,
                deriveMetric -> deriveMetric.getSpecialAttribute().getModifiers().stream().map(ModifierDto::getModifierCode)
                        .collect(Collectors.toList())));
        return relyDeriveMetricMap.entrySet().stream().filter(deriveMetric ->
                deriveMetric.getValue().contains(modifierCode))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
