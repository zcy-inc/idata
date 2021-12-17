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
package cn.zhengcaiyun.idata.system.service.impl;

import cn.zhengcaiyun.idata.commons.dto.BaseTreeNodeDto;
import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.core.spi.loader.ServiceProvidersLoader;
import cn.zhengcaiyun.idata.core.spi.loader.ServiceProvidersLoaders;
import cn.zhengcaiyun.idata.system.IDataSystem;
import cn.zhengcaiyun.idata.system.dal.dao.SysConfigDao;
import cn.zhengcaiyun.idata.system.dal.dao.SysFeatureDao;
import cn.zhengcaiyun.idata.system.dal.model.SysConfig;
import cn.zhengcaiyun.idata.system.dal.model.SysFeature;
import cn.zhengcaiyun.idata.system.dto.*;
import cn.zhengcaiyun.idata.system.service.SystemService;
import cn.zhengcaiyun.idata.system.spi.BaseTreeNodeService;
import cn.zhengcaiyun.idata.system.zcy.ZcyService;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.system.dal.dao.SysConfigDynamicSqlSupport.sysConfig;
import static cn.zhengcaiyun.idata.system.dal.dao.SysFeatureDynamicSqlSupport.sysFeature;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author shiyin
 * @date 2021-03-13 23:12
 */
@Service
public class SystemServiceImpl implements SystemService {

    @Autowired
    private SysConfigDao sysConfigDao;
    @Autowired
    private SysFeatureDao sysFeatureDao;
    @Autowired
    private ZcyService zcyService;
    @Autowired
    private ServiceProvidersLoader serviceProvidersLoader;

    @Override
    public SystemStateDto getSystemState() {
        SystemStateDto systemStateDto = new SystemStateDto();
        systemStateDto.setSysStartTime(IDataSystem.getSysStartTime());
        SysConfig config = sysConfigDao.selectOne(c -> c.where(sysConfig.keyOne,
                isEqualTo(SysConfKeyEnum.REGISTER_ENABLE.name()))).orElse(null);
        // 修改valueOne类型
        if (config != null && PojoUtil.copyOne(config, ConfigDto.class).getValueOne().get(SysConfKeyEnum.REGISTER_ENABLE.name())
                .getConfigValue().trim().equalsIgnoreCase("true") ) {
            systemStateDto.setRegisterEnable(true);
        }
        else {
            systemStateDto.setRegisterEnable(false);
        }
        systemStateDto.setVersion("0.1.0");
        return systemStateDto;
    }

    @Override
    public List<FeatureTreeNodeDto> getFeatureTree(FeatureTreeMode mode, Set<String> enableFeatureCodes) {
        List<SysFeature> sysFeatures = new ArrayList<>();
        if (FeatureTreeMode.FULL.equals(mode)
                || FeatureTreeMode.FULL_ENABLE.equals(mode)) {
            sysFeatures = sysFeatureDao.select(c -> c.where(sysFeature.del, isNotEqualTo(1)));
        }
        else if (FeatureTreeMode.PART.equals(mode)) {
            Map<String, String> featureParentMap = new HashMap<>();
            sysFeatureDao.select(c -> c.where(sysFeature.del, isNotEqualTo(1)))
                    .forEach(feature -> featureParentMap.put(feature.getFeatureCode(), feature.getParentCode()));
            Set<String> partFeatureCodes = completeParentCodes(enableFeatureCodes, featureParentMap);
            sysFeatures = sysFeatureDao.select(c -> c.where(sysFeature.del, isNotEqualTo(1)))
                    .stream().filter(feature -> partFeatureCodes.contains(feature.getFeatureCode()))
                    .collect(Collectors.toList());
        }
        return getFeatureChildren(null, sysFeatures, enableFeatureCodes, mode);
    }

    private List<FeatureTreeNodeDto> getFeatureChildren(String parentCode,
                                                        List<SysFeature> sysFeatures,
                                                        Set<String> enableFeatureCodes,
                                                        FeatureTreeMode mode) {
        return sysFeatures.stream().filter(f -> (f.getParentCode() == null && parentCode == null)
                || (f.getParentCode() != null && f.getParentCode().equals(parentCode))).map(feature -> {
            FeatureTreeNodeDto featureTreeNodeDto = PojoUtil.copyOne(feature, FeatureTreeNodeDto.class,
                    "featureCode", "parentCode");
            featureTreeNodeDto.setType(feature.getFeatureType());
            featureTreeNodeDto.setName(feature.getFeatureName());
            if (FeatureTreeMode.FULL_ENABLE.equals(mode)
                    || (enableFeatureCodes != null && enableFeatureCodes.contains(featureTreeNodeDto.getFeatureCode()))) {
                featureTreeNodeDto.setEnable(true);
            }
            else {
                featureTreeNodeDto.setEnable(false);
            }
            featureTreeNodeDto.setChildren(getFeatureChildren(featureTreeNodeDto.getFeatureCode(),
                    sysFeatures, enableFeatureCodes, mode));
            return featureTreeNodeDto;
        }).collect(Collectors.toList());
    }

