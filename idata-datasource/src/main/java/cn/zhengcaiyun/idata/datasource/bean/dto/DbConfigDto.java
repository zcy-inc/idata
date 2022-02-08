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

package cn.zhengcaiyun.idata.datasource.bean.dto;

import cn.zhengcaiyun.idata.commons.enums.EnvEnum;

/**
 * @description: 数据库配置
 * @author: yangjianhua
 * @create: 2021-09-15 16:31
 **/
public class DbConfigDto {
    /**
     * 环境
     */
    private EnvEnum env;
    /**
     * 数据库名
     */
    private String dbName;
    /**
     * 数据库账号
     */
    private String username;
    /**
     * 数据库密码
     */
    private String password;
    /**
     * 数据库连接地址
     */
    private String host;
    /**
     * 数据库连接端口
     */
    private Integer port;
    /**
     * 目录
     */
    private String schema;

    public EnvEnum getEnv() {
        return env;
    }

    public void setEnv(EnvEnum env) {
        this.env = env;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
