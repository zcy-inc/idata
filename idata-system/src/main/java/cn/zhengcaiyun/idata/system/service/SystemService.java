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
package cn.zhengcaiyun.idata.system.service;

import cn.zhengcaiyun.idata.commons.dto.BaseTreeNodeDto;
import cn.zhengcaiyun.idata.system.dto.FeatureTreeNodeDto;
import cn.zhengcaiyun.idata.system.dto.FolderTreeNodeDto;
import cn.zhengcaiyun.idata.system.dto.SystemStateDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author shiyin
 * @date 2021-03-13 22:53
 */
public interface SystemService {

    SystemStateDto getSystemState();

    enum FeatureTreeMode {
        FULL, // 返回全部节点功能树
        FULL_ENABLE, // 返回全部节点功能树并且全部使能
        PART // 只返回使能节点相关的功能树
    }
    List<FeatureTreeNodeDto> getFeatureTree(FeatureTreeMode mode, Set<String> enableFeatureCodes);

    /**
     * @param folderPermissionMap key：resource type + access key; value: filePermission
     */
    List<FolderTreeNodeDto> getDevFolderTree(Map<String, Integer> folderPermissionMap);
}
