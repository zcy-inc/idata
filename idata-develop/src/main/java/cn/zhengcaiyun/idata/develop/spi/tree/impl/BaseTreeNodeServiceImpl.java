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

import cn.zhengcaiyun.idata.commons.dto.BaseTreeNodeDto;
import cn.zhengcaiyun.idata.commons.pojo.PojoUtil;
import cn.zhengcaiyun.idata.develop.condition.tree.DevTreeCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.FunctionModuleEnum;
import cn.zhengcaiyun.idata.develop.dto.tree.DevTreeNodeDto;
import cn.zhengcaiyun.idata.develop.service.folder.CompositeFolderService;
import cn.zhengcaiyun.idata.system.spi.BaseTreeNodeService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author caizhedong
 * @date 2021-12-17 上午9:30
 */

public class BaseTreeNodeServiceImpl implements BaseTreeNodeService {

    @Autowired
    private CompositeFolderService compositeFolderService;


    @Override
    public List<BaseTreeNodeDto> getBaseTree() {
        List<String> treeTypeList = new ArrayList<>(FunctionModuleEnum.map.keySet());
        DevTreeCondition condition = new DevTreeCondition();
        condition.setBelongFunctions(treeTypeList);
        return changeToBaseTreeNode(compositeFolderService.searchDevTree(condition, null), null);
    }

    private List<BaseTreeNodeDto> changeToBaseTreeNode(List<DevTreeNodeDto> devTreeNodes, String nodeType) {
        List<BaseTreeNodeDto> echoList = devTreeNodes.stream().map(devTreeNode -> {
            BaseTreeNodeDto echo = PojoUtil.copyOne(devTreeNode, BaseTreeNodeDto.class,
                    "id", "name", "parentId");
            String folderType = StringUtils.isNotEmpty(nodeType) ? nodeType : null;
            if (folderType == null) {
                FunctionModuleEnum functionModule = FunctionModuleEnum.getEnum(devTreeNode.getBelong()).get();
                if (FunctionModuleEnum.DESIGN.equals(functionModule)) {
                    folderType = "R_DATA_DEVELOP_DW_DIR";
                } else if (FunctionModuleEnum.DAG.equals(functionModule)) {
                    folderType = "R_DATA_DEVELOP_DIG_DIR";
                } else if (FunctionModuleEnum.DI.equals(functionModule)) {
                    folderType = "R_DATA_DEVELOP_DI_DIR";
                } else if (FunctionModuleEnum.DEV.equals(functionModule)) {
                    folderType = "R_DATA_DEVELOP_DD_DIR";
                }
            }
            echo.setType(folderType);
            if (ObjectUtils.isNotEmpty(devTreeNode.getChildren())) {
                echo.setChildren(changeToBaseTreeNode(devTreeNode.getChildren(), folderType));
            }
            return echo;
        }).collect(Collectors.toList());
        return echoList;
    }
}
