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

import cn.zhengcaiyun.idata.map.bean.condition.CategoryCond;
import cn.zhengcaiyun.idata.map.bean.dto.CategoryTreeNodeDto;
import cn.zhengcaiyun.idata.map.spi.category.CategorySupplier;
import cn.zhengcaiyun.idata.map.spi.category.CategorySupplierFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-19 16:00
 **/
@Component
public class CategoryManager {

    private final CategorySupplierFactory categorySupplierFactory;

    @Autowired
    public CategoryManager(CategorySupplierFactory categorySupplierFactory) {
        this.categorySupplierFactory = categorySupplierFactory;
    }

    public List<CategoryTreeNodeDto> getCategoryTreeNode(String categoryType, CategoryCond condition) {
        checkArgument(isNotEmpty(categoryType), "类别类型不能为空");
        checkArgument(isNotEmpty(condition), "查询条件不能为空");

        CategorySupplier categorySupplier = categorySupplierFactory.getSupplier(categoryType);
        return categorySupplier.supply(condition);
    }
}
