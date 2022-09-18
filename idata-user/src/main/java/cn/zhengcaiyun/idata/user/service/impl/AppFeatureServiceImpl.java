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

import cn.zhengcaiyun.idata.commons.encrypt.RandomUtil;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.commons.util.AppRandomUtil;
import cn.zhengcaiyun.idata.system.dal.model.SysFeature;
import cn.zhengcaiyun.idata.system.service.SystemConfigService;
import cn.zhengcaiyun.idata.user.dal.dao.UacAppFeatureDao;
import cn.zhengcaiyun.idata.user.dal.dao.UacAppInfoDao;
import cn.zhengcaiyun.idata.user.dal.model.UacAppFeature;
import cn.zhengcaiyun.idata.user.dal.model.UacAppInfo;
import cn.zhengcaiyun.idata.user.dto.AppInfoDto;
import cn.zhengcaiyun.idata.user.dto.UserInfoDto;
import cn.zhengcaiyun.idata.user.service.AppFeatureService;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.user.dal.dao.UacAppFeatureDynamicSqlSupport.uacAppFeature;
import static cn.zhengcaiyun.idata.user.dal.dao.UacAppInfoDynamicSqlSupport.uacAppInfo;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author caizhedong
 * @date 2022-09-18 下午1:56
 */
public class AppFeatureServiceImpl implements AppFeatureService {

    @Autowired
    private UacAppInfoDao uacAppInfoDao;
    @Autowired
    private UacAppFeatureDao uacAppFeatureDao;
    @Autowired
    private SystemConfigService systemConfigService;

    private final String[] appInfoFields = {"id", "del", "createTime", "creator", "editTime", "editor",
            "appName", "appKey", "appSecret", "description"};

    @Override
    public AppInfoDto findById(Long id) {
        UacAppInfo appInfo = uacAppInfoDao.selectByPrimaryKey(id)
                .orElseThrow(() -> new IllegalArgumentException("应用不存在"));
        UacAppFeature appFeature = uacAppFeatureDao.selectOne(c ->
                c.where(uacAppFeature.del, isNotEqualTo(1), and(uacAppFeature.appKey, isEqualTo(appInfo.getAppKey()))))
                .get();

        AppInfoDto echo = PojoUtil.copyOne(appInfo, AppInfoDto.class, appInfoFields);
        List<SysFeature> featureList = systemConfigService
                .getFeaturesByCodes(Arrays.asList(appFeature.getFeatureCodes().split(",")));
        echo.setAppFeatures(featureList);
        echo.setFeatureCodes(appFeature.getFeatureCodes());
        return echo;
    }

    @Override
    public Page<AppInfoDto> findApps(Integer limit, Integer offset) {
        List<UacAppInfo> appInfoList = uacAppInfoDao.select(c -> c.where(uacAppInfo.del, isNotEqualTo(1))
                .orderBy(uacAppInfo.editTime.descending())
                .limit(limit).offset(offset));
        List<String> appKeyList = appInfoList.stream().map(UacAppInfo::getAppKey).collect(Collectors.toList());
        List<UacAppFeature> appFeatureList = uacAppFeatureDao.select(c -> c.where(uacAppFeature.del, isNotEqualTo(1),
                and(uacAppFeature.appKey, isIn(appKeyList))));
        List<SysFeature> allFeatureList = systemConfigService.getFeaturesByCodes(null);
        Map<String, List<String>> appFeatureCodesMap = appFeatureList.stream()
                .collect(Collectors.toMap(UacAppFeature::getAppKey,
                        appFeature -> Arrays.asList(appFeature.getFeatureCodes().split(","))));
        Map<String, List<SysFeature>> appFeaturesMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : appFeatureCodesMap.entrySet()) {
            List<SysFeature> featureList = allFeatureList.stream()
                    .filter(c -> entry.getValue().contains(c.getFeatureCode()))
                    .collect(Collectors.toList());
            appFeaturesMap.put(entry.getKey(), featureList);
        }
        List<AppInfoDto> echoList = PojoUtil.copyList(appInfoList, AppInfoDto.class, appInfoFields).stream()
                .peek(c -> {
                    c.setFeatureCodes(String.join(",", appFeatureCodesMap.get(c.getAppKey())));
                    c.setAppFeatures(appFeaturesMap.get(c.getAppKey()));
                }).collect(Collectors.toList());
        long total = uacAppInfoDao.count(c -> c.where(uacAppInfo.del, isNotEqualTo(1)));
        return Page.newOne(echoList, total);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public AppInfoDto add(AppInfoDto appInfoDto) {
        // appInfo
        UacAppInfo appInfo = PojoUtil.copyOne(appInfoDto, UacAppInfo.class, appInfoFields);
        appInfo.setEditor(appInfoDto.getCreator());
        appInfo.setAppKey(AppRandomUtil.getSimpleLetterNumber(12));
        appInfo.setAppSecret(AppRandomUtil.getSimpleLetterNumber(16));
        uacAppInfoDao.insertSelective(appInfo);
        // appFeature
        UacAppFeature appFeature = new UacAppFeature();
        appFeature.setCreator(appInfoDto.getCreator());
        appFeature.setEditor(appInfoDto.getCreator());
        appFeature.setAppKey(appInfo.getAppKey());
        appFeature.setFeatureCodes(appInfoDto.getFeatureCodes());
        return findById(appInfo.getId());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public AppInfoDto update(AppInfoDto appInfoDto) {
        UacAppInfo appInfo = uacAppInfoDao.selectByPrimaryKey(appInfoDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("应用不存在"));
        UacAppFeature appFeature = uacAppFeatureDao.selectOne(c ->
                c.where(uacAppFeature.del, isNotEqualTo(1), and(uacAppFeature.appKey, isEqualTo(appInfo.getAppKey()))))
                .get();

        // appInfo
        appInfo.setEditor(appInfoDto.getEditor());
        appInfo.setEditTime(new Timestamp(System.currentTimeMillis()));
        appInfo.setAppName(appInfoDto.getAppName());
        appInfo.setDescription(appInfoDto.getDescription());
        uacAppInfoDao.updateByPrimaryKeySelective(appInfo);
        // appFeature
        // 为保持列表修改时间倒序，均更新
        appFeature.setEditor(appInfoDto.getEditor());
        appFeature.setEditTime(new Timestamp(System.currentTimeMillis()));
        appFeature.setFeatureCodes(appInfoDto.getFeatureCodes());
        uacAppFeatureDao.updateByPrimaryKeySelective(appFeature);
        return findById(appInfoDto.getId());
    }
}
