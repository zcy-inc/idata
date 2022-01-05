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

package cn.zhengcaiyun.idata.map.bean.condition;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-14 16:59
 **/
public class ViewCountCond {
    private final String entitySource;
    private final List<String> entityCodes;
    private final Long userId;

    private ViewCountCond(Builder builder) {
        this.entitySource = builder.entitySource;
        this.entityCodes = builder.entityCodes;
        this.userId = builder.userId;
    }

    public static class Builder {
        private String entitySource;
        private List<String> entityCodes;
        private Long userId;

        public Builder entitySource(String entitySource) {
            this.entitySource = entitySource;
            return this;
        }

        public Builder entityCodes(List<String> entityCodes) {
            this.entityCodes = entityCodes;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public ViewCountCond build() {
            return new ViewCountCond(this);
        }
    }

    public String getEntitySource() {
        return entitySource;
    }

    public List<String> getEntityCodes() {
        return entityCodes;
    }

    public Long getUserId() {
        return userId;
    }
}
