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

import cn.zhengcaiyun.idata.commons.encrypt.DigestUtil;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.system.dto.FeatureTreeNodeDto;
import cn.zhengcaiyun.idata.system.dto.FolderTreeNodeDto;
import cn.zhengcaiyun.idata.user.dto.RoleDto;
import cn.zhengcaiyun.idata.system.service.SystemService;
import cn.zhengcaiyun.idata.user.dal.dao.UacRoleAccessDao;
import cn.zhengcaiyun.idata.user.dal.dao.UacRoleDao;
import cn.zhengcaiyun.idata.user.dal.dao.UacUserRoleDao;
import cn.zhengcaiyun.idata.user.dal.model.UacRole;
import cn.zhengcaiyun.idata.user.dal.model.UacRoleAccess;
import cn.zhengcaiyun.idata.user.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.user.dal.dao.UacRoleAccessDynamicSqlSupport.uacRoleAccess;
import static cn.zhengcaiyun.idata.user.dal.dao.UacRoleDynamicSqlSupport.uacRole;
import static cn.zhengcaiyun.idata.user.dal.dao.UacUserRoleDynamicSqlSupport.uacUserRole;
import static com.google.common.base.Preconditions.checkArgument;
import static org.mybatis.dynamic.sql.SqlBuilder.*;
import static org.mybatis.dynamic.sql.select.SelectDSL.select;

