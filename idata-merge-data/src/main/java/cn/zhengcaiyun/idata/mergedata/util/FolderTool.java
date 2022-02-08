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

package cn.zhengcaiyun.idata.mergedata.util;

import cn.zhengcaiyun.idata.develop.dal.model.folder.CompositeFolder;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-11 17:02
 **/
public class FolderTool {

    public static Optional<CompositeFolder> findFolder(Long oldFolderId, List<CompositeFolder> folderList, String module) {
        String namePrefix = IdPadTool.padId(oldFolderId.toString()) + "#_";
        if (StringUtils.isNotBlank(module)) {
            namePrefix = namePrefix + module + "#_";
        }
        String finalNamePrefix = namePrefix;
        return folderList.stream()
                .filter(compositeFolder -> compositeFolder.getName().startsWith(finalNamePrefix))
                .findFirst();
    }
}