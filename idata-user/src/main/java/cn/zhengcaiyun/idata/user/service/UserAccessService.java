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
package cn.zhengcaiyun.idata.user.service;

import cn.zhengcaiyun.idata.system.dto.FeatureTreeNodeDto;
import cn.zhengcaiyun.idata.system.dto.FolderTreeNodeDto;

import java.util.List;

/**
 * @author shiyin
 * @date 2021-03-30 20:22
 */
public interface UserAccessService {
    List<FeatureTreeNodeDto> getUserFeatureTree(Long userId);
    List<FolderTreeNodeDto> getUserFolderTree(Long userId);
    List<String> getAccessKeys(Long userId, String accessType);
    boolean checkAccess(Long userId, String accessCode);
    boolean checkAccess(Long userId, List<String> accessTypes, String accessKey);
}
