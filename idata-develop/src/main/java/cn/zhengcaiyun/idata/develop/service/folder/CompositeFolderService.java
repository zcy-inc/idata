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

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.develop.condition.tree.DevTreeCondition;
import cn.zhengcaiyun.idata.develop.dto.folder.CompositeFolderDto;
import cn.zhengcaiyun.idata.develop.dto.tree.DevTreeNodeDto;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-17 18:07
 **/
public interface CompositeFolderService {

    List<DevTreeNodeDto> getFunctionTree();

    List<DevTreeNodeDto> searchDevTree(DevTreeCondition condition, Long userId);

    Long addFolder(CompositeFolderDto folderDto, Operator operator);

    Boolean editFolder(CompositeFolderDto folderDto, Operator operator);

    CompositeFolderDto getFolder(Long id);

    Boolean removeFolder(Long id, Operator operator);

    List<CompositeFolderDto> getFolders(String belong);
}
