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

package cn.zhengcaiyun.idata.develop.dto.tree;

import cn.zhengcaiyun.idata.commons.enums.TreeNodeTypeEnum;
import cn.zhengcaiyun.idata.commons.pojo.TreeNodeDto;
import cn.zhengcaiyun.idata.develop.dal.model.folder.CompositeFolder;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-17 17:19
 **/
public class DevTreeNodeDto extends TreeNodeDto<Long, DevTreeNodeDto> {

    /**
     * 具体所属类别，如 DI_STREAM, DI_BATCH, SQL_FLINK 等
     */
    private String concreteBelong;

    @Override
    public boolean canHasChildren() {
        return TreeNodeTypeEnum.FUNCTION.name().equals(super.getType())
                || TreeNodeTypeEnum.FOLDER.name().equals(super.getType());
    }

    public static DevTreeNodeDto from(CompositeFolder folder) {
        DevTreeNodeDto nodeDto = new DevTreeNodeDto();
        nodeDto.setId(folder.getId());
        nodeDto.setName(folder.getName());
        nodeDto.setType(folder.getType());
        nodeDto.setBelong(folder.getBelong());
        nodeDto.setParentId(folder.getParentId());
        return nodeDto;
    }

    public String getConcreteBelong() {
        return concreteBelong;
    }

    public void setConcreteBelong(String concreteBelong) {
        this.concreteBelong = concreteBelong;
    }
}
