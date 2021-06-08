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
import cn.zhengcaiyun.idata.develop.dal.dao.DevColumnInfoDao;
import cn.zhengcaiyun.idata.develop.dal.model.DevColumnInfo;
import cn.zhengcaiyun.idata.develop.service.label.LabelService;
import cn.zhengcaiyun.idata.develop.service.table.ColumnInfoService;
import cn.zhengcaiyun.idata.dto.develop.label.LabelDto;
import cn.zhengcaiyun.idata.dto.develop.table.ColumnInfoDto;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevColumnInfoDynamicSqlSupport.devColumnInfo;
import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @author caizhedong
 * @date 2021-05-28 15:34
 */

@Service
public class ColumnInfoServiceImpl implements ColumnInfoService {

    @Autowired
    private DevColumnInfoDao devColumnInfoDao;
    @Autowired
    private LabelService labelService;

    private final String[] columnInfoFields = {"id", "del", "creator", "createTime", "editor", "editTime",
            "columnName", "tableId", "columnIndex"};

    @Override
    public ColumnInfoDto getColumnInfo(Long tableId, String columnName) {
        DevColumnInfo columnInfo = devColumnInfoDao.selectOne(c ->
                c.where(devColumnInfo.del, isNotEqualTo(1), and(devColumnInfo.tableId, isEqualTo(tableId)),
                                and(devColumnInfo.columnName, isEqualTo(columnName))))
                .orElseThrow(() -> new IllegalArgumentException("字段不存在"));
        List<LabelDto> columnLabelList = labelService.findLabels(tableId, columnName);

        ColumnInfoDto echo = PojoUtil.copyOne(columnInfo, ColumnInfoDto.class, columnInfoFields);
        echo.setColumnLabels(columnLabelList);
        return echo;
    }

    @Override
    public List<ColumnInfoDto> getColumns(Long tableId) {
        List<ColumnInfoDto> echoList = new ArrayList<>();
        List<DevColumnInfo> columnInfoList = devColumnInfoDao.selectMany(select(devColumnInfo.allColumns())
                .from(devColumnInfo)
                .where(devColumnInfo.del, isNotEqualTo(1), and(devColumnInfo.tableId, isEqualTo(tableId)))
                .build().render(RenderingStrategies.MYBATIS3));
        if (columnInfoList.size() > 0) {
            echoList = PojoUtil.copyList(columnInfoList, ColumnInfoDto.class, columnInfoFields);
            List<String> columnNameList = echoList.stream().map(ColumnInfoDto::getColumnName).collect(Collectors.toList());
            Map<String, List<LabelDto>> columnInfoMap = labelService.findColumnLabelMap(tableId, columnNameList);
            echoList = echoList.stream().peek(columnInfoDto ->
                    columnInfoDto.setColumnLabels(columnInfoMap.get(columnInfoDto.getColumnName()))
            ).collect(Collectors.toList());
        }

        return echoList;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public ColumnInfoDto create(ColumnInfoDto columnInfoDto, String operator) {
        checkArgument(isNotEmpty(operator), "创建者不能为空");
        checkArgument(columnInfoDto.getTableId() != null, "字段所属表ID不能为空");
        checkArgument(isNotEmpty(columnInfoDto.getColumnName()), "字段名不能为空");
        DevColumnInfo checkDevColumn = devColumnInfoDao.selectOne(c ->
                c.where(devColumnInfo.del, isNotEqualTo(1),
                        and(devColumnInfo.tableId, isEqualTo(columnInfoDto.getTableId())),
                        and(devColumnInfo.columnName, isEqualTo(columnInfoDto.getColumnName()))))
                .orElse(null);
        checkArgument(checkDevColumn == null, "该表中已存在该字段");

        // 插入字段表
        columnInfoDto.setCreator(operator);
        DevColumnInfo columnInfo = PojoUtil.copyOne(columnInfoDto, DevColumnInfo.class,
                "tableId", "columnName", "columnIndex");
        devColumnInfoDao.insertSelective(columnInfo);
        ColumnInfoDto echoColumnInfoDto = PojoUtil.copyOne(devColumnInfoDao.selectByPrimaryKey(columnInfo.getId()).get(),
                ColumnInfoDto.class);
        // 插入label表
        List<LabelDto> columnLabelList = columnInfoDto.getColumnLabels() != null && columnInfoDto.getColumnLabels().size() > 0
                ? columnInfoDto.getColumnLabels() : null;
        if (columnLabelList != null) {
            List<LabelDto> echoColumnLabelList = columnLabelList.stream()
                    .map(columnLabel -> labelService.label(columnLabel, operator))
                    .collect(Collectors.toList());
            echoColumnInfoDto.setColumnLabels(echoColumnLabelList);
        }
        return echoColumnInfoDto;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public ColumnInfoDto edit(ColumnInfoDto columnInfoDto, String operator) {
        checkArgument(isNotEmpty(operator), "修改者不能为空");
        checkArgument(columnInfoDto.getId() != null, "字段ID不能为空");
        DevColumnInfo checkDevColumn = devColumnInfoDao.selectOne(c ->
                c.where(devColumnInfo.del, isNotEqualTo(1),
                        and(devColumnInfo.tableId, isEqualTo(columnInfoDto.getTableId())),
                        and(devColumnInfo.columnName, isEqualTo(columnInfoDto.getColumnName()))))
                .orElse(null);
        checkArgument(checkDevColumn != null, "该表中不存在该字段");

        // 更新字段表
        columnInfoDto.setEditor(operator);
        DevColumnInfo columnInfo = PojoUtil.copyOne(columnInfoDto, DevColumnInfo.class,
                "id", "columnName", "columnIndex");
        devColumnInfoDao.updateByPrimaryKeySelective(columnInfo);
        ColumnInfoDto echoColumnInfoDto = PojoUtil.copyOne(devColumnInfoDao.selectByPrimaryKey(columnInfo.getId()).get(),
                ColumnInfoDto.class);
        // 更新label表
        List<LabelDto> columnLabelList = columnInfoDto.getColumnLabels() != null && columnInfoDto.getColumnLabels().size() > 0
                ? columnInfoDto.getColumnLabels() : null;
        if (columnLabelList != null) {
            List<LabelDto> echoColumnLabelList = columnLabelList.stream()
                    .map(columnLabel -> labelService.label(columnLabel, operator))
                    .collect(Collectors.toList());
            echoColumnInfoDto.setColumnLabels(echoColumnLabelList);
        }
        return echoColumnInfoDto;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean delete(Long columnId, String operator) {
        checkArgument(isNotEmpty(operator), "删除者不能为空");
        checkArgument(columnId != null, "字段ID不能为空");
        DevColumnInfo columnInfo = devColumnInfoDao.selectOne(c ->
                c.where(devColumnInfo.del, isNotEqualTo(1),
                        and(devColumnInfo.id, isEqualTo(columnId))))
                .orElse(null);
        checkArgument(columnInfo != null, "字段不存在");

        // 删除字段表记录
        devColumnInfoDao.update(c -> c.set(devColumnInfo.del).equalTo(1)
                .set(devColumnInfo.editor).equalTo(operator)
                .where(devColumnInfo.id, isEqualTo(columnId)));
        // 删除label表记录
        List<LabelDto> columnLabelList = labelService.findLabels(columnInfo.getTableId(), columnInfo.getColumnName());

        return columnLabelList.stream().allMatch(columnLabel ->
                labelService.removeLabel(columnLabel, operator));
    }
}
