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
import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.dto.user.SignInAuthTypeEnum;
import cn.zhengcaiyun.idata.dto.user.SignInDto;
import cn.zhengcaiyun.idata.dto.user.UserInfoDto;
import cn.zhengcaiyun.idata.user.dal.dao.UacUserDao;
import cn.zhengcaiyun.idata.user.dal.model.UacUser;
import cn.zhengcaiyun.idata.user.service.LdapService;
import cn.zhengcaiyun.idata.user.service.TokenService;
import cn.zhengcaiyun.idata.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static cn.zhengcaiyun.idata.user.dal.dao.UacUserDynamicSqlSupport.uacUser;
import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author shiyin
 * @date 2021-03-09 15:22
 */
@Service
public class UserServiceImpl implements UserService {

    private final String SALT = "IData";

    @Autowired
    private UacUserDao uacUserDao;
    @Autowired
    private LdapService ldapService;
    @Autowired
    private TokenService tokenService;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public UserInfoDto register(UserInfoDto userInfoDto, boolean isManagerMode, String creator) {
        // TODO 系统注册开关未考虑
        checkArgument(userInfoDto != null, "注册信息不能为空");
        checkArgument(userInfoDto.getUsername() != null, "用户名不能为空");
        Optional<UacUser> oneUser = uacUserDao.selectOne(c ->
                c.where(uacUser.username, isEqualTo(userInfoDto.getUsername())));
        checkArgument(oneUser.isEmpty(), "用户名已存在");
        checkArgument(userInfoDto.getAuthType() != null, "认证方式不能为空");
        SignInAuthTypeEnum.valueOf(userInfoDto.getAuthType());
        UacUser user;
        if (SignInAuthTypeEnum.REGISTER.name().equals(userInfoDto.getAuthType())) {
            checkArgument(userInfoDto.getPassword() != null, "密码不能为空");
            checkArgument(userInfoDto.getNickname() != null, "昵称不能为空");
            user = PojoUtil.copyOne(userInfoDto, UacUser.class, "username", "authType", "password",
                    "nickname", "employeeId", "department", "realName", "email", "mobile");
            if (isManagerMode) {
                user.setCreator(creator);
            }
            else {
                user.setCreator(userInfoDto.getNickname());
            }
            user.setPassword(DigestUtil.md5WithSalt(user.getPassword(), SALT));
            uacUserDao.insertSelective(user);
        }
        else {
            // LDAP
            if (isManagerMode) {
                checkArgument(isNotEmpty(creator), "创建者不能为空");
                UserInfoDto ldapUser = ldapService.findUserByUsername(userInfoDto.getUsername());
                checkArgument(ldapUser != null, "LDAP账号不存在");
                user = PojoUtil.copyOne(userInfoDto, UacUser.class, "username");
                user = PojoUtil.copyTo(ldapUser, user, "nickname", "employeeId", "realName",
                        "email", "mobile", "avatar");
                user.setAuthType(SignInAuthTypeEnum.LDAP.name());
                user.setCreator(creator);
                uacUserDao.insertSelective(user);
            }
            else {
                throw new IllegalArgumentException(userInfoDto.getAuthType() + "认证类型不需要注册，请直接登录");
            }
        }
        UserInfoDto userInfo = PojoUtil.copyOne(user, UserInfoDto.class,
                "id", "creator", "username", "authType", "nickname",
                "employeeId", "department", "realName", "email", "mobile", "avatar");
        // 生成token
        if (!isManagerMode) {
            userInfo.setToken(tokenService.createToken(userInfo));
        }
        return userInfo;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public UserInfoDto signIn(SignInDto signInDto) {
        checkArgument(signInDto != null, "登录信息不能为空");
        checkArgument(signInDto.getUsername() != null, "用户名不能为空");
        checkArgument(signInDto.getPassword() != null, "密码不能为空");
        UacUser oneUser = uacUserDao.selectOne(c -> c.where(uacUser.username,
                isEqualTo(signInDto.getUsername()))).orElse(null);
        UacUser user;
        if (oneUser != null) {
            checkArgument(oneUser.getDel() != 1, "账号已删除");
            if (SignInAuthTypeEnum.REGISTER.name().equals(oneUser.getAuthType())) {
                checkArgument(DigestUtil.md5WithSalt(signInDto.getPassword(), SALT).equals(oneUser.getPassword()),
                        "账号密码错误");
            }
            else {
                // LDAP登录
                checkArgument(ldapService.checkUser(signInDto.getUsername(), signInDto.getPassword()),
                        "LDAP账号密码错误");
            }
            user = oneUser;
        }
        else {
            // 没有账号尝试用LDAP方式注册并登录
            checkArgument(ldapService.checkUser(signInDto.getUsername(), signInDto.getPassword()),
                    "LDAP账号密码错误");
            UserInfoDto ldapUser = ldapService.findUserByUsername(signInDto.getUsername());
            checkArgument(ldapUser != null, "LDAP账号不存在");
            user = PojoUtil.copyOne(signInDto, UacUser.class, "username");
            user = PojoUtil.copyTo(ldapUser, user, "nickname", "employeeId", "realName",
                    "email", "mobile", "avatar");
            user.setAuthType(SignInAuthTypeEnum.LDAP.name());
            user.setCreator("");
            uacUserDao.insertSelective(user);
        }
        // 生成token
        UserInfoDto userInfo = PojoUtil.copyOne(user, UserInfoDto.class,
                "id", "username", "authType", "nickname",
                "employeeId", "department", "realName", "email", "mobile", "avatar");
        userInfo.setToken(tokenService.createToken(userInfo));
        return userInfo;
    }

    @Override
    public boolean signOut(String token) {
        return tokenService.destroyToken(token);
    }
}
