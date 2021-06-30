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
import cn.zhengcaiyun.idata.develop.dal.dao.*;
import cn.zhengcaiyun.idata.develop.dal.model.*;
import cn.zhengcaiyun.idata.develop.service.label.EnumService;
import cn.zhengcaiyun.idata.develop.dto.label.EnumDto;
import cn.zhengcaiyun.idata.develop.dto.label.EnumValueDto;
import cn.zhengcaiyun.idata.develop.dto.label.MetaTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumDynamicSqlSupport.devEnum;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumValueDynamicSqlSupport.devEnumValue;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDynamicSqlSupport.devLabelDefine;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.devLabel;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDynamicSqlSupport.devTableInfo;
import static com.google.common.base.Preconditions.checkArgument;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author shiyin
 * @date 2021-05-30 20:28
 */
@Service
public class EnumServiceImpl implements EnumService {

    @Autowired
    private DevEnumDao devEnumDao;
    @Autowired
    private DevEnumValueDao devEnumValueDao;
    @Autowired
    private DevLabelDao devLabelDao;
    @Autowired
    private DevLabelDefineDao devLabelDefineDao;
    @Autowired
    private DevTableInfoDao devTableInfoDao;
    @Autowired
    private DevLabelDefineMyDao devLabelDefineMyDao;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public EnumDto createOrEdit(EnumDto enumDto, String operator) {
        if (enumDto.getEnumCode() == null) {
            checkArgument(enumDto.getEnumName() != null, "enumName不能为空");
            enumDto.setEnumCode(RandomUtil.randomStr(10) + ":ENUM");
            enumDto.setCreator(operator);
            devEnumDao.insertSelective(PojoUtil.copyOne(enumDto, DevEnum.class,
                    "creator", "enumCode", "enumName", "folderId"));
            if (enumDto.getEnumValues() != null) {
                enumDto.getEnumValues().forEach(enumValueDto -> {
                    enumValueDto.setEnumCode(enumDto.getEnumCode());
                    createEnumValue(enumValueDto, operator);
                });
            }
        }
        else {
            DevEnum enumRecord = devEnumDao.selectOne(c -> c.where(devEnum.enumCode, isEqualTo(enumDto.getEnumCode()),
                    and(devEnum.del, isNotEqualTo(1))))
                    .orElseThrow(() -> new IllegalArgumentException("enumCode不存在, " + enumDto.getEnumCode()));
            enumDto.setId(enumRecord.getId());
            enumDto.setEditor(operator);
            devEnumDao.updateByPrimaryKeySelective(PojoUtil.copyOne(enumDto, DevEnum.class,
                    "id", "editor", "enumName", "folderId"));
            if (enumDto.getEnumValues() != null) {
                enumDto.getEnumValues().forEach(enumValueDto -> {
                    if (enumValueDto.getValueCode() == null) {
                        createEnumValue(enumValueDto, operator);
                    }
                    else {
                        editEnumValue(enumValueDto, operator);
                    }
                });
            }
        }
        return PojoUtil.copyOne(devEnumDao.selectOne(c ->
                c.where(devEnum.enumCode, isEqualTo(enumDto.getEnumCode()))).get(), EnumDto.class);
    }

    private void createEnumValue(EnumValueDto enumValueDto, String operator) {
        enumValueDto.setCreator(operator);
        checkArgument(enumValueDto.getEnumValue() != null, "enumValue不能为空");
        checkArgument(enumValueDto.getValueCode() != null, "valueCode不能为空");
        devEnumValueDao.insertSelective(PojoUtil.copyOne(enumValueDto, DevEnumValue.class,
                "creator", "enumCode", "valueCode", "enumValue",
                "enumAttributes", "parentCode"));
    }

    private void editEnumValue(EnumValueDto enumValueDto, String operator) {
        DevEnumValue enumValue = devEnumValueDao.selectOne(c ->
                c.where(devEnumValue.valueCode, isEqualTo(enumValueDto.getValueCode()),
                        and(devEnumValue.del, isNotEqualTo(1))))
                .orElseThrow(() -> new IllegalArgumentException("valueCode不存在, " + enumValueDto.getValueCode()));
        enumValueDto.setId(enumValue.getId());
        enumValueDto.setEditor(operator);
        devEnumValueDao.updateByPrimaryKeySelective(PojoUtil.copyOne(enumValueDto, DevEnumValue.class,
                "id", "editor", "enumValue", "enumAttributes", "parentCode"));
    }

    @Override
    public EnumDto findEnum(String enumCode) {
        checkArgument(enumCode != null, "enumCode不能为空");
        EnumDto enumDto = PojoUtil.copyOne(devEnumDao.selectOne(c ->
                c.where(devEnum.enumCode, isEqualTo(enumCode),
                        and(devEnum.del, isNotEqualTo(1))))
                .orElseThrow(() -> new IllegalArgumentException("enumCode不存在, " + enumCode)), EnumDto.class);

        enumDto.setEnumValues(getEnumValues(enumCode));
        return enumDto;
    }

