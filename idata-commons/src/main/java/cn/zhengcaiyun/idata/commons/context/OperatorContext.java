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

package cn.zhengcaiyun.idata.commons.context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Base64;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * @description: 当前操作人信息
 * @author: yangjianhua
 * @create: 2021-07-12 14:55
 **/
public class OperatorContext {
    private static final ThreadLocal<Operator> operatorTl = new ThreadLocal<>();

    public static void setCurrentOperator(Operator operator) {
        operatorTl.set(operator);
    }

    public static Operator getOperatorIfPresent() {
        return Optional.ofNullable(operatorTl.get()).orElse(null);
    }

    public static Operator getCurrentOperator() {
        Operator operator = operatorTl.get();
        checkState(!isNull(operator), "未找到当前用户信息");
        return operator;
    }

    public static void clearCurrentOperator() {
        operatorTl.remove();
    }

    public static Operator from(String token) {
        checkState(isNotEmpty(token), "未找到当前用户信息");
        JSONObject jsonObject = JSON.parseObject(new String(Base64.getUrlDecoder()
                .decode(StringUtils.split(token.replace("\r\n", ""), ".")[1])));
        return new Operator.Builder(jsonObject.getLong("id"))
                .username(jsonObject.getString("username"))
                .nickname(jsonObject.getString("nickname"))
                .employeeId(jsonObject.getString("employeeId"))
                .realName(jsonObject.getString("realName"))
                .avatar(jsonObject.getString("avatar"))
                .email(jsonObject.getString("email"))
                .mobile(jsonObject.getString("mobile"))
                .build();
    }

}
