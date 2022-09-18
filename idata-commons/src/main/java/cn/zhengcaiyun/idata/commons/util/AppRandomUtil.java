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
package cn.zhengcaiyun.idata.commons.util;

import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

/**
 * @author caizhedong
 * @date 2022-09-18 下午6:19
 */

public class AppRandomUtil {

    private static final String LETTER_NUMBER = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // 包含有数字和字符的字符串 62
    private static final String SIMPLE_LETTER_NUMBER_SYMBOL = "abcdefhkmnpqrstuvwxy3456789";

    /**
     * 获得随机的字母和数字组合
     * @return
     */
    public static String getRandomLetterAndNumber(int length) {
        if(length <= 0) {
            return StringUtils.EMPTY;
        }
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; ++i) {
            sb.append(LETTER_NUMBER.charAt(random.nextInt(LETTER_NUMBER.length())));
        }
        return sb.toString();
    }

    /**
     * 仅仅包括数字和字母，去掉易混淆字母
     * @param length
     * @return
     */
    public static String getSimpleLetterNumber(int length) {
        if(length <= 0) {
            return StringUtils.EMPTY;
        }
        Random random = new SecureRandom();
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < length; i++){
            sb.append(SIMPLE_LETTER_NUMBER_SYMBOL.charAt(random.nextInt(SIMPLE_LETTER_NUMBER_SYMBOL.length())));
        }
        return sb.toString();
    }

    public static String generateAppSecret() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
