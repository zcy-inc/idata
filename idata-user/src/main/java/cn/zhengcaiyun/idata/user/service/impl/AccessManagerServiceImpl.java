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

import cn.zhengcaiyun.idata.user.dal.dao.UacRoleAccessDao;
import cn.zhengcaiyun.idata.user.service.AccessManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cn.zhengcaiyun.idata.user.dal.dao.UacRoleAccessDynamicSqlSupport.uacRoleAccess;
import static com.google.common.base.Preconditions.checkArgument;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @author shiyin
 * @date 2021-03-29 14:25
 */
@Service
public class AccessManagerServiceImpl implements AccessManagerService {

    @Autowired
    private UacRoleAccessDao uacRoleAccessDao;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean deleteResourceAccess(String resourceType, String accessKey, String editor) {
        checkArgument(resourceType.startsWith("R_"), "资源类型格式不正确");
        uacRoleAccessDao.update(c -> c.set(uacRoleAccess.del).equalTo(1)
                        .set(uacRoleAccess.editor).equalTo(editor)
                        .where(uacRoleAccess.accessType, isLike(resourceType + "%"),
                                and(uacRoleAccess.accessKey, isEqualTo(accessKey)),
                                and(uacRoleAccess.del, isNotEqualTo(1))));
        return true;
    }
}
