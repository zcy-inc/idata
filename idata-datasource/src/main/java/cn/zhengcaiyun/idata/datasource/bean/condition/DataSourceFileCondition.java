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

package cn.zhengcaiyun.idata.datasource.bean.condition;

import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.EnvEnum;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-17 10:32
 **/
public class DataSourceFileCondition {

    /**
     * 数据源类型
     */
    private DataSourceTypeEnum type;

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 环境
     */
    private EnvEnum env;

    public DataSourceTypeEnum getType() {
        return type;
    }

    public void setType(DataSourceTypeEnum type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EnvEnum getEnv() {
        return env;
    }

    public void setEnv(EnvEnum env) {
        this.env = env;
    }
}
