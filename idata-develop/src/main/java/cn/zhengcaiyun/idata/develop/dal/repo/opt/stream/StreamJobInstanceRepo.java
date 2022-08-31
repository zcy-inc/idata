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

package cn.zhengcaiyun.idata.develop.dal.repo.opt.stream;

import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.develop.condition.opt.stream.StreamJobInstanceCondition;
import cn.zhengcaiyun.idata.develop.constant.enums.StreamJobInstanceStatusEnum;
import cn.zhengcaiyun.idata.develop.dal.model.opt.stream.StreamJobInstance;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-08-22 16:02
 **/
public interface StreamJobInstanceRepo {

    Page<StreamJobInstance> paging(StreamJobInstanceCondition condition);

    List<StreamJobInstance> queryList(StreamJobInstanceCondition condition, long limit, long offset);

    long count(StreamJobInstanceCondition condition);

    List<StreamJobInstance> queryList(StreamJobInstanceCondition condition);

    Long save(StreamJobInstance instance);

    Boolean update(StreamJobInstance instance);

    Boolean updateStatus(List<Long> ids, StreamJobInstanceStatusEnum statusEnum, String operator);

    Boolean updateStatus(Long id, StreamJobInstanceStatusEnum statusEnum);

    Boolean updateRunParam(Long id, String runParams, String operator);

    Optional<StreamJobInstance> query(Long id);

    List<StreamJobInstance> queryList(Long jobId, Integer version, String env, List<Integer> instanceStatusList);

    List<StreamJobInstance> queryList(Long jobId, String env, List<Integer> instanceStatusList);

    List<StreamJobInstance> queryList(Long jobId, String env, StreamJobInstanceStatusEnum statusEnum);
}
