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

import cn.zhengcaiyun.idata.commons.enums.TreeNodeTypeEnum;
import cn.zhengcaiyun.idata.develop.constant.enums.FunctionModuleEnum;
import cn.zhengcaiyun.idata.develop.dal.dao.DevEnumDao;
import cn.zhengcaiyun.idata.develop.dto.label.EnumDto;
import cn.zhengcaiyun.idata.develop.dto.tree.DevTreeNodeDto;
import cn.zhengcaiyun.idata.develop.service.label.EnumService;
import cn.zhengcaiyun.idata.develop.spi.tree.BizTreeNodeSupplier;
import cn.zhengcaiyun.idata.develop.spi.tree.BizTreeNodeSupplierFactory;
import com.google.common.collect.Lists;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumDynamicSqlSupport.del;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevEnumDynamicSqlSupport.devEnum;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-18 14:28
 **/
@Component
public class DesignEnumTreeNodeSupplier implements BizTreeNodeSupplier<EnumDto> {

    @Autowired
    private DevEnumDao devEnumDao;
    @Autowired
    private EnumService enumService;

    @PostConstruct
    public void register() {
        BizTreeNodeSupplierFactory.register(FunctionModuleEnum.DESIGN_ENUM, this);
    }

    @Override
    public List<DevTreeNodeDto> supply(FunctionModuleEnum moduleEnum) {
        List<EnumDto> enumList = enumService.getEnums();
        if (enumList.size() <= 0) return Lists.newArrayList();

        return enumList.stream()
                .map(enumDto -> assemble(moduleEnum, enumDto))
                .collect(Collectors.toList());
    }

    @Override
    public Long countBizNode(FunctionModuleEnum moduleEnum, Long folderId) {
        if (Objects.isNull(folderId)) return null;
        return devEnumDao.count(select(count()).from(devEnum)
                .where(devEnum.del, isNotEqualTo(1), and(devEnum.folderId, isEqualTo(folderId)))
                .build().render(RenderingStrategies.MYBATIS3));
    }

    @Override
    public DevTreeNodeDto assemble(FunctionModuleEnum moduleEnum, EnumDto bizRecord) {
        DevTreeNodeDto dto = new DevTreeNodeDto();
        dto.setId(bizRecord.getId());
        dto.setName(bizRecord.getEnumName());
        dto.setParentId(bizRecord.getFolderId());
        dto.setType(TreeNodeTypeEnum.RECORD.name());
        dto.setBelong(moduleEnum.code);
        return dto;
    }
}
