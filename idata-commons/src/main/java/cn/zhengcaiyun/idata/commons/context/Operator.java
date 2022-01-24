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

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.isNull;

/**
 * @description: 操作人dto
 * @author: yangjianhua
 * @create: 2021-07-12 14:29
 **/
public class Operator {
    private final Long id;
    private final String username;
    private final String nickname;
    private final String employeeId;
    private final String realName;
    private final String avatar;
    private final String email;
    private final String mobile;

    private Operator(Builder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.nickname = builder.nickname;
        this.employeeId = builder.employeeId;
        this.realName = builder.realName;
        this.avatar = builder.avatar;
        this.email = builder.email;
        this.mobile = builder.mobile;
    }

    public static class Builder {
        private final Long id;
        private String username;
        private String nickname;
        private String employeeId;
        private String realName;
        private String avatar;
        private String email;
        private String mobile;

        public Builder(Long id) {
            checkArgument(!isNull(id), "操作人id不能为空");
            this.id = id;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder nickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder employeeId(String employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public Builder realName(String realName) {
            this.realName = realName;
            return this;
        }

        public Builder avatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder mobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public Operator build() {
            return new Operator(this);
        }
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getRealName() {
        return realName;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }
}
