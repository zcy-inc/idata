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
import cn.zhengcaiyun.idata.dto.system.FeatureTreeNodeDto;
import cn.zhengcaiyun.idata.dto.system.FolderTreeNodeDto;
import cn.zhengcaiyun.idata.dto.user.UserInfoDto;
import cn.zhengcaiyun.idata.user.dal.dao.UacAccessDao;
import cn.zhengcaiyun.idata.user.dal.dao.UacRoleAccessDao;
import cn.zhengcaiyun.idata.user.dal.dao.UacUserDao;
import cn.zhengcaiyun.idata.user.dal.dao.UacUserRoleDao;
import cn.zhengcaiyun.idata.user.dal.model.UacAccess;
import cn.zhengcaiyun.idata.user.dal.model.UacRoleAccess;
import cn.zhengcaiyun.idata.user.dal.model.UacUser;
import cn.zhengcaiyun.idata.user.dal.model.UacUserRole;
import cn.zhengcaiyun.idata.user.service.TokenService;
import cn.zhengcaiyun.idata.user.service.UserManagerService;
import cn.zhengcaiyun.idata.user.service.UserService;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.user.dal.dao.UacAccessDynamicSqlSupport.uacAccess;
import static cn.zhengcaiyun.idata.user.dal.dao.UacRoleAccessDynamicSqlSupport.uacRoleAccess;
import static cn.zhengcaiyun.idata.user.dal.dao.UacUserDynamicSqlSupport.uacUser;
import static cn.zhengcaiyun.idata.user.dal.dao.UacUserRoleDynamicSqlSupport.uacUserRole;
import static com.google.common.base.Preconditions.checkArgument;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author shiyin
 * @date 2021-03-12 23:05
 */
@Service
public class UserManagerServiceImpl implements UserManagerService {

    private final String SALT = "IData";
    private final String DEFAULT_PASSWORD = "123456";

    @Autowired
    private UacUserDao uacUserDao;
    @Autowired
    private UacUserRoleDao uacUserRoleDao;
    @Autowired
    private UacRoleAccessDao uacRoleAccessDao;
    @Autowired
    private UacAccessDao uacAccessDao;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    private final String[] userInfoFields = {"id", "del", "createTime", "creator", "editTime", "editor",
            "username", "sysAdmin", "authType", "nickname", "employeeId", "department", "realName", "email",
            "mobile", "avatar"};

    @Override
    public Page<UserInfoDto> findUsers(String name, Integer limit, Integer offset) {
        var builder = select(uacUser.allColumns()).from(uacUser).where(uacUser.del, isNotEqualTo((short) 1));
        var countBuilder = select(count()).from(uacUser).where(uacUser.del, isNotEqualTo((short) 1));
        if (name != null) {
            builder.where(uacUser.username, isLike("%" + name + "%"),
                    or(uacUser.nickname, isLike("%" + name + "%")),
                    or(uacUser.realName, isLike("%" + name + "%")));
            countBuilder.where(uacUser.username, isLike("%" + name + "%"),
                    or(uacUser.nickname, isLike("%" + name + "%")),
                    or(uacUser.realName, isLike("%" + name + "%")));
        }
        long total = uacUserDao.count(countBuilder.build().render(RenderingStrategies.MYBATIS3));
        builder.orderBy(uacUser.editTime.descending()).limit(Page.limitCheck(limit)).offset(Page.offsetCheck(offset));
        List<UacUser> users = uacUserDao.selectMany(builder.build().render(RenderingStrategies.MYBATIS3));
        return Page.newOne(PojoUtil.copyList(users, UserInfoDto.class, userInfoFields), total);
    }

    @Override
    public UserInfoDto getUserInfo(Long userId) {
        UacUser user = uacUserDao.selectOne(c -> c.where(uacUser.id, isEqualTo(userId)))
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        UserInfoDto userInfoDto = PojoUtil.copyOne(user, UserInfoDto.class, userInfoFields);
        userInfoDto.setRoleCodes(new ArrayList<>());
        userInfoDto.setRoleNames(new ArrayList<>());
        return userInfoDto;
    }

    @Override
    public List<FeatureTreeNodeDto> getUserFeatureTree(Long userId) {
        return null;
    }

    @Override
    public List<FolderTreeNodeDto> getUserFolderTree(Long userId) {
        return null;
    }

