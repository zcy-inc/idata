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
package cn.zhengcaiyun.idata.develop.service.folder;

import cn.zhengcaiyun.idata.develop.dal.model.DevTableInfo;
import cn.zhengcaiyun.idata.develop.dto.folder.DevelopFolderDto;
import cn.zhengcaiyun.idata.develop.dto.folder.DevelopFolderTreeNodeDto;

import java.util.List;
import java.util.Set;

/**
 * @author caizhedong
 * @date 2021-05-25 15:20
 */

public interface DevFolderService {
    List<DevelopFolderTreeNodeDto> getDevelopFolderTree(String devFolderType, String treeNodeName);
    List<DevelopFolderDto> getDevelopFolders(String folderName);
    DevelopFolderDto create(DevelopFolderDto developFolderDto, String operator);
    DevelopFolderDto edit(DevelopFolderDto developFolderDto, String operator);
    Set<String> getUserTableFolderIds(Long userId);
    Set<String> getUserMeasureFolderIds(Long userId);
    boolean delete(Long devFolderId, String operator);
    boolean checkMeasureResAccess(Long userId, String folderId, String accessType);
}
