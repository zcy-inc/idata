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

import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.dto.system.FeatureTreeNodeDto;
import cn.zhengcaiyun.idata.dto.system.FolderTreeNodeDto;
import cn.zhengcaiyun.idata.dto.user.RoleDto;
import cn.zhengcaiyun.idata.user.dal.dao.UacAccessDao;
import cn.zhengcaiyun.idata.user.dal.dao.UacRoleAccessDao;
import cn.zhengcaiyun.idata.user.dal.dao.UacRoleDao;
import cn.zhengcaiyun.idata.user.dal.model.UacAccess;
import cn.zhengcaiyun.idata.user.dal.model.UacRole;
import cn.zhengcaiyun.idata.user.dal.model.UacRoleAccess;
import cn.zhengcaiyun.idata.user.service.RoleService;
import org.mybatis.dynamic.sql.SqlBuilder;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static cn.zhengcaiyun.idata.user.dal.dao.UacAccessDynamicSqlSupport.uacAccess;
import static cn.zhengcaiyun.idata.user.dal.dao.UacRoleAccessDynamicSqlSupport.uacRoleAccess;
import static cn.zhengcaiyun.idata.user.dal.dao.UacRoleDynamicSqlSupport.uacRole;
import static com.google.common.base.Preconditions.checkArgument;
import static org.mybatis.dynamic.sql.SqlBuilder.*;
import static org.mybatis.dynamic.sql.select.SelectDSL.select;

/**
 * @author shiyin
 * @date 2021-03-13 22:49
 */
@Service
public class RoleServiceImpl implements RoleService {

    private static final String READ = "R";
    private static final String WRITE = "W";
    private static final String DEL = "D";

    @Autowired
    private UacRoleDao uacRoleDao;
    @Autowired
    private UacAccessDao uacAccessDao;
    @Autowired
    private UacRoleAccessDao uacRoleAccessDao;

    @Override
    public Page<RoleDto> findRoles(Integer limit, Integer offset) {
        var builder = select(uacRole.allColumns()).from(uacRole).where(uacRole.del, isNotEqualTo((short) 1));
        var countBuilder = select(count()).from(uacRole).where(uacRole.del, isNotEqualTo((short) 1));
        long total = uacRoleDao.count(countBuilder.build().render(RenderingStrategies.MYBATIS3));
        builder.orderBy(uacRole.editTime.descending()).limit(Page.limitCheck(limit)).offset(Page.offsetCheck(offset));
        List<UacRole> roles = uacRoleDao.selectMany(builder.build().render(RenderingStrategies.MYBATIS3));
        return Page.newOne(PojoUtil.copyList(roles, RoleDto.class), total);
    }

    @Override
    public List<FeatureTreeNodeDto> getRoleFeatureTree(Long roleId) {
        return null;
    }