    @Override
    public List<EnumDto> getEnumNames() {
        return PojoUtil.copyList(devEnumDao.select(c ->
                c.where(devEnum.del, isNotEqualTo(1))), EnumDto.class);
    }

    @Override
    public List<EnumValueDto> getEnumValues(String enumCode) {
        Map<String, String> enumValueMap = new HashMap<>();
        List<EnumValueDto> enumValues = devEnumValueDao.select(c ->
                c.where(devEnumValue.enumCode, isEqualTo(enumCode),
                        and(devEnumValue.del, isNotEqualTo(1))))
                .stream().map(devEnumValue -> {
                    enumValueMap.put(devEnumValue.getValueCode(), devEnumValue.getEnumValue());
                    return PojoUtil.copyOne(devEnumValue, EnumValueDto.class);
                }).collect(Collectors.toList());
        enumValues.forEach(enumValue -> {
            enumValue.setParentValue(enumValueMap.get(enumValue.getParentCode()));
            if (enumValue.getEnumAttributes() != null) {
                enumValue.getEnumAttributes().forEach(attributeDto -> {
                    if (attributeDto.getAttributeType() != null
                            && attributeDto.getAttributeType().endsWith(":ENUM")) {
                        attributeDto.setEnumName(getEnumName(attributeDto.getAttributeType()));
                        if (attributeDto.getAttributeValue() != null) {
                            attributeDto.setEnumValue(getEnumValue(attributeDto.getAttributeValue()));
                        }
                    }
                    if (MetaTypeEnum.ENUM.name().equals(attributeDto.getAttributeType())) {
                        attributeDto.setEnumName(getEnumName(attributeDto.getAttributeValue()));
                    }
                });
            }
        });
        return enumValues;
    }

    @Override
    public String getEnumName(String enumCode) {
        return devEnumDao.selectOne(c ->
                c.where(devEnum.enumCode, isEqualTo(enumCode),
                        and(devEnum.del, isNotEqualTo(1))))
                .orElseThrow(() -> new IllegalArgumentException("enumCode不存在, " + enumCode)).getEnumName();
    }

    @Override
    public String getEnumValue(String valueCode) {
        return devEnumValueDao.selectOne(c ->
                c.where(devEnumValue.valueCode, isEqualTo(valueCode),
                        and(devEnumValue.del, isNotEqualTo(1))))
                .orElseThrow(() -> new IllegalArgumentException("valueCode不存在, " + valueCode)).getEnumValue();
    }

    @Override
    public List<String> getEnumValues(List<String> valueCodes) {
        return devEnumValueDao.select(c ->
                c.where(devEnumValue.valueCode, isIn(valueCodes), and(devEnumValue.del, isNotEqualTo(1))))
                .stream().map(DevEnumValue::getEnumValue).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean delete(String enumCode, String operator) {
        checkArgument(enumCode != null, "enumCode不能为空");
        devEnumDao.selectOne(c -> c.where(devEnum.enumCode, isEqualTo(enumCode),
                and(devEnum.del, isNotEqualTo(1))))
                .orElseThrow(() -> new IllegalArgumentException("enumCode不存在, " + enumCode));
        // TODO special_attributes字段的依赖暂时未判断
        List<DevLabelDefine> labelDefines = devLabelDefineMyDao.selectLabelDefineByEnumCode(enumCode);
        if (labelDefines.size() > 0) {
            throw new IllegalArgumentException(labelDefines.get(0).getLabelName() + "标签依赖此枚举，不能删除");
        }
        List<DevLabel> labels = devLabelDao.select(c ->
                c.where(devLabel.del, isNotEqualTo(1),
                        and(devLabel.labelParamValue, isEqualTo(enumCode))));
        if (labels.size() > 0) {
            List<DevTableInfo> tableInfos = devTableInfoDao.select(c -> c.where(devTableInfo.id, isEqualTo(labels.get(0).getTableId())));
            if (tableInfos.size() > 0) {
                throw new IllegalArgumentException(tableInfos.get(0).getTableName() + "表依赖此枚举，不能删除");
            }
        }
        devEnumDao.update(c -> c.set(devEnum.del).equalTo(1)
                .set(devEnum.editor).equalTo(operator)
                .where(devEnum.enumCode, isEqualTo(enumCode),
                        and(devEnum.del, isNotEqualTo(1))));
        devEnumValueDao.update(c -> c.set(devEnumValue.del).equalTo(1)
                .set(devEnumValue.editor).equalTo(operator)
                .where(devEnumValue.enumCode, isEqualTo(enumCode),
                        and(devEnumValue.del, isNotEqualTo(1))));
        return true;
    }
}