    private Set<String> completeParentCodes(Set<String> featureCodes, Map<String, String> featureParentMap) {
        if (featureCodes == null) return new HashSet<>();
        Set<String> completedFeatureCodes = new ConcurrentSkipListSet<>(featureCodes);
        completedFeatureCodes.forEach(featureCode -> findParentCodes(featureCode, completedFeatureCodes, featureParentMap));
        return completedFeatureCodes;
    }

    private void findParentCodes(String featureCode, Set<String> featureCodes, Map<String, String> featureParentMap) {
        if (featureParentMap.get(featureCode) != null && !featureCodes.contains(featureParentMap.get(featureCode))) {
            featureCodes.add(featureParentMap.get(featureCode));
            findParentCodes(featureParentMap.get(featureCode), featureCodes, featureParentMap);
        }
    }

    @Override
    public List<FolderTreeNodeDto> getFolderTree(Map<String, Integer> folderPermissionMap) {
        Set<String> folderMenuSet = new HashSet<>(Arrays.asList("F_MENU_DW_DESIGN", "F_MENU_JOB_MANAGE",
                "F_MENU_RESOURCE_MANAGE", "F_MENU_FUNCTION_MANAGE", "F_MENU_API_DEVELOP"));
        List<FolderTreeNodeDto> folderTree = PojoUtil.castType(getFeatureTree(FeatureTreeMode.PART, folderMenuSet),
                new TypeReference<>(){});
        folderTree.forEach(folderTreeNode -> setFolderTreeChild(folderTreeNode, folderPermissionMap));
        return folderTree;
    }

    @Override
    public List<FolderTreeNodeDto> getDevFolderTree(Map<String, Integer> folderPermissionMap) {
        BaseTreeNodeService baseTreeNode = ServiceProvidersLoaders.loadProviderIfPresent(serviceProvidersLoader,
                BaseTreeNodeService.class, "bizTree");
        List<BaseTreeNodeDto> baseTreeNodeList = baseTreeNode.getBaseTree();
        List<FolderTreeNodeDto> folderTreeNodeList = changeBaseToFolder(baseTreeNodeList, folderPermissionMap);
        List<String> resourceCodeList = folderTreeNodeList.stream().map(FolderTreeNodeDto::getType).collect(Collectors.toList());
        List<FeatureTreeNodeDto> featureTree = getFeatureTreeNodeByCode(resourceCodeList);
        List<FolderTreeNodeDto> echo = featureTree.stream().map(featureTreeNode -> {
            FolderTreeNodeDto folderTreeNode = PojoUtil.copyOne(featureTreeNode, FolderTreeNodeDto.class, "name",
                    "type", "featureCode");
            if (ObjectUtils.isNotEmpty(featureTreeNode.getChildren())) {
                List<FolderTreeNodeDto> childrenFolderTreeNodeList = PojoUtil.copyList(featureTreeNode.getChildren(),
                        FolderTreeNodeDto.class, "name", "type", "featureCode");
                childrenFolderTreeNodeList.forEach(childrenFolderTreeNode -> {
                    childrenFolderTreeNode.setParentCode(folderTreeNode.getFeatureCode());
                    if (FeatureCodeEnum.F_MENU_DATA_DEVELOP.name().equals(childrenFolderTreeNode.getFeatureCode())) {
                        childrenFolderTreeNode.setChildren(folderTreeNodeList);
                    }
                });
                folderTreeNode.setChildren(childrenFolderTreeNodeList);
            }
            return folderTreeNode;
        }).collect(Collectors.toList());
        return echo;
    }

    private List<FolderTreeNodeDto> changeBaseToFolder(List<BaseTreeNodeDto> baseTreeNodeList, Map<String, Integer> folderPermissionMap) {
        List<FolderTreeNodeDto> echo = baseTreeNodeList.stream().map(treeNode -> {
            FolderTreeNodeDto folderTreeNode = new FolderTreeNodeDto();
            folderTreeNode.setName(treeNode.getName());
            folderTreeNode.setType(treeNode.getType());
            folderTreeNode.setParentId(treeNode.getParentId().toString());
            folderTreeNode.setFolderId(treeNode.getId().toString());
            if (folderPermissionMap.containsKey(folderTreeNode.getType() + folderTreeNode.getFolderId())
                    && folderPermissionMap.get(folderTreeNode.getType() + folderTreeNode.getFolderId()) != null
                    && folderPermissionMap.get(folderTreeNode.getType() + folderTreeNode.getFolderId()) > 0) {
                folderTreeNode.setFilePermission(folderPermissionMap.get(folderTreeNode.getType() + folderTreeNode.getFolderId()));
            }
            else {
                folderTreeNode.setFilePermission(0);
            }
            if (ObjectUtils.isNotEmpty(treeNode.getChildren())) {
                folderTreeNode.setChildren(changeBaseToFolder(treeNode.getChildren(), folderPermissionMap));
            }
            return folderTreeNode;
        }).collect(Collectors.toList());
        return echo;
    }