    @Override
    public boolean checkAccess(Long userId, String accessCode) {
        UacUser user = uacUserDao.selectOne(c -> c.where(uacUser.id, isEqualTo(userId),
                and(uacUser.del, isNotEqualTo((short) 1))))
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        if (1 == user.getSysAdmin() || 2 == user.getSysAdmin()) return true;
        checkArgument(accessCode != null, "权限编码不能为空");
        List<String> roleCodes = uacUserRoleDao.select(c -> c.where(uacUserRole.userId, isEqualTo(userId),
                and(uacUserRole.del, isNotEqualTo((short) 1)))).stream().map(UacUserRole::getRoleCode)
                .collect(Collectors.toList());
        if (roleCodes.size() == 0) {
            return false;
        }
        UacRoleAccess roleAccess = uacRoleAccessDao.selectOne(c -> c.where(uacRoleAccess.accessCode, isEqualTo(accessCode),
                and(uacRoleAccess.del, isNotEqualTo((short) 1)), and(uacRoleAccess.roleCode, isIn(roleCodes))))
                .orElse(null);
        if (roleAccess != null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkAccess(Long userId, String accessType, String accessKey) {
        UacUser user = uacUserDao.selectOne(c -> c.where(uacUser.id, isEqualTo(userId),
                and(uacUser.del, isNotEqualTo((short) 1))))
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        if (1 == user.getSysAdmin() || 2 == user.getSysAdmin()) return true;
        checkArgument(accessType != null, "权限类型不能为空");
        checkArgument(accessKey != null, "权限资源键不能为空");
        List<String> roleCodes = uacUserRoleDao.select(c -> c.where(uacUserRole.userId, isEqualTo(userId),
                and(uacUserRole.del, isNotEqualTo((short) 1)))).stream().map(UacUserRole::getRoleCode)
                .collect(Collectors.toList());
        if (roleCodes.size() == 0) {
            return false;
        }
        UacAccess access = uacAccessDao.selectOne(c -> c.where(uacAccess.accessType, isEqualTo(accessType),
                and(uacAccess.accessKey, isEqualTo(accessKey)), and(uacAccess.del, isNotEqualTo((short) 1)))).orElse(null);
        if (access == null) {
            return false;
        }
        UacRoleAccess roleAccess = uacRoleAccessDao.selectOne(c -> c.where(uacRoleAccess.accessCode, isEqualTo(access.getAccessCode()),
                and(uacRoleAccess.del, isNotEqualTo((short) 1)), and(uacRoleAccess.roleCode, isIn(roleCodes))))
                .orElse(null);
        if (roleAccess != null) {
            return true;
        }
        return false;
    }

    @Override
    public UserInfoDto create(UserInfoDto userInfoDto, String creator) {
        // TODO 角色未考虑
        return userService.register(userInfoDto, true, creator);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public UserInfoDto edit(UserInfoDto userInfoDto, String editor) {
        // TODO 角色未考虑
        checkArgument(userInfoDto.getId() != null, "用户ID不能为空");
        UacUser user = uacUserDao.selectOne(c -> c.where(uacUser.id, isEqualTo(userInfoDto.getId()),
                and(uacUser.del, isNotEqualTo((short) 1)))).orElse(null);
        checkArgument(user != null, "用户不存在");
        user = PojoUtil.copyOne(user, "id", "nickname", "employeeId", "department",
                "realName", "avatar", "email", "mobile");
        user.setEditor(editor);
        uacUserDao.updateByPrimaryKeySelective(user);
        user = uacUserDao.selectByPrimaryKey(userInfoDto.getId()).orElse(null);
        return PojoUtil.copyOne(user, UserInfoDto.class, userInfoFields);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean resetUserPassword(Long userId, String editor) {
        checkArgument(userId != null, "用户ID不能为空");
        UacUser user = uacUserDao.selectOne(c -> c.where(uacUser.id, isEqualTo(userId),
                and(uacUser.del, isNotEqualTo((short) 1)))).orElse(null);
        checkArgument(user != null, "用户不存在");
        uacUserDao.update(c -> c.set(uacUser.password).equalTo(DigestUtil.md5WithSalt(DEFAULT_PASSWORD, SALT))
                .set(uacUser.editor).equalTo(editor)
                .where(uacUser.id, isEqualTo(userId)));
        return true;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean delete(Long userId, String editor) {
        checkArgument(userId != null, "用户ID不能为空");
        UacUser user = uacUserDao.selectOne(c -> c.where(uacUser.id, isEqualTo(userId),
                and(uacUser.del, isNotEqualTo((short) 1)))).orElse(null);
        checkArgument(user != null, "用户不存在");
        uacUserDao.update(c -> c.set(uacUser.del).equalTo((short) 1)
                .set(uacUser.editor).equalTo(editor)
                .where(uacUser.id, isEqualTo(userId)));
        tokenService.destroyUserToken(userId);
        return true;
    }
}
