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

import cn.zhengcaiyun.idata.commons.dto.general.SingleIdPair;
import cn.zhengcaiyun.idata.commons.encrypt.DigestUtil;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.user.dal.dao.UacRoleDao;
import cn.zhengcaiyun.idata.user.dal.dao.UacUserDao;
import cn.zhengcaiyun.idata.user.dal.dao.UacUserRoleDao;
import cn.zhengcaiyun.idata.user.dal.model.UacRole;
import cn.zhengcaiyun.idata.user.dal.model.UacUser;
import cn.zhengcaiyun.idata.user.dal.model.UacUserRole;
import cn.zhengcaiyun.idata.user.dal.repo.UserRepo;
import cn.zhengcaiyun.idata.user.dto.UserInfoDto;
import cn.zhengcaiyun.idata.user.service.TokenService;
import cn.zhengcaiyun.idata.user.service.UserManagerService;
import cn.zhengcaiyun.idata.user.service.UserService;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.user.dal.dao.UacRoleDynamicSqlSupport.uacRole;
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
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UacUserDao uacUserDao;
    @Autowired
    private UacUserRoleDao uacUserRoleDao;
    @Autowired
    private UacRoleDao uacRoleDao;
    @Autowired
    private UserRepo userRepo;

    private final String[] userInfoFields = {"id", "del", "createTime", "creator", "editTime", "editor",
            "username", "sysAdmin", "authType", "nickname", "employeeId", "department", "realName", "email",
            "mobile", "avatar"};

    @Override
    public Page<UserInfoDto> findUsers(String name, Integer limit, Integer offset) {
        var builder = select(uacUser.allColumns()).from(uacUser).where(uacUser.del, isNotEqualTo(1));
        var countBuilder = select(count()).from(uacUser).where(uacUser.del, isNotEqualTo(1));
        if (name != null) {
            builder.and(uacUser.username, isLike("%" + name + "%"),
                    or(uacUser.nickname, isLike("%" + name + "%")),
                    or(uacUser.realName, isLike("%" + name + "%")));
            countBuilder.and(uacUser.username, isLike("%" + name + "%"),
                    or(uacUser.nickname, isLike("%" + name + "%")),
                    or(uacUser.realName, isLike("%" + name + "%")));
        }
        long total = uacUserDao.count(countBuilder.build().render(RenderingStrategies.MYBATIS3));
        builder.orderBy(uacUser.editTime.descending()).limit(Page.limitCheck(limit)).offset(Page.offsetCheck(offset));
        List<UacUser> users = uacUserDao.selectMany(builder.build().render(RenderingStrategies.MYBATIS3));
        return Page.newOne(PojoUtil.copyList(users, UserInfoDto.class, userInfoFields)
                .stream().peek(this::setRoleInfos).collect(Collectors.toList()), total);
    }

    @Override
    public UserInfoDto getUserInfo(Long userId) {
        UacUser user = uacUserDao.selectOne(c -> c.where(uacUser.id, isEqualTo(userId)))
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        UserInfoDto userInfoDto = PojoUtil.copyOne(user, UserInfoDto.class, userInfoFields);
        setRoleInfos(userInfoDto);
        return userInfoDto;
    }

    private void setRoleInfos(UserInfoDto userInfo) {
        List<String> roleCodes = uacUserRoleDao.select(c -> c.where(uacUserRole.userId, isEqualTo(userInfo.getId()),
                and(uacUserRole.del, isNotEqualTo(1)))).stream().map(UacUserRole::getRoleCode).collect(Collectors.toList());
        List<String> roleNames = new ArrayList<>();
        if (roleCodes.size() > 0) {
            roleNames = uacRoleDao.select(c -> c.where(uacRole.roleCode, isIn(roleCodes),
                    and(uacRole.del, isNotEqualTo(1)))).stream().map(UacRole::getRoleName).collect(Collectors.toList());
        }
        userInfo.setRoleCodes(roleCodes);
        userInfo.setRoleNames(roleNames);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public UserInfoDto create(UserInfoDto userInfoDto, String creator) {
        UserInfoDto userInfo = userService.register(userInfoDto, true, creator);
        // 插入用户角色关系表
        if (userInfoDto.getRoleCodes() != null && userInfoDto.getRoleCodes().size() > 0) {
            userInfoDto.getRoleCodes().forEach(roleCode -> {
                UacRole role = uacRoleDao.selectOne(c -> c.where(uacRole.roleCode, isEqualTo(roleCode),
                        and(uacRole.del, isNotEqualTo(1)))).orElse(null);
                checkArgument(role != null, roleCode + "角色不存在");
                UacUserRole userRole = new UacUserRole();
                userRole.setCreator(creator);
                userRole.setRoleCode(roleCode);
                userRole.setUserId(userInfo.getId());
                uacUserRoleDao.insertSelective(userRole);
            });
        }
        return userInfo;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public UserInfoDto edit(UserInfoDto userInfoDto, String editor) {
        checkArgument(userInfoDto.getId() != null, "用户ID不能为空");
        UacUser user = uacUserDao.selectOne(c -> c.where(uacUser.id, isEqualTo(userInfoDto.getId()),
                and(uacUser.del, isNotEqualTo(1)))).orElse(null);
        checkArgument(user != null, "用户不存在");

        user = PojoUtil.copyOne(userInfoDto, UacUser.class, "id", "nickname", "employeeId", "department",
                "realName", "avatar", "email", "mobile");
        user.setEditor(editor);
        uacUserDao.updateByPrimaryKeySelective(user);
        // 更新用户角色关系表
        if (userInfoDto.getRoleCodes() != null) {
            Set<String> newRoleCodes = new HashSet<>(userInfoDto.getRoleCodes());
            Set<String> curRoleCodes = uacUserRoleDao.select(c -> c.where(uacUserRole.userId, isEqualTo(userInfoDto.getId()),
                    and(uacUserRole.del, isNotEqualTo(1)))).stream().map(UacUserRole::getRoleCode)
                    .collect(Collectors.toSet());
            Set<String> addRoleCodes = new HashSet<>(newRoleCodes);
            addRoleCodes.removeAll(curRoleCodes);
            addRoleCodes.forEach(addRoleCode -> {
                UacRole role = uacRoleDao.selectOne(c -> c.where(uacRole.roleCode, isEqualTo(addRoleCode),
                        and(uacRole.del, isNotEqualTo(1)))).orElse(null);
                checkArgument(role != null, addRoleCode + "角色不存在");
                UacUserRole userRole = new UacUserRole();
                userRole.setUserId(userInfoDto.getId());
                userRole.setRoleCode(addRoleCode);
                userRole.setCreator(editor);
                uacUserRoleDao.insertSelective(userRole);
            });
            Set<String> delRoleCodes = new HashSet<>(curRoleCodes);
            delRoleCodes.removeAll(newRoleCodes);
            delRoleCodes.forEach(delRoleCode -> uacUserRoleDao.update(c -> c.set(uacUserRole.del).equalTo(1)
                    .where(uacUserRole.userId, isEqualTo(userInfoDto.getId()),
                    and(uacUserRole.roleCode, isEqualTo(delRoleCode)),
                            and(uacUserRole.del, isNotEqualTo(1)))));
        }
        user = uacUserDao.selectByPrimaryKey(userInfoDto.getId()).orElse(null);
        return PojoUtil.copyOne(user, UserInfoDto.class, userInfoFields);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean resetUserPassword(Long userId, String editor) {
        checkArgument(userId != null, "用户ID不能为空");
        UacUser user = uacUserDao.selectOne(c -> c.where(uacUser.id, isEqualTo(userId),
                and(uacUser.del, isNotEqualTo(1)))).orElse(null);
        checkArgument(user != null, "用户不存在");
        checkArgument(user.getSysAdmin() != 1, "不能重置初始系统管理员的密码");
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
                and(uacUser.del, isNotEqualTo(1)))).orElse(null);
        checkArgument(user != null, "用户不存在");
        checkArgument(user.getSysAdmin() != 1, "不能删除初始系统管理员");
        uacUserDao.update(c -> c.set(uacUser.del).equalTo(1)
                .set(uacUser.editor).equalTo(editor)
                .where(uacUser.id, isEqualTo(userId)));
        tokenService.destroyUserToken(userId);
        uacUserRoleDao.update(c -> c.set(uacUserRole.del).equalTo(1)
                .set(uacUserRole.editor).equalTo(editor)
                .where(uacUserRole.userId, isEqualTo(userId),
                        and(uacUserRole.del, isNotEqualTo(1))));
        return true;
    }

    @Override
    public List<SingleIdPair<String>> getUserKeyValList() {
        return userRepo.queryList()
                .stream()
                .map(user -> new SingleIdPair<String>(user.getId().toString(),user.getNickname()))
                .collect(Collectors.toList());
    }

}
