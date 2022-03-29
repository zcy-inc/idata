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

package cn.zhengcaiyun.idata.develop.util;

import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.exception.GeneralException;
import cn.zhengcaiyun.idata.commons.util.DesUtil;
import cn.zhengcaiyun.idata.datasource.bean.dto.DbConfigDto;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-03-23 16:11
 **/
public class FlinkSqlUtil {

    public static final String MYSQL_TEMPLATE = "CREATE TABLE IF NOT EXISTS DemoTableJdbc ( \n" +
            "    id BIGINT, \n" +
            "    name STRING, \n" +
            "    cnt INT, \n" +
            "    status BOOLEAN, \n" +
            "    rowtime TIMESTAMP(3), \n" +
            "    proctime AS PROCTIME(), \n" +
            "    WATERMARK FOR rowtime AS rowtime - INTERVAL '5' SECOND, \n" +
            "    PRIMARY KEY (id) NOT ENFORCED \n" +
            ") WITH ( \n" +
            "    'connector' = 'jdbc', \n" +
            "    'url' = '${%s}', \n" +
            "    'username' = '${%s}', \n" +
            "    'password' = '${%s}', \n" +
            "    'table-name' = 'demo_table_jdbc' \n" +
            ");";

    public static final String KAFKA_TEMPLATE = "CREATE TABLE DemoTableKafka (\n" +
            "    user BIGINT, \n" +
            "    message STRING, \n" +
            "    rowtime TIMESTAMP(3) METADATA FROM 'timestamp', \n" +
            "    proctime AS PROCTIME(), \n" +
            "    WATERMARK FOR rowtime AS rowtime - INTERVAL '5' SECOND \n" +
            ") WITH ( \n" +
            "    'connector' = 'kafka', \n" +
            "    'topic' = 'topic_demo', \n" +
            "    'scan.startup.mode' = 'earliest-offset', \n" +
            "    'properties.bootstrap.servers' = '${%s}', \n" +
            "    'properties.group.id' = 'demo_group', \n" +
            "    'format' = 'json' \n" +
            ");";

    public static String generateTemplate(String dataSourceType, String dataSourceUDCode) {
        if (DataSourceTypeEnum.mysql.name().equals(dataSourceType)
                || DataSourceTypeEnum.postgresql.name().equals(dataSourceType)) {
            return generateJDBCTemplate(dataSourceType, dataSourceUDCode);
        }

        if (DataSourceTypeEnum.kafka.name().equals(dataSourceType)) {
            return generateKafkaTemplate(dataSourceType, dataSourceUDCode);
        }
        return "do not support " + dataSourceType + " now.\n";
    }

    public static Map<String, String> generateProperties(String dataSourceType, String dataSourceUDCode,
                                                         DataSourceTypeEnum sourceTypeEnum, DbConfigDto dbConfigDto) {
        if (DataSourceTypeEnum.mysql.name().equals(dataSourceType)
                || DataSourceTypeEnum.postgresql.name().equals(dataSourceType)) {
            return generateJDBCProperties(dataSourceType, dataSourceUDCode, sourceTypeEnum, dbConfigDto);
        }

        if (DataSourceTypeEnum.kafka.name().equals(dataSourceType)) {
            return generateKafkaProperties(dataSourceType, dataSourceUDCode, sourceTypeEnum, dbConfigDto);
        }
        throw new GeneralException("Flink do not support dataSourceType: " + dataSourceType);
    }

    public static String generateJDBCTemplate(String dataSourceType, String dataSourceUDCode) {
        return String.format(MYSQL_TEMPLATE, generateJDBCKeyWord(dataSourceType, dataSourceUDCode));
    }

    public static String[] generateJDBCKeyWord(String dataSourceType, String dataSourceUDCode) {
        String[] keywords = new String[3];
        keywords[0] = dataSourceType + "." + dataSourceUDCode + "." + "url";
        keywords[1] = dataSourceType + "." + dataSourceUDCode + "." + "username";
        keywords[2] = dataSourceType + "." + dataSourceUDCode + "." + "password";
        return keywords;
    }

    public static Map<String, String> generateJDBCProperties(String dataSourceType, String dataSourceUDCode,
                                                             DataSourceTypeEnum sourceTypeEnum, DbConfigDto dbConfigDto) {
        Map<String, String> props = Maps.newHashMap();
        String urlKey = "${" + dataSourceType + "." + dataSourceUDCode + "." + "url" + "}";
        String jdbcUrlVal = String.format("jdbc:%s://%s:%d/%s", sourceTypeEnum.name(), dbConfigDto.getHost(), dbConfigDto.getPort(), dbConfigDto.getDbName());
        props.put(urlKey, DesUtil.encrypt(jdbcUrlVal));
        String nameKey = "${" + dataSourceType + "." + dataSourceUDCode + "." + "username" + "}";
        props.put(nameKey, DesUtil.encrypt(StringUtils.defaultString(dbConfigDto.getUsername())));
        String pwdKey = "${" + dataSourceType + "." + dataSourceUDCode + "." + "password" + "}";
        props.put(pwdKey, DesUtil.encrypt(StringUtils.defaultString(dbConfigDto.getPassword())));
        return props;
    }

    public static String generateKafkaTemplate(String dataSourceType, String dataSourceUDCode) {
        return String.format(KAFKA_TEMPLATE, generateKafkaKeyWord(dataSourceType, dataSourceUDCode));
    }

    public static String[] generateKafkaKeyWord(String dataSourceType, String dataSourceUDCode) {
        String[] keywords = new String[1];
        keywords[0] = dataSourceType + "." + dataSourceUDCode + "." + "servers";
        return keywords;
    }

    public static Map<String, String> generateKafkaProperties(String dataSourceType, String dataSourceUDCode,
                                                              DataSourceTypeEnum sourceTypeEnum, DbConfigDto dbConfigDto) {
        Map<String, String> props = Maps.newHashMap();
        String serversKey = "${" + dataSourceType + "." + dataSourceUDCode + "." + "servers" + "}";
        String serversVal = dbConfigDto.getHost();
        props.put(serversKey, DesUtil.encrypt(serversVal));
        return props;
    }
}
