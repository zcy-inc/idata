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

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-03-23 16:11
 **/
public class FlinkSqlUtil {

    public static final String MYSQL_TEMPLATE = "CREATE TABLE IF NOT EXISTS DemoTableJdbc ( \n" +
            "id BIGINT, \n" +
            "name STRING, \n" +
            "cnt INT, \n" +
            "status BOOLEAN, \n" +
            "rowtime TIMESTAMP(3), \n" +
            "proctime AS PROCTIME(), \n" +
            "WATERMARK FOR rowtime AS rowtime - INTERVAL '5' SECOND \n" +
            "PRIMARY KEY (id) NOT ENFORCED \n" +
            ") WITH ( \n" +
            "'connector' = 'jdbc', \n" +
            "'url' = '{0}', \n" +
            "'username' = '{1}', \n" +
            "'password' = '{2}', \n" +
            "'table-name' = 'demo_table_jdbc' \n" +
            ");\n";

    public static final String KAFKA_TEMPLATE = "CREATE TABLE DemoTableKafka (\n" +
            "user BIGINT, \n" +
            "message STRING, \n" +
            "rowtime TIMESTAMP(3) METADATA FROM 'timestamp', \n" +
            "proctime AS PROCTIME(), \n" +
            "WATERMARK FOR rowtime AS rowtime - INTERVAL '5' SECOND \n" +
            ") WITH ( \n" +
            "'connector' = 'kafka', \n" +
            "'topic' = 'topic_demo', \n" +
            "'scan.startup.mode' = 'earliest-offset', \n" +
            "'properties.bootstrap.servers' = '{0}', \n" +
            "'properties.group.id' = 'demo_group', \n" +
            "'format' = 'json' \n" +
            ");\n";

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

    public static String generateJDBCTemplate(String dataSourceType, String dataSourceUDCode) {
        return String.format(MYSQL_TEMPLATE, generateJDBCKeyWord(dataSourceType, dataSourceUDCode));
    }

    public static String[] generateJDBCKeyWord(String dataSourceType, String dataSourceUDCode) {
        String[] keywords = new String[3];
        keywords[0] = "${" + dataSourceType + "." + dataSourceUDCode + "." + "url" + "}";
        keywords[1] = "${" + dataSourceType + "." + dataSourceUDCode + "." + "username" + "}";
        keywords[2] = "${" + dataSourceType + "." + dataSourceUDCode + "." + "password" + "}";
        return keywords;
    }

    public static String generateKafkaTemplate(String dataSourceType, String dataSourceUDCode) {
        return String.format(KAFKA_TEMPLATE, generateKafkaKeyWord(dataSourceType, dataSourceUDCode));
    }

    public static String[] generateKafkaKeyWord(String dataSourceType, String dataSourceUDCode) {
        String[] keywords = new String[1];
        keywords[0] = "${" + dataSourceType + "." + dataSourceUDCode + "." + "servers" + "}";
        return keywords;
    }
}
