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

package cn.zhengcaiyun.idata.commons.enums;


import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * @description: 数据源类型
 * @author: yangjianhua
 * @create: 2021-09-15 15:48
 **/
public enum DataSourceTypeEnum {
    mysql,
    postgresql,
    hive,
    presto,
    kylin,
    phoenix,
    elasticsearch,
    mssql,
    kafka,
    doris,
    csv,
    ;

    private static final Map<String, DataSourceTypeEnum> map = Maps.newHashMap();

    static {
        Arrays.stream(DataSourceTypeEnum.values())
                .forEach(enumObj -> map.put(enumObj.name(), enumObj));
    }

    public static Optional<DataSourceTypeEnum> getEnum(String enumName) {
        if (StringUtils.isEmpty(enumName)) return Optional.empty();
        return Optional.ofNullable(map.get(enumName));
    }
}
