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

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.FolderTypeEnum;
import cn.zhengcaiyun.idata.commons.util.TreeNodeFilter;
import cn.zhengcaiyun.idata.commons.util.TreeNodeGenerator;
import cn.zhengcaiyun.idata.develop.cache.DevTreeNodeCacheValue;
import cn.zhengcaiyun.idata.develop.cache.DevTreeNodeLocalCache;
import cn.zhengcaiyun.idata.develop.condition.tree.DevTreeCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.FunctionModuleEnum;
import cn.zhengcaiyun.idata.develop.dal.model.folder.CompositeFolder;
import cn.zhengcaiyun.idata.develop.dal.repo.folder.CompositeFolderRepo;
import cn.zhengcaiyun.idata.develop.dto.folder.CompositeFolderDto;
import cn.zhengcaiyun.idata.develop.dto.tree.DevTreeNodeDto;
import cn.zhengcaiyun.idata.develop.service.folder.CompositeFolderService;
import cn.zhengcaiyun.idata.develop.spi.tree.BizTreeNodeSupplier;
import cn.zhengcaiyun.idata.develop.spi.tree.BizTreeNodeSupplierFactory;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_YES;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-17 18:08
 **/
@Service
public class CompositeFolderServiceImpl implements CompositeFolderService {

    private final CompositeFolderRepo compositeFolderRepo;
    private final DevTreeNodeLocalCache devTreeNodeLocalCache;
    private final BizTreeNodeSupplierFactory bizTreeNodeSupplierFactory;

    @Autowired
    public CompositeFolderServiceImpl(CompositeFolderRepo compositeFolderRepo,
                                      DevTreeNodeLocalCache devTreeNodeLocalCache,
                                      BizTreeNodeSupplierFactory bizTreeNodeSupplierFactory) {
        this.compositeFolderRepo = compositeFolderRepo;
        this.devTreeNodeLocalCache = devTreeNodeLocalCache;
        this.bizTreeNodeSupplierFactory = bizTreeNodeSupplierFactory;
    }

    @Override
    public List<DevTreeNodeDto> getFunctionTree() {
        List<CompositeFolder> folderList = compositeFolderRepo.queryFunctionFolder();
        if (ObjectUtils.isEmpty(folderList)) return Lists.newArrayList();

        List<DevTreeNodeDto> nodeDtoList = folderList.stream()
                .map(DevTreeNodeDto::from)
                .collect(Collectors.toList());
        return TreeNodeGenerator.withExpandedNodes(nodeDtoList).makeTree(() -> "0");
    }

    @Override
    public List<DevTreeNodeDto> searchDevTree(DevTreeCondition condition) {
        // 获取选择的功能模块，未选择则默认所有功能模块
        List<FunctionModuleEnum> moduleEnumList = getSelectModuleOrAll(condition.getBelongFunctions());
        // 获取功能模块下的所有文件夹和文件
        List<DevTreeNodeDto> expandedNodeDtoList = getExpandedDevTreeNodes(moduleEnumList);
        // 生成文件树
        List<DevTreeNodeDto> treeNodeDtoList = TreeNodeGenerator.withExpandedNodes(expandedNodeDtoList).makeTree(() -> "0");
        return filterTreeNodes(treeNodeDtoList, condition.getKeyWord());
    }

    @Override
    public Long addFolder(CompositeFolderDto folderDto, Operator operator) {
        Optional<FunctionModuleEnum> moduleEnumOptional = FunctionModuleEnum.getEnum(folderDto.getBelong());
        checkArgument(moduleEnumOptional.isPresent(), "功能文件夹为空");
        Optional<FolderTypeEnum> typeEnumOptional = FolderTypeEnum.getEnum(folderDto.getType());
        checkArgument(typeEnumOptional.isPresent(), "文件夹类型为空");

        checkArgument(Objects.nonNull(folderDto.getParentId()), "父文件夹为空");
        Optional<CompositeFolder> parentFolderOptional = compositeFolderRepo.queryFolder(folderDto.getParentId());
        checkArgument(parentFolderOptional.isPresent(), "父文件夹不存在");

        checkArgument(StringUtils.isNotBlank(folderDto.getName()), "文件夹名称为空");
        Optional<CompositeFolder> dupNameFolderOptional = compositeFolderRepo.queryFolder(folderDto.getName(), folderDto.getParentId());
        checkArgument(dupNameFolderOptional.isPresent(), "父文件夹下存在同名文件夹");

        folderDto.setOperator(operator);
        CompositeFolder folder = folderDto.toModel();
        return compositeFolderRepo.createFolder(folder);
    }

    @Override
    public Boolean editFolder(CompositeFolderDto folderDto, Operator operator) {
        checkArgument(Objects.nonNull(folderDto.getId()), "文件夹编号为空");
        Optional<CompositeFolder> existFolderOptional = compositeFolderRepo.queryFolder(folderDto.getId());
        checkArgument(existFolderOptional.isPresent(), "文件夹不存在");

        Optional<FunctionModuleEnum> moduleEnumOptional = FunctionModuleEnum.getEnum(folderDto.getBelong());
        checkArgument(moduleEnumOptional.isPresent(), "功能文件夹为空");
        Optional<FolderTypeEnum> typeEnumOptional = FolderTypeEnum.getEnum(folderDto.getType());
        checkArgument(typeEnumOptional.isPresent(), "文件夹类型为空");

        checkArgument(Objects.nonNull(folderDto.getParentId()), "父文件夹为空");
        Optional<CompositeFolder> parentFolderOptional = compositeFolderRepo.queryFolder(folderDto.getParentId());
        checkArgument(parentFolderOptional.isPresent(), "父文件夹不存在");

        checkArgument(StringUtils.isNotBlank(folderDto.getName()), "文件夹名称为空");
        Optional<CompositeFolder> dupNameFolderOptional = compositeFolderRepo.queryFolder(folderDto.getName(), folderDto.getParentId());
        if (dupNameFolderOptional.isPresent()) {
            checkArgument(Objects.equals(folderDto.getId(), dupNameFolderOptional.get().getId()), "父文件夹下存在同名文件夹");
        }

        folderDto.resetEditor(operator);
        CompositeFolder folder = folderDto.toModel();
        return compositeFolderRepo.updateFolder(folder);
    }

