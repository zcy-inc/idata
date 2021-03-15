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
import cn.zhengcaiyun.idata.user.dal.dao.UacRoleDao;
import cn.zhengcaiyun.idata.user.dal.model.UacRole;
import cn.zhengcaiyun.idata.user.service.RoleService;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Autowired
    private UacRoleDao uacRoleDao;

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
        // TODO 角色的权限没考虑
        checkArgument(roleDto.getRoleCode() != null, "角色编码不能为空");
        UacRole oneRole = uacRoleDao.selectOne(c -> c.where(uacRole.roleCode,
                isEqualTo(roleDto.getRoleCode()))).orElse(null);
        checkArgument(oneRole == null, "角色编码已存在");
        checkArgument(roleDto.getRoleName() != null, "角色名称不能为空");
        checkArgument(creator != null, "创建者不能为空");

        UacRole role = PojoUtil.copyOne(roleDto, UacRole.class, "roleCode", "roleName");
        role.setCreator(creator);
        uacRoleDao.insertSelective(role);
        return PojoUtil.copyOne(uacRoleDao.selectByPrimaryKey(role.getId()), RoleDto.class);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public RoleDto edit(RoleDto roleDto, String editor) {
        // TODO 角色的权限没考虑
        checkArgument(roleDto.getId() != null, "角色ID不能为空");
        UacRole oneRole = uacRoleDao.selectOne(c -> c.where(uacRole.id, isEqualTo(roleDto.getId()),
                and(uacRole.del, isNotEqualTo((short) 1)))).orElse(null);
        checkArgument(oneRole != null, "角色不存在");
        checkArgument(editor != null, "修改者不能为空");

        UacRole role = PojoUtil.copyOne(roleDto, UacRole.class, "id", "roleName");
        role.setEditor(editor);
        uacRoleDao.updateByPrimaryKeySelective(role);
        return PojoUtil.copyOne(uacRoleDao.selectByPrimaryKey(role.getId()), RoleDto.class);
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
}
