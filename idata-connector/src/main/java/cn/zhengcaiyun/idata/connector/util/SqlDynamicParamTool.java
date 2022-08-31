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
        Stack<String> sqlStack = new Stack<>();
        List<Integer> indexList = new ArrayList<>();
        for(int i = 0; i < sourceSql.length(); i++) {
            String subStr = sourceSql.substring(i, i + 1);
            if (i + 1 < sourceSql.length() && sqlStack.size() == 0 && "$".equals(subStr)
                    && "{".equals(sourceSql.substring(i + 1, i + 2))) {
                indexList.add(i);
                sqlStack.push("${");
            }
            else if (sqlStack.size() > 0 && "}".equals(subStr)) {
                indexList.add(i + 1);
                sqlStack.pop();
            }
        }
        if (!indexList.contains(sourceSql.length())) {
            indexList.add(sourceSql.length());
        }
        // 处理字符串是否从index为0开始替换，影响判断list中bizParam的取值范围
        int listSplitRemainder = indexList.contains(0) ? 1 : 0;
        if (!indexList.contains(0)) {
            indexList.add(0, 0);
        }
        // 必定包括起始index
        if (indexList.size() == 2) return sourceSql;
        List<String> sqlList = new ArrayList<>();
        for (int i = 0; i < indexList.size() - 1; i++) {
            String sql = sourceSql.substring(indexList.get(i), indexList.get(i + 1));
            if (i % 2 == listSplitRemainder) {
                sqlList.add(sql);
            }
            else {
                String bizParamKey = sourceSql.substring(indexList.get(i) + 2, indexList.get(i + 1) - 1);
                if (PARAMS.contains(bizParamKey)) {
                    sqlList.add(paramMap.get(bizParamKey));
                }
                else if ((bizParamKey.contains("+") && PARAMS.contains(bizParamKey.split("\\+")[0]))
                        || (bizParamKey.contains("-") && PARAMS.contains(bizParamKey.split("-")[0]))) {
                    sqlList.add(getParsedDate(bizParamKey, paramMap));
                }
                else {
                    sqlList.add(sql);
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
        DateTimeFormatter yyyyMMddCriteria = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String yesterdayCriteria = yyyyMMddCriteria.format(localDate);
        DateTimeFormatter yyyyMM = DateTimeFormatter.ofPattern("yyyyMM");
        String month = yyyyMM.format(localDate);
        DateTimeFormatter yyyy = DateTimeFormatter.ofPattern("yyyy");
        String year = yyyy.format(localDate);
        paramMap.put("bizdate", yesterday);
        paramMap.put("bizmonth", month);
        paramMap.put("bizquarter", yearQuarter(localDate));
        paramMap.put("bizyear", year);
        paramMap.put("day", yesterday);
        paramMap.put("dt", yesterdayCriteria);
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
        String format = formatPattern(dateValue.length());
        try {
            Date date = DateUtils.parseDate(dateValue, format);
            if (unit == 'Y' || unit == 'y') {
                date = DateUtils.addYears(date, offset);
            } else if (unit == 'M' || unit == 'm') {
                date = DateUtils.addMonths(date, offset);
            } else if (unit == 'D' || unit == 'd') {
                date = DateUtils.addDays(date, offset);
            } else {
                date = DateUtils.addMinutes(date, offset);
            }
            return DateFormatUtils.format(date, format);
        } catch (Exception e) {
            throw new IllegalArgumentException("The timeExp " + exp + " is not support expression.");
        }
    }

    private static String formatPattern(int len) {
        String format;
        switch (len) {
            case 2:
                format = "HH";
                break;
            case 4:
                format = "yyyy";
                break;
            case 6:
                format = "yyyyMM";
                break;
            case 7:
                format = "yyyy-MM";
                break;
            case 8:
                format = "yyyyMMdd";
                break;
            default:
                format = "yyyy-MM-dd";
        }
        return format;
    }
}
