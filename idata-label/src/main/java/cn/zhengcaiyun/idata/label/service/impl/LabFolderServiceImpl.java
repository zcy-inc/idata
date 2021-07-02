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
package cn.zhengcaiyun.idata.label.service.impl;

import cn.zhengcaiyun.idata.label.dal.dao.LabFolderDao;
import cn.zhengcaiyun.idata.label.dal.model.LabFolder;
import cn.zhengcaiyun.idata.label.dto.LabFolderDto;
import cn.zhengcaiyun.idata.label.dto.LabFolderTreeNodeDto;
import cn.zhengcaiyun.idata.label.enums.FolderTreeNodeTypeEnum;
import cn.zhengcaiyun.idata.label.service.LabFolderService;
import cn.zhengcaiyun.idata.label.service.folder.LabFolderManager;
import cn.zhengcaiyun.idata.label.service.folder.LabFolderTreeNodeSupplier;
import cn.zhengcaiyun.idata.label.service.folder.LabFolderTreeNodeSupplierFactory;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.MultimapBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_NO;
import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_YES;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * @description: 文件夹接口实现
 * @author: yangjianhua
 * @create: 2021-06-21 16:11
 **/
@Service
public class LabFolderServiceImpl implements LabFolderService {

    private final LabFolderDao labFolderDao;
    private final LabFolderManager labFolderManager;

    @Autowired
    public LabFolderServiceImpl(LabFolderDao labFolderDao,
                                LabFolderManager labFolderManager) {
        checkNotNull(labFolderDao, "labFolderDao must not be null.");
        checkNotNull(labFolderManager, "labFolderManager must not be null.");
        this.labFolderDao = labFolderDao;
        this.labFolderManager = labFolderManager;
    }

    @Override
    public Long createFolder(LabFolderDto labFolderDto, String operator) {
        LabFolder existFolder = labFolderManager.getFolder(labFolderDto.getName());
        checkState(existFolder == null, "文件夹名称已存在");
        Optional.ofNullable(labFolderDto.getParentId())
                .ifPresent(parentId -> labFolderManager.getFolder(parentId, "父文件夹不存在"));

        LabFolder labFolder = newCreatedFolder(labFolderDto, operator);
        labFolderDao.insertSelective(labFolder);
        return labFolder.getId();
    }

    @Override
    public Long editFolder(LabFolderDto labFolderDto, String operator) {
        LabFolder folder = labFolderManager.getFolder(labFolderDto.getId(), "文件夹不存在");
        LabFolder checkNameFolder = labFolderManager.getFolder(labFolderDto.getName());
        if (!Objects.isNull(checkNameFolder)) {
            checkState(Objects.equals(folder.getId(), checkNameFolder.getId()), "文件夹名称已存在");
        }
        Optional.ofNullable(labFolderDto.getParentId())
                .ifPresent(parentId -> labFolderManager.getFolder(parentId, "父文件夹不存在"));

        LabFolder updateFolder = newEditedFolder(labFolderDto, operator);
        labFolderDao.updateByPrimaryKeySelective(updateFolder);
        return updateFolder.getId();
    }

    @Override
    public LabFolderDto getFolder(Long id) {
        LabFolder folder = labFolderManager.getFolder(id, "文件夹不存在");
        LabFolderDto dto = new LabFolderDto();
        BeanUtils.copyProperties(folder, dto);
        return dto;
    }

    @Override
    public Boolean deleteFolder(Long id, String operator) {
        LabFolder folder = labFolderManager.getFolder(id, "文件夹不存在");
        //已删除
        if (folder.getDel().equals(DEL_YES.val)) return true;

        //存在子文件夹或文件，则不能删除
        List<LabFolder> subFolders = labFolderManager.querySubFolders(id);
        checkState(CollectionUtils.isEmpty(subFolders), "不能删除该文件夹");
        LabFolderTreeNodeSupplier supplier = LabFolderTreeNodeSupplierFactory.getSupplier(folder.getBelong());
        Optional.ofNullable(supplier)
                .ifPresent(sup -> checkState(!sup.hasSubTreeNode(id), "不能删除该文件夹"));

        // 软删除，修改del状态
        labFolderManager.deleteFolder(id, operator);
        return Boolean.TRUE;
    }

