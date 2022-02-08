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

package cn.zhengcaiyun.idata.map.constant.enums;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * @description: 实体源
 * @author: yangjianhua
 * @create: 2021-07-14 16:08
 **/
public enum EntitySourceEnum {
    TABLE("table", "数仓表"),
    INDICATOR("indicator", "数据指标"),
    ;

    private String code;
    private String desc;

    EntitySourceEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    private static final Map<String, EntitySourceEnum> map = Maps.newHashMap();

    static {
        Arrays.stream(EntitySourceEnum.values())
                .forEach(enumObj -> map.put(enumObj.getCode(), enumObj));
    }

    public static Optional<EntitySourceEnum> getEnum(String code) {
        if (StringUtils.isEmpty(code)) return Optional.empty();
        return Optional.ofNullable(map.get(code));
    }
}
