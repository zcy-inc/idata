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
package cn.zhengcaiyun.idata.develop.service;

import cn.zhengcaiyun.idata.dto.develop.folder.DevFolderDto;
import cn.zhengcaiyun.idata.dto.develop.folder.FolderTreeNodeDto;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-05-25 15:20
 */

public interface DevFolderService {
    List<FolderTreeNodeDto> getDevFolderTree(String devFolderType);
    DevFolderDto create(DevFolderDto devFolderDto, String creator);
    DevFolderDto edit(DevFolderDto devFolderDto, String editor);
    boolean delete(Long devFolderId, String editor);
}
