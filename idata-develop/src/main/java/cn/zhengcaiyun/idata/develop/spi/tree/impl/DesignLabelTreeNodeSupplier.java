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

//package cn.zhengcaiyun.idata.develop.spi.tree.impl;
//
//import cn.zhengcaiyun.idata.commons.enums.TreeNodeTypeEnum;
//import cn.zhengcaiyun.idata.develop.constant.enums.FunctionModuleEnum;
//import cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDao;
//import cn.zhengcaiyun.idata.develop.dto.label.LabelDefineDto;
//import cn.zhengcaiyun.idata.develop.dto.tree.DevTreeNodeDto;
//import cn.zhengcaiyun.idata.develop.service.label.LabelService;
//import cn.zhengcaiyun.idata.develop.spi.tree.BizTreeNodeSupplier;
//import cn.zhengcaiyun.idata.develop.spi.tree.BizTreeNodeSupplierFactory;
//import com.google.common.collect.Lists;
//import org.mybatis.dynamic.sql.render.RenderingStrategies;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDefineDynamicSqlSupport.devLabelDefine;
//import static org.mybatis.dynamic.sql.SqlBuilder.*;
//
///**
// * @description:
// * @author: yangjianhua
// * @create: 2021-09-18 14:27
// **/
//@Component
//public class DesignLabelTreeNodeSupplier implements BizTreeNodeSupplier<LabelDefineDto> {
//
//    @Autowired
//    private DevLabelDefineDao devLabelDefineDao;
//    @Autowired
//    private LabelService labelService;
//
//    @PostConstruct
//    public void register() {
//        BizTreeNodeSupplierFactory.register(FunctionModuleEnum.DESIGN_LABEL, this);
//    }
//
//    @Override
//    public List<DevTreeNodeDto> supply(FunctionModuleEnum moduleEnum) {
//        List<LabelDefineDto> labelDefineList = labelService.findAllDefines();
//        if (labelDefineList.size() <= 0) return Lists.newArrayList();
//
//        return labelDefineList.stream()
//                .map(labelDefineDto -> assemble(moduleEnum, labelDefineDto))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public Long countBizNode(FunctionModuleEnum moduleEnum, Long folderId) {
//        if (Objects.isNull(folderId)) return null;
//        return devLabelDefineDao.count(select(count()).from(devLabelDefine)
//                .where(devLabelDefine.del, isNotEqualTo(1), and(devLabelDefine.folderId, isEqualTo(folderId)))
//                .build().render(RenderingStrategies.MYBATIS3));
//    }
//
//    @Override
//    public DevTreeNodeDto assemble(FunctionModuleEnum moduleEnum, LabelDefineDto bizRecord) {
//        DevTreeNodeDto dto = new DevTreeNodeDto();
//        dto.setId(bizRecord.getId());
//        dto.setName(bizRecord.getLabelName());
//        dto.setParentId(bizRecord.getFolderId());
//        dto.setType(TreeNodeTypeEnum.RECORD.name());
//        dto.setBelong(moduleEnum.code);
//        return dto;
//    }
//}
