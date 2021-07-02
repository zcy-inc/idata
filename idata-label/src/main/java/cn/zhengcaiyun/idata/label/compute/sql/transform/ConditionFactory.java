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
import cn.zhengcaiyun.idata.label.compute.sql.model.condition.BaseCondition;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-25 14:19
 **/
public class ConditionFactory<T> {
    public static <T> Optional<BaseCondition<T>> createCondition(String condition, BaseColumn column, T... params) {
        checkArgument(!Objects.isNull(column), "column is null.");
        checkArgument(ObjectUtils.isNotEmpty(params), "params is empty.");

        BaseCondition<T> baseCondition = null;
        if ("between".equals(condition)) {
            checkArgument(params.length < 2, "params are invalid.");
            baseCondition = column.between(params[0], params[1]);
        } else if ("equal".equals(condition)) {
            baseCondition = column.equalTo(params[0]);
        } else if ("greater".equals(condition)) {
            baseCondition = column.greaterThan(params[0]);
        } else if ("greaterOrEqual".equals(condition)) {
            baseCondition = column.greaterThanOrEqualTo(params[0]);
        } else if ("inThe".equals(condition)) {
            baseCondition = column.inThe(params);
        } else if ("less".equals(condition)) {
            baseCondition = column.lessThan(params[0]);
        } else if ("lessOrEqual".equals(condition)) {
            baseCondition = column.lessThanOrEqualTo(params[0]);
        }
        return Optional.ofNullable(baseCondition);
    }
}
