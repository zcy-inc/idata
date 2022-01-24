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

import com.google.common.base.Strings;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-10-18 15:01
 **/
public class JobVersionHelper {
    public static String getVersionDisplay(Integer version, Date versionCreatedDate) {
        String datePart = LocalDateTime.ofInstant(versionCreatedDate.toInstant(), ZoneId.systemDefault())
                .format(DateTimeFormatter.BASIC_ISO_DATE);
        return "V" + datePart + "-" + Strings.padStart(version.toString(), 3, '0');
    }

}
