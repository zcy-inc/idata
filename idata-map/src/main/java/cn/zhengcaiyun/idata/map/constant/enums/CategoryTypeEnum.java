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
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-14 16:13
 **/
public enum CategoryTypeEnum {
    PROPERTY_DIR("property_dir", "资产目录"),
    BIZ_PROCESS("biz_process", "业务过程"),
    ;

    private String type;
    private String desc;

    CategoryTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    private static final Map<String, CategoryTypeEnum> map = Maps.newHashMap();

    static {
        Arrays.stream(CategoryTypeEnum.values())
                .forEach(enumObj -> map.put(enumObj.getType(), enumObj));
    }

    public static Optional<CategoryTypeEnum> getEnum(String code) {
        if (StringUtils.isEmpty(code)) return Optional.empty();
        return Optional.ofNullable(map.get(code));
    }
}
