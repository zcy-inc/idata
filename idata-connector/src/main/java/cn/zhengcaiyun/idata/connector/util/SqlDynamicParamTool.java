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

package cn.zhengcaiyun.idata.connector.util;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-07-28 16:34
 **/
public class SqlDynamicParamTool {

    private static final Set<String> PARAMS = Sets.newHashSet("bizdate", "bizmonth", "bizquarter", "bizyear", "day", "dt");

    public static String replaceParam(String sourceSql) {
        Map<String, String> paramMap = getParamMap();
        sourceSql = sourceSql.replace("{0}", paramMap.get("day"));
        String[] sourceSqls = sourceSql.split("[${}]");
        List<String> sourceSqlList = Arrays.asList(sourceSqls);
        List<String> sqlList = new ArrayList<>();
        Map<Integer, String> bizChangeMap = new HashMap<>();
        for (int i = 0; i < sourceSqlList.size(); i++) {
            String sql = sourceSqlList.get(i);
            if ("".equals(sql)) {
                if (i < sourceSqlList.size() - 1) {
                    String nextSql = sourceSqlList.get(i + 1);
                    if (PARAMS.contains(nextSql)) {
                        bizChangeMap.put(i + 1, paramMap.get(nextSql));
                    } else if (nextSql.contains("+") || nextSql.contains("-")) {
                        bizChangeMap.put(i + 1, getParsedDate(nextSql, paramMap));
                    }
                }
            }
        }
        for (int i = 0; i < sourceSqlList.size(); i++) {
            if (!"".equals(sourceSqlList.get(i))) {
                if (bizChangeMap.containsKey(i)) {
                    sqlList.add(bizChangeMap.get(i));
                } else {
                    sqlList.add(sourceSqlList.get(i));
                }
            }
        }
        return String.join("", sqlList);
    }

    private static Map<String, String> getParamMap() {
        Map<String, String> paramMap = new HashMap<>();
        LocalDate localDate = LocalDate.now().plusDays(-1L);
        DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
        String yesterday = yyyyMMdd.format(localDate);
        DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");
        String month = yyyyMM.format(localDate);
        DateTimeFormatter yyyy = DateTimeFormatter.ofPattern("yyyy");
        String year = yyyy.format(localDate);
        paramMap.put("bizdate", yesterday);
        paramMap.put("bizmonth", month);
        paramMap.put("bizquarter", yearQuarter(localDate));
        paramMap.put("bizyear", year);
        paramMap.put("day", yesterday);
        return paramMap;
    }

    private static String yearQuarter(LocalDate localDate) {
        int month = localDate.getMonth().getValue();
        int quarter = (month + 2) / 3;
        int year = localDate.getYear();
        return year + "0" + quarter;
    }

    private static String getParsedDate(String exp, Map<String, String> param) {
        char unit = exp.charAt(exp.length() - 1);
        int index = StringUtils.indexOfAny(exp, "+", "-");
        int offset = NumberUtils.toInt(exp.substring(index, exp.length() - 1));
        String rKey = exp.substring(0, index);
        String dateValue = param.get(rKey);
        String format = dateValue.length() == 2 ? "HH" :
                dateValue.length() == 4 ? "HHmm" :
                        dateValue.length() == 8 ? "yyyyMMdd" : "yyyy-MM-dd";
        try {
            Date date = DateUtils.parseDate(dateValue, format);
            if (unit == 'Y' || unit == 'y') {
                date = DateUtils.addYears(date, offset);
            } else if (unit == 'M' || unit == 'm') {
                date = DateUtils.addMonths(date, offset);
            } else if (unit == 'D' || unit == 'd') {
                date = DateUtils.addDays(date, offset);
            } else if (unit == 'H' || unit == 'h') {
                date = DateUtils.addHours(date, offset);
            } else {
                date = DateUtils.addMinutes(date, offset);
            }
            return DateFormatUtils.format(date, format);
        } catch (Exception e) {
            throw new IllegalArgumentException("The timeExp " + exp + " is not support expression.");
        }
    }
}
