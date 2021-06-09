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
package cn.zhengcaiyun.idata.develop.service.impl;

import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.develop.dal.dao.*;
import cn.zhengcaiyun.idata.develop.dal.model.DevFolder;
import cn.zhengcaiyun.idata.develop.service.DevFolderService;
import cn.zhengcaiyun.idata.develop.dto.folder.DevelopFolderDto;
import cn.zhengcaiyun.idata.develop.dto.folder.DevelopFolderTreeNodeDto;
import cn.zhengcaiyun.idata.develop.dto.folder.DevelopTreeTypeEnum;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevFolderDynamicSqlSupport.devFolder;
import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author caizhedong
 * @date 2021-06-01 15:25
 */

@Service
public class DevFolderServiceImpl implements DevFolderService {

    @Autowired
    private DevFolderDao devFolderDao;
    @Autowired
    private DevFolderMyDao devFolderMyDao;

    @Override
    public List<DevelopFolderTreeNodeDto> getDevelopFolderTree(String devTreeType) {
        List<DevelopFolderTreeNodeDto> folderTreeList = devFolderMyDao.getDevelopFolders(devTreeType);
        return getFolderTreeNodeList(null, true, folderTreeList);
    }

    private List<DevelopFolderTreeNodeDto> getFolderTreeNodeList(Long parentId, boolean isRecursive,
                                                                 List<DevelopFolderTreeNodeDto> folderTreeNodeList) {
        List<DevelopFolderTreeNodeDto> echo = folderTreeNodeList.stream()
                .filter(folderTreeNode -> parentId == folderTreeNode.getParentId())
                .map(folderTreeNode -> {
                    DevelopFolderTreeNodeDto echoFolderTreeNode = PojoUtil.copyOne(folderTreeNode, DevelopFolderTreeNodeDto.class,
                            "type", "name", "cid", "fileCode", "folderId");
                    if (isRecursive && DevelopTreeTypeEnum.FOLDER.name().equals(folderTreeNode.getType())) {
                        echoFolderTreeNode.setChildren(getFolderTreeNodeList(folderTreeNode.getFolderId(), true, folderTreeNodeList));
                    }
                    return echoFolderTreeNode;
                }).collect(Collectors.toList());
        return echo;
    }

    @Override
    public List<DevelopFolderDto> getDevelopFolders(String folderName) {
        var builder = select(devFolder.allColumns()).from(devFolder).where(devFolder.del, isNotEqualTo(1));
        if (isNotEmpty(folderName)) {
            builder.and(devFolder.folderName, isLike("%" + folderName + "%"));
        }

        return PojoUtil.copyList(devFolderDao.selectMany(builder.build().render(RenderingStrategies.MYBATIS3)),
                DevelopFolderDto.class);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public DevelopFolderDto create(DevelopFolderDto developFolderDto, String operator) {
        checkArgument(isNotEmpty(operator), "创建者不能为空");
        checkArgument(isNotEmpty(developFolderDto.getFolderName()), "文件夹名不能为空");
        DevFolder checkFolder = devFolderDao.selectOne(c ->
                c.where(devFolder.del, isNotEqualTo(1), and(devFolder.folderName,
                        isEqualTo(developFolderDto.getFolderName()))))
                .orElse(null);
        checkArgument(checkFolder == null, "文件夹名称已存在");

        developFolderDto.setCreator(operator);
        DevFolder folder = PojoUtil.copyOne(developFolderDto, DevFolder.class);
        devFolderDao.insertSelective(folder);
        return PojoUtil.copyOne(devFolderDao.selectByPrimaryKey(folder.getId()).get(), DevelopFolderDto.class);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public DevelopFolderDto edit(DevelopFolderDto developFolderDto, String operator) {
        checkArgument(isNotEmpty(operator), "修改者不能为空");
        checkArgument(developFolderDto.getId() != null, "文件夹ID不能为空");
        DevFolder checkFolder = devFolderDao.selectOne(c ->
                c.where(devFolder.del, isNotEqualTo(1), and(devFolder.id, isEqualTo(developFolderDto.getId()))))
                .orElse(null);
        checkArgument(checkFolder != null, "文件夹不存在");

        developFolderDto.setEditor(operator);
        DevFolder folder = PojoUtil.copyOne(developFolderDto, DevFolder.class);
        devFolderDao.updateByPrimaryKeySelective(folder);
        return PojoUtil.copyOne(devFolderDao.selectByPrimaryKey(folder.getId()).get(), DevelopFolderDto.class);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean delete(Long devFolderId, String operator) {
        checkArgument(isNotEmpty(operator), "删除者不能为空");
        checkArgument(devFolderId != null, "文件夹ID不能为空");
        DevFolder checkFolder = devFolderDao.selectOne(c ->
                c.where(devFolder.del, isNotEqualTo(1), and(devFolder.id, isEqualTo(devFolderId))))
                .orElse(null);
        checkArgument(checkFolder != null, "文件夹不存在");

        devFolderDao.update(c -> c.set(devFolder.del).equalTo(1)
                .set(devFolder.editor).equalTo(operator)
                .where(devFolder.id, isEqualTo(devFolderId)));
        return true;
    }
}
