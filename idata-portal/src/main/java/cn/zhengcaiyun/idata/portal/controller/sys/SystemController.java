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
package cn.zhengcaiyun.idata.portal.controller.sys;

import cn.zhengcaiyun.idata.commons.context.OperatorContext;
import cn.zhengcaiyun.idata.commons.dto.BaseTreeNodeDto;
import cn.zhengcaiyun.idata.commons.pojo.RestResult;
import cn.zhengcaiyun.idata.develop.dto.label.LabelDefineDto;
import cn.zhengcaiyun.idata.system.dal.model.SysFeature;
import cn.zhengcaiyun.idata.system.dto.*;
import cn.zhengcaiyun.idata.system.service.SystemConfigService;
import cn.zhengcaiyun.idata.system.service.SystemService;
import cn.zhengcaiyun.idata.user.service.TokenService;
import cn.zhengcaiyun.idata.user.service.UserAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author shiyin
 * @date 2021-03-02 11:02
 */
@RestController
public class SystemController {

    @Value("${idataEtl.checkToken:#{null}}")
    private String IDATA_ETL_CHECK_TOKEN;

    @Autowired
    private SystemService systemService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private UserAccessService userAccessService;

    private final String CONFIG_CENTER_ACCESS_CODE = "F_MENU_CONFIG_CENTER";
    private final String CONFIG_LDAP_ACCESS_CODE = "F_MENU_LDAP_CENTER";

    @GetMapping("/p0/sys/state")
    public RestResult<SystemStateDto> getSystemState() {
        return RestResult.success(systemService.getSystemState());
    }

    @GetMapping("/p1/sys/featureTree")
    public RestResult<List<FeatureTreeNodeDto>> getSystemFeatureTree() {
        return RestResult.success(systemService.getFeatureTree(SystemService.FeatureTreeMode.FULL, null));
    }

    @GetMapping("/p1/sys/folderTree")
    public RestResult<List<FolderTreeNodeDto>> getSystemFolderTree() {
        return RestResult.success(systemService.getFolderTree(new HashMap<>()));
    }

    @GetMapping("/p1/sys/configTypes")
    public RestResult<List<String>> getConfigTypes() {
        return RestResult.success(systemConfigService.getConfigTypes());
    }

    @GetMapping("/p1/sys/configs")
    public RestResult<List<ConfigDto>> getConfigsByType(@RequestParam("configType") String configType,
                                                        @RequestHeader("Authorization") String token) throws IllegalAccessException {
        // 内部调用特殊token鉴权
        if (IDATA_ETL_CHECK_TOKEN != null && IDATA_ETL_CHECK_TOKEN.equals(token)) {
            return RestResult.success(systemConfigService.getSystemConfigs(configType));
        }
        if (!userAccessService.checkAccess(OperatorContext.getCurrentOperator().getId(), CONFIG_CENTER_ACCESS_CODE)) {
            throw new IllegalAccessException("没有集成配置权限");
        }
        return RestResult.success(systemConfigService.getSystemConfigs(configType));
    }

    @GetMapping("/p1/sys/ldap/configs")
    public RestResult<List<ConfigDto>> getLDAPConfigs() throws IllegalAccessException {
        if (!userAccessService.checkAccess(OperatorContext.getCurrentOperator().getId(), CONFIG_LDAP_ACCESS_CODE)) {
            throw new IllegalAccessException("没有LDAP配置权限");
        }
        return RestResult.success(systemConfigService.getSystemConfigs(ConfigTypeEnum.LDAP.name()));
    }

    @GetMapping("/p1/sys/features")
    public RestResult<List<SysFeature>> getFeaturesByCodes(@RequestParam(value = "featureCodes", required = false) String featureCodes) {
        List<String> featureCodeList = featureCodes != null ? Arrays.asList(featureCodes.split(",")) : null;
        return RestResult.success(systemConfigService.getFeaturesByCodes(featureCodeList));
    }

    @PostMapping("/p1/sys/xmlConfigValue")
    public RestResult<Map<String, ConfigValueDto>> getXmlConfigValue(@RequestParam("xmlFile") MultipartFile xmlFile) throws IOException {
        return RestResult.success(systemConfigService.getXmlConfigValues(xmlFile));
    }

    @PostMapping("/p1/sys/checkConnection")
    public RestResult<Boolean> checkConfigConnection(@RequestBody ConnectionDto connection) {
        return RestResult.success(systemConfigService.checkConnection(connection));
    }

    @PutMapping("/p1/sys/config")
    public RestResult<List<ConfigDto>> editSystemConfig(@RequestBody List<ConfigDto> configs) {
        return RestResult.success(systemConfigService.editSystemConfigs(configs, OperatorContext.getCurrentOperator().getNickname()));
    }
}
