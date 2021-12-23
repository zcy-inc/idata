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
package cn.zhengcaiyun.idata.system.api.impl;

import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.system.api.SystemConfigApi;
import cn.zhengcaiyun.idata.system.dal.model.SysConfig;
import cn.zhengcaiyun.idata.system.dal.repo.SystemConfigRepo;
import cn.zhengcaiyun.idata.system.dto.ConfigDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * @author caizhedong
 * @date 2021-12-23 下午1:44
 */

@Service
public class SystemConfigApiImpl implements SystemConfigApi {

    @Autowired
    private SystemConfigRepo systemConfigRepo;

    @Override
    public ConfigDto getSystemConfigByKey(String configKey) {
        checkArgument(StringUtils.isNotBlank(configKey), "配置键不能为空");
        Optional<SysConfig> configOptional = systemConfigRepo.queryByKey(configKey);
        checkState(configOptional.isPresent(), "配置键%s没有相应的配置", configKey);
        return PojoUtil.copyOne(configOptional.get(), ConfigDto.class);
    }
}
