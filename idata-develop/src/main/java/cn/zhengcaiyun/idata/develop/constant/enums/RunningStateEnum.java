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
 * @create: 2021-09-26 15:30
 **/
public enum RunningStateEnum {
    pause(0, "暂停运行"),
    resume(1, "恢复运行");

    public final int val;
    public final String desc;

    RunningStateEnum(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    private static final Map<Integer, RunningStateEnum> map = Maps.newHashMap();

    static {
        Arrays.stream(RunningStateEnum.values())
                .forEach(enumObj -> map.put(enumObj.val, enumObj));
    }

    public static Optional<RunningStateEnum> getEnum(Integer val) {
        if (Objects.isNull(val)) return Optional.empty();
        return Optional.ofNullable(map.get(val));
    }
}
