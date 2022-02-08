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

package cn.zhengcaiyun.idata.develop.dal.repo.integration;

import cn.zhengcaiyun.idata.develop.dal.model.integration.DSDependenceNode;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-15 14:17
 **/
public interface DSDependenceNodeRepo {

    Long create(DSDependenceNode dependenceNode);

    Boolean delete(Long id);

    Boolean delete(List<Long> ids);

    Boolean deleteByDependenceNode(Long dependenceNodeCode);

    Boolean deleteByTaskRelation(Long taskCode, Long prevTaskCode);

    List<DSDependenceNode> queryDependenceNodeInWorkflow(Long workflowCode, Long prevTaskCode);

    List<DSDependenceNode> queryDependenceNodeInWorkflow(Long taskCode, Long workflowCode, Long prevTaskCode);

    List<DSDependenceNode> queryByDependenceNode(Long dependenceNodeCode);
}