    @Override
    public CompositeFolderDto getFolder(Long id) {
        checkArgument(Objects.nonNull(id), "文件夹编号为空");
        Optional<CompositeFolder> existFolderOptional = compositeFolderRepo.queryFolder(id);
        checkArgument(existFolderOptional.isPresent(), "文件夹不存在");
        return CompositeFolderDto.from(existFolderOptional.get());
    }

    @Override
    public Boolean removeFolder(Long id, Operator operator) {
        checkArgument(Objects.nonNull(id), "文件夹编号为空");
        Optional<CompositeFolder> existFolderOptional = compositeFolderRepo.queryFolder(id);
        checkArgument(existFolderOptional.isPresent(), "文件夹不存在");
        CompositeFolder folder = existFolderOptional.get();
        //已删除
        if (folder.getDel().equals(DEL_YES.val)) return true;

        //存在子文件夹或文件，则不能删除
        List<CompositeFolder> subFolders = compositeFolderRepo.querySubFolder(id);
        checkState(CollectionUtils.isEmpty(subFolders), "存在子文件夹，不能删除该文件夹");
        Optional<FunctionModuleEnum> moduleEnumOptional = FunctionModuleEnum.getEnum(folder.getBelong());
        checkArgument(moduleEnumOptional.isPresent(), "功能文件夹不存在");
        Optional<BizTreeNodeSupplier> supplierOptional = bizTreeNodeSupplierFactory.getSupplier(moduleEnumOptional.get());
        if (supplierOptional.isEmpty()) return false;
        BizTreeNodeSupplier supplier = supplierOptional.get();
        Long bizNodeCount = supplier.countBizNode(moduleEnumOptional.get(), id);
        checkArgument(bizNodeCount != null && bizNodeCount > 0, "存在子文件，不能删除该文件夹");

        // 软删除，修改del状态
        return compositeFolderRepo.deleteFolder(id, operator.getNickname());
    }

    @Override
    public List<CompositeFolderDto> getFolders(String belong) {
        if (isEmpty(belong)) return Lists.newArrayList();
        Optional<FunctionModuleEnum> moduleEnumOptional = FunctionModuleEnum.getEnum(belong);
        if (moduleEnumOptional.isEmpty()) return Lists.newArrayList();

        List<CompositeFolder> folderList = compositeFolderRepo.queryFolder(moduleEnumOptional.get());

        List<CompositeFolderDto> folderDtoList = folderList.stream()
                .map(CompositeFolderDto::from)
                .collect(Collectors.toList());
        return folderDtoList;
    }

    private List<FunctionModuleEnum> getSelectModuleOrAll(List<String> belongFunctions) {
        List<FunctionModuleEnum> moduleEnumList = Arrays.stream(FunctionModuleEnum.values()).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(belongFunctions)) {
            return moduleEnumList;
        }

        Set<String> funCodeSet = Sets.newHashSet();
        Splitter splitter = Splitter.on(".").omitEmptyStrings().trimResults();
        for (String belong : belongFunctions) {
            if (StringUtils.isNotBlank(belong)) {
                List<String> codes = splitter.splitToList(belong);
                funCodeSet.add(belong);
                funCodeSet.add(codes.get(0));
            }
        }
        return moduleEnumList.stream()
                .filter(moduleEnum -> funCodeSet.contains(moduleEnum.name()))
                .collect(Collectors.toList());
    }

    private List<DevTreeNodeDto> getExpandedDevTreeNodes(List<FunctionModuleEnum> moduleEnumList) {
        if (ObjectUtils.isEmpty(moduleEnumList)) return Lists.newArrayList();

        List<DevTreeNodeDto> nodeDtoList = Lists.newArrayList();
        for (FunctionModuleEnum moduleEnum : moduleEnumList) {
            Optional<DevTreeNodeCacheValue> optional = devTreeNodeLocalCache.get(moduleEnum);
            if (optional.isPresent()) {
                DevTreeNodeCacheValue cacheValue = optional.get();
                if (ObjectUtils.isNotEmpty(cacheValue.getFolders())) {
                    nodeDtoList.addAll(cacheValue.getFolders());
                }
                if (ObjectUtils.isNotEmpty(cacheValue.getRecords())) {
                    nodeDtoList.addAll(cacheValue.getRecords());
                }
            }
        }
        return nodeDtoList;
    }

    private List<DevTreeNodeDto> filterTreeNodes(List<DevTreeNodeDto> treeNodeDtoList, String keyWord) {
        if (ObjectUtils.isEmpty(treeNodeDtoList)) return treeNodeDtoList;
        if (StringUtils.isBlank(keyWord)) return treeNodeDtoList;

        Long id = null;
        try {
            id = Long.parseLong(keyWord);
        } catch (Exception ex) {
            // 转换失败，不是数字，不用判断id是否匹配
        }
        TreeNodeFilter.FilterCondition<DevTreeNodeDto> condition = new TreeNodeFilter.FilterCondition<>(keyWord, id);
        return TreeNodeFilter.withTreeNodes(treeNodeDtoList).filterTree(condition);
    }

}
