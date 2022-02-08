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
package cn.zhengcaiyun.idata.label.service;

import cn.zhengcaiyun.idata.label.dto.LabFolderDto;
import cn.zhengcaiyun.idata.label.dto.LabFolderTreeNodeDto;

import java.util.List;

/**
 * @description: 文件夹service
 * @author: yangjianhua
 * @create: 2021-06-21 15:38
 **/
public interface LabFolderService {
    /**
     * 创建文件夹
     *
     * @param labFolderDto
     * @param operator
     * @returns
     */
    Long createFolder(LabFolderDto labFolderDto, String operator);

    /**
     * 编辑文件夹
     *
     * @param labFolderDto
     * @param operator
     * @return
     */
    Long editFolder(LabFolderDto labFolderDto, String operator);

    /**
     * 获取指定文件夹
     *
     * @param id
     * @return
     */
    LabFolderDto getFolder(Long id);

    /**
     * 删除文件夹
     *
     * @param id
     * @param operator
     * @return
     */
    Boolean deleteFolder(Long id, String operator);

    /**
     * 获取文件夹list
     *
     * @return
     */
    List<LabFolderDto> getFolders(String belong);

    /**
     * 获取文件树
     *
     * @param belong
     * @return
     */
    List<LabFolderTreeNodeDto> getFolderTree(String belong, Long userId);

    /**
     * 校验文件夹权限
     *
     * @param userId
     * @param folderId
     * @return
     */
    Boolean checkResAccess(Long userId, String folderId);
}
