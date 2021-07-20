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

import cn.zhengcaiyun.idata.map.bean.condition.DataSearchCond;
import cn.zhengcaiyun.idata.map.bean.dto.ColumnAttrDto;
import cn.zhengcaiyun.idata.map.bean.dto.DataEntityDto;
import cn.zhengcaiyun.idata.map.constant.enums.EntitySourceEnum;
import cn.zhengcaiyun.idata.map.manager.TableManager;
import cn.zhengcaiyun.idata.map.util.DataEntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

/**
 * @description: 获取表实体数据
 * @author: yangjianhua
 * @create: 2021-07-14 15:56
 **/
@Component
public class TableEntitySupplier implements DataEntitySupplier<DataSearchCond, DataEntityDto> {

    private final TableManager tableManager;

    @Autowired
    public TableEntitySupplier(TableManager tableManager) {
        this.tableManager = tableManager;
    }

    @PostConstruct
    public void register() {
        DataEntitySupplierFactory.register(EntitySourceEnum.TABLE.getCode(), this);
    }

    @Override
    public List<DataEntityDto> supply(DataSearchCond condition) {
        return null;
    }

    @Override
    public List<DataEntityDto> getDataEntity(List<String> entityCodes) {
        return null;
    }

    @Override
    public List<DataEntityDto> getExtraInfo(List<DataEntityDto> entities) {
        if (isEmpty(entities)) return entities;

        // 单独查询表字段信息
        List<String> codes = DataEntityUtil.getEntityCode(entities);
        Map<String, List<ColumnAttrDto>> tableColumnMap = tableManager.getTableColumnInfo(codes);
        if (isEmpty(tableColumnMap)) return entities;

        entities.stream()
                .forEach(dataEntityDto ->
                        dataEntityDto.putMoreAttr("columns", tableColumnMap.get(dataEntityDto.getEntityCode())));
        return entities;
    }
}
