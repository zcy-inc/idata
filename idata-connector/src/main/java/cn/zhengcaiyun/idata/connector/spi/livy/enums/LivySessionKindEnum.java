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
package cn.zhengcaiyun.idata.connector.spi.livy.enums;

/**
 * @author caizhedong
 * @date 2021-11-24 下午2:08
 */

public enum LivySessionKindEnum {
    spark,
    pyspark,
    sparkr,
    sql;

    public static boolean checkSessionKind(String checkKind) {
        boolean isSessionKind = false;
        try {
            LivySessionKindEnum.valueOf(checkKind);
            isSessionKind = true;
        } catch (Exception ignored){}
        return isSessionKind;
    }
}
