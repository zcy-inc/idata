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
package cn.zhengcaiyun.idata.label.compute.sql.transform;

import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;
import cn.zhengcaiyun.idata.label.compute.sql.model.function.*;

import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-25 11:02
 **/
public class FunctionColumnFactory {
    public static Optional<BaseColumn> createFunctionColumn(String function, BaseColumn column, String aliasName) {
        BaseFunction baseFunction = null;
        if ("AGGREGATOR_SUM:ENUM_VALUE".equals(function)) {
            baseFunction = Sum.of(column, aliasName);
        } else if ("AGGREGATOR_AVG:ENUM_VALUE".equals(function)) {
            baseFunction = Avg.of(column, aliasName);
        } else if ("AGGREGATOR_MAX:ENUM_VALUE".equals(function)) {
            baseFunction = Max.of(column, aliasName);
        } else if ("AGGREGATOR_MIN:ENUM_VALUE".equals(function)) {
            baseFunction = Min.of(column, aliasName);
        } else if ("AGGREGATOR_CNT:ENUM_VALUE".equals(function)) {
            baseFunction = Count.of(column, aliasName);
        } else if ("AGGREGATOR_CNTD:ENUM_VALUE".equals(function)) {
            baseFunction = CountDistinct.of(column, aliasName);
        }
        return Optional.ofNullable(baseFunction);
    }
}
