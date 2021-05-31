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
import cn.zhengcaiyun.idata.develop.dal.dao.DevEnumDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevEnumValueDao;
import cn.zhengcaiyun.idata.develop.dal.model.DevEnum;
import cn.zhengcaiyun.idata.develop.service.label.EnumService;
import cn.zhengcaiyun.idata.dto.develop.label.EnumDto;
import cn.zhengcaiyun.idata.dto.develop.label.EnumValueDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumDynamicSqlSupport.devEnum;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumValueDynamicSqlSupport.devEnumValue;
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

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public EnumDto createOrEdit(EnumDto enumDto, String operator) {
        if (enumDto.getEnumCode() == null) {
            checkArgument(enumDto.getEnumName() != null, "enumName不能为空");
            enumDto.setEnumCode(RandomUtil.randomStr(10));
            enumDto.setCreator(operator);
            devEnumDao.insertSelective(PojoUtil.copyOne(enumDto, DevEnum.class,
                    "creator", "enumCode", "enumName", "folderId"));
            // TODO enumValues
        }
        else {
            DevEnum enumRecord = devEnumDao.selectOne(c -> c.where(devEnum.enumCode, isEqualTo(enumDto.getEnumCode()),
                    and(devEnum.del, isNotEqualTo(1))))
                    .orElseThrow(() -> new IllegalArgumentException("enumCode不存在"));
            enumDto.setId(enumRecord.getId());
            enumDto.setEditor(operator);
            devEnumDao.updateByPrimaryKeySelective(PojoUtil.copyOne(enumDto, DevEnum.class,
                    "id", "editor", "enumName", "folderId"));
            // TODO enumValues
        }
        return PojoUtil.copyOne(devEnumDao.selectOne(c ->
                c.where(devEnum.enumCode, isEqualTo(enumDto.getEnumCode()))), EnumDto.class);
    }

    @Override
    public EnumDto findEnum(String enumCode) {
        checkArgument(enumCode != null, "enumCode不能为空");
        EnumDto enumDto = PojoUtil.copyOne(devEnumDao.selectOne(c ->
                c.where(devEnum.enumCode, isEqualTo(enumCode),
                        and(devEnum.del, isNotEqualTo(1))))
                .orElseThrow(() -> new IllegalArgumentException("enumCode不存在")), EnumDto.class);
        // TODO enumAttributes
        List<EnumValueDto> enumValues = PojoUtil.copyList(devEnumValueDao.select(c ->
                c.where(devEnumValue.enumCode, isEqualTo(enumCode),
                        and(devEnumValue.del, isNotEqualTo(1)))), EnumValueDto.class);
        enumDto.setEnumValues(enumValues);
        return enumDto;
    }

    @Override
    public List<EnumDto> getEnumNames() {
        return PojoUtil.copyList(devEnumDao.select(c ->
                c.where(devEnum.del, isNotEqualTo(1))), EnumDto.class);
    }

    @Override
    public List<EnumValueDto> getEnumValues(String enumCode) {
        return PojoUtil.copyList(devEnumValueDao.select(c ->
                c.where(devEnumValue.enumCode, isEqualTo(enumCode),
                        and(devEnumValue.del, isNotEqualTo(1)))), EnumValueDto.class);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean delete(String enumCode, String operator) {
        checkArgument(enumCode != null, "enumCode不能为空");
        devEnumDao.selectOne(c -> c.where(devEnum.enumCode, isEqualTo(enumCode),
                and(devEnum.del, isNotEqualTo(1))))
                .orElseThrow(() -> new IllegalArgumentException("enumCode不存在"));
        // TODO 依赖没有判断
        devEnumDao.update(c -> c.set(devEnum.del).equalTo(1)
                .where(devEnum.enumCode, isEqualTo(enumCode),
                        and(devEnum.del, isNotEqualTo(1))));
        devEnumValueDao.update(c -> c.set(devEnumValue.del).equalTo(1)
                .where(devEnumValue.enumCode, isEqualTo(enumCode),
                        and(devEnumValue.del, isNotEqualTo(1))));
        return true;
    }
}
