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
package cn.zhengcaiyun.idata.user.service.impl;

import cn.zhengcaiyun.idata.commons.dto.BaseTreeNodeDto;
import cn.zhengcaiyun.idata.commons.encrypt.DigestUtil;
import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.core.spi.loader.ServiceProvidersLoader;
import cn.zhengcaiyun.idata.core.spi.loader.ServiceProvidersLoaders;
import cn.zhengcaiyun.idata.system.dal.dao.SysResourceDao;
import cn.zhengcaiyun.idata.system.dal.dao.SysResourceDynamicSqlSupport;
import cn.zhengcaiyun.idata.system.dal.model.SysFeature;
import cn.zhengcaiyun.idata.system.dal.model.SysResource;
import cn.zhengcaiyun.idata.system.dto.FeatureTreeNodeDto;
import cn.zhengcaiyun.idata.system.dto.FolderTreeNodeDto;
import cn.zhengcaiyun.idata.system.dto.ResourceTypeEnum;
import cn.zhengcaiyun.idata.system.service.SysResourceService;
import cn.zhengcaiyun.idata.system.service.SystemConfigService;
import cn.zhengcaiyun.idata.system.service.SystemService;
import cn.zhengcaiyun.idata.system.spi.BaseTreeNodeService;
import cn.zhengcaiyun.idata.user.dal.dao.UacRoleAccessDao;
import cn.zhengcaiyun.idata.user.dal.dao.UacUserDao;
import cn.zhengcaiyun.idata.user.dal.dao.UacUserRoleDao;
import cn.zhengcaiyun.idata.user.dal.model.UacRoleAccess;
import cn.zhengcaiyun.idata.user.dal.model.UacUser;
import cn.zhengcaiyun.idata.user.dal.model.UacUserRole;
import cn.zhengcaiyun.idata.user.service.UserAccessService;
import cn.zhengcaiyun.idata.user.spi.DevFolderService;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.user.dal.dao.UacRoleAccessDynamicSqlSupport.uacRoleAccess;
import static cn.zhengcaiyun.idata.user.dal.dao.UacUserDynamicSqlSupport.uacUser;
import static cn.zhengcaiyun.idata.user.dal.dao.UacUserRoleDynamicSqlSupport.uacUserRole;
import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author shiyin
 * @date 2021-03-30 20:29
 */
@Service
public class UserAccessServiceImpl implements UserAccessService {

    @Value("${access.mode:#{null}}")
    private String ACCESS_MODE;

    @Autowired
    private SystemService systemService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private UacUserDao uacUserDao;
    @Autowired
    private UacUserRoleDao uacUserRoleDao;
    @Autowired
    private UacRoleAccessDao uacRoleAccessDao;
    @Autowired
    private ServiceProvidersLoader serviceProvidersLoader;
    @Autowired
    private SysResourceDao sysResourceDao;

    @Autowired
    private SysResourceService sysResourceService;

    @Override
    public List<FeatureTreeNodeDto> getUserFeatureTree(Long userId) {
        UacUser user = uacUserDao.selectOne(c -> c.where(uacUser.id, isEqualTo(userId),
                and(uacUser.del, isNotEqualTo(1)))).orElse(null);
        checkArgument(user != null, "用户不存在");
        if (1 == user.getSysAdmin() || 2 == user.getSysAdmin()) {
            return systemService.getFeatureTree(SystemService.FeatureTreeMode.FULL_ENABLE, null);
        }
        Set<String> roleCodes = uacUserRoleDao.select(c ->
                c.where(uacUserRole.userId, isEqualTo(userId),
                        and(uacUserRole.del, isNotEqualTo(1))))
                .stream().map(UacUserRole::getRoleCode).collect(Collectors.toSet());
        if (roleCodes.size() == 0) return new ArrayList<>();
        Set<String> enableFeatureCodes = uacRoleAccessDao.select(c ->
                c.where(uacRoleAccess.roleCode, isIn(roleCodes),
                        and(uacRoleAccess.del, isNotEqualTo(1)),
                        and(uacRoleAccess.accessCode, isLike("F_%"))))
                .stream().map(UacRoleAccess::getAccessCode).collect(Collectors.toSet());
        return systemService.getFeatureTree(SystemService.FeatureTreeMode.PART, enableFeatureCodes);
    }

