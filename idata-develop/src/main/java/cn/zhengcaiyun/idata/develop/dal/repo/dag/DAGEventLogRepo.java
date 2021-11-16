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

package cn.zhengcaiyun.idata.develop.dal.repo.dag;

import cn.zhengcaiyun.idata.develop.dal.model.dag.DAGEventLog;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-11-04 14:55
 **/
public interface DAGEventLogRepo {

    Long create(DAGEventLog eventLog);

    Boolean update(DAGEventLog eventLog);

    Optional<DAGEventLog> query(Long id);

    List<DAGEventLog> query(Long dagId, String event, Integer status);

    List<DAGEventLog> query(Long dagId, Integer status);
}
