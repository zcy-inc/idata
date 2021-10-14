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

package cn.zhengcaiyun.idata.datasource.spi;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-10-14 10:34
 **/
@Component
public class DataSourceUsageSupplierFactory {
    private static final Map<String, DataSourceUsageSupplier> supplierMap = Maps.newConcurrentMap();

    public static void register(String function, DataSourceUsageSupplier supplier) {
        supplierMap.put(function, supplier);
    }

    public Optional<DataSourceUsageSupplier> getSupplier(String function) {
        if (StringUtils.isEmpty(function)) return Optional.empty();
        return Optional.ofNullable(supplierMap.get(function));
    }

    public List<DataSourceUsageSupplier> getSuppliers() {
        return Lists.newArrayList(supplierMap.values());
    }
}