    @Override
    public List<FolderTreeNodeDto> getUserFolderTree(Long userId) {
        UacUser user = uacUserDao.selectOne(c -> c.where(uacUser.id, isEqualTo(userId),
                and(uacUser.del, isNotEqualTo(1)))).orElse(null);
        checkArgument(user != null, "用户不存在");
        Set<String> roleCodes = uacUserRoleDao.select(c ->
                c.where(uacUserRole.userId, isEqualTo(userId),
                        and(uacUserRole.del, isNotEqualTo(1))))
                .stream().map(UacUserRole::getRoleCode).collect(Collectors.toSet());
        if (roleCodes.size() == 0) return new ArrayList<>();
        Map<String, Integer> folderPermissionMap = new HashMap<>();
        uacRoleAccessDao.select(c ->
                c.where(uacRoleAccess.roleCode, isIn(roleCodes),
                        and(uacRoleAccess.accessType, isLike("R_%")),
                        and(uacRoleAccess.del, isNotEqualTo(1))))
                .forEach(roleAccess -> {
                    String key = roleAccess.getAccessType().substring(0, roleAccess.getAccessType().length() - 2)
                            + roleAccess.getAccessKey();
                    Integer value = folderPermissionMap.get(key);
                    int add = 0;
                    if (roleAccess.getAccessType().endsWith("R")) {
                        add = 1;
                    }
                    else if (roleAccess.getAccessType().endsWith("W")) {
                        add = 2;
                    }
                    else if (roleAccess.getAccessType().endsWith("D")) {
                        add = 4;
                    }
                    if (value != null) {
                        folderPermissionMap.put(key, value + add);
                    }
                    else {
                        folderPermissionMap.put(key, add);
                    }
                });
        List<FolderTreeNodeDto> folderTree = systemService.getFolderTree(folderPermissionMap);
        return folderTree;
    }

    @Override
    public List<String> getAccessKeys(Long userId, String accessType) {
        UacUser user = uacUserDao.selectOne(c -> c.where(uacUser.id, isEqualTo(userId),
                and(uacUser.del, isNotEqualTo(1)))).orElse(null);
        checkArgument(user != null, "用户不存在");
        List<String> roleCodes = uacUserRoleDao.select(c ->
                c.where(uacUserRole.userId, isEqualTo(userId),
                        and(uacUserRole.del, isNotEqualTo(1))))
                .stream().map(UacUserRole::getRoleCode).collect(Collectors.toList());
        if (roleCodes.size() == 0) return new ArrayList<>();
        return uacRoleAccessDao.select(c ->
                c.where(uacRoleAccess.roleCode, isIn(roleCodes),
                        and(uacRoleAccess.accessType, isEqualTo(accessType)),
                        and(uacRoleAccess.del, isNotEqualTo(1))))
                .stream().map(UacRoleAccess::getAccessKey).collect(Collectors.toList());
    }

    @Override
    public boolean checkAccess(Long userId, String accessCode) {
        // 迁移不校验权限
        if ("idata-merge".equals(ACCESS_MODE)) return true;
        UacUser user = uacUserDao.selectOne(c -> c.where(uacUser.id, isEqualTo(userId),
                and(uacUser.del, isNotEqualTo(1))))
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        if (1 == user.getSysAdmin() || 2 == user.getSysAdmin()) return true;
        checkArgument(accessCode != null, "权限编码不能为空");
        List<String> roleCodes = uacUserRoleDao.select(c -> c.where(uacUserRole.userId, isEqualTo(userId),
                and(uacUserRole.del, isNotEqualTo(1)))).stream().map(UacUserRole::getRoleCode)
                .collect(Collectors.toList());
        if (roleCodes.size() == 0) {
            return false;
        }
        UacRoleAccess roleAccess = uacRoleAccessDao.selectOne(c -> c.where(uacRoleAccess.accessCode, isEqualTo(accessCode),
                and(uacRoleAccess.del, isNotEqualTo(1)), and(uacRoleAccess.roleCode, isIn(roleCodes))))
                .orElse(null);
        return roleAccess != null;
    }

    @Override
    public boolean checkAccess(Long userId, List<String> accessTypes, String accessKey) {
        // 数据迁移不校验权限
        if ("idata-merge".equals(ACCESS_MODE)) return true;
        UacUser user = uacUserDao.selectOne(c -> c.where(uacUser.id, isEqualTo(userId),
                and(uacUser.del, isNotEqualTo(1))))
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        if (1 == user.getSysAdmin() || 2 == user.getSysAdmin()) return true;
        checkArgument(accessTypes != null && accessTypes.size() > 0, "权限类型不能为空");
        checkArgument(accessKey != null, "权限资源键不能为空");
        List<String> roleCodes = uacUserRoleDao.select(c -> c.where(uacUserRole.userId, isEqualTo(userId),
                and(uacUserRole.del, isNotEqualTo(1)))).stream().map(UacUserRole::getRoleCode)
                .collect(Collectors.toList());
        if (roleCodes.size() == 0) {
            return false;
        }
        Set<String> accessCodes = accessTypes.stream().map(accessType ->
                DigestUtil.md5(accessType + accessKey)).collect(Collectors.toSet());
        Set<String> roleAccessCodes = uacRoleAccessDao.select(c -> c.where(uacRoleAccess.roleCode, isIn(roleCodes),
                and(uacRoleAccess.accessCode, isIn(accessCodes)),
                and(uacRoleAccess.del, isNotEqualTo(1)))).stream().map(UacRoleAccess::getAccessCode)
                .collect(Collectors.toSet());
        return roleAccessCodes.size() == accessCodes.size();
    }

