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
import cn.zhengcaiyun.idata.develop.dal.model.DevLabelDefine;
import cn.zhengcaiyun.idata.develop.dto.label.*;
import cn.zhengcaiyun.idata.develop.dto.measure.MeasureDto;
import cn.zhengcaiyun.idata.develop.service.label.LabelService;
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
 * @date 2021-06-23 09:46
 */

@Service
public class ModifierServiceImpl implements ModifierService {

    @Autowired
    private DevLabelDefineDao devLabelDefineDao;
    @Autowired
    private DevLabelDao devLabelDao;
    @Autowired
    private LabelService labelService;
    @Autowired
    private ModifierService modifierService;
    @Autowired
    private ColumnInfoService columnInfoService;

    private String[] modifierInfos = new String[]{"enName", "modifierEnum", "modifierDefine"};

    @Override
    public MeasureDto findModifier(String modifierCode) {
        return getModifierByCode(modifierCode);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public MeasureDto create(MeasureDto modifier, String operator) {
        checkArgument(isNotEmpty(operator), "创建者不能为空");
        checkArgument(isNotEmpty(modifier.getLabelName()), "修饰词名称不能为空");
        checkArgument(isNotEmpty(modifier.getLabelTag()), "类型不能为空");
        DevLabelDefine checkModifier = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelName, isEqualTo(modifier.getLabelName()))))
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
        checkArgument(modifier.getMeasureLabels() != null && modifier.getMeasureLabels().size() > 0, "关联信息不能为空");

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
        PojoUtil.copyTo(modifier, existModifier, "labelName", "labelAttributes", "folderId");
        MeasureDto echoDimension = PojoUtil.copyOne(labelService.defineLabel(PojoUtil.copyOne(existModifier, LabelDefineDto.class), operator),
                MeasureDto.class);
        if (modifierLabelList != null) {
            List<LabelDto> existModifierLabelList = PojoUtil.copyList(devLabelDao.selectMany(select(devLabel.allColumns())
                            .from(devLabel)
                            .where(devLabel.del, isNotEqualTo(1), and(devLabel.labelCode,
                                    isEqualTo(modifier.getLabelCode())))
                            .build().render(RenderingStrategies.MYBATIS3)),
                    LabelDto.class);
            Set<String> existModifierStr = existModifierLabelList.stream().map(existModifierLabel ->
                    existModifierLabel.getTableId() + "_" + existModifierLabel.getColumnName())
                    .collect(Collectors.toSet());
            Set<String> modifierStr = modifierLabelList.stream().map(modifierLabel ->
                    modifierLabel.getTableId() + "_" + modifierLabel.getColumnName())
                    .collect(Collectors.toSet());
            Set<String> addModifierStr = new HashSet<>(modifierStr);
            addModifierStr.removeAll(existModifierStr);
            List<LabelDto> addModifierLabelList = modifierLabelList.stream().filter(modifierLabel ->
                    addModifierStr.contains(modifierLabel.getTableId() + "_" + modifierLabel.getColumnName())
            ).collect(Collectors.toList());
            Set<String> deleteModifierStr = new HashSet<>(existModifierStr);
            deleteModifierStr.removeAll(modifierStr);
            List<LabelDto> deleteModifierLabelList = existModifierLabelList.stream().filter(modifierLabel ->
                    deleteModifierStr.contains(modifierLabel.getTableId() + "_" + modifierLabel.getColumnName())
            ).collect(Collectors.toList());
            List<LabelDto> addEchoModifierLabelList = addModifierLabelList.stream().map(modifierLabel ->
                    labelService.label(modifierLabel, operator))
                    .collect(Collectors.toList());
            boolean isDelete = deleteModifierLabelList.stream().allMatch(deleteModifierLabel ->
                    labelService.removeLabel(deleteModifierLabel, operator));
            echoDimension.setMeasureLabels(labelService.findLabelsByCode(modifier.getLabelCode()));
        }
        return echoDimension;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public MeasureDto disable(String modifierCode, String operator) {
        checkArgument(isNotEmpty(operator), "修改者不能为空");
        checkArgument(isNotEmpty(modifierCode), "修饰词Code不能为空");
        DevLabelDefine checkModifier = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(modifierCode)), and(devLabelDefine.labelTag,
                        isEqualTo(LabelTagEnum.MODIFIER_LABEL.name()))))
                .orElse(null);
        checkArgument(checkModifier != null, "修饰词不存在或已停用");

        // 派生指标依赖校验
        List<String> relyDeriveMetricNameList = getRelyDeriveMetricName(modifierCode);
        checkArgument(relyDeriveMetricNameList.size() == 0,
                String.join(",", relyDeriveMetricNameList) + "依赖该修饰词，不能停用");
        devLabelDefineDao.update(c -> c.set(devLabelDefine.labelTag).equalTo(LabelTagEnum.MODIFIER_LABEL_DISABLE.name())
                .set(devLabelDefine.editor).equalTo(operator)
                .where(devLabelDefine.del, isNotEqualTo(1), and(devLabelDefine.labelCode, isEqualTo(modifierCode)),
                        and(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.MODIFIER_LABEL.name()))));

        return getModifierByCode(modifierCode);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean delete(String modifierCode, String operator) {
        checkArgument(isNotEmpty(operator), "删除者不能为空");
        checkArgument(isNotEmpty(modifierCode), "修饰词Code不能为空");
        DevLabelDefine existDimension = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(modifierCode)), and(devLabelDefine.labelTag,
                        isEqualTo(LabelTagEnum.MODIFIER_LABEL_DISABLE.name()))))
                .orElse(null);
        checkArgument(existDimension != null, "修饰词不存在");
        // 派生指标依赖校验
        List<String> relyDeriveMetricNameList = getRelyDeriveMetricName(modifierCode);
        checkArgument(relyDeriveMetricNameList.size() == 0,
                String.join(",", relyDeriveMetricNameList) + "依赖该修饰词，不能删除");
        return labelService.deleteDefine(modifierCode, operator);
    }

    private MeasureDto getModifierByCode(String modifierCode) {
        DevLabelDefine modifier = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(modifierCode))))
                .orElse(null);
        checkArgument(modifier != null, "修饰词不存在");

        MeasureDto echoModifier = PojoUtil.copyOne(modifier, MeasureDto.class);
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
