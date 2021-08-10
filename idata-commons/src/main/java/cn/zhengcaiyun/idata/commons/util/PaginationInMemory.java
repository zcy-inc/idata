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

import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

/**
 * @description: 内存分页辅助类
 * @author: yangjianhua
 * @create: 2021-07-15 15:22
 **/
public class PaginationInMemory<T extends Comparable<T>> {
    private final List<T> dataList;

    private PaginationInMemory(List<T> dataList) {
        this.dataList = dataList;
    }

    public static <T extends Comparable<T>> PaginationInMemory<T> of(List<T> dataList) {
        checkArgument(isNotEmpty(dataList), "分页数据为空");
        return new PaginationInMemory<>(dataList);
    }

    public PaginationInMemory sort() {
        // 并行流执行内存排序
        List<T> sortedList = dataList.parallelStream().sorted().collect(Collectors.toList());
        return new PaginationInMemory(sortedList);
    }

    public Page<T> paging(final PageParam pageParam) {
        checkArgument(nonNull(pageParam), "分页参数为空");
        List<T> pagingList = dataList.stream()
                .skip(firstNonNull(pageParam.getOffset(), 0L))
                .limit(pageParam.getLimit())
                .collect(Collectors.toList());
        return Page.newOne(pagingList, dataList.size());
    }
}
