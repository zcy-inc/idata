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

package cn.zhengcaiyun.idata.develop.service.opt.stream;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.develop.condition.opt.stream.StreamJobInstanceCondition;
import cn.zhengcaiyun.idata.develop.dto.opt.stream.StreamJobInstanceDto;
import cn.zhengcaiyun.idata.develop.dto.opt.stream.StreamJobRunParamDto;
import cn.zhengcaiyun.idata.develop.dto.opt.stream.StreamJobStartParamConfig;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-08-22 17:30
 **/
public interface StreamJobInstanceService {

    Page<StreamJobInstanceDto> paging(StreamJobInstanceCondition condition);

    Boolean start(Long id, StreamJobRunParamDto runParamDto, Operator operator);

    Boolean stop(Long id, Operator operator);

    Boolean destroy(Long id, Operator operator);

    StreamJobStartParamConfig getStartParamConfig(Long id);
}