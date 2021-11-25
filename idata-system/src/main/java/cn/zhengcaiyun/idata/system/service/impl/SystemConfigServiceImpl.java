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
package cn.zhengcaiyun.idata.system.service.impl;

import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.commons.rpc.HttpInput;
import cn.zhengcaiyun.idata.commons.rpc.HttpUtil;
import cn.zhengcaiyun.idata.system.dal.dao.SysConfigDao;
import cn.zhengcaiyun.idata.system.dal.dao.SysFeatureDao;
import cn.zhengcaiyun.idata.system.dal.model.SysConfig;
import cn.zhengcaiyun.idata.system.dal.model.SysFeature;
import cn.zhengcaiyun.idata.system.dal.repo.SystemConfigRepo;
import cn.zhengcaiyun.idata.system.dto.ConfigDto;
import cn.zhengcaiyun.idata.system.dto.ConfigTypeEnum;
import cn.zhengcaiyun.idata.system.dto.ConfigValueDto;
import cn.zhengcaiyun.idata.system.dto.ConnectionDto;
import cn.zhengcaiyun.idata.system.service.SystemConfigService;
import cn.zhengcaiyun.idata.system.zcy.ZcyService;
import com.alibaba.fastjson.JSON;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.system.dal.dao.SysConfigDynamicSqlSupport.*;
import static cn.zhengcaiyun.idata.system.dal.dao.SysConfigDynamicSqlSupport.sysConfig;
import static cn.zhengcaiyun.idata.system.dal.dao.SysFeatureDynamicSqlSupport.sysFeature;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.mybatis.dynamic.sql.SqlBuilder.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @author caizhedong
 * @date 2021-11-03 下午4:45
 */

@Service
public class SystemConfigServiceImpl implements SystemConfigService {

    private static final Logger logger = LoggerFactory.getLogger(SystemConfigServiceImpl.class);

    @Autowired
    private SysFeatureDao sysFeatureDao;
    @Autowired
    private SystemConfigRepo systemConfigRepo;

    @Override
    public List<String> getConfigTypes() {
        return Arrays.stream(ConfigTypeEnum.values()).collect(Collectors.toList())
                .stream().map(Enum::name).collect(Collectors.toList());
    }

    @Override
    public List<ConfigDto> getSystemConfigs(String configType) {
        ConfigTypeEnum configTypeEnum = ConfigTypeEnum.valueOf(configType);
        List<SysConfig> configList = systemConfigRepo.getConfigsByType(configType);
        if (configList != null && configList.size() > 0) {
            return PojoUtil.copyList(configList, ConfigDto.class);
        }
        else {
            return null;
        }
    }

    @Override
    public SysFeature getFeature(String urlPath) {
        return sysFeatureDao.selectOne(c -> c.where(sysFeature.del, isNotEqualTo(1),
                and(sysFeature.featureUrlPath, isLike("%" + urlPath + "%")))).orElse(null);
    }

    @Override
    public boolean checkConnection(ConnectionDto connection) {
        ConfigTypeEnum configTypeEnum = ConfigTypeEnum.valueOf(connection.getConnectionType());
        checkArgument(ConfigTypeEnum.DS == configTypeEnum || ConfigTypeEnum.HIVE_METASTORE == configTypeEnum
                || ConfigTypeEnum.LIVY == configTypeEnum, "配置类型有误，无法测试连通性");
        if (ConfigTypeEnum.HIVE_METASTORE == configTypeEnum) {
            return checkJdbcConnection(connection);
        }
        else {
            return checkHttpConnection(connection);
        }
    }

    @Override
    public Map<String, ConfigValueDto> getXmlConfigValues(MultipartFile xmlFile) {
        Map<String, ConfigValueDto> echoConfigValueMap = new HashMap<>();
        if (xmlFile != null && xmlFile.getSize() > 0) {
            try {
                InputStream in = xmlFile.getInputStream();
                File file = new File(Objects.requireNonNull(xmlFile.getOriginalFilename()));
                copyInputStreamToFile(in, file);
                in.close();
                SAXReader reader = new SAXReader();
                Document doc = reader.read(file);
                Element root = doc.getRootElement();
                Element foo;
                for (Iterator i = root.elementIterator(); i.hasNext();) {
                    foo = (Element) i.next();
                    ConfigValueDto echo = new ConfigValueDto();
                    echo.setConfigValue(foo.elementText("value"));
                    echoConfigValueMap.put(foo.elementText("name"), echo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return echoConfigValueMap.size() == 0 ? new HashMap<>() : echoConfigValueMap;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public List<ConfigDto> editSystemConfigs(List<ConfigDto> configs, String editor) {
        return configs.stream().map(config -> editConfig(config, editor)).collect(Collectors.toList());
    }

    private boolean checkJdbcConnection(ConnectionDto connection) {
        boolean isConnection = false;
        try {
            Connection conn = DriverManager.getConnection(connection.getConnectionUri(), connection.getUsername(), connection.getPassword());
            isConnection =  nonNull(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isConnection;
    }

    private boolean checkHttpConnection(ConnectionDto connection) {
        boolean isConnected = false;
        ConfigTypeEnum configTypeEnum = ConfigTypeEnum.valueOf(connection.getConnectionType());
        HttpInput httpInput = new HttpInput();
        if (ConfigTypeEnum.DS == configTypeEnum) {
            httpInput.setUri(connection.getConnectionUri() + "/users/get-user-info");
            Map<String, String> headerMap = new HashMap<>();
            headerMap.put("token", connection.getToken());
            httpInput = httpInput.setMethod(RequestMethod.GET.name()).setHeaderMap(headerMap);
        }
        else if (ConfigTypeEnum.LIVY == configTypeEnum) {
            httpInput.setUri(connection.getConnectionUri()).setMethod(RequestMethod.GET.name());
        }
        try (Response response = HttpUtil.executeHttpRequest(httpInput)) {
            isConnected = response.code() == 200 || response.code() == 201;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isConnected;
    }

    private ConfigDto editConfig(ConfigDto configDto, String editor) {
        checkArgument(configDto.getId() != null, "配置ID不能为空");
        checkArgument(StringUtils.isNotEmpty(editor), "修改者不能为空");
        SysConfig config = systemConfigRepo.getConfigById(configDto.getId());
        checkArgument(config != null, "配置不存在");

        config.setValueOne(configDto.getValueOne());
        config.setEditor(editor);
        config.setEditTime(new Timestamp((System.currentTimeMillis())));
        if (systemConfigRepo.updateById(config)) {
            return PojoUtil.copyOne(systemConfigRepo.getConfigById(configDto.getId()),
                    ConfigDto.class);
        }
        else {
            return null;
        }
    }

    private static void copyInputStreamToFile(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
