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
package cn.zhengcaiyun.idata.label.service.folder;

import cn.zhengcaiyun.idata.label.dto.LabFolderTreeNodeDto;

import java.util.List;
import java.util.function.Supplier;

/**
 * @description: 文件树节点提供者接口，由各业务模块实现，提供业务树节点数据
 * @author: yangjianhua
 * @create: 2021-06-22 11:40
 **/
public interface LabFolderTreeNodeSupplier extends Supplier<List<LabFolderTreeNodeDto>> {
    Boolean hasSubTreeNode(Long folderId);
}
