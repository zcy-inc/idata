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
package cn.zhengcaiyun.idata.portal.config;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.system.dal.dao.SysFeatureDao;
import cn.zhengcaiyun.idata.system.dal.model.SysFeature;
import cn.zhengcaiyun.idata.user.dal.dao.UacUserDao;
import cn.zhengcaiyun.idata.user.dal.model.UacUser;
import cn.zhengcaiyun.idata.user.service.TokenService;
import cn.zhengcaiyun.idata.user.service.UserAccessService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static cn.zhengcaiyun.idata.system.dal.dao.SysFeatureDynamicSqlSupport.sysFeature;
import static cn.zhengcaiyun.idata.user.dal.dao.UacUserDynamicSqlSupport.uacUser;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author shiyin
 * @date 2021-03-14 21:55
 */
@Component
public class WebAuthFilter implements Filter {

    private final String[] featureTypes = {"F_MENU", "F_ICON"};

//    @Value("${access.mode:#{null}}")
//    private String ACCESS_MODE;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UacUserDao uacUserDao;
    @Autowired
    private UserAccessService userAccessService;
    @Autowired
    private SysFeatureDao sysFeatureDao;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        String token;
        MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest((HttpServletRequest) servletRequest);
        if (mutableRequest.getHeader("Authorization") != null) {
            token = mutableRequest.getHeader("Authorization");
        }
        else {
            Cookie tokenCookie = WebUtils.getCookie((HttpServletRequest) servletRequest, "Authorization");
            token = (tokenCookie != null ? tokenCookie.getValue() : null);
            if (token != null) {
                mutableRequest.putHeader("Authorization", token);
            }
        }
        String path = ((HttpServletRequest) servletRequest).getRequestURI();
        if (path.contains("/v2/api-docs")
                || path.contains("/p0/")) {
            filterChain.doFilter(mutableRequest, servletResponse);
            return;
        }
        if (token == null || !tokenService.checkToken(token)) {
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_OK);
            servletResponse.setContentType("application/json; charset=UTF-8");
            servletResponse.getWriter().write(JSON.toJSONString(
                    RestResult.error(RestResult.UNAUTHORIZED_ERROR_CODE, "未登录", null)));
            servletResponse.getWriter().flush();
            return;
        }
        // check feature
//        else {
//            // accessMode选择使用哪个模式的权限
//            if ("idata-pro".equals(ACCESS_MODE)) {
//                // 兼容旧版权限
//                if (path.contains("/p1/uac/currentUser")
//                        || path.contains("/p1/uac/currentFeatureTree")
//                        // 旧版鉴权调用
//                        || path.contains("/p1/uac/currentAccessKeys")
//                        || path.contains("/p1/uac/checkCurrentAccess")
//                        || path.contains("/p1/uac/resourceAccess")) {
//                    filterChain.doFilter(mutableRequest, servletResponse);
//                    return;
//                }
//                // 新版权限
//                Long userId = tokenService.getUserId(mutableRequest);
//                UacUser user = uacUserDao.selectOne(c ->
//                        c.where(uacUser.id, isEqualTo(userId), and(uacUser.del, isNotEqualTo(1)))).get();
//                if (1 != user.getSysAdmin() && 2 != user.getSysAdmin()) {
//                    SysFeature feature = sysFeatureDao.selectOne(c -> c.where(sysFeature.del, isNotEqualTo(1),
//                            and(sysFeature.featureUrlPath, isEqualTo(path))))
//                            .orElse(null);
//                    if (feature != null && Arrays.asList(featureTypes).contains(feature.getFeatureType())
//                            && !userAccessService.checkFeatureAccess(userId, path)) {
//                        servletResponse.setContentType("application/json; charset=UTF-8");
//                        servletResponse.getWriter().write(JSON.toJSONString(
//                                RestResult.error(RestResult.FORBIDDEN_ERROR_CODE, "无权限", null)));
//                        servletResponse.getWriter().flush();
//                        return;
//                    }
//                }
//            }
//        }
        else {
            if (path.contains("/p1/uac/currentUser")
                    || path.contains("/p1/uac/currentFeatureTree")
                    // 旧版鉴权调用
                    || path.contains("/p1/uac/currentAccessKeys")
                    || path.contains("/p1/uac/checkCurrentAccess")
                    || path.contains("/p1/uac/resourceAccess")) {
                filterChain.doFilter(mutableRequest, servletResponse);
                return;
            }
            // 新版权限
//            Long userId = tokenService.getUserId(mutableRequest);
//            UacUser user = uacUserDao.selectOne(c ->
//                    c.where(uacUser.id, isEqualTo(userId), and(uacUser.del, isNotEqualTo(1)))).get();
//            if (1 != user.getSysAdmin() && 2 != user.getSysAdmin()) {
//                SysFeature feature = sysFeatureDao.selectOne(c -> c.where(sysFeature.del, isNotEqualTo(1),
//                        and(sysFeature.featureUrlPath, isEqualTo(path))))
//                        .orElse(null);
//                if (feature != null && Arrays.asList(featureTypes).contains(feature.getFeatureType())
//                        && !userAccessService.checkFeatureAccess(userId, path)) {
//                    servletResponse.setContentType("application/json; charset=UTF-8");
//                    servletResponse.getWriter().write(JSON.toJSONString(
//                            RestResult.error(RestResult.FORBIDDEN_ERROR_CODE, "无权限", null)));
//                    servletResponse.getWriter().flush();
//                    return;
//                }
//            }
        }

        try {
            setCurrentOperator(token);
            filterChain.doFilter(mutableRequest, servletResponse);
        } finally {
            // 清理操作人数据
            clearCurrentOperator();
        }
    }

    private void setCurrentOperator(String token){
        OperatorContext.setCurrentOperator(OperatorContext.from(token));
    }

    private void clearCurrentOperator(){
        OperatorContext.clearCurrentOperator();
    }

    static class MutableHttpServletRequest extends HttpServletRequestWrapper {
        private final Map<String, String> customHeaders;

        MutableHttpServletRequest(HttpServletRequest request){
            super(request);
            this.customHeaders = new HashMap<>();
        }

        void putHeader(String name, String value){
            this.customHeaders.put(name, value);
        }

        public String getHeader(String name) {
            String headerValue = customHeaders.get(name);

            if (headerValue != null){
                return headerValue;
            }
            return ((HttpServletRequest) getRequest()).getHeader(name);
        }

        public Enumeration<String> getHeaderNames() {
            Set<String> set = new HashSet<>(customHeaders.keySet());

            Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
            while (e.hasMoreElements()) {
                String n = e.nextElement();
                set.add(n);
            }
            return Collections.enumeration(set);
        }

        public Enumeration<String> getHeaders(String name) {
            Set<String> headerValues = new HashSet<>();
            if (this.customHeaders.get(name) != null) {
                headerValues.add(this.customHeaders.get(name));
            }

            Enumeration<String> underlyingHeaderValues = ((HttpServletRequest) getRequest()).getHeaders(name);
            while (underlyingHeaderValues.hasMoreElements()) {
                headerValues.add(underlyingHeaderValues.nextElement());
            }
            return Collections.enumeration(headerValues);
        }
    }
}
