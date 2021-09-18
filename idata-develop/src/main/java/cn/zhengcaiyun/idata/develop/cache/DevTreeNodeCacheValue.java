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

package cn.zhengcaiyun.idata.develop.cache;

import cn.zhengcaiyun.idata.develop.constant.enums.FunctionModuleEnum;
import cn.zhengcaiyun.idata.develop.dto.tree.DevTreeNodeDto;
import com.google.common.collect.ImmutableList;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-18 16:04
 **/
public class DevTreeNodeCacheValue {
    private final FunctionModuleEnum moduleEnum;
    private final ImmutableList<DevTreeNodeDto> folders;
    private final ImmutableList<DevTreeNodeDto> records;

    public DevTreeNodeCacheValue(FunctionModuleEnum moduleEnum,
                                 ImmutableList<DevTreeNodeDto> folders,
                                 ImmutableList<DevTreeNodeDto> records) {
        this.moduleEnum = moduleEnum;
        this.folders = folders;
        this.records = records;
    }

    public FunctionModuleEnum getModuleEnum() {
        return moduleEnum;
    }

    public ImmutableList<DevTreeNodeDto> getFolders() {
        return folders;
    }

    public ImmutableList<DevTreeNodeDto> getRecords() {
        return records;
    }
}
