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
package cn.zhengcaiyun.idata.user.service;

import cn.zhengcaiyun.idata.commons.encrypt.DigestUtil;
import cn.zhengcaiyun.idata.dto.user.UserInfoDto;
import cn.zhengcaiyun.idata.user.dal.dao.UacUserTokenDao;
import cn.zhengcaiyun.idata.user.dal.model.UacUserToken;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static cn.zhengcaiyun.idata.user.dal.dao.UacUserTokenDynamicSqlSupport.uacUserToken;
import static com.google.common.base.Preconditions.checkArgument;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author shiyin
 * @date 2021-03-14 16:49
 */
@Service
public class TokenService {
    @Value("${auth.jwt.security}")
    private String jwtSecurity;
    private final int TOKEN_TTL_SECOND = 3600 * 24;

    @Autowired
    private UacUserTokenDao uacUserTokenDao;

    @Transactional(rollbackFor = Throwable.class)
    public String createToken(UserInfoDto userInfo) {
        checkArgument(userInfo.getId() != null, "用户ID不能为空");
        Map<String, Object> map = new HashMap<>();
        map.put("id", userInfo.getId());
        map.put("username", userInfo.getUsername());
        map.put("nickname", userInfo.getNickname());
        map.put("employeeId", userInfo.getEmployeeId());
        map.put("realName", userInfo.getRealName());
        map.put("avatar", userInfo.getAvatar());
        map.put("email", userInfo.getEmail());
        map.put("mobile", userInfo.getMobile());
        map.put("exp", System.currentTimeMillis() / 1000 + TOKEN_TTL_SECOND);
        UacUserToken userToken =  new UacUserToken();
        userToken.setUserId(userInfo.getId());
        String token = Jwts.builder()
                .setPayload(JSON.toJSONString(map))
                .signWith(SignatureAlgorithm.HS512, jwtSecurity)
                .compact();
        userToken.setToken(DigestUtil.md5(token));
        uacUserTokenDao.update(c -> c.set(uacUserToken.del).equalTo(1)
                .where(uacUserToken.userId, isEqualTo(userInfo.getId()),
                        and(uacUserToken.del, isNotEqualTo(1))));
        uacUserTokenDao.insertSelective(userToken);
        return token;
    }

    public boolean checkToken(String token) {
        UacUserToken userToken = findByToken(token);
        if (userToken == null) {
            return false;
        }
        boolean isExpired;
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecurity)
                    .parseClaimsJws(token)
                    .getBody();
            isExpired = new Date().after(claims.getExpiration());
        } catch (Exception e) {
            // expired token
            isExpired = true;
        }
        if (!isExpired) {
            return true;
        }
        // 防止一直在使用，token过期的问题
        if (System.currentTimeMillis() - userToken.getEditTime().getTime() < 6 * 3600 * 1000) {
            return true;
        }
        else if (System.currentTimeMillis() - userToken.getEditTime().getTime() >= 6 * 3600 * 1000
                && System.currentTimeMillis() - userToken.getEditTime().getTime() <= 12 * 3600 * 1000) {
            uacUserTokenDao.update(c -> c.set(uacUserToken.editTime).equalTo(new Date())
                    .where(uacUserToken.id, isEqualTo(userToken.getId())));
            return true;
        }
        else {
            return false;
        }
    }

    private UacUserToken findByToken(String token) {
        String tokenMd5 = DigestUtil.md5(token);
        return uacUserTokenDao.selectOne(c -> c.where(uacUserToken.token, isEqualTo(tokenMd5),
                and(uacUserToken.del, isNotEqualTo(1)))).orElse(null);
    }

    public String getNickname(HttpServletRequest request) {
        return getPayload(request.getHeader("Authorization")).getString("nickname");
    }

    public Long getUserId(HttpServletRequest request) {
        return getPayload(request.getHeader("Authorization")).getLong("id");
    }

    public String getToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    private JSONObject getPayload(String token) {
        return JSON.parseObject(new String(Base64.getUrlDecoder()
                .decode(StringUtils.split(token.replace("\r\n", ""), ".")[1])));
    }

    @Transactional(rollbackFor = Throwable.class)
    public boolean destroyToken(String token) {
        String tokenMd5 = DigestUtil.md5(token);
        uacUserTokenDao.update(c -> c.set(uacUserToken.del).equalTo(1)
                .where(uacUserToken.del, isNotEqualTo(1), and(uacUserToken.token, isEqualTo(tokenMd5))));
        return true;
    }

    @Transactional(rollbackFor = Throwable.class)
    public boolean destroyUserToken(Long userId) {
        uacUserTokenDao.update(c -> c.set(uacUserToken.del).equalTo(1)
                .where(uacUserToken.del, isNotEqualTo(1), and(uacUserToken.userId, isEqualTo(userId))));
        return true;
    }
}
