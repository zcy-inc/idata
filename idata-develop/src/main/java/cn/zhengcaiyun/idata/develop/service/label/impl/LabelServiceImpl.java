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
package cn.zhengcaiyun.idata.develop.service.label.impl;

import cn.zhengcaiyun.idata.commons.encrypt.RandomUtil;
import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.develop.cache.DevTreeNodeLocalCache;
import cn.zhengcaiyun.idata.develop.constant.enums.FunctionModuleEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.*;
import cn.zhengcaiyun.idata.develop.dal.model.DevEnumValue;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabel;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabelDefine;
import cn.zhengcaiyun.idata.develop.dal.model.DevTableInfo;
import cn.zhengcaiyun.idata.develop.dto.table.ColumnTypeEnum;
import cn.zhengcaiyun.idata.develop.dto.table.DataTypeEnum;
import cn.zhengcaiyun.idata.develop.service.label.EnumService;
import cn.zhengcaiyun.idata.develop.service.label.LabelService;
import cn.zhengcaiyun.idata.develop.dto.label.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.dynamic.sql.VisitableCondition;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumValueDynamicSqlSupport.devEnumValue;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDynamicSqlSupport.devLabelDefine;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDynamicSqlSupport.labelRequired;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.devLabel;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDynamicSqlSupport.devTableInfo;
import static cn.zhengcaiyun.idata.develop.dto.label.SysLabelCodeEnum.DB_NAME_LABEL;
import static cn.zhengcaiyun.idata.develop.dto.label.SysLabelCodeEnum.checkSysLabelCode;
import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author shiyin
 * @date 2021-05-30 20:28
 */
@Service
public class LabelServiceImpl implements LabelService {

    @Autowired
    private DevLabelDefineDao devLabelDefineDao;
    @Autowired
    private DevLabelDao devLabelDao;
    @Autowired
    private DevLabelMyDao devLabelMyDao;
    @Autowired
    private DevTableInfoDao devTableInfoDao;
    @Autowired
    private EnumService enumService;
    @Autowired
    private DevEnumValueDao devEnumValueDao;
    @Autowired
    private DevTreeNodeLocalCache devTreeNodeLocalCache;

