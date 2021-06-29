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
import cn.zhengcaiyun.idata.develop.dto.label.AttributeDto;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDefineDto;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.label.LabelTagEnum;
import cn.zhengcaiyun.idata.develop.dto.measure.MeasureDto;
import cn.zhengcaiyun.idata.develop.service.label.LabelService;
import cn.zhengcaiyun.idata.develop.service.measure.DimensionService;
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
 * @date 2021-06-23 09:47
 */

@Service
public class DimensionServiceImpl implements DimensionService {

    @Autowired
    private DevLabelDefineDao devLabelDefineDao;
    @Autowired
    private DevLabelDao devLabelDao;
    @Autowired
    private LabelService labelService;
    @Autowired
    private DimensionService dimensionService;
    @Autowired
    private ColumnInfoService columnInfoService;

    private String[] dimensionInfos = new String[]{"enName", "dimensionId", "dimensionDefine"};

    @Override
    public MeasureDto findDimension(String dimensionCode) {
        DevLabelDefine dimension = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(dimensionCode))))
                .orElse(null);
        checkArgument(dimension != null, "维度不存在");

        MeasureDto echoDimension = PojoUtil.copyOne(dimension, MeasureDto.class);
        echoDimension.setMeasureLabels(labelService.findLabelsByCode(dimensionCode));
        return echoDimension;
    }

    @Override
    public List<MeasureDto> findDimensionsByLabelCode(String labelCode) {
        return null;
    }

    @Override
    public List<String> findDimensionValues(String dimensionCode) {
        // TODO 展天底表暂无，mock数据用于联调
        String[] mockDimensionValues = new String[]{"339900", "330899", "330802"};
        return Arrays.asList(mockDimensionValues);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public MeasureDto create(MeasureDto dimension, String operator) {
        checkArgument(isNotEmpty(operator), "创建者不能为空");
        checkArgument(isNotEmpty(dimension.getLabelName()), "维度名称不能为空");
        checkArgument(isNotEmpty(dimension.getLabelTag()), "类型不能为空");
        DevLabelDefine checkDimension = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelName, isEqualTo(dimension.getLabelName()))))
                .orElse(null);
        checkArgument(checkDimension == null, "维度已存在");
        checkArgument(dimension.getLabelAttributes() != null && dimension.getLabelAttributes().size() > 0, "基本信息不能为空");
        List<String> dimensionInfoList = new ArrayList<>(Arrays.asList(dimensionInfos));
        List<String> dimensionAttributeKeyList = dimension.getLabelAttributes().stream().map(AttributeDto::getAttributeKey)
                .collect(Collectors.toList());
        if (!dimensionAttributeKeyList.containsAll(dimensionInfoList)) {
            dimensionInfoList.removeAll(dimensionAttributeKeyList);
            throw new IllegalArgumentException(String.join(",", dimensionInfoList) + "不能为空");
        }
        checkArgument(dimension.getMeasureLabels() != null && dimension.getMeasureLabels().size() > 0, "关联信息不能为空");
        checkArgument(dimension.getSpecialAttribute().getDegradeDim() != null, "是否退化维不能我空");

        List<LabelDto> dimensionLabelList = dimension.getMeasureLabels();
        // 校验关联信息
        List<LabelDto> dimTableIdList = dimensionLabelList.stream().filter(dimensionLabel ->
                "true".equals(dimensionLabel.getLabelParamValue()))
                .collect(Collectors.toList());
        checkArgument(dimTableIdList.size() == 1, "主表数量有误");
        dimensionLabelList.forEach(dimensionLabel -> {
            checkArgument(columnInfoService.checkColumn(dimensionLabel.getColumnName(), dimensionLabel.getTableId()),
                    String.format("表%s不含%s字段", dimensionLabel.getTableId(), dimensionLabel.getColumnName()));
        });

        MeasureDto echoDimension = PojoUtil.copyOne(labelService.defineLabel(dimension, operator), MeasureDto.class);
        List<LabelDto> echoDimensionLabelList = dimensionLabelList.stream().map(dimensionLabel -> {
            dimensionLabel.setLabelCode(echoDimension.getLabelCode());
            return labelService.label(dimensionLabel, operator);
        }).collect(Collectors.toList());
        echoDimension.setMeasureLabels(echoDimensionLabelList);
        return echoDimension;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public MeasureDto edit(MeasureDto dimension, String operator) {
        checkArgument(isNotEmpty(operator), "修改者不能为空");
        checkArgument(isNotEmpty(dimension.getLabelCode()), "维度Code不能为空");
        DevLabelDefine existDimension = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(dimension.getLabelCode()))))
                .orElse(null);
        checkArgument(existDimension != null, "维度不存在");

        List<LabelDto> dimensionLabelList = dimension.getMeasureLabels() != null && dimension.getMeasureLabels().size() > 0
                ? dimension.getMeasureLabels() : null;
        dimension.setMeasureLabels(null);
        PojoUtil.copyTo(dimension, existDimension, "labelName", "labelAttributes", "specialAttribute", "folderId");
        MeasureDto echoDimension = PojoUtil.copyOne(labelService.defineLabel(PojoUtil.copyOne(existDimension, LabelDefineDto.class), operator),
                MeasureDto.class);
        if (dimensionLabelList != null) {
            List<LabelDto> existDimensionLabelList = PojoUtil.copyList(devLabelDao.selectMany(select(devLabel.allColumns())
                            .from(devLabel)
                            .where(devLabel.del, isNotEqualTo(1), and(devLabel.labelCode, isEqualTo(dimension.getLabelCode())))
                            .build().render(RenderingStrategies.MYBATIS3)),
                    LabelDto.class);
            Set<String> existDimensionStr = existDimensionLabelList.stream().map(existDimensionLabel ->
                    existDimensionLabel.getTableId() + "_" + existDimensionLabel.getColumnName())
                    .collect(Collectors.toSet());
            Set<String> dimensionStr = dimensionLabelList.stream().map(dimensionLabel ->
                    dimensionLabel.getTableId() + "_" + dimensionLabel.getColumnName())
                    .collect(Collectors.toSet());
            Set<String> addDimensionStr = new HashSet<>(dimensionStr);
            addDimensionStr.removeAll(existDimensionStr);
            List<LabelDto> addDimensionLabelList = dimensionLabelList.stream().filter(dimensionLabel ->
                    addDimensionStr.contains(dimensionLabel.getTableId() + "_" + dimensionLabel.getColumnName())
            ).collect(Collectors.toList());
            Set<String> deleteDimensionStr = new HashSet<>(existDimensionStr);
            deleteDimensionStr.removeAll(dimensionStr);
            List<LabelDto> deleteDimensionLabelList = existDimensionLabelList.stream().filter(dimensionLabel ->
                    deleteDimensionStr.contains(dimensionLabel.getTableId() + "_" + dimensionLabel.getColumnName())
            ).collect(Collectors.toList());
            List<LabelDto> addEchoDimensionLabelList = addDimensionLabelList.stream().map(dimensionLabel ->
                    labelService.label(dimensionLabel, operator))
                    .collect(Collectors.toList());
            boolean isDelete = deleteDimensionLabelList.stream().allMatch(deleteDimensionLabel ->
                    labelService.removeLabel(deleteDimensionLabel, operator));

            echoDimension.setMeasureLabels(labelService.findLabelsByCode(dimension.getLabelCode()));
        }
        return echoDimension;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public MeasureDto disable(String dimensionCode, String operator) {
        checkArgument(isNotEmpty(operator), "修改者不能为空");
        checkArgument(isNotEmpty(dimensionCode), "维度Code不能为空");
        DevLabelDefine checkDimension = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(dimensionCode)), and(devLabelDefine.labelTag,
                        isEqualTo(LabelTagEnum.DIMENSION_LABEL.name()))))
                .orElse(null);
        checkArgument(checkDimension != null, "维度不存在或已停用");

        devLabelDefineDao.update(c -> c.set(devLabelDefine.labelTag).equalTo(LabelTagEnum.DIMENSION_LABEL_DISABLE.name())
                .set(devLabelDefine.editor).equalTo(operator)
                .where(devLabelDefine.del, isNotEqualTo(1), and(devLabelDefine.labelCode, isEqualTo(dimensionCode)),
                        and(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.DIMENSION_LABEL.name()))));
        return dimensionService.findDimension(dimensionCode);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean delete(String dimensionCode, String operator) {
        checkArgument(isNotEmpty(operator), "删除者不能为空");
        checkArgument(isNotEmpty(dimensionCode), "维度Code不能为空");
        DevLabelDefine checkDimension = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(dimensionCode))))
                .orElse(null);
        checkArgument(checkDimension != null, "维度不存在");

        return labelService.deleteDefine(dimensionCode, operator);
    }
}
