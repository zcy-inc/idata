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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.user.dal.dao.UacAppFeatureDynamicSqlSupport.uacAppFeature;
import static cn.zhengcaiyun.idata.user.dal.dao.UacAppInfoDynamicSqlSupport.uacAppInfo;
import static com.google.common.base.Preconditions.checkArgument;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author caizhedong
 * @date 2022-09-18 下午1:56
 */

@Service
public class AppFeatureServiceImpl implements AppFeatureService {

    @Autowired
    private UacAppInfoDao uacAppInfoDao;
    @Autowired
    private UacAppFeatureDao uacAppFeatureDao;
    @Autowired
    private SystemConfigService systemConfigService;

    private final String[] appInfoFields = {"id", "del", "createTime", "creator", "editTime", "editor",
            "appName", "appKey", "appSecret", "description"};
    private final String FEATURE_CODE_PREFIX = "F_MENU_";

    @Override
    public AppInfoDto findById(Long id) {
        UacAppInfo appInfo = uacAppInfoDao.selectByPrimaryKey(id)
                .orElseThrow(() -> new IllegalArgumentException("应用不存在"));
        UacAppFeature appFeature = uacAppFeatureDao.selectOne(c ->
                c.where(uacAppFeature.del, isNotEqualTo(1), and(uacAppFeature.appKey, isEqualTo(appInfo.getAppKey()))))
                .get();

        AppInfoDto echo = PojoUtil.copyOne(appInfo, AppInfoDto.class, appInfoFields);
        // 恢复F_MENU前缀
        List<String> featureCodeList = changeOriginalFeatureCodes(Arrays.asList(appFeature.getFeatureCodes().split(",")));
        List<SysFeature> featureList = systemConfigService.getFeaturesByCodes(featureCodeList);
        echo.setAppFeatures(featureList);
        echo.setFeatureCodes(String.join(",", featureCodeList));
        return echo;
    }

    @Override
    public Page<AppInfoDto> findApps(String featureCode, Integer limit, Integer offset) {
        var builder = select(uacAppFeature.allColumns()).from(uacAppFeature).where(uacAppFeature.del, isNotEqualTo(1));
        if (featureCode != null) {
            builder.and(uacAppFeature.featureCodes, isLike("%" + featureCode + "%"));
        }
        List<UacAppFeature> appFeatureList = uacAppFeatureDao.selectMany(builder.orderBy(uacAppFeature.editTime.descending()).limit(limit).offset(offset)
                .build().render(RenderingStrategies.MYBATIS3));
        if (appFeatureList.size() == 0) return Page.newOne(new ArrayList<>(), 0L);

        List<String> appKeyList = appFeatureList.stream().map(UacAppFeature::getAppKey).collect(Collectors.toList());
        List<UacAppInfo> appInfoList = uacAppInfoDao.select(c -> c.where(uacAppInfo.del, isNotEqualTo(1),
                and(uacAppInfo.appKey, isIn(appKeyList))));
        Map<String, UacAppInfo> appInfoMap = appInfoList.stream()
                .collect(Collectors.toMap(UacAppInfo::getAppKey, Function.identity()));
        List<SysFeature> allFeatureList = systemConfigService.getFeaturesByCodes(null);
        List<AppInfoDto> echoList = new ArrayList<>();
        for (UacAppFeature appFeature : appFeatureList) {
            List<String> appFeatureCodeList = Arrays.asList(appFeature.getFeatureCodes().split(","));
            List<SysFeature> featureList = allFeatureList.stream()
                    // 恢复F_MENU前缀
                    .filter(c -> changeOriginalFeatureCodes(appFeatureCodeList).contains(c.getFeatureCode()))
                    .collect(Collectors.toList());
            AppInfoDto echo = PojoUtil.copyOne(appInfoMap.get(appFeature.getAppKey()), AppInfoDto.class, appInfoFields);
            echo.setFeatureCodes(appFeature.getFeatureCodes());
            echo.setAppFeatures(featureList);
            echoList.add(echo);
        }
        long total = uacAppInfoDao.count(c -> c.where(uacAppInfo.del, isNotEqualTo(1)));
        return Page.newOne(echoList, total);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public AppInfoDto add(AppInfoDto appInfoDto) {
        UacAppInfo dupNameAppInfo = uacAppInfoDao.selectOne(c -> c.where(uacAppInfo.del, isNotEqualTo(1),
                and(uacAppInfo.appName, isEqualTo(appInfoDto.getAppName()))))
                .orElse(null);
        checkArgument(dupNameAppInfo == null, "应用名称重复，新建失败");
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
        // 去除F_MENU前缀
        appFeature.setFeatureCodes(appInfoDto.getFeatureCodes().replaceAll(FEATURE_CODE_PREFIX, ""));
        uacAppFeatureDao.insertSelective(appFeature);
        return findById(appInfo.getId());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public AppInfoDto update(AppInfoDto appInfoDto) {
        UacAppInfo existAppInfo = uacAppInfoDao.selectByPrimaryKey(appInfoDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("应用不存在"));
        UacAppInfo dupNameAppInfo = uacAppInfoDao.selectOne(c -> c.where(uacAppInfo.del, isNotEqualTo(1),
                and(uacAppInfo.appName, isEqualTo(appInfoDto.getAppName())), and(uacAppInfo.id, isNotEqualTo(appInfoDto.getId()))))
                .orElse(null);
        checkArgument(dupNameAppInfo == null, "应用名称重复，新建失败");
        UacAppFeature appFeature = uacAppFeatureDao.selectOne(c ->
                c.where(uacAppFeature.del, isNotEqualTo(1), and(uacAppFeature.appKey, isEqualTo(existAppInfo.getAppKey()))))
                .get();

        // appInfo
        UacAppInfo newAppInfo = new UacAppInfo();
        newAppInfo.setId(appInfoDto.getId());
        newAppInfo.setEditor(appInfoDto.getEditor());
        newAppInfo.setEditTime(new Timestamp(System.currentTimeMillis()));
        newAppInfo.setAppName(appInfoDto.getAppName());
        newAppInfo.setDescription(appInfoDto.getDescription());
        uacAppInfoDao.updateByPrimaryKeySelective(newAppInfo);
        // appFeature
        // 为保持列表修改时间倒序，均更新
        UacAppFeature newAppFeature = new UacAppFeature();
        newAppFeature.setId(appFeature.getId());
        newAppFeature.setEditor(appInfoDto.getEditor());
        newAppFeature.setEditTime(new Timestamp(System.currentTimeMillis()));
        newAppFeature.setFeatureCodes(appInfoDto.getFeatureCodes().replaceAll(FEATURE_CODE_PREFIX, ""));
        uacAppFeatureDao.updateByPrimaryKeySelective(newAppFeature);
        return findById(appInfoDto.getId());
    }

    // 原始featureCode为F_MENU_featureCode
    private List<String> changeOriginalFeatureCodes(List<String> featureCodes) {
        return featureCodes.stream()
                .map(featureCode -> FEATURE_CODE_PREFIX + featureCode)
                .collect(Collectors.toList());
    }
}
