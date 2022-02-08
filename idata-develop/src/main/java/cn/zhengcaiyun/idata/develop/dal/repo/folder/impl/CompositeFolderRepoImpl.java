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

package cn.zhengcaiyun.idata.develop.dal.repo.folder.impl;

import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.commons.enums.FolderTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.FunctionModuleEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.folder.CompositeFolderDao;
import cn.zhengcaiyun.idata.develop.dal.model.folder.CompositeFolder;
import cn.zhengcaiyun.idata.develop.dal.repo.folder.CompositeFolderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_YES;
import static cn.zhengcaiyun.idata.develop.dal.dao.folder.CompositeFolderDynamicSqlSupport.compositeFolder;
import static org.mybatis.dynamic.sql.SqlBuilder.and;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-17 18:07
 **/
@Repository
public class CompositeFolderRepoImpl implements CompositeFolderRepo {

    private final CompositeFolderDao compositeFolderDao;

    @Autowired
    public CompositeFolderRepoImpl(CompositeFolderDao compositeFolderDao) {
        this.compositeFolderDao = compositeFolderDao;
    }

    @Override
    public List<CompositeFolder> queryFunctionFolder() {
        return queryGeneralFolder(FolderTypeEnum.FUNCTION);
    }

    @Override
    public List<CompositeFolder> queryGeneralFolder() {
        return queryGeneralFolder(FolderTypeEnum.FOLDER);
    }

    @Override
    public List<CompositeFolder> queryAllFolder() {
        return compositeFolderDao.select(dsl -> dsl.where(compositeFolder.del, isEqualTo(DeleteEnum.DEL_NO.val))
                .orderBy(compositeFolder.id));
    }

    @Override
    public List<CompositeFolder> queryFolder(FunctionModuleEnum moduleEnum) {
        return compositeFolderDao.select(dsl -> dsl.where(
                        compositeFolder.belong, isEqualTo(moduleEnum.code),
                        and(compositeFolder.del, isEqualTo(DeleteEnum.DEL_NO.val)))
                .orderBy(compositeFolder.id));
    }

    @Override
    public List<CompositeFolder> querySubFolder(Long parentId) {
        return compositeFolderDao.select(dsl -> dsl.where(compositeFolder.parentId, isEqualTo(parentId),
                        and(compositeFolder.del, isEqualTo(DeleteEnum.DEL_NO.val)))
                .orderBy(compositeFolder.id));
    }

    @Override
    public Long createFolder(CompositeFolder folder) {
        int ret = compositeFolderDao.insertSelective(folder);
        if (ret <= 0) return null;
        return folder.getId();
    }

    @Override
    public Boolean updateFolder(CompositeFolder folder) {
        int ret = compositeFolderDao.updateByPrimaryKeySelective(folder);
        return ret > 0;
    }

    @Override
    public Boolean deleteFolder(Long id, String operator) {
        int ret = compositeFolderDao.update(dsl -> dsl.set(compositeFolder.del).equalTo(DEL_YES.val)
                .set(compositeFolder.editor).equalTo(operator)
                .where(compositeFolder.id, isEqualTo(id)));
        return ret > 0;
    }

    @Override
    public Optional<CompositeFolder> queryFolder(Long id) {
        return compositeFolderDao.selectByPrimaryKey(id);
    }

    @Override
    public Optional<CompositeFolder> queryFolder(String name, Long parentId) {
        return compositeFolderDao.selectOne(dsl -> dsl.where(compositeFolder.name, isEqualTo(name),
                        and(compositeFolder.parentId, isEqualTo(parentId)),
                        and(compositeFolder.del, isEqualTo(DeleteEnum.DEL_NO.val)))
                .orderBy(compositeFolder.id.descending())
                .limit(1));
    }

    private List<CompositeFolder> queryGeneralFolder(FolderTypeEnum typeEnum) {
        return compositeFolderDao.select(dsl -> dsl.where(
                        compositeFolder.type, isEqualTo(typeEnum.name()),
                        and(compositeFolder.del, isEqualTo(DeleteEnum.DEL_NO.val)))
                .orderBy(compositeFolder.id));
    }
}