    @Override
    public List<LabFolderDto> getFolders(String belong) {
        if (isEmpty(belong)) return Lists.newArrayList();

        List<LabFolder> folders = labFolderManager.queryFolders(belong);
        List<LabFolderDto> folderDtoList = folders.stream().map(folder -> {
            LabFolderDto folderDto = new LabFolderDto();
            BeanUtils.copyProperties(folder, folderDto);
            return folderDto;
        }).collect(Collectors.toList());
        return folderDtoList;
    }

    @Override
    public List<LabFolderTreeNodeDto> getFolderTree(String belong) {
        if (isEmpty(belong)) return Lists.newArrayList();
        List<LabFolder> folders = labFolderManager.queryFolders(belong);
        if (CollectionUtils.isEmpty(folders)) return Lists.newArrayList();

        List<LabFolderTreeNodeDto> treeNodeDtoList = Lists.newArrayListWithCapacity(256);
        treeNodeDtoList.addAll(convertTreeNodes(folders));
        treeNodeDtoList.addAll(getBizEntityTreeNodes(belong));
        return makeTree(treeNodeDtoList);
    }

    private List<LabFolderTreeNodeDto> makeTree(List<LabFolderTreeNodeDto> nodeDtoList) {
        if (CollectionUtils.isEmpty(nodeDtoList)) return Lists.newArrayList();

        ListMultimap<Long, LabFolderTreeNodeDto> treeListMultimap = MultimapBuilder.treeKeys().arrayListValues().build();
        nodeDtoList.stream().forEach(nodeDto -> treeListMultimap.put(nodeDto.getParentId(), nodeDto));

        // 最上层文件夹parent id 为0
        List<LabFolderTreeNodeDto> treeList = makeTree(0L, treeListMultimap);
        return treeList == null ? Lists.newArrayList() : treeList;
    }

    private List<LabFolderTreeNodeDto> makeTree(Long parentId, ListMultimap<Long, LabFolderTreeNodeDto> listMultimap) {
        List<LabFolderTreeNodeDto> nodeDtoList = listMultimap.get(parentId);
        if (CollectionUtils.isEmpty(nodeDtoList)) return null;

        for (LabFolderTreeNodeDto nodeDto : nodeDtoList) {
            if (FolderTreeNodeTypeEnum.FOLDER.getCode().equals(nodeDto.getType()))
                nodeDto.setChildren(makeTree(nodeDto.getId(), listMultimap));
        }
        return nodeDtoList;
    }

    private List<LabFolderTreeNodeDto> getBizEntityTreeNodes(String belong) {
        LabFolderTreeNodeSupplier supplier = LabFolderTreeNodeSupplierFactory.getSupplier(belong);
        return supplier == null ? Lists.newArrayList() : supplier.get();
    }

    private List<LabFolderTreeNodeDto> convertTreeNodes(List<LabFolder> folders) {
        if (CollectionUtils.isEmpty(folders)) return Lists.newArrayList();
        return folders.stream()
                .map(folder -> convertTreeNode(folder))
                .collect(Collectors.toList());
    }

    private LabFolderTreeNodeDto convertTreeNode(LabFolder folder) {
        LabFolderTreeNodeDto nodeDto = new LabFolderTreeNodeDto();
        nodeDto.setId(folder.getId());
        nodeDto.setName(folder.getName());
        nodeDto.setType(FolderTreeNodeTypeEnum.FOLDER.getCode());
        nodeDto.setBelong(folder.getBelong());
        nodeDto.setParentId(folder.getParentId());
        nodeDto.setCid(nodeDto.getType() + "_" + nodeDto.getId());
        return nodeDto;
    }

    private LabFolder newCreatedFolder(LabFolderDto labFolderDto, String operator) {
        LabFolder labFolder = new LabFolder();
        labFolder.setName(labFolderDto.getName());
        labFolder.setParentId(MoreObjects.firstNonNull(labFolderDto.getParentId(), 0L));
        labFolder.setBelong(labFolderDto.getBelong());
        labFolder.setCreator(operator);
        labFolder.setEditor(operator);
        labFolder.setDel(DEL_NO.val);
        return labFolder;
    }

    private LabFolder newEditedFolder(LabFolderDto labFolderDto, String operator) {
        LabFolder labFolder = new LabFolder();
        labFolder.setId(labFolderDto.getId());
        labFolder.setName(labFolderDto.getName());
        labFolder.setParentId(MoreObjects.firstNonNull(labFolderDto.getParentId(), 0L));
        labFolder.setEditor(operator);
        return labFolder;
    }
}