/**
 * @author shiyin
 * @date 2021-03-13 22:49
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private UacRoleDao uacRoleDao;
    @Autowired
    private UacUserRoleDao uacUserRoleDao;
    @Autowired
    private UacRoleAccessDao uacRoleAccessDao;
    @Autowired
    private SystemService systemService;

    @Override
    public Page<RoleDto> findRoles(Integer limit, Integer offset) {
        var builder = select(uacRole.allColumns()).from(uacRole).where(uacRole.del, isNotEqualTo(1));
        var countBuilder = select(count()).from(uacRole).where(uacRole.del, isNotEqualTo(1));
        long total = uacRoleDao.count(countBuilder.build().render(RenderingStrategies.MYBATIS3));
        builder.orderBy(uacRole.editTime.descending()).limit(Page.limitCheck(limit)).offset(Page.offsetCheck(offset));
        List<UacRole> roles = uacRoleDao.selectMany(builder.build().render(RenderingStrategies.MYBATIS3));
        return Page.newOne(PojoUtil.copyList(roles, RoleDto.class), total);
    }

    @Override
    public List<FeatureTreeNodeDto> getRoleFeatureTree(Long roleId) {
        UacRole role = uacRoleDao.selectOne(c -> c.where(uacRole.id, isEqualTo(roleId),
                and(uacRole.del, isNotEqualTo(1)))).orElse(null);
        checkArgument(role != null, "角色不存在");
        Set<String> enableFeatureCodes = uacRoleAccessDao.select(c ->
                c.where(uacRoleAccess.roleCode, isEqualTo(role.getRoleCode()),
                        and(uacRoleAccess.accessCode, isLike("F_%")),
                        and(uacRoleAccess.del, isNotEqualTo(1))))
                .stream().map(UacRoleAccess::getAccessCode).collect(Collectors.toSet());
        return systemService.getFeatureTree(SystemService.FeatureTreeMode.FULL, enableFeatureCodes);
    }

    @Override
    public List<FolderTreeNodeDto> getRoleFolderTree(Long roleId) {
        checkArgument(roleId != null, "角色ID不能为空");
        UacRole oneRole = uacRoleDao.selectOne(c -> c.where(uacRole.id, isEqualTo(roleId),
                and(uacRole.del, isNotEqualTo(1)))).orElse(null);
        checkArgument(oneRole != null, "角色不存在");
        Map<String, Integer> folderPermissionMap = new HashMap<>();
        uacRoleAccessDao.select(c ->
                c.where(uacRoleAccess.roleCode, isEqualTo(oneRole.getRoleCode()),
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
        return systemService.getFolderTree(folderPermissionMap);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public RoleDto create(RoleDto roleDto, String creator) {
        checkArgument(roleDto.getRoleCode() != null, "角色编码不能为空");
        UacRole oneRole = uacRoleDao.selectOne(c -> c.where(uacRole.roleCode,
                isEqualTo(roleDto.getRoleCode()))).orElse(null);
        checkArgument(oneRole == null, "角色编码已存在");
        checkArgument(roleDto.getRoleName() != null, "角色名称不能为空");
        checkArgument(creator != null, "创建者不能为空");
        // 插入角色表
        UacRole role = PojoUtil.copyOne(roleDto, UacRole.class, "roleCode", "roleName");
        role.setCreator(creator);
        uacRoleDao.insertSelective(role);
        // 插入角色权限表
        Map<String, UacRoleAccess> accessCodeMap = new HashMap<>();
        Set<String> accessCodes = getFeatureCodes(roleDto.getFeatureTree(), accessCodeMap);
        accessCodes.addAll(getFolderAccessCodes(roleDto.getFolderTree(), accessCodeMap));
        accessCodes.forEach(accessCode -> {
            UacRoleAccess roleAccess = new UacRoleAccess();
            roleAccess.setAccessCode(accessCode);
            roleAccess.setAccessType(accessCodeMap.get(accessCode).getAccessType());
            roleAccess.setAccessKey(accessCodeMap.get(accessCode).getAccessKey());
            roleAccess.setRoleCode(roleDto.getRoleCode());
            roleAccess.setCreator(creator);
            uacRoleAccessDao.insertSelective(roleAccess);
        });
        return PojoUtil.copyOne(uacRoleDao.selectByPrimaryKey(role.getId()).get(), RoleDto.class);
    }

    private Set<String> getFeatureCodes(List<FeatureTreeNodeDto> featureTree, Map<String, UacRoleAccess> accessCodeMap) {
        Set<String> featureCodes = new HashSet<>();
        if (featureTree == null) return featureCodes;
        featureTree.forEach(featureTreeNode -> addFeatureCodes(featureTreeNode, featureCodes, accessCodeMap));
        return featureCodes;
    }

    private void addFeatureCodes(FeatureTreeNodeDto featureTreeNode, Set<String> featureCodes, Map<String, UacRoleAccess> accessCodeMap) {
        if (featureTreeNode.getEnable() != null
                && featureTreeNode.getEnable()
                && StringUtils.isNotEmpty(featureTreeNode.getFeatureCode())) {
            featureCodes.add(featureTreeNode.getFeatureCode());
            UacRoleAccess roleAccess = new UacRoleAccess();
            roleAccess.setAccessType(featureTreeNode.getType());
            roleAccess.setAccessKey(featureTreeNode.getFeatureCode().replace(featureTreeNode.getType() + "_", ""));
            accessCodeMap.put(featureTreeNode.getFeatureCode(), roleAccess);
        }
        if (featureTreeNode.getChildren() != null) {
            featureTreeNode.getChildren().forEach(childFeatureNode -> addFeatureCodes(childFeatureNode, featureCodes, accessCodeMap));
        }
    }

    private Set<String> getFolderAccessCodes(List<FolderTreeNodeDto> folderTree, Map<String, UacRoleAccess> accessCodeMap) {
        Set<String> folderAccessCodes = new HashSet<>();
        if (folderTree == null) return folderAccessCodes;
        folderTree.forEach(folderTreeNode -> addFolderAccessCodes(folderTreeNode, folderAccessCodes, accessCodeMap));
        return folderAccessCodes;
    }

    private void addFolderAccessCodes(FolderTreeNodeDto folderTreeNode, Set<String> folderAccessCodes, Map<String, UacRoleAccess> accessCodeMap) {
        if (folderTreeNode.getFolderId() != null
                && folderTreeNode.getType() != null
                && folderTreeNode.getFilePermission() != null
                && folderTreeNode.getFilePermission() > 0
                && folderTreeNode.getFilePermission() < 8) {
            String accessCode;
            if (folderTreeNode.getFilePermission() % 2 == 1) {
                accessCode = DigestUtil.md5(folderTreeNode.getType() + "_R" + folderTreeNode.getFolderId());
                folderAccessCodes.add(accessCode);
                UacRoleAccess roleAccess = new UacRoleAccess();
                roleAccess.setAccessType(folderTreeNode.getType() + "_R");
                roleAccess.setAccessKey(folderTreeNode.getFolderId());
                accessCodeMap.put(accessCode, roleAccess);
            }
            if (folderTreeNode.getFilePermission() / 2 % 2 == 1) {
                accessCode = DigestUtil.md5(folderTreeNode.getType() + "_W" + folderTreeNode.getFolderId());
                folderAccessCodes.add(accessCode);
                UacRoleAccess roleAccess = new UacRoleAccess();
                roleAccess.setAccessType(folderTreeNode.getType() + "_W");
                roleAccess.setAccessKey(folderTreeNode.getFolderId());
                accessCodeMap.put(accessCode, roleAccess);
            }
            if (folderTreeNode.getFilePermission() / 4 % 2 == 1) {
                accessCode = DigestUtil.md5(folderTreeNode.getType() + "_D" + folderTreeNode.getFolderId());
                folderAccessCodes.add(accessCode);
                UacRoleAccess roleAccess = new UacRoleAccess();
                roleAccess.setAccessType(folderTreeNode.getType() + "_D");
                roleAccess.setAccessKey(folderTreeNode.getFolderId());
                accessCodeMap.put(accessCode, roleAccess);
            }
        }
        if (folderTreeNode.getChildren() != null) {
            folderTreeNode.getChildren().forEach(childFolderTreeNode -> addFolderAccessCodes(childFolderTreeNode, folderAccessCodes, accessCodeMap));
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public RoleDto edit(RoleDto roleDto, String editor) {
        checkArgument(roleDto.getId() != null, "角色ID不能为空");
        UacRole oneRole = uacRoleDao.selectOne(c -> c.where(uacRole.id, isEqualTo(roleDto.getId()),
                and(uacRole.del, isNotEqualTo(1)))).orElse(null);
        checkArgument(oneRole != null, "角色不存在");
        checkArgument(editor != null, "修改者不能为空");
        // 修改角色表
        UacRole role = PojoUtil.copyOne(roleDto, UacRole.class, "id", "roleName");
        role.setEditor(editor);
        uacRoleDao.updateByPrimaryKeySelective(role);
        // 修改角色权限表
        Map<String, UacRoleAccess> accessCodeMap = new HashMap<>();
        Set<String> newAccessCodes = getFeatureCodes(roleDto.getFeatureTree(), accessCodeMap);
        newAccessCodes.addAll(getFolderAccessCodes(roleDto.getFolderTree(), accessCodeMap));
        Set<String> curAccessCodes = uacRoleAccessDao.select(c -> c.where(uacRoleAccess.roleCode, isEqualTo(oneRole.getRoleCode()),
                and(uacRoleAccess.del, isNotEqualTo(1)))).stream().map(UacRoleAccess::getAccessCode)
                .collect(Collectors.toSet());
        Set<String> delAccessCodes = new HashSet<>(curAccessCodes);
        delAccessCodes.removeAll(newAccessCodes);
        delAccessCodes.forEach(accessCode -> uacRoleAccessDao.update(c -> c.set(uacRoleAccess.del).equalTo(1)
                .set(uacRoleAccess.editor).equalTo(editor)
                .where(uacRoleAccess.del, isNotEqualTo(1),
                        and(uacRoleAccess.roleCode, isEqualTo(oneRole.getRoleCode())),
                        and(uacRoleAccess.accessCode, isEqualTo(accessCode)))));
        Set<String> addAccessCodes = new HashSet<>(newAccessCodes);
        addAccessCodes.removeAll(curAccessCodes);
        addAccessCodes.forEach(addAccessCode -> {
            UacRoleAccess oneRoleAccess = uacRoleAccessDao.selectOne(c ->
                    c.where(uacRoleAccess.roleCode, isEqualTo(oneRole.getRoleCode()),
                            and(uacRoleAccess.accessCode, isEqualTo(addAccessCode))))
                    .orElse(null);
            if (oneRoleAccess != null) {
                // 之前被逻辑删除的恢复
                uacRoleAccessDao.update(c -> c.set(uacRoleAccess.del).equalTo(0)
                        .set(uacRoleAccess.editor).equalTo(editor)
                        .where(uacRoleAccess.del, isEqualTo(1),
                                and(uacRoleAccess.roleCode, isEqualTo(oneRole.getRoleCode())),
                                and(uacRoleAccess.accessCode, isEqualTo(addAccessCode))));
            }
            else {
                UacRoleAccess roleAccess = new UacRoleAccess();
                roleAccess.setAccessCode(addAccessCode);
                roleAccess.setAccessType(accessCodeMap.get(addAccessCode).getAccessType());
                roleAccess.setAccessKey(accessCodeMap.get(addAccessCode).getAccessKey());
                roleAccess.setRoleCode(oneRole.getRoleCode());
                roleAccess.setCreator(editor);
                uacRoleAccessDao.insertSelective(roleAccess);
            }
        });
        return PojoUtil.copyOne(uacRoleDao.selectByPrimaryKey(role.getId()).get(), RoleDto.class);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean delete(Long roleId, String editor) {
        checkArgument(roleId != null, "角色ID不能为空");
        UacRole oneRole = uacRoleDao.selectOne(c -> c.where(uacRole.id, isEqualTo(roleId),
                and(uacRole.del, isNotEqualTo(1)))).orElse(null);
        checkArgument(oneRole != null, "角色不存在");
        checkArgument(editor != null, "修改者不能为空");
        // 删除角色表记录
        uacRoleDao.update(c -> c.set(uacRole.del).equalTo(1)
                .set(uacRole.editor).equalTo(editor)
                .where(uacRole.id, isEqualTo(roleId),
                        and(uacRole.del, isNotEqualTo(1))));
        // 删除用户角色表记录
        uacUserRoleDao.update(c -> c.set(uacUserRole.del).equalTo(1)
                .set(uacUserRole.editor).equalTo(editor)
                .where(uacUserRole.roleCode, isEqualTo(oneRole.getRoleCode()),
                        and(uacUserRole.del, isNotEqualTo(1))));
        // 删除角色权限表记录
        uacRoleAccessDao.update(c -> c.set(uacRoleAccess.del).equalTo(1)
                .set(uacRoleAccess.editor).equalTo(editor)
                .where(uacRoleAccess.roleCode, isEqualTo(oneRole.getRoleCode()),
                        and(uacRoleAccess.del, isNotEqualTo(1))));
        return true;
    }
}
