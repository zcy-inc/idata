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

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-18 14:11
 **/
public enum JobTimeOutStrategyEnum {
    ALARM("alarm", "超时告警"),
    FAIL("fail", "超时失败"),
    ;

    public final String code;
    public final String desc;

    JobTimeOutStrategyEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private static final Map<String, JobTimeOutStrategyEnum> map = Maps.newHashMap();

    static {
        Arrays.stream(JobTimeOutStrategyEnum.values())
                .forEach(enumObj -> map.put(enumObj.code, enumObj));
    }

    public static Optional<JobTimeOutStrategyEnum> getEnum(Integer val) {
        if (Objects.isNull(val)) return Optional.empty();
        return Optional.ofNullable(map.get(val));
    }
}
