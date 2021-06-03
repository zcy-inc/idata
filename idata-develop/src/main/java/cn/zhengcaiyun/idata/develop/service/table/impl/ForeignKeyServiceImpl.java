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
import cn.zhengcaiyun.idata.develop.service.table.ForeignKeyService;
import cn.zhengcaiyun.idata.dto.develop.table.ForeignKeyDto;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    private String[] foreignKeyFields = {"id", "del", "creator", "create_time", "editor", "edit_time",
            "table_id", "column_names", "refer_table_id", "refer_column_names", "er_type"};
    private final String db_name = "db_name";

    @Override
    public List<ForeignKeyDto> getForeignKeys(Long tableId) {
        var builder = select(devForeignKey.allColumns()).from(devForeignKey).where(devForeignKey.del, isNotEqualTo(1));
        if (tableId != null) {
            builder.and(devForeignKey.tableId, isEqualTo(tableId));
        }
        List<DevForeignKey> foreignKeyList = devForeignKeyDao.selectMany(builder.build().render(RenderingStrategies.MYBATIS3));

        List<ForeignKeyDto> echoForeignKeyDtoList = PojoUtil.copyList(foreignKeyList, ForeignKeyDto.class, foreignKeyFields)
                .stream().peek(
                        foreignKeyDto -> foreignKeyDto.setReferDbName(getDbName(foreignKeyDto.getTableId())))
                .collect(Collectors.toList());
        return echoForeignKeyDtoList;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public ForeignKeyDto create(ForeignKeyDto foreignKeyDto, String operator) {
        checkArgument(isNotEmpty(operator), "创建者不能为空");
        checkArgument(foreignKeyDto.getTableId() != null, "外键所属表ID不能为空");
        checkArgument(isNotEmpty(foreignKeyDto.getColumnNames()), "外键列名不能为空");
        checkArgument(foreignKeyDto.getReferTableId() != null, "外键引用表ID不能为空");
        checkArgument(isNotEmpty(foreignKeyDto.getReferColumnNames()), "外键引用列名称不能为空");
        checkArgument(isNotEmpty(foreignKeyDto.getErType()), "ER联系类别不能为空");
        DevForeignKey checkDevForeignKey = devForeignKeyDao.selectOne(c ->
                c.where(devForeignKey.del, isNotEqualTo(1),
                        and(devForeignKey.tableId, isEqualTo(foreignKeyDto.getTableId()))))
                .orElse(null);
        checkArgument(checkDevForeignKey == null, "该表中已存在该外键");

        foreignKeyDto.setCreator(operator);
        DevForeignKey foreignKey = PojoUtil.copyOne(foreignKeyDto, DevForeignKey.class, foreignKeyFields);
        devForeignKeyDao.insertSelective(foreignKey);

        ForeignKeyDto echoForeignKeyDto = PojoUtil.copyOne(devForeignKeyDao.selectByPrimaryKey(foreignKey.getId()),
                ForeignKeyDto.class);
        echoForeignKeyDto.setReferDbName(getDbName(echoForeignKeyDto.getTableId()));
        return echoForeignKeyDto;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public ForeignKeyDto edit(ForeignKeyDto foreignKeyDto, String operator) {
        checkArgument(isNotEmpty(operator), "修改者不能为空");
        checkArgument(foreignKeyDto.getId() != null, "外键ID不能为空");
        DevForeignKey checkDevForeignKey = devForeignKeyDao.selectOne(c ->
                c.where(devForeignKey.del, isNotEqualTo(1),
                        and(devForeignKey.id, isEqualTo(foreignKeyDto.getId()))))
                .orElse(null);
        checkArgument(checkDevForeignKey != null, "外键不存在");
        checkArgument(checkDevForeignKey.getTableId().equals(foreignKeyDto.getTableId()), "外键所属表不允许修改");

        foreignKeyDto.setEditor(operator);
        DevForeignKey foreignKey = PojoUtil.copyOne(foreignKeyDto, DevForeignKey.class, foreignKeyFields);
        devForeignKeyDao.updateByPrimaryKeySelective(foreignKey);

        ForeignKeyDto echoForeignKeyDto = PojoUtil.copyOne(devForeignKeyDao.selectByPrimaryKey(foreignKey.getId()),
                ForeignKeyDto.class);
        echoForeignKeyDto.setReferDbName(getDbName(echoForeignKeyDto.getTableId()));
        return echoForeignKeyDto;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean delete(Long foreignKeyId, String operator) {
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
                .where(devLabel.del, isNotEqualTo(1), and(devLabel.labelCode, isEqualTo(db_name)),
                        and(devLabel.tableId, isEqualTo(tableId))))
                .get().getLabelParamValue();
    }
}
