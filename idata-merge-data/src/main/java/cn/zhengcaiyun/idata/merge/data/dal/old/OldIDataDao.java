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
package cn.zhengcaiyun.idata.merge.data.dal.old;

import cn.zhengcaiyun.idata.merge.data.util.PgStringArrayJsonSerializer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.postgresql.jdbc.PgArray;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author caizhedong
 * @date 2021-08-10 19:20
 */

@Component
public class OldIDataDao implements InitializingBean, DisposableBean {

    private HikariDataSource oldDatasource;
    private JdbcTemplate oldJdbcTemplate;

    @Value("${pg.datasource.jdbcUrl}")
    private String jdbcUrl;
    @Value("${pg.datasource.username}")
    private String username;
    @Value("${pg.datasource.password}")
    private String password;
    @Value("${pg.datasource.maximumPoolSize:#{null}}")
    private Integer maximumPoolSize;
    @Value("${pg.datasource.minimumIdle:#{null}}")
    private Integer minimumIdle;
    @Value("${pg.datasource.idleTimeout:#{null}}")
    private Long idleTimeout;

    @Override
    public void destroy() throws Exception {
        if (oldDatasource != null) {
            oldDatasource.close();
            oldDatasource = null;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(1);
//        hikariConfig.setMinimumIdle(minimumIdle);
//        hikariConfig.setIdleTimeout(idleTimeout);
        this.oldDatasource = new HikariDataSource(hikariConfig);
        this.oldJdbcTemplate = new JdbcTemplate(oldDatasource);
    }

    public List<JSONObject> queryList(String table, List<String> columns, String condition) {
        String querySql = "select " + Joiner.on(",").skipNulls().join(columns) + " from " + table;
        if (StringUtils.isNotEmpty(condition)) {
            querySql += " where " + condition;
        }

        return oldJdbcTemplate.queryForList(querySql)
                .stream()
                .map(recordMap -> JSON.parseObject(JSON.toJSONString(recordMap)))
                .collect(Collectors.toList());
    }

    public List<String> queryJsonStringList(String table, List<String> columns, String condition) {
        String querySql = "select " + Joiner.on(",").skipNulls().join(columns) + " from " + table;
        if (StringUtils.isNotEmpty(condition)) {
            querySql += " where " + condition;
        }

        return oldJdbcTemplate.queryForList(querySql)
                .stream()
                .map(recordMap -> JSON.toJSONString(recordMap))
                .collect(Collectors.toList());
    }

    public List<JSONObject> queryListWithCustom(String table, List<String> columns, String condition) {
        String querySql = "select " + Joiner.on(",").skipNulls().join(columns) + " from " + table;
        if (StringUtils.isNotEmpty(condition)) {
            querySql += " where " + condition;
        }

        Gson gson = new GsonBuilder().registerTypeAdapter(PgArray.class, new PgStringArrayJsonSerializer()).create();
        return oldJdbcTemplate.queryForList(querySql)
                .stream()
                .map(recordMap -> JSON.parseObject(gson.toJson(recordMap)))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> queryMapList(String table, List<String> columns, String condition) {
        String querySql = "select " + Joiner.on(",").skipNulls().join(columns) + " from " + table;
        if (StringUtils.isNotEmpty(condition)) {
            querySql += " where " + condition;
        }

        return oldJdbcTemplate.queryForList(querySql);
    }

    public List<JSONObject> queryList(String querySql) {
        return oldJdbcTemplate.queryForList(querySql)
                .stream()
                .map(recordMap -> JSON.parseObject(JSON.toJSONString(recordMap)))
                .collect(Collectors.toList());
    }

    public List<String> queryJsonStringList(String querySql) {
        return oldJdbcTemplate.queryForList(querySql)
                .stream()
                .map(recordMap -> JSON.toJSONString(recordMap))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> queryMapList(String querySql) {
        return oldJdbcTemplate.queryForList(querySql);
    }

    public List<Map<String, Object>> selectList(String sql) {
        return oldJdbcTemplate.queryForList(sql);
    }
}