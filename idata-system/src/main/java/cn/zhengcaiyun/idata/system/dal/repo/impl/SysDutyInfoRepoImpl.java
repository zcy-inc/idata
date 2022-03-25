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

package cn.zhengcaiyun.idata.system.dal.repo.impl;

import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.system.dal.dao.SysDutyInfoDao;
import cn.zhengcaiyun.idata.system.dal.model.SysDutyInfo;
import cn.zhengcaiyun.idata.system.dal.repo.SysDutyInfoRepo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static cn.zhengcaiyun.idata.system.dal.dao.SysDutyInfoDynamicSqlSupport.SYS_DUTY_INFO;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-13 14:10
 **/
@Repository
public class SysDutyInfoRepoImpl implements SysDutyInfoRepo {

    private final SysDutyInfoDao sysDutyInfoDao;

    public SysDutyInfoRepoImpl(SysDutyInfoDao sysDutyInfoDao) {
        this.sysDutyInfoDao = sysDutyInfoDao;
    }

    @Override
    public Optional<SysDutyInfo> queryOne() {
        return sysDutyInfoDao.selectOne(dsl -> dsl.where(SYS_DUTY_INFO.del, isEqualTo(DeleteEnum.DEL_NO.val)));
    }
}
