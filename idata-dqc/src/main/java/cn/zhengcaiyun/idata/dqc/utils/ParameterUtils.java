/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.zhengcaiyun.idata.dqc.utils;

import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * parameter parse utils
 */
public class ParameterUtils {
    public static final String DATE_PARSE_PATTERN = "\\$\\{([^\\$\\]]+)}";

    public static final String DATE_START_PATTERN = "^[0-9]";

    private ParameterUtils() {
        throw new UnsupportedOperationException("Construct ParameterUtils");
    }

    /**
     * 根据${}中的表达式获取日期
     * @param templateStr
     * @param date
     * @return
     */
    public static String dateTemplateParse(String templateStr, Date date) {
        if (templateStr == null) {
            return null;
        }
        Pattern pattern = Pattern.compile(DATE_PARSE_PATTERN);

        StringBuffer newValue = new StringBuffer(templateStr.length());

        Matcher matcher = pattern.matcher(templateStr);

        while (matcher.find()) {
            String key = matcher.group(1);
            if (Pattern.matches(DATE_START_PATTERN, key)) {
                continue;
            }
            String value = TimePlaceholderUtils.getPlaceHolderTime(key, date);
            assert value != null;
            matcher.appendReplacement(newValue, value);
        }

        matcher.appendTail(newValue);

        return newValue.toString();
    }

    /**
     * 根据表达式获取上一个周期的日期
     * @param templateStr
     * @param date
     * @return
     */
    public static String getPreCycleDate(String templateStr, Date date) {
        if (templateStr == null) {
            return null;
        }
        Pattern pattern = Pattern.compile(DATE_PARSE_PATTERN);

        StringBuffer newValue = new StringBuffer(templateStr.length());

        Matcher matcher = pattern.matcher(templateStr);

        while (matcher.find()) {
            String key = matcher.group(1);
            if (Pattern.matches(DATE_START_PATTERN, key)) {
                continue;
            }
            if (key.contains("+")) {
                int index = key.lastIndexOf('+');

                if (Character.isDigit(key.charAt(index + 1))) {
                    key = key.substring(0,index);
                }
            } else if (key.contains("-")) {
                int index = key.lastIndexOf('-');

                if (Character.isDigit(key.charAt(index + 1))) {
                    String addMinuteExpr = key.substring(index + 1);
                    key = key.substring(0,index)+"-"+addMinuteExpr+"*2";
                }else{
                    key += "-1";
                }
            }

            String value = TimePlaceholderUtils.getPlaceHolderTime(key, date);
            assert value != null;
            matcher.appendReplacement(newValue, value);
        }

        matcher.appendTail(newValue);

        return newValue.toString();
    }



    public static void main(String[] args) {
//        String date1 = ParameterUtils.dateTemplateParse("dasda${yyyy-MM-dd HH:mm:ss-1/24}", new Date());
//        String date1 = ParameterUtils.dateTemplateParse("dasda${yyyy-MM-dd-1*7}", new Date());
//        String date = ParameterUtils.getPreCycleDate("dasda${yyyy-MM-dd-1*7}", new Date());
//        String date1 = ParameterUtils.getPreCycleDate("dasda${yyyy-MM-dd}", new Date());
//        String date = ParameterUtils.dateTemplateParse("dasda${yyyy-MM-dd}", new Date());
        String date1 = ParameterUtils.dateTemplateParse("select count(*) from dwd.dqc_test where bizdate=${yyyyMMdd} ", new Date());
//        String date1 = ParameterUtils.dateTemplateParse("pt=${yyyyMMdd+7}", new Date());
//        String date = ParameterUtils.getPreCycleDate("pt=${yyyyMMdd+7}", new Date());
//        String date3 = ParameterUtils.dateTemplateParse("${yyyyMMdd-7}", new Date());
//        String date4 = ParameterUtils.dateTemplateParse("${HHmmss+1/24/60}", new Date());
//        String date5 = ParameterUtils.dateTemplateParse("${HH-1/24}", new Date());
//        System.out.println(date);
        System.out.println(date1);
//        System.out.println(date2);
//        System.out.println(date3);
//        System.out.println(date4);
//        System.out.println(date5);
    }
}
