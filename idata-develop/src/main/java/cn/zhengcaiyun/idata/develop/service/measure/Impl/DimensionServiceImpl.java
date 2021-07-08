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

import cn.zhengcaiyun.idata.commons.exception.ExecuteSqlException;
import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDao;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabel;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabelDefine;
import cn.zhengcaiyun.idata.develop.dal.model.DevTableInfo;
import cn.zhengcaiyun.idata.develop.dto.label.AttributeDto;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDefineDto;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDto;
import cn.zhengcaiyun.idata.develop.dto.label.LabelTagEnum;
import cn.zhengcaiyun.idata.develop.dto.measure.MeasureDto;
import cn.zhengcaiyun.idata.develop.service.label.LabelService;
import cn.zhengcaiyun.idata.develop.service.measure.DimensionService;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import com.alibaba.fastjson.JSON;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDynamicSqlSupport.devLabelDefine;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.devLabel;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDynamicSqlSupport.devTableInfo;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
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
    private DevTableInfoDao devTableInfoDao;
    @Autowired
    private LabelService labelService;
    @Autowired
    private DimensionService dimensionService;
    @Autowired
    private ColumnInfoService columnInfoService;

    private final String[] dimensionInfos = new String[]{"enName", "dimensionId", "dimensionDefine"};
    private final String DB_NAME = "dbName:LABEL";
    private final String EN_NAME = "enName";

    @Override
    public MeasureDto findDimension(String dimensionCode) {
        return getDimensionByCode(dimensionCode);
    }

    // 利用指标code反查维度
    @Override
    public List<MeasureDto> findDimensionsByMetricCode(String metricCode) {
        DevLabelDefine metricLabelDefine = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelTag, isLike("%_METRIC_LABEL%")), and(devLabelDefine.labelCode, isEqualTo(metricCode))))
                .orElse(null);
        checkArgument(metricLabelDefine != null, "指标不存在");
        List<MeasureDto> echoDimensionList = new ArrayList<>();
        if (LabelTagEnum.ATOMIC_METRIC_LABEL.name().equals(metricLabelDefine.getLabelTag())) {
            echoDimensionList = getDimensionsByAtomicMetricCode(metricCode);
        }
        else if (LabelTagEnum.DERIVE_METRIC_LABEL.name().equals(metricLabelDefine.getLabelTag())) {
            echoDimensionList = getDimensionsByAtomicMetricCode(metricLabelDefine.getSpecialAttribute().getAtomicMetricCode());
        }
        return echoDimensionList;
    }

    @Override
    public List<String> findDimensionValues(String dimensionCode) {
        DevLabelDefine dimensionDefine = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.DIMENSION_LABEL.name())),
                and(devLabelDefine.labelCode, isEqualTo(dimensionCode))))
                .orElseThrow(() -> new IllegalArgumentException("维度不存在"));
        DevLabel dimensionLabel = devLabelDao.selectOne(c -> c.where(devLabel.del, isNotEqualTo(1),
                and(devLabel.labelCode, isEqualTo(dimensionCode)), and(devLabel.labelParamValue, isEqualTo("true"))))
                .orElse(null);
        if (dimensionLabel == null) { return null; }
        DevTableInfo tableInfo = devTableInfoDao.selectOne(c -> c.where(devTableInfo.del, isNotEqualTo(1),
                and(devTableInfo.id, isEqualTo(dimensionLabel.getTableId())))).get();
        String dbName = devLabelDao.selectOne(c -> c.where(devLabel.del, isEqualTo(1),
                and(devLabel.tableId, isEqualTo(dimensionLabel.getTableId())), and(devLabel.labelCode, isEqualTo(DB_NAME))))
                .get().getLabelParamValue();
        String selectSql = String.format("SELECT DISTINCT %s FROM %s.%s", dimensionLabel.getColumnName(), dbName,
                tableInfo.getTableName());
        List<String> echoDimensionValues = new ArrayList<>();
        // TODO mock数据用于联调，sql已完成，待调用查询方法
        echoDimensionValues = Arrays.asList("339900", "330899", "330802");
        return echoDimensionValues;
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
        checkArgument(dimension.getSpecialAttribute().getDegradeDim() != null, "是否退化维不能为空");

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
    public MeasureDto disableOrAble(String dimensionCode, String labelTag, String operator) {
        checkArgument(isNotEmpty(operator), "修改者不能为空");
        checkArgument(isNotEmpty(dimensionCode), "维度Code不能为空");
        String existLabelTag = LabelTagEnum.DIMENSION_LABEL_DISABLE.name().equals(labelTag) ?
                LabelTagEnum.DIMENSION_LABEL.name() : LabelTagEnum.DIMENSION_LABEL_DISABLE.name();
        DevLabelDefine checkDimension = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(dimensionCode)), and(devLabelDefine.labelTag,
                        isEqualTo(existLabelTag))))
                .orElse(null);
        checkArgument(checkDimension != null, "维度不存在");

        devLabelDefineDao.update(c -> c.set(devLabelDefine.labelTag).equalTo(labelTag)
                .set(devLabelDefine.editor).equalTo(operator)
                .where(devLabelDefine.del, isNotEqualTo(1), and(devLabelDefine.labelCode, isEqualTo(dimensionCode)),
                        and(devLabelDefine.labelTag, isEqualTo(existLabelTag))));
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

    private MeasureDto getDimensionByCode(String dimensionCode) {
        DevLabelDefine dimension = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.labelCode, isEqualTo(dimensionCode))))
                .orElse(null);
        checkArgument(dimension != null, "维度不存在");

        MeasureDto echoDimension = PojoUtil.copyOne(dimension, MeasureDto.class);
        echoDimension.setMeasureLabels(labelService.findLabelsByCode(dimensionCode));
        return echoDimension;
    }

    private List<MeasureDto> getDimensionsByAtomicMetricCode(String atomicMetricCode) {
        List<LabelDto> metricLabelList = PojoUtil.copyList(devLabelDao.select(c -> c.where(devLabel.del,
                isNotEqualTo(1), and(devLabel.labelCode, isEqualTo(atomicMetricCode)))), LabelDto.class);
        List<Long> metricLabelTableIdList = metricLabelList.stream().map(LabelDto::getTableId).collect(Collectors.toList());
        Map<Long, String> tableMap = devTableInfoDao.select(c -> c.where(devTableInfo.del, isNotEqualTo(1)))
                .stream().collect(Collectors.toMap(DevTableInfo::getId, DevTableInfo::getTableName));
        List<LabelDto> dimensionLabelList = PojoUtil.copyList(devLabelDao.selectMany(select(devLabel.allColumns())
                .from(devLabel)
                .leftJoin(devLabelDefine).on(devLabel.labelCode, equalTo(devLabelDefine.labelCode))
                .where(devLabel.del, isNotEqualTo(1), and(devLabel.tableId, isIn(metricLabelTableIdList)),
                        and(devLabelDefine.del, isNotEqualTo(1)),
                        and(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.DIMENSION_LABEL.name())))
                .build().render(RenderingStrategies.MYBATIS3)), LabelDto.class)
                .stream().peek(dimensionLabel -> dimensionLabel.setTableName(tableMap.get(dimensionLabel.getTableId())))
                .collect(Collectors.toList());
        if (dimensionLabelList.size() == 0) {
            return null;
        }
        Map<String, List<LabelDto>> dimensionLabelMap = dimensionLabelList.stream().collect(Collectors.groupingBy(LabelDto::getLabelCode));
        Set<String> dimensionLabelCodeList = dimensionLabelList.stream().map(LabelDto::getLabelCode).collect(Collectors.toSet());
        List<LabelDefineDto> dimensionLabelDefineList = PojoUtil.copyList(devLabelDefineDao.select(c ->
                c.where(devLabelDefine.del, isNotEqualTo(1),
                        and(devLabelDefine.labelTag, isEqualTo(LabelTagEnum.DIMENSION_LABEL.name())),
                        and(devLabelDefine.labelCode, isIn(dimensionLabelCodeList)))), LabelDefineDto.class);
        return dimensionLabelDefineList.stream().map(dimension -> {
            MeasureDto echoDimension = new MeasureDto();
            echoDimension.setLabelCode(dimension.getLabelCode());
            echoDimension.setLabelName(dimension.getLabelName());
            String enName = dimension.getLabelAttributes()
                    .stream().filter(labelAttribute -> labelAttribute.getAttributeKey().equals(EN_NAME))
                    .findAny()
                    .get().getAttributeValue();
            echoDimension.setEnName(enName);
            List<LabelDto> testLabels = dimensionLabelMap.get(dimension.getLabelCode());
            echoDimension.setMeasureLabels(dimensionLabelMap.get(dimension.getLabelCode()));
            return echoDimension;
        }).collect(Collectors.toList());
    }
}
