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
package cn.zhengcaiyun.idata.develop.service.table.impl;

import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.develop.dal.dao.DevForeignKeyDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDao;
import cn.zhengcaiyun.idata.develop.dal.model.DevForeignKey;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabel;
import cn.zhengcaiyun.idata.develop.service.table.ForeignKeyService;
import cn.zhengcaiyun.idata.develop.dto.table.ERelationTypeEnum;
import cn.zhengcaiyun.idata.develop.dto.table.ForeignKeyDto;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevForeignKeyDynamicSqlSupport.devForeignKey;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.devLabel;
import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author caizhedong
 * @date 2021-05-28 15:36
 */

@Service
public class ForeignKeyServiceImpl implements ForeignKeyService {

    @Autowired
    private DevForeignKeyDao devForeignKeyDao;
    @Autowired
    private DevLabelDao devLabelDao;

    private String[] foreignKeyFields = {"id", "del", "creator", "createTime", "editor", "editTime",
            "tableId", "columnNames", "referTableId", "referColumnNames", "erType"};
    private final String DB_NAME_LABEL = "dbName";

    @Override
    public List<ForeignKeyDto> getForeignKeys(Long tableId) {
        var builder = select(devForeignKey.allColumns()).from(devForeignKey).where(devForeignKey.del, isNotEqualTo(1));
        if (tableId != null) {
            builder.and(devForeignKey.tableId, isEqualTo(tableId));
        }
        List<DevForeignKey> foreignKeyList = devForeignKeyDao.selectMany(builder.build().render(RenderingStrategies.MYBATIS3));

        List<ForeignKeyDto> echoForeignKeyDtoList = PojoUtil.copyList(foreignKeyList, ForeignKeyDto.class, foreignKeyFields)
                .stream().peek(
                        foreignKeyDto -> foreignKeyDto.setReferDbName(getDbName(foreignKeyDto.getReferTableId())))
                .collect(Collectors.toList());
        return echoForeignKeyDtoList;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public List<ForeignKeyDto> createOrEdit(List<ForeignKeyDto> foreignKeyDtoList, Long tableId,
                                            List<String> columnNameList, String operator) {
//        checkArgument(isNotEmpty(operator), "创建者不能为空");
//        checkArgument(foreignKeyDto.getTableId() != null, "外键所属表ID不能为空");
//        checkArgument(isNotEmpty(foreignKeyDto.getColumnNames()), "外键列名不能为空");
//        checkArgument(foreignKeyDto.getReferTableId() != null, "外键引用表ID不能为空");
//        checkArgument(isNotEmpty(foreignKeyDto.getReferColumnNames()), "外键引用列名称不能为空");
//        checkArgument(foreignKeyDto.getColumnNames().split(",").length
//                == foreignKeyDto.getReferColumnNames().split(",").length, "外键列数量和外键引用列数量需一致");
//        checkArgument(isNotEmpty(foreignKeyDto.getErType()), "ER联系类别不能为空");
//        ERelationTypeEnum.valueOf(foreignKeyDto.getErType());
//
//        foreignKeyDto.setCreator(operator);
//        DevForeignKey foreignKey = PojoUtil.copyOne(foreignKeyDto, DevForeignKey.class, foreignKeyFields);
//        devForeignKeyDao.insertSelective(foreignKey);
//
//        ForeignKeyDto echoForeignKeyDto = PojoUtil.copyOne(devForeignKeyDao.selectByPrimaryKey(foreignKey.getId()).get(),
//                ForeignKeyDto.class);
//        echoForeignKeyDto.setReferDbName(getDbName(echoForeignKeyDto.getReferTableId()));
//        return echoForeignKeyDto;
        List<DevForeignKey> existForeignKeyList = devForeignKeyDao.selectMany(select(devForeignKey.allColumns())
                .from(devForeignKey)
                .where(devForeignKey.del, isNotEqualTo(1), and(devForeignKey.tableId, isEqualTo(tableId)))
                .build().render(RenderingStrategies.MYBATIS3));
        Map<String, Long> existForeignKeyMap = existForeignKeyList.stream().collect(Collectors.toMap(existForeignKey ->
                        existForeignKey.getTableId() + existForeignKey.getColumnNames()
                                + existForeignKey.getReferTableId() + existForeignKey.getReferColumnNames(),
                        DevForeignKey::getId));
        List<String> foreignKeyStrList = foreignKeyDtoList.stream().map(foreignKeyDto ->
                tableId + foreignKeyDto.getColumnNames() + foreignKeyDto.getReferTableId() + foreignKeyDto.getReferColumnNames())
                .collect(Collectors.toList());
        List<Long> deleteForeignKeyIdList = existForeignKeyMap.entrySet().stream()
                .filter(existForeignKeyStr -> !foreignKeyStrList.contains(existForeignKeyStr.getKey()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
        // 删除不再是外键的记录
        deleteForeignKeyIdList.forEach(deleteForeignKeyId -> deleteForeignKey(deleteForeignKeyId, operator));

        List<ForeignKeyDto> echoForeignKeyDtoList = foreignKeyDtoList.stream().map(foreignKeyDto -> {
            foreignKeyDto.setTableId(tableId);
            return createOrUpdateForeignKey(foreignKeyDto, columnNameList, operator);})
                .collect(Collectors.toList());
        return echoForeignKeyDtoList;
    }

//    @Override
//    @Transactional(rollbackFor = Throwable.class)
//    public ForeignKeyDto edit(ForeignKeyDto foreignKeyDto, String operator) {
//        checkArgument(isNotEmpty(operator), "修改者不能为空");
//        checkArgument(foreignKeyDto.getId() != null, "外键ID不能为空");
//        DevForeignKey checkDevForeignKey = devForeignKeyDao.selectOne(c ->
//                c.where(devForeignKey.del, isNotEqualTo(1),
//                        and(devForeignKey.id, isEqualTo(foreignKeyDto.getId()))))
//                .orElse(null);
//        checkArgument(checkDevForeignKey != null, "外键不存在");
//        checkArgument(checkDevForeignKey.getTableId().equals(foreignKeyDto.getTableId()), "外键所属表不允许修改");
//        if (isNotEmpty(foreignKeyDto.getColumnNames())) {
//            if (isNotEmpty(foreignKeyDto.getReferColumnNames())) {
//                checkArgument(foreignKeyDto.getColumnNames().split(",").length
//                        == foreignKeyDto.getReferColumnNames().split(",").length, "外键列数量和外键引用列数量需一致");
//            } else {
//                checkArgument(checkDevForeignKey.getReferColumnNames().split(",").length
//                        == foreignKeyDto.getColumnNames().split(",").length, "外键列数量和外键引用列数量需一致");
//            }
//        }
//        if (isNotEmpty(foreignKeyDto.getReferColumnNames())) {
//            if (isNotEmpty(foreignKeyDto.getColumnNames())) {
//                checkArgument(foreignKeyDto.getReferColumnNames().split(",").length
//                        == foreignKeyDto.getColumnNames().split(",").length, "外键列数量和外键引用列数量需一致");
//            } else {
//                checkArgument(foreignKeyDto.getReferColumnNames().split(",").length
//                        == checkDevForeignKey.getColumnNames().split(",").length, "外键列数量和外键引用列数量需一致");
//            }
//        }
//
//
//        foreignKeyDto.setEditor(operator);
//        DevForeignKey foreignKey = PojoUtil.copyOne(foreignKeyDto, DevForeignKey.class, foreignKeyFields);
//        devForeignKeyDao.updateByPrimaryKeySelective(foreignKey);
//
//        ForeignKeyDto echoForeignKeyDto = PojoUtil.copyOne(devForeignKeyDao.selectByPrimaryKey(foreignKey.getId()).get(),
//                ForeignKeyDto.class);
//        echoForeignKeyDto.setReferDbName(getDbName(echoForeignKeyDto.getTableId()));
//        return echoForeignKeyDto;
//    }

    private ForeignKeyDto createOrUpdateForeignKey(ForeignKeyDto foreignKeyDto, List<String> columnNameList, String operator) {
        ForeignKeyDto echoForeignKey;
        DevForeignKey checkForeignKey = devForeignKeyDao.selectOne(c ->
                c.where(devForeignKey.del, isNotEqualTo(1),
                        and(devForeignKey.tableId, isEqualTo(foreignKeyDto.getTableId()),
                        and(devForeignKey.columnNames, isEqualTo(foreignKeyDto.getColumnNames())),
                        and(devForeignKey.referTableId, isEqualTo(foreignKeyDto.getReferTableId())),
                        and(devForeignKey.referColumnNames, isEqualTo(foreignKeyDto.getReferColumnNames())),
                        and(devForeignKey.erType, isEqualTo(foreignKeyDto.getErType())))))
                .orElse(null);
        if (checkForeignKey == null) {
            checkArgument(foreignKeyDto.getTableId() != null, "外键所属表ID不能为空");
            checkArgument(isNotEmpty(foreignKeyDto.getColumnNames()), "外键列名不能为空");
            checkArgument(foreignKeyDto.getReferTableId() != null, "外键引用表ID不能为空");
            checkArgument(isNotEmpty(foreignKeyDto.getReferColumnNames()), "外键引用列名称不能为空");
            checkArgument(foreignKeyDto.getColumnNames().split(",").length
                    == foreignKeyDto.getReferColumnNames().split(",").length, "外键列数量和外键引用列数量需一致");
            checkArgument(isNotEmpty(foreignKeyDto.getErType()), "ER联系类别不能为空");
            ERelationTypeEnum.valueOf(foreignKeyDto.getErType());
            // 校验列名是否存在
            checkArgument(columnNameList.containsAll(Arrays.asList(foreignKeyDto.getColumnNames().split(","))),
                    "外键列名不存在");

            foreignKeyDto.setCreator(operator);
            DevForeignKey foreignKey = PojoUtil.copyOne(foreignKeyDto, DevForeignKey.class, foreignKeyFields);
            devForeignKeyDao.insertSelective(foreignKey);

            echoForeignKey = PojoUtil.copyOne(devForeignKeyDao.selectByPrimaryKey(foreignKey.getId()).get(),
                    ForeignKeyDto.class);
            echoForeignKey.setReferDbName(getDbName(echoForeignKey.getReferTableId()));
        }
        else {
            checkArgument(checkForeignKey.getTableId().equals(foreignKeyDto.getTableId()), "外键所属表不允许修改");
            if (isNotEmpty(foreignKeyDto.getColumnNames())) {
                // 校验列名是否存在
                checkArgument(columnNameList.containsAll(Arrays.asList(foreignKeyDto.getColumnNames().split(","))),
                        "外键列名不存在");
                if (isNotEmpty(foreignKeyDto.getReferColumnNames())) {
                    checkArgument(foreignKeyDto.getColumnNames().split(",").length
                            == foreignKeyDto.getReferColumnNames().split(",").length, "外键列数量和外键引用列数量需一致");
                } else {
                    checkArgument(checkForeignKey.getReferColumnNames().split(",").length
                            == foreignKeyDto.getColumnNames().split(",").length, "外键列数量和外键引用列数量需一致");
                }
            }
            if (isNotEmpty(foreignKeyDto.getReferColumnNames())) {
                if (isNotEmpty(foreignKeyDto.getColumnNames())) {
                    // 校验列名是否存在
                    checkArgument(columnNameList.containsAll(Arrays.asList(foreignKeyDto.getColumnNames().split(","))),
                            "外键列名不存在");
                    checkArgument(foreignKeyDto.getReferColumnNames().split(",").length
                            == foreignKeyDto.getColumnNames().split(",").length, "外键列数量和外键引用列数量需一致");
                } else {
                    checkArgument(foreignKeyDto.getReferColumnNames().split(",").length
                            == checkForeignKey.getColumnNames().split(",").length, "外键列数量和外键引用列数量需一致");
                }
            }

            foreignKeyDto.setEditor(operator);
            foreignKeyDto.setId(checkForeignKey.getId());
            DevForeignKey foreignKey = PojoUtil.copyOne(foreignKeyDto, DevForeignKey.class, foreignKeyFields);
            devForeignKeyDao.updateByPrimaryKeySelective(foreignKey);

            echoForeignKey = PojoUtil.copyOne(devForeignKeyDao.selectByPrimaryKey(foreignKey.getId()).get(),
                    ForeignKeyDto.class);
            echoForeignKey.setReferDbName(getDbName(echoForeignKey.getTableId()));
        }
        return echoForeignKey;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean delete(Long foreignKeyId, String operator) {
        return deleteForeignKey(foreignKeyId, operator);
    }

    private boolean deleteForeignKey(Long foreignKeyId, String operator) {
        checkArgument(isNotEmpty(operator), "删除者不能为空");
        checkArgument(foreignKeyId != null, "外键ID不能为空");
        DevForeignKey checkForeignKey = devForeignKeyDao.selectOne(c ->
                c.where(devForeignKey.del, isNotEqualTo(1), and(devForeignKey.id, isEqualTo(foreignKeyId))))
                .orElse(null);
        checkArgument(checkForeignKey != null, "外键不存在");

        devForeignKeyDao.update(c -> c.set(devForeignKey.del).equalTo(1)
                .set(devForeignKey.editor).equalTo(operator)
                .where(devForeignKey.id, isEqualTo(foreignKeyId)));

        return true;
    }

    private String getDbName(Long tableId) {
        return devLabelDao.selectOne(c -> c
                .where(devLabel.del, isNotEqualTo(1), and(devLabel.labelCode, isEqualTo(DB_NAME_LABEL)),
                        and(devLabel.tableId, isEqualTo(tableId))))
                .get().getLabelParamValue();
    }
}
