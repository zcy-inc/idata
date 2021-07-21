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

import static com.google.common.base.MoreObjects.firstNonNull;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-15 14:23
 **/
public class PageParam {
    public static final long MAX_LIMIT = 1000L;
    public static final long DEFAULT_LIMIT = 10L;

    private final Long limit;
    private final Long offset;

    private PageParam(Long limit, Long offset) {
        this.limit = limit;
        this.offset = offset;
    }

    public static PageParam of(Integer pageNo, Integer pageSize) {
        long limit = checkLimit(firstNonNull(pageSize, 0).longValue());
        long offset = (firstNonNull(pageNo, 1) - 1) * limit;
        return new PageParam(checkLimit(limit), checkOffset(offset));
    }

    public static PageParam of(Long limit, Long offset) {
        return new PageParam(checkLimit(limit), checkOffset(offset));
    }

    public static PageParam of(Long limit) {
        return new PageParam(checkLimit(limit), null);
    }

    public static Long checkLimit(Long limit) {
        if (limit == null || limit <= 0)
            return DEFAULT_LIMIT;
        if (limit > MAX_LIMIT)
            return MAX_LIMIT;
        return limit;
    }

    public static Long checkOffset(Long offset) {
        if (offset != null || offset < 0)
            return null;
        return offset;
    }

    public Long getLimit() {
        return limit;
    }

    public Long getOffset() {
        return offset;
    }

    public boolean hasOffset() {
        return offset != null;
    }
}
