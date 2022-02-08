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
package cn.zhengcaiyun.idata.develop.service.access.impl;

import cn.zhengcaiyun.idata.develop.service.access.DevAccessService;
import cn.zhengcaiyun.idata.system.dto.ResourceTypeEnum;
import cn.zhengcaiyun.idata.user.service.UserAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author caizhedong
 * @date 2021-12-27 下午9:04
 */

@Service
public class DevAccessServiceImpl implements DevAccessService {

    @Autowired
    private UserAccessService userAccessService;

    private final String DATA_DEVELOP_ACCESS_CODE = "F_MENU_DATA_DEVELOP";
    private final String DATA_DEVELOP_RESOURCE_CODE = ResourceTypeEnum.R_DATA_DEVELOP_DIR.name();

    @Override
    public boolean checkAddAccess(Long userId, Long parentId) throws IllegalAccessException {
        if (!userAccessService.checkAddAccess(userId, parentId, DATA_DEVELOP_ACCESS_CODE, DATA_DEVELOP_RESOURCE_CODE)) {
            throw new IllegalAccessException("无权限");
        }
        return true;
    }

    @Override
    public boolean checkUpdateAccess(Long userId, Long originalParentId, Long removeParentId) throws IllegalAccessException {
        if (!userAccessService.checkUpdateAccess(userId, originalParentId,
                removeParentId, DATA_DEVELOP_RESOURCE_CODE)) {
            throw new IllegalAccessException("无权限");
        }
        return true;
    }

    @Override
    public boolean checkDeleteAccess(Long userId, Long parentId) throws IllegalAccessException {
        if (!userAccessService.checkDeleteAccess(userId, parentId, DATA_DEVELOP_RESOURCE_CODE)) {
            throw new IllegalAccessException("无权限");
        }
        return true;
    }
}
