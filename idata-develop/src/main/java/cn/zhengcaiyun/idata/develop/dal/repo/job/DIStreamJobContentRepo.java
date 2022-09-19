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

package cn.zhengcaiyun.idata.develop.dal.repo.job;

import cn.zhengcaiyun.idata.develop.constant.enums.EditableEnum;
import cn.zhengcaiyun.idata.develop.dal.model.job.DIStreamJobContent;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-06-24 14:09
 **/
public interface DIStreamJobContentRepo {

    Long save(DIStreamJobContent content);

    Boolean update(DIStreamJobContent content);

    Integer newVersion(Long jobId);

    Boolean updateEditable(Long id, EditableEnum editable, String operator);

    Optional<DIStreamJobContent> query(Long jobId, Integer version);

    Optional<DIStreamJobContent> queryLatest(Long jobId);

    List<DIStreamJobContent> queryList(Long jobId);

    List<DIStreamJobContent> queryList(List<Long> ids);

    List<DIStreamJobContent> queryByDataSource(Long srcDataSourceId, Long destDataSourceId);

    Set<Long> queryJobIdByDataSource(Long srcDataSourceId, Long destDataSourceId);
}