    private final String DB_NAME = "dbName:LABEL";
    private final String COL_COMMENT = "columnComment:LABEL";
    private final String TBL_COMMENT = "tblComment:LABEL";
    private final String METABASE_URL = "metabaseUrl:LABEL";
    private final String COLUMN_TYPE = "columnType:LABEL";
    private final String COL_TYPE_ENUM = "hiveColTypeEnum:ENUM";

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public LabelDefineDto defineLabel(LabelDefineDto labelDefineDto, String operator) {
        if (labelDefineDto.getLabelCode() == null) {
            if (labelDefineDto.getLabelIndex() == null || labelDefineDto.getLabelIndex() < 0) {
                labelDefineDto.setLabelIndex(null);
            }
            else {
                DevLabelDefine checkLabelDefine = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                        and(devLabelDefine.subjectType, isEqualTo(labelDefineDto.getSubjectType())),
                        and(devLabelDefine.labelIndex, isEqualTo(labelDefineDto.getLabelIndex()))))
                        .orElse(null);
                checkArgument(checkLabelDefine == null, "相同主体的排序编号已存在");
            }
            labelDefineDto.setLabelCode(RandomUtil.randomStr(10) + ":LABEL");
            checkArgument(labelDefineDto.getLabelName() != null, "labelName不能为空");
            checkArgument(labelDefineDto.getLabelTag() != null, "labelTag不能为空");
            LabelTagEnum labelTagEnum = LabelTagEnum.valueOf(labelDefineDto.getLabelTag());
            checkLabelParamType(labelTagEnum, labelDefineDto.getLabelParamType());
            if (LabelTagEnum.ATTRIBUTE_LABEL.equals(labelTagEnum)
                    || LabelTagEnum.DIMENSION_LABEL.equals(labelTagEnum)
                    || LabelTagEnum.MODIFIER_LABEL.equals(labelTagEnum)
                    || LabelTagEnum.ATOMIC_METRIC_LABEL.equals(labelTagEnum)
                    || LabelTagEnum.DERIVE_METRIC_LABEL.equals(labelTagEnum)
                    || LabelTagEnum.COMPLEX_METRIC_LABEL.equals(labelTagEnum)) {
                labelDefineDto.setLabelParamType(null);
            }
            // TODO specialAttributes, folderId没有校验
            if (labelDefineDto.getLabelAttributes() != null) {
                labelDefineDto.getLabelAttributes().forEach(attributeDto -> {
                    checkArgument(attributeDto.getAttributeKey() != null, "attributeKey不能为空");
                    checkArgument(attributeDto.getAttributeType() != null, "attributeType不能为空");
                    checkArgument(attributeDto.getAttributeValue() != null, "attributeValue不能为空");
                    if (attributeDto.getAttributeType().endsWith(":ENUM")) {
                        enumService.getEnumName(attributeDto.getAttributeType());
                        enumService.getEnumValue(attributeDto.getAttributeValue());
                    }
                    if (MetaTypeEnum.ENUM.name().equals(attributeDto.getAttributeType())) {
                        enumService.getEnumName(attributeDto.getAttributeValue());
                    }
                });
            }
            checkArgument(labelDefineDto.getSubjectType() != null, "subjectType不能为空");
            SubjectTypeEnum.valueOf(labelDefineDto.getSubjectType());
            // 标签作用域暂时不做，都是全局的
            labelDefineDto.setLabelScope(null);
            if (labelDefineDto.getLabelRequired() != null) {
                if (labelDefineDto.getLabelRequired() < 0) {
                    labelDefineDto.setLabelRequired(null);
                }
                if (labelDefineDto.getLabelRequired() > 1) {
                    labelDefineDto.setLabelRequired(1);
                }
            }
            labelDefineDto.setCreator(operator);
            devLabelDefineDao.insertSelective(PojoUtil.copyOne(labelDefineDto, DevLabelDefine.class,
                    "labelCode", "labelName", "labelTag", "labelParamType",
                    "labelAttributes", "specialAttribute", "subjectType", "labelIndex",
                    "labelRequired", "labelScope", "folderId", "creator"));
        }
        else {
            DevLabelDefine labelDefine = devLabelDefineDao.selectOne(c ->
                    c.where(devLabelDefine.labelCode, isEqualTo(labelDefineDto.getLabelCode()),
                            and(devLabelDefine.del, isNotEqualTo(1)))).orElseThrow(
                                    () -> new IllegalArgumentException("labelCode不存在, " + labelDefineDto.getLabelCode()));

            if (labelDefineDto.getLabelIndex() == null) {
                labelDefineDto.setLabelIndex(null);
            }
            else if (labelDefineDto.getLabelIndex() < 0) {
                labelDefineDto.setLabelIndex(labelDefine.getLabelIndex());
            }
            PojoUtil.copyTo(labelDefineDto, labelDefine, "labelName", "specialAttribute",
                    "folderId", "labelAttributes", "labelIndex");
            if (labelDefineDto.getLabelRequired() != null) {
                labelDefine.setLabelRequired(labelDefineDto.getLabelRequired());
            }
            labelDefine.setEditor(operator);
            labelDefine.setEditTime(new Timestamp(System.currentTimeMillis()));
            devLabelDefineDao.updateByPrimaryKey(labelDefine);
        }
        // clear cache
