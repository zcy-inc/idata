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
package cn.zhengcaiyun.idata.label.compute.sql.model;

import cn.zhengcaiyun.idata.label.compute.sql.model.condition.*;
import cn.zhengcaiyun.idata.label.compute.sql.transform.ConditionFactory;

import java.util.Optional;

/**
 * @description: 参考mybatis dynamic sql，BasicColumn
 * @author: yangjianhua
 * @create: 2021-06-24 15:24
 **/
public interface BaseColumn extends ModelRender {
    Optional<String> alias();

    default String renderSqlWithAlias() {
        String renderName = renderSql();
        return alias().map(a -> renderName + " as " + a)
                .orElse(renderName);
    }

    default <T> BaseCondition<T> between(T littler, T bigger) {
        return Between.of(this, littler, bigger);
    }

    default <T> BaseCondition<T> equalTo(T param) {
        return EqualTo.of(this, param);
    }

    default <T> BaseCondition<T> greaterThan(T param) {
        return GreaterThan.of(this, param);
    }

    default <T> BaseCondition<T> greaterThanOrEqualTo(T param) {
        return GreaterThanOrEqualTo.of(this, param);
    }

    default <T> BaseCondition<T> lessThan(T param) {
        return LessThan.of(this, param);
    }

    default <T> BaseCondition<T> lessThanOrEqualTo(T param) {
        return LessThanOrEqualTo.of(this, param);
    }

    default <T> BaseCondition<T> inThe(T... params) {
        return InThe.of(this, params);
    }

    default <T> Optional<BaseCondition<T>> withCondition(String condition, T... params) {
        return ConditionFactory.createCondition(condition, this, params);
    }
}
