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
package cn.zhengcaiyun.idata.develop.spi;

import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.folder.CompositeFolderDao;
import cn.zhengcaiyun.idata.develop.dal.model.folder.CompositeFolder;
import cn.zhengcaiyun.idata.develop.dal.repo.folder.CompositeFolderRepo;
import cn.zhengcaiyun.idata.user.spi.DevFolderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.folder.CompositeFolderDynamicSqlSupport.compositeFolder;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author caizhedong
 * @date 2021-12-27 下午5:38
 */


public class DevFolderServiceImpl implements DevFolderService {

    @Autowired
    private CompositeFolderDao compositeFolderDao;

    @Override
    public List<String> getDevParentFolderIds(String folderId, List<String> folderIds) {
        List<String> parentFolderIdList = new ArrayList<>(compositeFolderDao.select(c ->
                c.where(compositeFolder.del, isEqualTo(DeleteEnum.DEL_NO.val),
                        and(compositeFolder.id, isEqualTo(Long.valueOf(folderId))),
                        and(compositeFolder.parentId, isNotEqualTo(0L)))))
                .stream().map(folder -> String.valueOf(folder.getParentId())).collect(Collectors.toList());
        folderIds.addAll(parentFolderIdList);
        for (String parentFolderId : parentFolderIdList) {
            folderIds = getDevParentFolderIds(parentFolderId, folderIds);
        }
        return folderIds;
    }
}
