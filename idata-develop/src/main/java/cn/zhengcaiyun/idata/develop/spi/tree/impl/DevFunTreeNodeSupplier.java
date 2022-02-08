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

package cn.zhengcaiyun.idata.develop.spi.tree.impl;

import cn.zhengcaiyun.idata.commons.enums.DeleteEnum;
import cn.zhengcaiyun.idata.commons.enums.TreeNodeTypeEnum;
import cn.zhengcaiyun.idata.commons.util.MybatisHelper;
import cn.zhengcaiyun.idata.develop.condition.job.JobInfoCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.FunctionModuleEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.JobTypeEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobUdfDao;
import cn.zhengcaiyun.idata.develop.dal.model.job.DevJobUdf;
import cn.zhengcaiyun.idata.develop.dal.model.job.JobInfo;
import cn.zhengcaiyun.idata.develop.dal.repo.job.JobInfoRepo;
import cn.zhengcaiyun.idata.develop.dto.tree.DevTreeNodeDto;
import cn.zhengcaiyun.idata.develop.spi.tree.BizTreeNodeSupplier;
import cn.zhengcaiyun.idata.develop.spi.tree.BizTreeNodeSupplierFactory;
import com.google.common.collect.Lists;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.job.DevJobUdfDynamicSqlSupport.devJobUdf;
import static cn.zhengcaiyun.idata.develop.dal.dao.job.JobInfoDynamicSqlSupport.jobInfo;
import static org.mybatis.dynamic.sql.SqlBuilder.*;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-18 14:29
 **/
@Component
public class DevFunTreeNodeSupplier implements BizTreeNodeSupplier<DevJobUdf> {

    private final DevJobUdfDao devJobUdfDao;

    @Autowired
    public DevFunTreeNodeSupplier(DevJobUdfDao devJobUdfDao) {
        this.devJobUdfDao = devJobUdfDao;
    }

    @PostConstruct
    public void register() {
        BizTreeNodeSupplierFactory.register(FunctionModuleEnum.DEV_FUN, this);
    }

    @Override
    public List<DevTreeNodeDto> supply(FunctionModuleEnum moduleEnum) {
        JobInfoCondition condition = new JobInfoCondition();
        List<DevJobUdf> udfList = devJobUdfDao.selectMany(select(devJobUdf.allColumns())
                .from(devJobUdf)
                .where(devJobUdf.del, isNotEqualTo(1))
                .build().render(RenderingStrategies.MYBATIS3));
        return udfList.stream()
                .map(jobInfo -> assemble(moduleEnum, jobInfo))
                .collect(Collectors.toList());
    }

    @Override
    public Long countBizNode(FunctionModuleEnum moduleEnum, Long folderId) {
        return devJobUdfDao.count(dsl -> dsl.where(jobInfo.folderId, isEqualToWhenPresent(folderId),
                and(jobInfo.del, isEqualTo(DeleteEnum.DEL_NO.val))));
    }

    @Override
    public DevTreeNodeDto assemble(FunctionModuleEnum moduleEnum, DevJobUdf bizRecord) {
        DevTreeNodeDto dto = new DevTreeNodeDto();
        dto.setId(bizRecord.getId());
        dto.setName(bizRecord.getUdfName());
        dto.setParentId(bizRecord.getFolderId());
        dto.setType(TreeNodeTypeEnum.RECORD.name());
        dto.setBelong(moduleEnum.code);
        return dto;
    }
}
