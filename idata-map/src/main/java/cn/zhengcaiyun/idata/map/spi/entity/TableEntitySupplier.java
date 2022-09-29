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

package cn.zhengcaiyun.idata.map.spi.entity;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.develop.api.TableInfoApi;
import cn.zhengcaiyun.idata.develop.dto.table.ColumnInfoDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableDetailDto;
import cn.zhengcaiyun.idata.map.bean.condition.DataSearchCond;
import cn.zhengcaiyun.idata.map.bean.dto.ColumnAttrDto;
import cn.zhengcaiyun.idata.map.bean.dto.DataEntityDto;
import cn.zhengcaiyun.idata.map.constant.enums.EntitySourceEnum;
import cn.zhengcaiyun.idata.map.dal.dao.MapUserFavouriteDao;
import cn.zhengcaiyun.idata.map.dal.model.MapUserFavourite;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.map.dal.dao.MapUserFavouriteDynamicSqlSupport.mapUserFavourite;
import static org.mybatis.dynamic.sql.SqlBuilder.and;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description: 获取表实体数据
 * @author: yangjianhua
 * @create: 2021-07-14 15:56
 **/
@Component
public class TableEntitySupplier implements DataEntitySupplier<DataSearchCond, DataEntityDto> {

    private final TableInfoApi tableInfoApi;

    @Autowired
    private MapUserFavouriteDao mapUserFavouriteDao;

    @Autowired
    public TableEntitySupplier(TableInfoApi tableInfoApi) {
        this.tableInfoApi = tableInfoApi;
    }

    @PostConstruct
    public void register() {
        DataEntitySupplierFactory.register(EntitySourceEnum.TABLE.getCode(), this);
    }

    /**
     * 根据条件查询数仓表
     *
     * @param condition
     * @return
     */
    @Override
    public List<DataEntityDto> queryDataEntity(DataSearchCond condition) {
        // 从数仓设计模块查询表数据，封装为DataEntityDto对象集合
        String tableSearchRange = Strings.emptyToNull(condition.getTableSearchRange());
        if (tableSearchRange != null && "all".equals(condition.getTableSearchRange()))
            tableSearchRange = null;

        List<Long> tableIds = tableInfoApi.getTableIds(condition.getKeyWords(), Strings.emptyToNull(condition.getCategoryId()),
                Strings.emptyToNull(condition.getTableLayer()), tableSearchRange == null ? null : tableSearchRange.toUpperCase());
        if (ObjectUtils.isEmpty(tableIds)) return Lists.newArrayList();

        if (condition.isMyFavorite()) {
            List<MapUserFavourite> list = mapUserFavouriteDao.select(c ->
                    c.where(mapUserFavourite.userId, isEqualTo(OperatorContext.getCurrentOperator().getId()),
                            and(mapUserFavourite.del, isEqualTo(0))));
            List<Long> idList = list.stream().map(e -> Long.parseLong(e.getEntityCode())).collect(Collectors.toList());
            // 交集
            tableIds.retainAll(idList);
        }

        return tableIds.stream().map(id -> new DataEntityDto(id.toString())).collect(Collectors.toList());
    }

    /**
     * 根据表唯一标识集合查询数仓表设计数据
     *
     * @param entityCodes
     * @return
     */
    @Override
    public List<DataEntityDto> getDataEntity(List<String> entityCodes) {
        List<Long> tableIds = entityCodes.stream().map(Long::parseLong).collect(Collectors.toList());
        // 从数仓设计模块查询表数据，封装为DataEntityDto对象集合
        List<TableDetailDto> dtoList = tableInfoApi.getTablesByIds(tableIds);
        if (ObjectUtils.isEmpty(dtoList)) return Lists.newArrayList();

        return dtoList.stream().map(this::toDataEntity).collect(Collectors.toList());
    }

    private DataEntityDto toDataEntity(TableDetailDto detailDto) {
        DataEntityDto entityDto = new DataEntityDto(detailDto.getId().toString());
        entityDto.setEntityName(detailDto.getTableComment());
        entityDto.setEntityNameEn(detailDto.getTableName());
        entityDto.setMetabaseUrl(detailDto.getMetabaseUrl());
        entityDto.setCategoryPathNames(detailDto.getAssetCatalogues());
        if (Objects.nonNull(detailDto.getSecurityLevel())) {
            entityDto.putMoreAttr(DataEntityDto.more_table_security_level, detailDto.getSecurityLevel());
        }
        List<ColumnInfoDto> columnInfoDtoList = detailDto.getColumnInfos();
        if (ObjectUtils.isNotEmpty(columnInfoDtoList)) {
            List<ColumnAttrDto> columnAttrDtoList = columnInfoDtoList.stream()
                    .map(colInfo -> new ColumnAttrDto(colInfo.getColumnComment(), colInfo.getColumnName()))
                    .collect(Collectors.toList());
            entityDto.putMoreAttr(DataEntityDto.more_table_column, columnAttrDtoList);
        }
        return entityDto;
    }

}
