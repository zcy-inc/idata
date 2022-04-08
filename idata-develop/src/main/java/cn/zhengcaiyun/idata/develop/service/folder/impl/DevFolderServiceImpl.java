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
package cn.zhengcaiyun.idata.develop.service.folder.impl;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.enums.FolderTypeEnum;
import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.develop.dal.dao.*;
import cn.zhengcaiyun.idata.develop.dal.model.DevEnum;
import cn.zhengcaiyun.idata.develop.dal.model.DevFolder;
import cn.zhengcaiyun.idata.develop.dal.model.DevLabelDefine;
import cn.zhengcaiyun.idata.develop.dal.model.DevTableInfo;
import cn.zhengcaiyun.idata.develop.dal.model.folder.CompositeFolder;
import cn.zhengcaiyun.idata.develop.dal.repo.folder.CompositeFolderRepo;
import cn.zhengcaiyun.idata.develop.dto.folder.DevelopFolderDto;
import cn.zhengcaiyun.idata.develop.dto.folder.DevelopFolderTreeNodeDto;
import cn.zhengcaiyun.idata.develop.dto.folder.DevelopTreeTypeEnum;
import cn.zhengcaiyun.idata.develop.dto.label.LabelTagEnum;
import cn.zhengcaiyun.idata.develop.service.folder.DevFolderService;
import cn.zhengcaiyun.idata.system.dto.ResourceTypeEnum;
import cn.zhengcaiyun.idata.user.dto.UserInfoDto;
import cn.zhengcaiyun.idata.user.service.UserAccessService;
import cn.zhengcaiyun.idata.user.service.UserManagerService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumDynamicSqlSupport.devEnum;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevFolderDynamicSqlSupport.devFolder;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDynamicSqlSupport.devLabelDefine;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDynamicSqlSupport.folderId;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDynamicSqlSupport.devTableInfo;
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
    private DevLabelDefineDao devLabelDefineDao;
    @Autowired
    private DevFolderMyDao devFolderMyDao;
    @Autowired
    private DevEnumDao devEnumDao;
    @Autowired
    private DevTableInfoDao devTableInfoDao;
    @Autowired
    private UserAccessService userAccessService;
    @Autowired
    private UserManagerService userManagerService;
    @Autowired
    private CompositeFolderRepo compositeFolderRepo;

    @Override
    public List<DevelopFolderTreeNodeDto> getDevelopFolderTree(String devTreeType, String treeNodeName) {
        Set<Long> accessFolderIdList = getUserMeasureFolderIds(OperatorContext.getCurrentOperator().getId(), devTreeType);
        if (accessFolderIdList.size() == 0) return Lists.newArrayList();

        var builder = select(devFolder.allColumns()).from(devFolder)
                .where(devFolder.del, isNotEqualTo(1), and(devFolder.id, isIn(accessFolderIdList)));
        if (StringUtils.isNotEmpty(devTreeType)) {
            builder.and(devFolder.folderType, isEqualTo(devTreeType));
        }
        if (StringUtils.isNotEmpty(treeNodeName)) {
            builder.and(devFolder.folderName, isLike(treeNodeName));
        }
        // 取用户的上下级所有folderId
        List<DevFolder> folderList = devFolderDao.selectMany(builder.build().render(RenderingStrategies.MYBATIS3));
        List<DevelopFolderTreeNodeDto> folderTreeList = folderList.stream().map(folder -> {
            DevelopFolderTreeNodeDto folderTreeNode = new DevelopFolderTreeNodeDto();
            folderTreeNode.setName(folder.getFolderName());
            folderTreeNode.setParentId(folder.getParentId());
            folderTreeNode.setFolderId(folder.getId());
            folderTreeNode.setType("FOLDER");
            folderTreeNode.setCid("F_" + folder.getId());
            folderTreeNode.setFolderType(folder.getFolderType());
            return folderTreeNode;
        }).collect(Collectors.toList());

        return getFolderTreeNodeList(0L, true, treeNodeName, folderTreeList).stream()
                .filter(record -> record.getFolderType() == null || record.getFolderType().equals(devTreeType))
                .collect(Collectors.toList());
    }

    private List<DevelopFolderTreeNodeDto> getFolderTreeNodeList(Long parentId, boolean isRecursive, String treeNodeName,
                                                                 List<DevelopFolderTreeNodeDto> folderTreeNodeList) {
        List<DevelopFolderTreeNodeDto> echo = folderTreeNodeList.stream()
                .filter(folderTreeNode -> parentId.equals(folderTreeNode.getParentId())).collect(Collectors.toList())
                .stream().map(folderTreeNode -> {
                    DevelopFolderTreeNodeDto echoFolderTreeNode = PojoUtil.copyOne(folderTreeNode, DevelopFolderTreeNodeDto.class,
                            "type", "name", "cid", "fileCode", "folderId", "folderType");
                    if (isRecursive && DevelopTreeTypeEnum.FOLDER.name().equals(folderTreeNode.getType())) {
                        echoFolderTreeNode.setChildren(getFolderTreeNodeList(folderTreeNode.getFolderId(), true,
                                treeNodeName, folderTreeNodeList));
                    }
                    return echoFolderTreeNode;
                }).collect(Collectors.toList());
        if (echo.size() == 0) return null;

        if (StringUtils.isEmpty(treeNodeName)) {
            return echo;
        }
        else {
            return echo.stream().filter(developFolderTreeNode ->
                    developFolderTreeNode.getChildren() == null || developFolderTreeNode.getChildren().size() > 0)
                    .collect(Collectors.toList());
        }
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
        var builder = select(devFolder.allColumns()).from(devFolder).where(devFolder.del, isNotEqualTo(1))
                .and(devFolder.folderName, isEqualTo(developFolderDto.getFolderName()));
        if (developFolderDto.getParentId() != null) {
            builder.and(devFolder.parentId, isEqualTo(developFolderDto.getParentId()));
        }
        DevFolder checkFolder = devFolderDao.selectOne(builder.build().render(RenderingStrategies.MYBATIS3)).orElse(null);
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
        checkArgument(!checkFolder.getId().equals(developFolderDto.getParentId()), "父文件夹ID有误");

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
        List<DevFolder> childrenFolderList = devFolderDao.select(c ->
                c.where(devFolder.del, isNotEqualTo(1), and(devFolder.parentId, isEqualTo(devFolderId))));
        checkArgument(childrenFolderList == null || childrenFolderList.size() == 0, "文件夹下存在文件夹，不能删除");
        List<DevLabelDefine> childrenLabelList = devLabelDefineDao.select(c ->
                c.where(devLabelDefine.del, isNotEqualTo(1), and(devLabelDefine.folderId, isEqualTo(devFolderId))));
        List<DevEnum> childrenEnumList = devEnumDao.select(c ->
                c.where(devEnum.del, isNotEqualTo(1), and(devEnum.folderId, isEqualTo(devFolderId))));
        List<DevTableInfo> childrenTableList = devTableInfoDao.select(c ->
                c.where(devTableInfo.del, isNotEqualTo(1), and(devTableInfo.folderId, isEqualTo(devFolderId))));
        checkArgument((childrenLabelList == null || childrenLabelList.size() == 0)
                && (childrenEnumList == null || childrenEnumList.size() == 0)
                && (childrenTableList == null || childrenTableList.size() == 0), "文件夹下存在文件，不能删除");

        devFolderDao.update(c -> c.set(devFolder.del).equalTo(1)
                .set(devFolder.editor).equalTo(operator)
                .where(devFolder.id, isEqualTo(devFolderId)));
        return true;
    }

    @Override
    public boolean checkMeasureResAccess(Long userId, String folderId, String accessType) {
        if (!"0".equals(folderId)) {
            if (!userAccessService.checkAccess(userId, Arrays.asList(accessType),
                    folderId)) {
                return checkMeasureResAccess(userId, String.valueOf(getParentFolderId(Long.valueOf(folderId))), accessType);
            }
            else {
                return true;
            }
        }
        return userAccessService.checkAccess(userId, Arrays.asList(accessType), folderId);
    }

    @Override
    public Set<String> getUserTableFolderIds(Long userId) {
        UserInfoDto user = userManagerService.getUserInfo(userId);
        if (1 == user.getSysAdmin() || 2 == user.getSysAdmin()) return getAllTableFolderIds();

        List<String> folderIdList = userAccessService.getAccessKeys(OperatorContext.getCurrentOperator().getId(),
                ResourceTypeEnum.R_DATA_DEVELOP_DIR.name() + "_R");
        folderIdList.addAll(userAccessService.getAccessKeys(OperatorContext.getCurrentOperator().getId(),
                ResourceTypeEnum.R_DATA_DEVELOP_DIR.name() + "_W"));
        folderIdList.addAll(userAccessService.getAccessKeys(OperatorContext.getCurrentOperator().getId(),
                ResourceTypeEnum.R_DATA_DEVELOP_DIR.name() + "_D"));
        if (folderIdList.size() == 0) return new HashSet<>();
        return new HashSet<>(folderIdList);
    }

    @Override
    public Set<Long> getUserMeasureFolderIds(Long userId, String folderType) {
        UserInfoDto user = userManagerService.getUserInfo(userId);
        if (1 == user.getSysAdmin() || 2 == user.getSysAdmin()) return getAllMeasureFolderIds(folderType);

        List<String> folderIdList = userAccessService.getAccessKeys(OperatorContext.getCurrentOperator().getId(),
                ResourceTypeEnum.R_MEASURE_MANAGE_DIR.name() + "_R");
        folderIdList.addAll(userAccessService.getAccessKeys(OperatorContext.getCurrentOperator().getId(),
                ResourceTypeEnum.R_MEASURE_MANAGE_DIR.name() + "_W"));
        folderIdList.addAll(userAccessService.getAccessKeys(OperatorContext.getCurrentOperator().getId(),
                ResourceTypeEnum.R_MEASURE_MANAGE_DIR.name() + "_D"));
        return getSubMeasureFolderIds(new HashSet<>(folderIdList), folderType);
    }

    private Set<String> getAllTableFolderIds() {
        return compositeFolderRepo.queryAllFolder()
                .stream().map(folder -> String.valueOf(folder.getId())).collect(Collectors.toSet());
    }

    private Set<Long> getAllMeasureFolderIds(String folderType) {
        return devFolderDao.select(c -> c.where(devFolder.del, isNotEqualTo(1),
                and(devFolder.folderType, isEqualTo(folderType))))
                .stream().map(DevFolder::getId).collect(Collectors.toSet());
    }

    private Set<Long> getSubMeasureFolderIds(Set<String> folderIds, String folderType) {
        Set<Long> folderIdList = folderIds.stream().map(Long::valueOf).collect(Collectors.toSet());
        // 判断folderIds符合指标类型
        Set<Long> echoFolderIdList = devFolderDao.select(c -> c.where(devFolder.del, isNotEqualTo(1),
                and(devFolder.folderType, isEqualTo(folderType)), and(devFolder.id, isIn(folderIdList))))
                .stream().map(DevFolder::getId).collect(Collectors.toSet());
        if (echoFolderIdList.size() == 0) return new HashSet<>();
        Set<Long> echoList = new HashSet<>(echoFolderIdList);
        Set<Long> parentFolderIdList = getParentFolderIds(echoFolderIdList, folderType);
        Set<Long> childFolderIdList = getChildFolderIds(echoFolderIdList, folderType);
        echoList.addAll(childFolderIdList);
        echoList.addAll(parentFolderIdList);
        return echoList;
    }

    @Override
    public Set<Long> getChildFolderIds(Set<Long> folderIds, String folderType) {
        Set<Long> childFolderIdList = devFolderDao.selectMany(select(devFolder.allColumns())
                .from(devFolder)
                .where(devFolder.del, isNotEqualTo(1), and(devFolder.parentId, isIn(folderIds)),
                        and(devFolder.folderType, isEqualTo(folderType)))
                .build().render(RenderingStrategies.MYBATIS3))
                .stream().map(DevFolder::getId).collect(Collectors.toSet());
        folderIds.addAll(childFolderIdList);
        if (childFolderIdList.size() > 0) {
            folderIds.addAll(getChildFolderIds(childFolderIdList, folderType));
        }
        return folderIds;
    }

    private Set<Long> getParentFolderIds(Set<Long> folderIds,String folderType) {
        Set<Long> parentFolderIdList = devFolderDao.selectMany(select(devFolder.allColumns())
                .from(devFolder)
                .where(devFolder.del, isNotEqualTo(1), and(devFolder.id, isIn(folderIds)),
                        and(devFolder.parentId, isNotEqualTo(0L)), and(devFolder.folderType, isEqualTo(folderType)))
                .build().render(RenderingStrategies.MYBATIS3))
                .stream().map(DevFolder::getParentId).collect(Collectors.toSet());
        folderIds.addAll(parentFolderIdList);
        if (parentFolderIdList.size() > 0) {
            return getParentFolderIds(parentFolderIdList, folderType);
        }
        return folderIds;
    }

    private Long getParentFolderId(Long folderId) {
        DevFolder folder = devFolderDao.selectOne(c -> c.where(devFolder.del, isNotEqualTo(1),
                and(devFolder.id, isEqualTo(folderId)))).orElse(null);
        return folder != null ? folder.getParentId() : 0L;
    }
}
