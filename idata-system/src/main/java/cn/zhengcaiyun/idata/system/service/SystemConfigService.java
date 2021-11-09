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
package cn.zhengcaiyun.idata.system.service;

import cn.zhengcaiyun.idata.system.dal.model.SysFeature;
import cn.zhengcaiyun.idata.system.dto.ConfigDto;
import cn.zhengcaiyun.idata.system.dto.ConfigValueDto;
import cn.zhengcaiyun.idata.system.dto.ConnectionDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author caizhedong
 * @date 2021-11-03 下午4:39
 */

public interface SystemConfigService {
    List<String> getConfigTypes();
    List<ConfigDto> getSystemConfigs(String configType);
    SysFeature getFeature(String urlPath);
    boolean checkConnection(ConnectionDto connection);
    Map<String, ConfigValueDto> getXmlConfigValues(MultipartFile xmlFile) throws IOException;
    List<ConfigDto> editSystemConfigs(List<ConfigDto> configs, String editor);
}