    private List<FeatureTreeNodeDto> getFeatureTreeNodeByCode(List<String> resourceCodes) {
        List<String> featureCodeList = getFeatureCodeByResourceCode(resourceCodes);
        List<SysFeature> sysFeatures = sysFeatureDao.select(c -> c.where(sysFeature.del, isNotEqualTo(1),
                and(sysFeature.featureCode, isIn(featureCodeList))));
        List<String> parentCodeList = sysFeatures.stream().map(SysFeature::getParentCode).collect(Collectors.toList());
        sysFeatures.addAll(sysFeatureDao.select(c -> c.where(sysFeature.del, isNotEqualTo(1),
                and(sysFeature.featureCode, isIn(parentCodeList)))));
        sysFeatures.addAll(sysFeatureDao.select(c -> c.where(sysFeature.del, isNotEqualTo(1),
                and(sysFeature.featureType, isEqualTo(FeatureTypeEnum.F_MENU.name())),
                and(sysFeature.featureCode, isNotIn(featureCodeList)),
                and(sysFeature.parentCode, isIn(parentCodeList)))));
        return getFeatureChildren(null, sysFeatures, null, FeatureTreeMode.FULL);
    }

    private List<String> getFeatureCodeByResourceCode(List<String> resourceCodes) {
        return resourceCodes.stream().map(resourceCode -> {
            String featureCode = null;
            if (resourceCode.contains("_DATA_DEVELOP_")) {
                featureCode = FeatureCodeEnum.F_MENU_DATA_DEVELOP.name();
            }
            else if (resourceCode.contains("_MEASURE_MANAGE_")) {
                featureCode = FeatureCodeEnum.F_MENU_MEASURE_MANAGE.name();
            }
            else if (resourceCode.contains("_DATA_LABEL_")) {
                featureCode = FeatureCodeEnum.F_MENU_LABEL_MANAGE.name();
            }
            return featureCode;
        }).collect(Collectors.toList());
    }

    private void setFolderTreeChild(FolderTreeNodeDto folderTreeNode, Map<String, Integer> folderPermissionMap) {
        ResourceTypeEnum resourceType = null;
        if ("F_MENU_DW_DESIGN".equals(folderTreeNode.getFeatureCode())) {
            resourceType = ResourceTypeEnum.R_DW_DESIGN_DIR;
        }
        else if ("F_MENU_JOB_MANAGE".equals(folderTreeNode.getFeatureCode())) {
            resourceType = ResourceTypeEnum.R_JOB_MANAGE_DIR;
        }
        else if ("F_MENU_RESOURCE_MANAGE".equals(folderTreeNode.getFeatureCode())) {
            resourceType = ResourceTypeEnum.R_RESOURCE_MANAGE_DIR;
        }
        else if ("F_MENU_FUNCTION_MANAGE".equals(folderTreeNode.getFeatureCode())) {
            resourceType = ResourceTypeEnum.R_FUNCTION_MANAGE_DIR;
        }
        else if ("F_MENU_API_DEVELOP".equals(folderTreeNode.getFeatureCode())) {
            resourceType = ResourceTypeEnum.R_API_DEVELOP_DIR;
        }
        if (resourceType != null) {
            folderTreeNode.setChildren(getFolderTree(resourceType, folderPermissionMap));
        }
        if (folderTreeNode.getChildren() != null) {
            folderTreeNode.getChildren().forEach(childNode -> setFolderTreeChild(childNode, folderPermissionMap));
        }
    }

    private List<FolderTreeNodeDto> getFolderTree(ResourceTypeEnum resourceType,
                                                  Map<String, Integer> folderPermissionMap) {
        List<FolderTreeNodeDto> folderTree = zcyService.getFolders(resourceType).stream().peek(folderTreeNode -> {
            Integer folderPermission = folderPermissionMap.get(folderTreeNode.getType() + folderTreeNode.getFolderId());
            if (folderPermission != null && folderPermission > 0) {
                folderTreeNode.setFilePermission(folderPermission);
            }
            else {
                folderTreeNode.setFilePermission(0);
            }
        }).collect(Collectors.toList());
        return getFolderChildren("-1", folderTree);
    }

    private List<FolderTreeNodeDto> getFolderChildren(String parentId, List<FolderTreeNodeDto> folderTreeNodes) {
        return folderTreeNodes.stream().filter(f -> (f.getParentId() != null && f.getParentId().equals(parentId)))
                .peek(folderTreeNode -> folderTreeNode.setChildren(getFolderChildren(folderTreeNode.getFolderId(), folderTreeNodes)))
                .collect(Collectors.toList());
    }
}
