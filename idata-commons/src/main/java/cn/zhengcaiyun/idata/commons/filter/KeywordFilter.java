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

package cn.zhengcaiyun.idata.commons.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-12-10 15:07
 **/
public class KeywordFilter {
    private final String keyWord;
    private final Pattern keyWordPattern;
    private final Matcher keyWordMatcher;

    public KeywordFilter(String keyWord) {
        this(keyWord, true);
    }

    public KeywordFilter(String keyWord, boolean caseInsensitive) {
        this.keyWord = keyWord;
        if (caseInsensitive) {
            this.keyWordPattern = Pattern.compile("^(.*" + keyWord + ".*)$", Pattern.CASE_INSENSITIVE);
        } else {
            this.keyWordPattern = Pattern.compile("^(.*" + keyWord + ".*)$");
        }
        // 重用 Matcher，不可用于多线程场景
        this.keyWordMatcher = this.keyWordPattern.matcher("");
    }

    public boolean match(String text) {
        keyWordMatcher.reset(text);
        if (keyWordMatcher.matches()) return true;

        return false;
    }
}
