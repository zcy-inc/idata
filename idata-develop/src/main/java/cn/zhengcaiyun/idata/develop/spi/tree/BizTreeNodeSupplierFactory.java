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

package cn.zhengcaiyun.idata.develop.spi.tree;

import cn.zhengcaiyun.idata.develop.constant.enums.FunctionModuleEnum;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-18 14:26
 **/
@Component
public class BizTreeNodeSupplierFactory {
    private static final Map<FunctionModuleEnum, BizTreeNodeSupplier> supplierMap = Maps.newConcurrentMap();

    public static void register(FunctionModuleEnum moduleEnum, BizTreeNodeSupplier supplier) {
        supplierMap.put(moduleEnum, supplier);
    }

    public Optional<BizTreeNodeSupplier> getSupplier(FunctionModuleEnum moduleEnum) {
        if (isNull(moduleEnum)) return Optional.empty();
        return Optional.ofNullable(supplierMap.get(moduleEnum));
    }
}
