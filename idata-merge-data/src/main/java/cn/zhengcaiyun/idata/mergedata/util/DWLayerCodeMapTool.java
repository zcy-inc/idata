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

package cn.zhengcaiyun.idata.mergedata.util;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-07 11:42
 **/
public class DWLayerCodeMapTool {
    public static Map<String, String> codeMap = Maps.newHashMap();

    public static Map<String, String> shortCodeMap = Maps.newHashMap();

    static {
        codeMap.put("ODS", "DW_LAYER_ODS:ENUM_VALUE");
        codeMap.put("ods", "DW_LAYER_ODS:ENUM_VALUE");

        codeMap.put("DWD", "DW_LAYER_DWD:ENUM_VALUE");
        codeMap.put("dwd", "DW_LAYER_DWD:ENUM_VALUE");

        codeMap.put("DIM", "DW_LAYER_DIM:ENUM_VALUE");
        codeMap.put("dim", "DW_LAYER_DIM:ENUM_VALUE");

        codeMap.put("DWS", "DW_LAYER_DWS:ENUM_VALUE");
        codeMap.put("dws", "DW_LAYER_DWS:ENUM_VALUE");

        codeMap.put("ADS", "DW_LAYER_ADS:ENUM_VALUE");
        codeMap.put("ads", "DW_LAYER_ADS:ENUM_VALUE");

        shortCodeMap.put("DW_LAYER_ODS:ENUM_VALUE", "ODS");
        shortCodeMap.put("DW_LAYER_DWD:ENUM_VALUE", "DWD");
        shortCodeMap.put("DW_LAYER_DIM:ENUM_VALUE", "DIM");
        shortCodeMap.put("DW_LAYER_DWS:ENUM_VALUE", "DWS");
        shortCodeMap.put("DW_LAYER_ADS:ENUM_VALUE", "ADS");
    }

    public static String getCodeEnum(String code) {
        return codeMap.get(code);
    }

    public static String getShortCode(String longCode) {
        return shortCodeMap.get(longCode);
    }

}