    // 只校验菜单的List
    @Override
    public boolean checkFeatureAccess(Long userId, String controllerPath) {
        UacUser user = uacUserDao.selectOne(c -> c.where(uacUser.id, isEqualTo(userId),
                and(uacUser.del, isNotEqualTo(1))))
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        if (1 == user.getSysAdmin() || 2 == user.getSysAdmin()) return true;
        List<String> roleCodes = uacUserRoleDao.select(c -> c.where(uacUserRole.userId, isEqualTo(userId),
                and(uacUserRole.del, isNotEqualTo(1)))).stream().map(UacUserRole::getRoleCode)
                .collect(Collectors.toList());
        if (roleCodes.size() == 0) {
            return false;
        }
        Set<String> accessCodes = uacRoleAccessDao.select(c -> c.where(uacRoleAccess.del, isNotEqualTo(1),
                and(uacRoleAccess.roleCode, isIn(roleCodes)))).stream().map(UacRoleAccess::getAccessCode)
                .collect(Collectors.toSet());
        String controllerUrl = controllerPath.split("api")[1];
        List<SysFeature> featureList = systemConfigService.getFeatures(controllerUrl);
        if (ObjectUtils.isEmpty(featureList)) {
            return false;
        }
        else {
            List<SysFeature> accessFeatureList = featureList.stream().filter(c -> c.getFeatureUrlPath().equals(controllerPath))
                    .collect(Collectors.toList());
            if (ObjectUtils.isEmpty(accessFeatureList)) return false;
            return accessCodes.contains(accessFeatureList.get(0).getFeatureCode());
        }
    }

    @Override
    public boolean checkResAccess(Long userId, List<String> accessTypes, String accessKey) {
        // 数据迁移不校验权限
        if ("idata-merge".equals(ACCESS_MODE)) return true;
        UacUser user = uacUserDao.selectOne(c -> c.where(uacUser.id, isEqualTo(userId),
                and(uacUser.del, isNotEqualTo(1))))
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        if (1 == user.getSysAdmin() || 2 == user.getSysAdmin()) return true;

        if (isEmpty(accessKey) || accessTypes == null || accessTypes.size() == 0) {return false;}

        boolean isAccess = false;
        Boolean checkAccess = checkAccess(userId, accessTypes, accessKey);
        if (!checkAccess) {
            // 逐层校验是否有资源权限
            List<String> parentFolderIdsList = new ArrayList<>();
            DevFolderService devFolder = ServiceProvidersLoaders.loadProviderIfPresent(serviceProvidersLoader,
                    DevFolderService.class, "devParentFolderIds");
            parentFolderIdsList = devFolder.getDevParentFolderIds(accessKey, parentFolderIdsList);
            for (String parentFolderId : parentFolderIdsList) {
                Boolean checkParentAccess = checkAccess(userId, accessTypes, parentFolderId);
                if (checkParentAccess) {
                    isAccess = checkParentAccess;
                    break;
                }
            }
        }
        else {
            isAccess = checkAccess;
        }
        return isAccess;
    }

    @Override
    public boolean checkAddAccess(Long userId, Long parentId, String featureType, String resourceType) {
        // 数据迁移不校验权限
        if ("idata-merge".equals(ACCESS_MODE)) return true;
        Boolean checkAccess = checkResAccess(userId, Arrays.asList(resourceType + "_W"), String.valueOf(parentId));
        // 若为0 同时校验功能权限
        if (checkAccess && parentId == 0L) {
            checkArgument(isEmpty(featureType), "无权限，请联系管理员");
            checkAccess = checkAccess(userId, featureType);
        }
        return checkAccess;
    }

    @Override
    public boolean checkUpdateAccess(Long userId, Long originalParentId, Long removeParentId, String resourceType) {
        // 数据迁移不校验权限
        if ("idata-merge".equals(ACCESS_MODE)) return true;
        Boolean checkAccess = checkResAccess(userId, Arrays.asList(resourceType + "_W"), String.valueOf(originalParentId));
        // 校验拖拽，最外层不可拖拽
        if (checkAccess && removeParentId != null) {
            checkArgument(removeParentId != 0L, "禁止移动文件夹至根目录");
            checkAccess = checkResAccess(userId, Arrays.asList(resourceType + "_W"), String.valueOf(removeParentId));
        }
        return checkAccess;
    }

    @Override
    public boolean checkDeleteAccess(Long userId, Long parentId, String resourceType) {
        // 数据迁移不校验权限
        if ("idata-merge".equals(ACCESS_MODE)) return true;
        return checkResAccess(userId, Arrays.asList(resourceType + "_D"), String.valueOf(parentId));
    }
}
