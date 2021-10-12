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

package cn.zhengcaiyun.idata.develop.cache;


import cn.zhengcaiyun.idata.develop.constant.enums.FunctionModuleEnum;
import cn.zhengcaiyun.idata.develop.dal.model.folder.CompositeFolder;
import cn.zhengcaiyun.idata.develop.dal.repo.folder.CompositeFolderRepo;
import cn.zhengcaiyun.idata.develop.dto.tree.DevTreeNodeDto;
import cn.zhengcaiyun.idata.develop.spi.tree.BizTreeNodeSupplier;
import cn.zhengcaiyun.idata.develop.spi.tree.BizTreeNodeSupplierFactory;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-18 14:52
 **/
@Component
public class DevTreeNodeLocalCache {

    private static final Logger logger = LoggerFactory.getLogger(DevTreeNodeLocalCache.class);

    private final LoadingCache<FunctionModuleEnum, DevTreeNodeCacheValue> cache = Caffeine.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(Duration.ofMinutes(60 * 24 * 7))
            .build(key -> load(key));

    private final BizTreeNodeSupplierFactory bizTreeNodeSupplierFactory;
    private final CompositeFolderRepo compositeFolderRepo;

    @Autowired
    public DevTreeNodeLocalCache(BizTreeNodeSupplierFactory bizTreeNodeSupplierFactory,
                                 CompositeFolderRepo compositeFolderRepo) {
        this.bizTreeNodeSupplierFactory = bizTreeNodeSupplierFactory;
        this.compositeFolderRepo = compositeFolderRepo;
    }

    @PostConstruct
    public void initCache() {
        refreshAll();
    }

    private DevTreeNodeCacheValue load(FunctionModuleEnum moduleEnum) {
        Optional<BizTreeNodeSupplier> optional = bizTreeNodeSupplierFactory.getSupplier(moduleEnum);
        ImmutableList<DevTreeNodeDto> records = null;
        if (optional.isPresent()) {
            BizTreeNodeSupplier supplier = optional.get();
            List<DevTreeNodeDto> nodeDtoList = supplier.supply(moduleEnum);
            if (ObjectUtils.isNotEmpty(nodeDtoList)) {
                records = ImmutableList.copyOf(nodeDtoList);
            }
        }

        ImmutableList<DevTreeNodeDto> folders = null;
        List<CompositeFolder> folderList = compositeFolderRepo.queryFolder(moduleEnum);
        if (ObjectUtils.isNotEmpty(folderList)) {
            List<DevTreeNodeDto> nodeDtoList = folderList.stream()
                    .map(DevTreeNodeDto::from)
                    .collect(Collectors.toList());
            folders = ImmutableList.copyOf(nodeDtoList);
        }
        return new DevTreeNodeCacheValue(moduleEnum, folders, records);
    }

    public Optional<DevTreeNodeCacheValue> get(FunctionModuleEnum moduleEnum) {
        try {
            return Optional.ofNullable(cache.get(moduleEnum));
        } catch (Exception ex) {
            logger.warn("get DevTreeNodeCacheValue of {} failed. ex: {}.", moduleEnum.name(), Throwables.getStackTraceAsString(ex));
        }
        return Optional.empty();
    }

    public void refresh(FunctionModuleEnum key) {
        cache.refresh(key);
    }

    public void refresh(List<FunctionModuleEnum> keys) {
        if (ObjectUtils.isEmpty(keys)) return;
        keys.forEach(moduleEnum -> refresh(moduleEnum));
    }

    public void refreshAll() {
        Arrays.stream(FunctionModuleEnum.values()).forEach(moduleEnum -> refresh(moduleEnum));
    }

    public void invalidate(FunctionModuleEnum key) {
        cache.invalidate(key);
    }

    public void invalidate(List<FunctionModuleEnum> keys) {
        cache.invalidateAll(keys);
    }

    public void invalidateAll() {
        cache.invalidateAll();
    }
}
