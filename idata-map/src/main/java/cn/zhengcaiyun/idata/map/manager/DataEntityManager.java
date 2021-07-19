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

package cn.zhengcaiyun.idata.map.manager;

import cn.zhengcaiyun.idata.map.bean.condition.DataSearchCond;
import cn.zhengcaiyun.idata.map.bean.dto.DataEntityDto;
import cn.zhengcaiyun.idata.map.spi.entity.DataEntitySupplier;
import cn.zhengcaiyun.idata.map.spi.entity.DataEntitySupplierFactory;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-19 15:24
 **/
@Component
public class DataEntityManager {

    private final DataEntitySupplierFactory dataEntitySupplierFactory;

    @Autowired
    public DataEntityManager(DataEntitySupplierFactory dataEntitySupplierFactory) {
        this.dataEntitySupplierFactory = dataEntitySupplierFactory;
    }

    public List<DataEntityDto> getDataEntity(String entitySource, List<String> entityCodes) {
        checkArgument(isNotEmpty(entitySource), "业务源标识不能为空");
        checkArgument(isNotEmpty(entityCodes), "业务标识不能为空");
        DataEntitySupplier supplier = dataEntitySupplierFactory.getSupplier(entitySource);
        return supplier.supply(entityCodes);
    }

    public List<DataEntityDto> getDataEntity(String entitySource, DataSearchCond condition) {
        checkArgument(isNotEmpty(entitySource), "业务源标识不能为空");
        checkArgument(isNotEmpty(condition), "查询条件不能为空");
        DataEntitySupplier supplier = dataEntitySupplierFactory.getSupplier(entitySource);
        return supplier.supply(condition);
    }

    public Map<String, DataEntityDto> getDataEntityMap(String entitySource, List<String> entityCodes) {
        List<DataEntityDto> entityDtoList = getDataEntity(entitySource, entityCodes);
        if (isEmpty(entityDtoList)) {
            return Maps.newHashMap();
        }
        return entityDtoList.stream()
                .collect(Collectors.toMap(DataEntityDto::getEntityCode, Function.identity()));
    }
}