//        devTreeNodeLocalCache.invalidate(FunctionModuleEnum.DESIGN_LABEL);

        return PojoUtil.copyOne(devLabelDefineDao.selectOne(c ->
                        c.where(devLabelDefine.labelCode, isEqualTo(labelDefineDto.getLabelCode()))).get(),
                LabelDefineDto.class);
    }

    @Override
    public LabelDefineDto defineLabelAndEnum(LabelDefineDto labelDefineDto, String operator) {
        if (labelDefineDto.getEnumValues() != null && labelDefineDto.getEnumValues().size() > 0) {
            EnumDto enumDto = new EnumDto();
            enumDto.setEnumName(labelDefineDto.getLabelName());
            enumDto.setEnumValues(labelDefineDto.getEnumValues());
            if (StringUtils.isNotEmpty(labelDefineDto.getLabelParamType())) {
                enumDto.setEnumCode(labelDefineDto.getLabelParamType());
            }
            EnumDto echoEnum = enumService.createOrEdit(enumDto, operator);
            labelDefineDto.setLabelParamType(echoEnum.getEnumCode());
            labelDefineDto.setEnumValues(enumService.getEnumValues(echoEnum.getEnumCode()));
        }

        if (labelDefineDto.getLabelCode() == null) {
            if (labelDefineDto.getLabelIndex() == null || labelDefineDto.getLabelIndex() < 0) {
                List<DevLabelDefine> labelDefineList = devLabelDefineDao.select(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                        and(devLabelDefine.subjectType, isEqualTo(labelDefineDto.getSubjectType())))
                        .orderBy(devLabelDefine.labelIndex.descending()));
                int existBiggestLabelIndex = labelDefineList.get(0).getLabelIndex() != null
                        ? labelDefineList.get(0).getLabelIndex() : 0;
                labelDefineDto.setLabelIndex(existBiggestLabelIndex + 1);
            }
            else {
                DevLabelDefine checkLabelDefine = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                        and(devLabelDefine.subjectType, isEqualTo(labelDefineDto.getSubjectType())),
                        and(devLabelDefine.labelIndex, isEqualTo(labelDefineDto.getLabelIndex()))))
                        .orElse(null);
                checkArgument(checkLabelDefine == null, "相同主体的排序编号已存在");
            }
            labelDefineDto.setLabelCode(RandomUtil.randomStr(10) + ":LABEL");
            checkArgument(labelDefineDto.getLabelName() != null, "labelName不能为空");
            checkArgument(labelDefineDto.getLabelTag() != null, "labelTag不能为空");
            LabelTagEnum labelTagEnum = LabelTagEnum.valueOf(labelDefineDto.getLabelTag());
            // 暂不做校验
