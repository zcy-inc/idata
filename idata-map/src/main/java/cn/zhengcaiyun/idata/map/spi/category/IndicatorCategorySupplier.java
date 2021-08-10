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

package cn.zhengcaiyun.idata.map.spi.category;

import cn.zhengcaiyun.idata.develop.api.EnumApi;
import cn.zhengcaiyun.idata.develop.dto.label.EnumValueDto;
import cn.zhengcaiyun.idata.map.bean.condition.CategoryCond;
import cn.zhengcaiyun.idata.map.bean.dto.CategoryTreeNodeDto;
import cn.zhengcaiyun.idata.map.constant.enums.CategoryTypeEnum;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 获取表实体数据
 * @author: yangjianhua
 * @create: 2021-07-14 15:56
 **/
@Component
public class IndicatorCategorySupplier implements CategorySupplier<CategoryCond, CategoryTreeNodeDto> {

    private final EnumApi enumApi;

    @Autowired
    public IndicatorCategorySupplier(EnumApi enumApi) {
        this.enumApi = enumApi;
    }

    @PostConstruct
    public void register() {
        CategorySupplierFactory.register(CategoryTypeEnum.BIZ_PROCESS.getType(), this);
    }

    /**
     * 根据条件查询业务过程
     *
     * @param categoryCond 查询条件
     * @return 业务过程list
     */
    @Override
    public List<CategoryTreeNodeDto> supply(CategoryCond categoryCond) {
        // 从指标库查询业务过程数据，封装为CategoryTreeNodeDto对象集合
        List<EnumValueDto> dtoList = enumApi.getEnumValues("bizTypeEnum:ENUM");
        if (ObjectUtils.isEmpty(dtoList)) return Lists.newArrayList();

        return dtoList.stream().map(this::toTreeNode).collect(Collectors.toList());
    }

    private CategoryTreeNodeDto toTreeNode(EnumValueDto dto) {
        CategoryTreeNodeDto nodeDto = new CategoryTreeNodeDto();
        nodeDto.setId(dto.getValueCode());
        nodeDto.setName(dto.getEnumValue());
        nodeDto.setParentId(MoreObjects.firstNonNull(dto.getParentCode(), ""));
        nodeDto.setType("bizTypeEnum:ENUM");
        return nodeDto;
    }
}
