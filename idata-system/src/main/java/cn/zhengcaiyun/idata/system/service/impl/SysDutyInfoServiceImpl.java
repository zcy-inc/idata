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

import cn.zhengcaiyun.idata.system.dal.model.SysDutyInfo;
import cn.zhengcaiyun.idata.system.dal.repo.SysDutyInfoRepo;
import cn.zhengcaiyun.idata.system.service.SysDutyInfoService;
import com.alibaba.fastjson.JSON;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-13 14:15
 **/
@Service
public class SysDutyInfoServiceImpl implements SysDutyInfoService {

    private final SysDutyInfoRepo sysDutyInfoRepo;

    @Autowired
    public SysDutyInfoServiceImpl(SysDutyInfoRepo sysDutyInfoRepo) {
        this.sysDutyInfoRepo = sysDutyInfoRepo;
    }

    @Override
    public Optional<String> findDutyPhone(LocalDate date) {
        LocalDate localDate = Objects.isNull(date) ? LocalDate.now() : date;
        Optional<SysDutyInfo> dutyInfoOptional = sysDutyInfoRepo.queryOne();
        checkArgument(dutyInfoOptional.isPresent(), "值班信息未配置");
        SysDutyInfo dutyInfo = dutyInfoOptional.get();
        checkArgument(StringUtils.isNotBlank(dutyInfo.getDutyInfo()) || StringUtils.isNotBlank(dutyInfo.getDutyDefaultPhone()), "值班信息未配置");

        HashMap<String, String> dayPhone = JSON.parseObject(dutyInfo.getDutyInfo(), HashMap.class);
        String day = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String phone = MoreObjects.firstNonNull(dayPhone.get(day), dutyInfo.getDutyDefaultPhone());
        return Optional.of(phone);
    }
}