//            checkLabelParamType(labelTagEnum, labelDefineDto.getLabelParamType());
            checkArgument(labelDefineDto.getSubjectType() != null, "subjectType不能为空");
            SubjectTypeEnum.valueOf(labelDefineDto.getSubjectType());
            labelDefineDto.setCreator(operator);
            devLabelDefineDao.insertSelective(PojoUtil.copyOne(labelDefineDto, DevLabelDefine.class,
                    "labelCode", "labelName", "labelTag", "labelParamType",
                    "labelAttributes", "specialAttribute", "subjectType", "labelIndex",
                    "labelRequired", "labelScope", "folderId", "creator"));
        }
        else {
            DevLabelDefine labelDefine = devLabelDefineDao.selectOne(c ->
                    c.where(devLabelDefine.labelCode, isEqualTo(labelDefineDto.getLabelCode()),
                            and(devLabelDefine.del, isNotEqualTo(1)))).orElseThrow(
                    () -> new IllegalArgumentException("labelCode不存在, " + labelDefineDto.getLabelCode()));

            if (labelDefineDto.getLabelIndex() == null) {
                labelDefineDto.setLabelIndex(null);
            }
            else if (labelDefineDto.getLabelIndex() < 0) {
                labelDefineDto.setLabelIndex(labelDefine.getLabelIndex());
            }
            PojoUtil.copyTo(labelDefineDto, labelDefine, "labelName", "specialAttribute",
                    "folderId", "labelAttributes", "labelIndex");
            if (labelDefineDto.getLabelRequired() != null) {
                labelDefine.setLabelRequired(labelDefineDto.getLabelRequired());
            }
            labelDefine.setEditor(operator);
            labelDefine.setEditTime(new Timestamp(System.currentTimeMillis()));
            devLabelDefineDao.updateByPrimaryKey(labelDefine);
        }

        LabelDefineDto echoLabelDefine = PojoUtil.copyOne(devLabelDefineDao.selectOne(c ->
                        c.where(devLabelDefine.labelCode, isEqualTo(labelDefineDto.getLabelCode()))).get(),
                LabelDefineDto.class);
        echoLabelDefine.setEnumValues(enumService.getEnumValues(echoLabelDefine.getLabelParamType()));


        return echoLabelDefine;
    }

    private void checkLabelParamType(LabelTagEnum labelTagEnum, String labelParamType) {
        if (LabelTagEnum.STRING_LABEL.equals(labelTagEnum)) {
            checkArgument(MetaTypeEnum.STRING.name().equals(labelParamType),
                    "labelParamType与labelTag不符");
        }
        if (LabelTagEnum.BOOLEAN_LABEL.equals(labelTagEnum)) {
            checkArgument(MetaTypeEnum.BOOLEAN.name().equals(labelParamType),
                    "labelParamType与labelTag不符");
        }
        if (LabelTagEnum.USER_LABEL.equals(labelTagEnum)) {
            checkArgument(MetaTypeEnum.WHOLE.name().equals(labelParamType),
                    "labelParamType与labelTag不符");
        }
        if (LabelTagEnum.ENUM_LABEL.equals(labelTagEnum)) {
            checkArgument(MetaTypeEnum.ENUM.name().equals(labelParamType),
                    "labelParamType与labelTag不符");
        }
        if (LabelTagEnum.ENUM_VALUE_LABEL.equals(labelTagEnum)) {
            checkArgument(labelParamType != null
                            && labelParamType.endsWith(":ENUM"),
                    "labelParamType与labelTag不符");
        }
    }

    @Override
    public LabelDefineDto findDefine(String labelCode) {
        DevLabelDefine labelDefine = devLabelDefineDao.selectOne(c ->
                c.where(devLabelDefine.labelCode, isEqualTo(labelCode),
                        and(devLabelDefine.del, isNotEqualTo(1))))
                .orElseThrow(() -> new IllegalArgumentException("labelCode不存在, " + labelCode));
        LabelDefineDto labelDefineDto = PojoUtil.copyOne(labelDefine, LabelDefineDto.class);
        if (labelDefineDto.getLabelAttributes() != null) {
            labelDefineDto.getLabelAttributes().forEach(attributeDto -> {
                if (attributeDto.getAttributeType() != null
                        && attributeDto.getAttributeType().endsWith(":ENUM")) {
                    attributeDto.setEnumName(enumService.getEnumName(attributeDto.getAttributeType()));
                    if (attributeDto.getAttributeValue() != null) {
                        attributeDto.setEnumValue(enumService.getEnumValue(attributeDto.getAttributeValue()));
                    }
                }
                if (MetaTypeEnum.ENUM.name().equals(attributeDto.getAttributeType())) {
                    attributeDto.setEnumName(enumService.getEnumName(attributeDto.getAttributeValue()));
                }
            });
        }
        if (labelDefineDto.getLabelTag().equals(LabelTagEnum.ENUM_VALUE_LABEL.name())) {
            labelDefineDto.setEnumValues(enumService.getEnumValues(labelDefineDto.getLabelParamType()));
        }
        return labelDefineDto;
    }

    @Override
    public String getLabelDefineCode(Long labelDefineId) {
        DevLabelDefine labelDefineDto = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.del, isNotEqualTo(1),
                and(devLabelDefine.id, isEqualTo(labelDefineId)))).orElseThrow(() -> new IllegalArgumentException("标签不存在"));
        return labelDefineDto.getLabelCode();
    }

    @Override
    public List<LabelDefineDto> findDefines(String subjectType, String labelTag) {
        checkArgument(subjectType != null || labelTag != null,
                "subjectType和labelTag不能同时为空");
        if (subjectType != null) {
            SubjectTypeEnum.valueOf(subjectType);
        }
        if (labelTag != null) {
            LabelTagEnum.valueOf(labelTag);
        }
        var builder = select(devLabelDefine.allColumns()).from(devLabelDefine)
                .where(devLabelDefine.del, isNotEqualTo(1));
        if (subjectType != null) {
            builder.and(devLabelDefine.subjectType, isEqualTo(subjectType));
        }
        if (labelTag != null) {
            if ("METRIC_LABEL".equals(labelTag)) {
                builder.and(devLabelDefine.labelTag, isLike("%_METRIC_LABEL"));
            }
            else {
                builder.and(devLabelDefine.labelTag, isEqualTo(labelTag));
            }
        }
        else {
            builder.and(devLabelDefine.labelTag, isNotLike("%_METRIC_LABEL%"))
                    .and(devLabelDefine.labelTag, isNotLike(LabelTagEnum.DIMENSION_LABEL.name() + "%"))
                    .and(devLabelDefine.labelTag, isNotLike(LabelTagEnum.MODIFIER_LABEL.name() + "%"));
        }
        return devLabelDefineDao.selectMany(builder.orderBy(devLabelDefine.labelIndex).build()
                        .render(RenderingStrategies.MYBATIS3))
                .stream().map(devLabelDefine -> {
                    LabelDefineDto labelDefineDto = PojoUtil.copyOne(devLabelDefine, LabelDefineDto.class);
                    if (labelDefineDto.getLabelParamType() != null
                            && labelDefineDto.getLabelParamType().endsWith(":ENUM")) {
                        labelDefineDto.setEnumValues(
                                enumService.getEnumValues(
                                        labelDefineDto.getLabelParamType()));
                    }
                    return labelDefineDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<LabelDefineDto> findAllDefines() {
        var builder = select(devLabelDefine.allColumns()).from(devLabelDefine)
                .where(devLabelDefine.del, isNotEqualTo(1))
                .and(devLabelDefine.labelTag, isNotLike("%_METRIC_LABEL%"))
                .and(devLabelDefine.labelTag, isNotLike(LabelTagEnum.DIMENSION_LABEL.name() + "%"))
                .and(devLabelDefine.labelTag, isNotLike(LabelTagEnum.MODIFIER_LABEL.name() + "%"));
        return devLabelDefineDao.selectMany(builder.orderBy(devLabelDefine.labelIndex).build()
                .render(RenderingStrategies.MYBATIS3))
                .stream().map(devLabelDefine -> {
                    LabelDefineDto labelDefineDto = PojoUtil.copyOne(devLabelDefine, LabelDefineDto.class);
                    if (labelDefineDto.getLabelParamType() != null
                            && labelDefineDto.getLabelParamType().endsWith(":ENUM")) {
                        labelDefineDto.setEnumValues(
                                enumService.getEnumValues(
                                        labelDefineDto.getLabelParamType()));
                    }
                    return labelDefineDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean deleteDefine(String labelCode, String operator) {
        DevLabelDefine labelDefine = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.labelCode, isEqualTo(labelCode),
                and(devLabelDefine.del, isNotEqualTo(1))))
                .orElseThrow(() -> new IllegalArgumentException("labelCode不存在, " + labelCode));


        checkArgument(!checkSysLabelCode(labelCode), "系统依赖的标签不能删除");
        // 查表若被引用则不准被删
        if (!checkSysLabelCode(labelCode)) {
            List<DevLabel> existLabelList = devLabelDao.select(c -> c.where(devLabel.del ,isNotEqualTo(1),
                    and(devLabel.labelCode, isEqualTo(labelCode))));
            checkArgument(ObjectUtils.isEmpty(existLabelList), "标签被依赖，不能删除");
        }
        devLabelDefineDao.update(c -> c.set(devLabelDefine.del).equalTo(1)
                .set(devLabelDefine.editor).equalTo(operator)
                .where(devLabelDefine.labelCode, isEqualTo(labelCode),
                        and(devLabelDefine.del, isNotEqualTo(1))));
        devLabelDao.update(c -> c.set(devLabel.del).equalTo(1)
                .where(devLabel.labelCode, isEqualTo(labelCode),
                        and(devLabel.del, isNotEqualTo(1))));
        // clear cache
//        devTreeNodeLocalCache.invalidate(FunctionModuleEnum.DESIGN_LABEL);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public LabelDto label(LabelDto labelDto, String operator) {
        checkArgument(labelDto.getLabelCode() != null, "labelCode不能为空");
        DevLabelDefine labelDefine = devLabelDefineDao.selectOne(c -> c.where(devLabelDefine.labelCode, isEqualTo(labelDto.getLabelCode()),
                and(devLabelDefine.del, isNotEqualTo(1))))
                .orElseThrow(() -> new IllegalArgumentException("labelCode不存在, " + labelDto.getLabelCode()));
        checkArgument(labelDto.getTableId() != null, "tableId不能为空");
        if (SubjectTypeEnum.COLUMN.name().equals(labelDefine.getSubjectType())) {
            checkArgument(labelDto.getColumnName() != null, "columnName不能为空");
        }
        if (LabelTagEnum.STRING_LABEL.name().equals(labelDefine.getLabelTag())
                || LabelTagEnum.BOOLEAN_LABEL.name().equals(labelDefine.getLabelTag())
                || LabelTagEnum.USER_LABEL.name().equals(labelDefine.getLabelTag())
                || LabelTagEnum.ENUM_LABEL.name().equals(labelDefine.getLabelTag())
                || LabelTagEnum.ENUM_VALUE_LABEL.name().equals(labelDefine.getLabelTag())
                || LabelTagEnum.DIMENSION_LABEL.name().equals(labelDefine.getLabelTag())
                || LabelTagEnum.MODIFIER_LABEL.name().equals(labelDefine.getLabelTag())) {
                checkArgument(labelDto.getLabelParamValue() != null, "labelParamValue不能为空");
        }
        else {
            labelDto.setLabelParamValue(null);
        }
        List<DevLabel> labels = devLabelDao.select(c -> c.where(devLabel.labelCode, isEqualTo(labelDto.getLabelCode()),
                and(devLabel.tableId, isEqualTo(labelDto.getTableId())),
                and(devLabel.columnId, isEqualTo(labelDto.getColumnId()), or(devLabel.columnId, isNull())),
                and(devLabel.del, isNotEqualTo(1))));
        if (labels.size() == 0) {
            labelDto.setCreator(operator);
            devLabelDao.insertSelective(PojoUtil.copyOne(labelDto, DevLabel.class,
                    "creator", "labelCode", "tableId", "columnName", "columnId", "labelParamValue"));
        }
        else if (labels.size() == 1) {
            labelDto.setId(labels.get(0).getId());
            labelDto.setEditor(operator);
            devLabelDao.updateByPrimaryKeySelective(PojoUtil.copyOne(labelDto, DevLabel.class,
                    "id", "editor", "columnName", "labelParamValue"));
        }
        else {
            throw new IllegalArgumentException("元数据标签状态异常");
        }
        return PojoUtil.copyOne(devLabelDao.selectOne(c ->
                        c.where(devLabel.labelCode, isEqualTo(labelDto.getLabelCode()),
                                and(devLabel.tableId, isEqualTo(labelDto.getTableId())),
                                and(devLabel.columnId, isEqualTo(labelDto.getColumnId()),
                                        or(devLabel.columnId, isNull())),
                                and(devLabel.del, isNotEqualTo(1))))
                        .get(),
                LabelDto.class);
    }

    private VisitableCondition<String> isEqual(String columnName) {
        if (columnName != null) {
            return isEqualTo(columnName);
        }
        else {
            return isNull();
        }
    }

    @Override
    public List<LabelDto> findLabels(Long tableId, String columnName) {
        List<LabelDto> echoLabelList = devLabelMyDao.selectLabelsBySubject(tableId, columnName);
        echoLabelList.forEach(labelDto -> {
            if (isEmpty(labelDto.getLabelParamType())) {
                return;
            }
            if ("ENUM".equals(labelDto.getLabelParamType())) {
                labelDto.setEnumNameOrValue(enumService.getEnumName(labelDto.getLabelParamValue()));
            }
            else if (labelDto.getLabelParamType().endsWith(":ENUM")) {
                List<String> enumValueCodeList = Arrays.asList(labelDto.getLabelParamValue().split(","));
                List<String> enumValueList = enumService.getEnumValues(enumValueCodeList)
                        .stream().map(DevEnumValue::getEnumValue).collect(Collectors.toList());
                labelDto.setEnumNameOrValue(String.join(",", enumValueList));
            }
        });
        return echoLabelList;
    }

    @Override
    public List<LabelDto> findLabelsByCode(String labelCode) {
        Map<Long, String> tableInfoMap = devTableInfoDao.selectMany(select(devTableInfo.id, devTableInfo.tableName)
                .from(devTableInfo).where(devTableInfo.del, isNotEqualTo(1)).build().render(RenderingStrategies.MYBATIS3))
                .stream().collect(Collectors.toMap(DevTableInfo::getId, DevTableInfo::getTableName));
        Map<Long, String> dwdTableInfoMap = devLabelDao.select(c -> c.where(devLabel.del, isNotEqualTo(1),
                and(devLabel.labelCode, isEqualTo(DB_NAME))))
                .stream().collect(Collectors.toMap(DevLabel::getTableId, DevLabel::getLabelParamValue));
        Map<String, String> columnCommentMap = devLabelDao.select(c -> c.where(devLabel.del, isNotEqualTo(1),
                and(devLabel.labelCode, isEqualTo(COL_COMMENT))))
                .stream().collect(Collectors.toMap(record -> record.getTableId() + "_" + record.getColumnName(), DevLabel::getLabelParamValue));
        Map<Long, String> tableCommentMap = devLabelDao.select(c -> c.where(devLabel.del, isNotEqualTo(1),
                and(devLabel.labelCode, isEqualTo(TBL_COMMENT))))
                .stream().collect(Collectors.toMap(DevLabel::getTableId, DevLabel::getLabelParamValue));
        Map<Long, String> tableMetabaseUrlMap = devLabelDao.select(c -> c.where(devLabel.del, isNotEqualTo(1),
                and(devLabel.labelCode, isEqualTo(METABASE_URL))))
                .stream().collect(Collectors.toMap(DevLabel::getTableId, DevLabel::getLabelParamValue));
        Map<String, String> columnTypeMap = devLabelDao.select(c -> c.where(devLabel.del, isNotEqualTo(1),
                and(devLabel.labelCode, isEqualTo(COLUMN_TYPE))))
                .stream().collect(Collectors.toMap(record -> record.getTableId() + "_" + record.getColumnName(), DevLabel::getLabelParamValue));
        Map<String, String> columnTypeEnumMap = devEnumValueDao.select(c -> c.where(devEnumValue.del, isNotEqualTo(1),
                and(devEnumValue.enumCode, isEqualTo(COL_TYPE_ENUM))))
                .stream().collect(Collectors.toMap(DevEnumValue::getValueCode, DevEnumValue::getEnumValue));
        return PojoUtil.copyList(devLabelDao.selectMany(select(devLabel.allColumns())
                .from(devLabel)
                .where(devLabel.del, isNotEqualTo(1), and(devLabel.labelCode, isEqualTo(labelCode)))
                .build().render(RenderingStrategies.MYBATIS3)), LabelDto.class)
                .stream().peek(labelDto -> {
                    checkArgument(tableInfoMap.containsKey(labelDto.getTableId()), "表不存在");
                    labelDto.setTableName(tableInfoMap.get(labelDto.getTableId()));
                    labelDto.setTableComment(tableCommentMap.get(labelDto.getTableId()));
                    labelDto.setDbName(dwdTableInfoMap.get(labelDto.getTableId()));
                    labelDto.setDbTableName(dwdTableInfoMap.get(labelDto.getTableId()) + "." + tableInfoMap.get(labelDto.getTableId()));
                    String columnComment = columnCommentMap.containsKey(labelDto.getTableId() + "_" + labelDto.getColumnName())
                            ? columnCommentMap.get(labelDto.getTableId() + "_" + labelDto.getColumnName()) : labelDto.getColumnName();
                    labelDto.setColumnComment(columnComment);
                    labelDto.setColumnDataType(ColumnTypeEnum.toDataType(columnTypeEnumMap
                            .get(columnTypeMap.get(labelDto.getTableId() + "_" + labelDto.getColumnName()))));
                    labelDto.setMetabaseUrl(tableMetabaseUrlMap.getOrDefault(labelDto.getTableId(), null));
                }).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<LabelDto>> findColumnLabelMap(Long tableId, List<String> columnNames) {
        checkArgument(columnNames != null && columnNames.size() > 0, "columnNames不能为空");
        List<LabelDto> columnLabelList = devLabelMyDao.selectLabelsBySubject(tableId, String.join(",", columnNames));
        List<String> columnEnumValueCodeList = columnLabelList.stream().filter(columnLabel ->
                columnLabel.getLabelParamValue().endsWith(":ENUM_VALUE")).collect(Collectors.toList())
                .stream().map(LabelDto::getLabelParamValue).collect(Collectors.toList());
        Map<String, String> columnEnumMap = enumService.getEnumValues(columnEnumValueCodeList)
                .stream().collect(Collectors.toMap(DevEnumValue::getValueCode, DevEnumValue::getEnumValue));
        columnLabelList.forEach(columnLabel -> {
            if (columnLabel.getLabelParamValue().endsWith(":ENUM_VALUE") && columnEnumMap.containsKey(columnLabel.getLabelParamValue())) {
                columnLabel.setEnumNameOrValue(columnEnumMap.get(columnLabel.getLabelParamValue()));
            }
        });
        return columnLabelList.stream().collect(Collectors.groupingBy(LabelDto::getColumnName));
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean removeLabel(LabelDto labelDto, String operator) {
        checkArgument(labelDto.getLabelCode() != null, "labelCode不能为空");
        checkArgument(labelDto.getTableId() != null, "tableId不能为空");
        devLabelDao.update(c -> c.set(devLabel.del).equalTo(1).set(devLabel.editor).equalTo(operator)
                .where(devLabel.labelCode, isEqualTo(labelDto.getLabelCode()),
                        and(devLabel.tableId, isEqualTo(labelDto.getTableId())),
                        and(devLabel.columnName, isEqual(labelDto.getColumnName())),
                        and(devLabel.del, isNotEqualTo(1))));
        return true;
    }

    @Override
    public List<TimeDimEnum> findTimeDimEnums() {
        return Arrays.asList(TimeDimEnum.values());
    }

    @Override
    public List<DevLabel> findByTableId(Long tableId) {
        List<DevLabel> labelList = devLabelDao.selectMany(select(devLabel.allColumns())
                .from(devLabel)
                .where(devLabel.del, isNotEqualTo(1), and(devLabel.tableId, isEqualTo(tableId)))
                .build().render(RenderingStrategies.MYBATIS3));
        return labelList;
    }

    @Override
    public String getDBName(Long tableId) {
        String dbName = devLabelDao.selectOne(c -> c.where(devLabel.del, isNotEqualTo(1),
                        and(devLabel.tableId, isEqualTo(tableId)), and(devLabel.labelCode, isEqualTo(DB_NAME))))
                .get().getLabelParamValue();
        return dbName;
    }

    @Override
    public void batchUpsert(List<DevLabel> labelList) {
        devLabelMyDao.batchUpsert(labelList);
    }

    @Override
    public void deleteDeprecatedHiveColumn(Long columnId, String hiveColumnName) {
        devLabelMyDao.deleteDeprecatedHiveColumn(columnId, hiveColumnName);
    }
}
