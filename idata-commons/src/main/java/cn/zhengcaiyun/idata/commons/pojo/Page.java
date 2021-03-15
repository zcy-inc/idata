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
package cn.zhengcaiyun.idata.commons.pojo;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author shiyin
 * @date 2021-03-03 00:23
 */
public class Page<T> {
    public static final Integer PAGE_SIZE_LIMIT = 1000;
    @ApiModelProperty(required = true)
    private List<T> content;
    @ApiModelProperty(required = true)
    private long total;

    public static <T> Page<T> newOne(List<T> content, long total) {
        Page<T> p = new Page<>();
        p.setContent(content);
        p.setTotal(total);
        return p;
    }

    public static int limitCheck(Integer limit) {
        return limit != null &&  limit >= 0 && limit <= PAGE_SIZE_LIMIT
                ? limit : Page.PAGE_SIZE_LIMIT;
    }

    public static int offsetCheck(Integer offset) {
        return offset != null && offset > 0 ? offset : 0;
    }

    // GaS
    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