    @Override
    public List<FolderTreeNodeDto> getRoleFolderTree(Long roleId) {
        return null;
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

        UacRole role = PojoUtil.copyOne(roleDto, UacRole.class, "roleCode", "roleName");
        role.setCreator(creator);
        uacRoleDao.insertSelective(role);

        // 角色权限关系表插入
        List<FeatureTreeNodeDto> featureTreeNodeList = roleDto.getFeatureTree() != null && roleDto.getFeatureTree().size() > 0
                ? roleDto.getFeatureTree() : null;
        List<FolderTreeNodeDto> folderTreeNodeList =
                roleDto.getFolderTree() != null && roleDto.getFolderTree().size() > 0
                        ? roleDto.getFolderTree() : null;
        RoleDto echo = PojoUtil.copyOne(uacRoleDao.selectByPrimaryKey(role.getId()).get(), RoleDto.class);
        String roleCode = echo.getRoleCode();
        List<UacRoleAccess> roleAccessList = new ArrayList<>();
        // 功能权限
        if (featureTreeNodeList != null) {
            roleAccessList = getFeatureRoleAccessList(featureTreeNodeList, roleCode, creator);
        }
        // 文件夹权限
        if (folderTreeNodeList != null) {
            roleAccessList = getFolderRoleAccessList(folderTreeNodeList, roleCode, creator);
        }

        if (roleAccessList.size() > 0) {
            for (UacRoleAccess roleAccess : roleAccessList) {
                uacRoleAccessDao.insertSelective(roleAccess);
            }
        }

        return echo;

    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public RoleDto edit(RoleDto roleDto, String editor) {
        checkArgument(roleDto.getId() != null, "角色ID不能为空");
        UacRole oneRole = uacRoleDao.selectOne(c -> c.where(uacRole.id, isEqualTo(roleDto.getId()),
                and(uacRole.del, isNotEqualTo((short) 1)))).orElse(null);
        checkArgument(oneRole != null, "角色不存在");
        checkArgument(editor != null, "修改者不能为空");

        UacRole role = PojoUtil.copyOne(roleDto, UacRole.class, "id", "roleName");
        role.setEditor(editor);
        uacRoleDao.updateByPrimaryKeySelective(role);

        // 角色权限关系表更新
        List<FeatureTreeNodeDto> featureTreeNodeList = roleDto.getFeatureTree() != null && roleDto.getFeatureTree().size() > 0
                ? roleDto.getFeatureTree() : null;
        List<FolderTreeNodeDto> folderTreeNodeList = roleDto.getFolderTree() != null && roleDto.getFolderTree().size() > 0
                ? roleDto.getFolderTree() : null;
        RoleDto echo = PojoUtil.copyOne(uacRoleDao.selectByPrimaryKey(role.getId()).get(), RoleDto.class);
        if (featureTreeNodeList == null && folderTreeNodeList == null) {
            return echo;
        }
        String roleCode = echo.getRoleCode();
        List<UacRoleAccess> roleAccessList = uacRoleAccessDao.selectMany(select(uacRoleAccess.allColumns()).from(uacRoleAccess)
                .where(uacRoleAccess.del, isNotEqualTo((short) 1), and(uacRoleAccess.roleCode, isEqualTo(roleCode)))
                .build().render(RenderingStrategies.MYBATIS3));
        List<String> originalAccessCodeList = new ArrayList<>();
        for (UacRoleAccess roleAccess : roleAccessList) {
            originalAccessCodeList.add(roleAccess.getAccessCode());
        }
        List<UacRoleAccess> insertRoleAccessList = new ArrayList<>();
        List<UacRoleAccess> delRoleAccessList = new ArrayList<>();
        List<String> updateAccessCodeList = new ArrayList<>();
        // 功能权限
        if (featureTreeNodeList != null) {
            for (FeatureTreeNodeDto featureTreeNode : featureTreeNodeList) {
                updateAccessCodeList = getFeatureAccessCodeList(featureTreeNode, updateAccessCodeList);
            }
        }
        if (folderTreeNodeList != null) {
            for (FolderTreeNodeDto folderTreeNode : folderTreeNodeList) {
                updateAccessCodeList = getFolderAccessCodeList(folderTreeNode, updateAccessCodeList);
            }
        }
        // 判断原始list和修改list的内容差异在哪里
        Map<String, Integer> accessCodeMap = new LinkedHashMap<>();
        for (String accessCode : originalAccessCodeList) {
            accessCodeMap.put(accessCode, 1);
        }
        for (String accessCode : updateAccessCodeList) {
            Integer accessCodeNum = accessCodeMap.get(accessCode);
            if (accessCodeNum != null) {
                accessCodeMap.put(accessCode, accessCodeNum + 1);
            }
            else {
                UacRoleAccess roleAccess = new UacRoleAccess();
                roleAccess.setCreator(editor);
                roleAccess.setAccessCode(accessCode);
                roleAccess.setRoleCode(roleCode);
                insertRoleAccessList.add(roleAccess);
            }
        }
        for (Map.Entry<String, Integer> entry : accessCodeMap.entrySet()) {
            if (entry.getValue() == 1) {
                delRoleAccessList.add(roleAccessList.get(originalAccessCodeList.indexOf(entry.getKey())));
            }
        }

        if (delRoleAccessList.size() > 0) {
            for (UacRoleAccess roleAccess : delRoleAccessList) {
                uacRoleAccessDao.deleteByPrimaryKey(roleAccess.getId());
            }
        }
        if (updateAccessCodeList.size() > 0) {
            for (UacRoleAccess roleAccess : insertRoleAccessList) {
                uacRoleAccessDao.insertSelective(roleAccess);
            }
        }

        return echo;
    }

    @Override
    public boolean delete(Long roleId, String editor) {
        checkArgument(roleId != null, "角色ID不能为空");
        UacRole oneRole = uacRoleDao.selectOne(c -> c.where(uacRole.id, isEqualTo(roleId),
                and(uacRole.del, isNotEqualTo((short) 1)))).orElse(null);
        checkArgument(oneRole != null, "角色不存在");
        checkArgument(editor != null, "修改者不能为空");

        uacRoleDao.update(c -> c.set(uacRole.del).equalTo((short) 1)
                .set(uacRole.editor).equalTo(editor)
                .where(uacRole.id, isEqualTo(roleId)));
        return true;
    }

    private List<UacRoleAccess> getFeatureRoleAccessList(List<FeatureTreeNodeDto> featureTreeNodeList,
                                                         String roleCode, String creator) {
        List<UacRoleAccess> roleAccessList = new ArrayList<>();
        for (FeatureTreeNodeDto featureTreeNode : featureTreeNodeList) {
            List<String> accessCodeList = new ArrayList<>();
            accessCodeList = getFeatureAccessCodeList(featureTreeNode, accessCodeList);
            for (String accessCode : accessCodeList) {
                UacRoleAccess roleAccess = new UacRoleAccess();
                roleAccess.setRoleCode(roleCode);
                roleAccess.setAccessCode(accessCode);
                roleAccess.setCreator(creator);
                roleAccessList.add(roleAccess);
            }
        }
        return roleAccessList;
    }

    private List<String> getFeatureAccessCodeList(FeatureTreeNodeDto featureTreeNode,
                                                  List<String> featureAccessCodeList) {
        if (featureTreeNode.getEnable() != null && featureTreeNode.getEnable()) {
            featureAccessCodeList.add(featureTreeNode.getFeatureCode());
            if (featureTreeNode.getChildren() != null && featureTreeNode.getChildren().size() > 0) {
                for (FeatureTreeNodeDto childFeatureTreeNode : featureTreeNode.getChildren()) {
                    featureAccessCodeList = getFeatureAccessCodeList(childFeatureTreeNode, featureAccessCodeList);
                }
            }
        }
        return featureAccessCodeList;
    }

    private List<UacRoleAccess> getFolderRoleAccessList(List<FolderTreeNodeDto> folderTreeNodeList,
                                                        String roleCode, String creator) {
        List<UacRoleAccess> roleAccessList = new ArrayList<>();
        for (FolderTreeNodeDto folderTreeNode : folderTreeNodeList) {
            List<String> accessCodeList = new ArrayList<>();
            accessCodeList = getFolderAccessCodeList(folderTreeNode, accessCodeList);
            for (String accessCode : accessCodeList) {
                UacRoleAccess roleAccess = new UacRoleAccess();
                roleAccess.setRoleCode(roleCode);
                roleAccess.setAccessCode(accessCode);
                roleAccess.setCreator(creator);
                roleAccessList.add(roleAccess);
            }
        }
        return roleAccessList;
    }

    private List<String> getFolderAccessCodeList(FolderTreeNodeDto folderTreeNode, List<String> folderAccessCodeList) {
        List<String> filePermissionList;
        if (folderTreeNode.getFilePermission() != null && folderTreeNode.getFilePermission() > 0
                && folderTreeNode.getFilePermission() <= 7) {
            filePermissionList = getFilePermissionList(folderTreeNode.getFilePermission());
        }
        else {
            return folderAccessCodeList;
        }

        var builder = SqlBuilder.select(uacAccess.accessCode).from(uacAccess)
                .where(uacAccess.del, isNotEqualTo((short) 1), and(uacAccess.accessType,
                        isLike(folderTreeNode.getType() + "%")),
                        and(uacAccess.accessKey, isEqualTo(folderTreeNode.getFolderId())));
        if (filePermissionList.size() == 1) {
            builder.where(uacAccess.accessType, isLike("%" + filePermissionList.get(0)));
        }
        else if (filePermissionList.size() == 2) {
            builder.where(uacAccess.accessType, isLike("%" + filePermissionList.get(0)),
                    or(uacAccess.accessType, isLike("%" + filePermissionList.get(1))));
        }
        else if (filePermissionList.size() == 3) {
            builder.where(uacAccess.accessType, isLike("%" + filePermissionList.get(0)),
                    or(uacAccess.accessType, isLike("%" + filePermissionList.get(1))),
                    or(uacAccess.accessType, isLike("%" + filePermissionList.get(2))));
        }
        List<UacAccess> folderAccess = uacAccessDao.selectMany(builder.build().render(RenderingStrategies.MYBATIS3));
        if (folderAccess == null) {
            return folderAccessCodeList;
        }
        for (UacAccess access : folderAccess) {
            folderAccessCodeList.add(access.getAccessCode());
        }

        if (folderTreeNode.getChildren() != null && folderTreeNode.getChildren().size() > 0) {
            for (FolderTreeNodeDto childFolderTreeNode : folderTreeNode.getChildren()) {
                folderAccessCodeList = getFolderAccessCodeList(childFolderTreeNode, folderAccessCodeList);
            }
        }

        return folderAccessCodeList;
    }

    private List<String> getFilePermissionList(Short filePermission) {
        List<String> filePermissionList = new ArrayList<>();
        if ((filePermission % 2) == 1) {
            filePermissionList.add(READ);
        }
        else if ((filePermission % 4) == 2 || (filePermission % 4) == 3) {
            filePermissionList.add(WRITE);
        }
        else if ((filePermission / 4) == 1) {
            filePermissionList.add(DEL);
        }
        return filePermissionList;
    }

}
