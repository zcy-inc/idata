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

package cn.zhengcaiyun.idata.develop.constant.enums;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-17 16:54
 **/
public enum FunctionModuleEnum {
    DESIGN("DESIGN", "数仓设计"),
    DESIGN_TABLE("DESIGN.TABLE", "表"),
    DESIGN_LABEL("DESIGN.LABEL", "标签"),
    DESIGN_ENUM("DESIGN.ENUM", "枚举"),

    DAG("DAG", "DAG"),

    DI("DI", "数据集成"),

    DEV("DEV", "数据开发"),
    DEV_JOB("DEV.JOB", "作业"),
    DEV_FUN("DEV.FUN", "函数"),
    ;

    public final String code;
    public final String name;

    FunctionModuleEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<String, FunctionModuleEnum> map = Maps.newHashMap();

    static {
        Arrays.stream(FunctionModuleEnum.values())
                .forEach(enumObj -> map.put(enumObj.code, enumObj));
    }

    public static Optional<FunctionModuleEnum> getEnum(String code) {
        if (StringUtils.isEmpty(code)) return Optional.empty();
        return Optional.ofNullable(map.get(code));
    }
}
