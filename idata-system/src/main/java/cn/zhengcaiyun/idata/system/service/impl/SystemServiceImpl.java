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

import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.dto.system.*;
import cn.zhengcaiyun.idata.system.IDataSystem;
import cn.zhengcaiyun.idata.system.dal.dao.SysConfigDao;
import cn.zhengcaiyun.idata.system.dal.dao.SysFeatureDao;
import cn.zhengcaiyun.idata.system.dal.model.SysConfig;
import cn.zhengcaiyun.idata.system.dal.model.SysFeature;
import cn.zhengcaiyun.idata.system.service.SystemService;
import cn.zhengcaiyun.idata.system.zcy.ZcyService;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.system.dal.dao.SysConfigDynamicSqlSupport.sysConfig;
import static cn.zhengcaiyun.idata.system.dal.dao.SysFeatureDynamicSqlSupport.sysFeature;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isNotEqualTo;

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

    @Override
    public SystemStateDto getSystemState() {
        SystemStateDto systemStateDto = new SystemStateDto();
        systemStateDto.setSysStartTime(IDataSystem.getSysStartTime());
        SysConfig config = sysConfigDao.selectOne(c -> c.where(sysConfig.keyOne,
                isEqualTo(SysConfKeyEnum.REGISTER_ENABLE.name()))).orElse(null);
        if (config != null && config.getValueOne().trim().equalsIgnoreCase("true") ) {
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
