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

import com.google.common.base.Splitter;

import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-19 16:05
 **/
public class KeywordUtil {

    public static List<String> parseKeyword(String keyword) {
        if (isEmpty(keyword)) return null;
        return Splitter.on(" ").omitEmptyStrings().trimResults().splitToList(keyword);
    }

}
