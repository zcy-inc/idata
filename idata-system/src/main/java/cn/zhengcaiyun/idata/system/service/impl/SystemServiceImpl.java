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
import cn.zhengcaiyun.idata.dto.system.FeatureTreeNodeDto;
import cn.zhengcaiyun.idata.dto.system.FolderTreeNodeDto;
import cn.zhengcaiyun.idata.dto.system.SysConfKeyEnum;
import cn.zhengcaiyun.idata.dto.system.SystemStateDto;
import cn.zhengcaiyun.idata.system.IDataSystem;
import cn.zhengcaiyun.idata.system.dal.dao.SysConfigDao;
import cn.zhengcaiyun.idata.system.dal.dao.SysFeatureDao;
import cn.zhengcaiyun.idata.system.dal.model.SysConfig;
import cn.zhengcaiyun.idata.system.dal.model.SysFeature;
import cn.zhengcaiyun.idata.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.system.dal.dao.SysConfigDynamicSqlSupport.sysConfig;
import static cn.zhengcaiyun.idata.system.dal.dao.SysFeatureDynamicSqlSupport.sysFeature;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;
import static org.mybatis.dynamic.sql.SqlBuilder.isNotEqualTo;

/**
 * @author shiyin
 * @date 2021-03-13 23:12
 */
@Service
public class SystemServiceImpl implements SystemService {

    @Autowired
    private SysConfigDao sysConfigDao;
    @Autowired
    private SysFeatureDao sysFeatureDao;

    @Override
    public SystemStateDto getSystemState() {
        SystemStateDto systemStateDto = new SystemStateDto();
        systemStateDto.setSysStartTime(IDataSystem.getSysStartTime());
        SysConfig config = sysConfigDao.selectOne(c -> c.where(sysConfig.keyOne,
                isEqualTo(SysConfKeyEnum.REGISTER_ENABLE.name()))).orElse(null);
        if (config != null && config.getValueOne().trim().equalsIgnoreCase("true") ) {
            systemStateDto.setRegisterEnable(true);
        }
        else {
            systemStateDto.setRegisterEnable(false);
        }
        return systemStateDto;
    }

    @Override
    public List<FeatureTreeNodeDto> getSystemFeatureTree() {
        List<SysFeature> sysFeatures = sysFeatureDao.select(c -> c.where(sysFeature.del, isNotEqualTo((short) 1)));
        return getSystemFeatureChildren(null, sysFeatures);
    }

    private List<FeatureTreeNodeDto> getSystemFeatureChildren(String parentCode, List<SysFeature> sysFeatures) {
        return sysFeatures.stream().filter(f -> (f.getParentCode() == null && parentCode == null)
                || (f.getParentCode() != null && f.getParentCode().equals(parentCode))).map(feature -> {
            FeatureTreeNodeDto featureTreeNodeDto = PojoUtil.copyOne(feature, FeatureTreeNodeDto.class,
                    "featureType", "featureCode", "featureName", "parentCode");
            featureTreeNodeDto.setAccessCode(featureTreeNodeDto.getFeatureCode());
            featureTreeNodeDto.setEnable(false);
            featureTreeNodeDto.setChildren(getSystemFeatureChildren(featureTreeNodeDto.getFeatureCode(), sysFeatures));
            return featureTreeNodeDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<FolderTreeNodeDto> getSystemFolderTree() {
        return new ArrayList<>();
    }
}
