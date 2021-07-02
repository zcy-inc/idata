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
package cn.zhengcaiyun.idata.label.service.folder;

import cn.zhengcaiyun.idata.label.dal.dao.LabFolderDao;
import cn.zhengcaiyun.idata.label.dal.model.LabFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_NO;
import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_YES;
import static cn.zhengcaiyun.idata.label.dal.dao.LabFolderDynamicSqlSupport.labFolder;
import static com.google.common.base.Preconditions.*;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.and;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description: 提供事务（避免一些事务直接加在service层，造成"大事务"）方法或基础、可复用的方法，想不到好点的名称，暂时取名manager
 * @author: yangjianhua
 * @create: 2021-06-23 13:49
 **/
@Component
public class LabFolderManager {

    private final LabFolderDao folderDao;

    @Autowired
    public LabFolderManager(LabFolderDao folderDao) {
        checkNotNull(folderDao, "folderDao must not be null.");
        this.folderDao = folderDao;
    }

    public List<LabFolder> queryFolders(String belong) {
        return folderDao.select(
                dsl -> dsl.where(labFolder.belong, isEqualTo(belong),
                        and(labFolder.del, isEqualTo(DEL_NO.val))));
    }

    public LabFolder getFolder(String folderName) {
        checkArgument(isNotEmpty(folderName), "文件夹名称不能为空");
        LabFolder folder = folderDao.selectOne(
                dsl -> dsl.where(labFolder.name, isEqualTo(folderName),
                        and(labFolder.del, isEqualTo(DEL_NO.val))))
                .orElse(null);
        return folder;
    }

    public LabFolder getFolder(Long folderId, String errorMsg) {
        checkArgument(folderId != null, "文件夹编号不能为空");
        Optional<LabFolder> folderOptional = folderDao.selectByPrimaryKey(folderId);
        checkState(folderOptional.isPresent(), errorMsg);
        return folderOptional.get();
    }

    public int deleteFolder(Long folderId, String operator) {
        return folderDao.update(dsl -> dsl.set(labFolder.del).equalTo(DEL_YES.val)
                .set(labFolder.editor).equalTo(operator).where(labFolder.id, isEqualTo(folderId)));
    }

    public List<LabFolder> querySubFolders(Long folderId) {
        return folderDao.select(
                dsl -> dsl.where(labFolder.parentId, isEqualTo(folderId),
                        and(labFolder.del, isEqualTo(DEL_NO.val))));
    }
}
