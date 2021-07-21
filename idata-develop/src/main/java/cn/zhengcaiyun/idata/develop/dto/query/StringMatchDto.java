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

package cn.zhengcaiyun.idata.develop.dto.query;

import cn.zhengcaiyun.idata.develop.constant.enums.LogicOperatorEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.StringMatchTypeEnum;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-21 16:56
 **/
public class StringMatchDto {
    /**
     * 逻辑操作
     */
    private LogicOperatorEnum logicOp;
    /**
     * 匹配类型
     */
    private StringMatchTypeEnum matchType;
    /**
     * 匹配字符串
     */
    private String matchStr;

    public LogicOperatorEnum getLogicOp() {
        return logicOp;
    }

    public void setLogicOp(LogicOperatorEnum logicOp) {
        this.logicOp = logicOp;
    }

    public StringMatchTypeEnum getMatchType() {
        return matchType;
    }

    public void setMatchType(StringMatchTypeEnum matchType) {
        this.matchType = matchType;
    }

    public String getMatchStr() {
        return matchStr;
    }

    public void setMatchStr(String matchStr) {
        this.matchStr = matchStr;
    }
}
