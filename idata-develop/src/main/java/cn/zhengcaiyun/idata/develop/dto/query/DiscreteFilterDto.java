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

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-21 16:56
 **/
public class DiscreteFilterDto {
    /**
     *
     */
    private List<String> select;
    /**
     *
     */
    private List<String> unselect;
    /**
     * 字符串匹配条件
     */
    private List<StringMatchDto> match;

    public List<String> getSelect() {
        return select;
    }

    public void setSelect(List<String> select) {
        this.select = select;
    }

    public List<String> getUnselect() {
        return unselect;
    }

    public void setUnselect(List<String> unselect) {
        this.unselect = unselect;
    }

    public List<StringMatchDto> getMatch() {
        return match;
    }

    public void setMatch(List<StringMatchDto> match) {
        this.match = match;
    }
}
