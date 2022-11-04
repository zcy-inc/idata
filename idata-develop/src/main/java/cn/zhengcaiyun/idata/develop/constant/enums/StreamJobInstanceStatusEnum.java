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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 运行实例状态，0：待启动，1：启动中，2：运行中，7：已失败，8：已停止，9：已下线
 * @author: yangjianhua
 * @create: 2022-08-22 16:21
 **/
public enum StreamJobInstanceStatusEnum {

    WAIT_START(0, "待启动"),
    STARTING(1, "启动中"),
    RUNNING(2, "运行中"),
    FAILED(7, "已失败"),
    STOPPED(8, "已停止"),
    DESTROYED(9, "已下线"),
    ;

    public final int val;
    public final String desc;

    StreamJobInstanceStatusEnum(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    private static final Map<Integer, StreamJobInstanceStatusEnum> map = Maps.newHashMap();

    private static final List<StreamJobInstanceStatusEnum> START_TO_STOP_STATUS = Lists.newArrayList(STARTING, RUNNING, FAILED, STOPPED);
    private static final List<StreamJobInstanceStatusEnum> NOT_DESTROYED = Lists.newArrayList(WAIT_START, STARTING, RUNNING, FAILED, STOPPED);

    static {
        Arrays.stream(StreamJobInstanceStatusEnum.values())
                .forEach(enumObj -> map.put(enumObj.val, enumObj));
    }

    public static Optional<StreamJobInstanceStatusEnum> getEnum(Integer val) {
        if (Objects.isNull(val)) return Optional.empty();
        return Optional.ofNullable(map.get(val));
    }

    public static List<StreamJobInstanceStatusEnum> getStartToStopStatusList() {
        return START_TO_STOP_STATUS;
    }

    public static List<Integer> getStartToStopStatusValList() {
        return START_TO_STOP_STATUS.stream()
                .map(statusEnum -> statusEnum.val)
                .collect(Collectors.toList());
    }

    public static List<StreamJobInstanceStatusEnum> getNotDestroyedStatusList() {
        return NOT_DESTROYED;
    }

    public static List<Integer> getNotDestroyedStatusValList() {
        return NOT_DESTROYED.stream()
                .map(statusEnum -> statusEnum.val)
                .collect(Collectors.toList());
    }

}
