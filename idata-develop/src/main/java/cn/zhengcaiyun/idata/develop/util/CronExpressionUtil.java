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

import com.cronutils.model.Cron;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;

import static com.cronutils.model.CronType.QUARTZ;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-04 11:00
 **/
public class CronExpressionUtil {

    public static boolean isValidExpression(String expression) {
        try {
            CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(QUARTZ);
            CronParser parser = new CronParser(cronDefinition);
            Cron quartzCron = parser.parse(expression);
            quartzCron.validate();
            return true;
        } catch (Exception ex) {
        }
        return false;
    }

}
