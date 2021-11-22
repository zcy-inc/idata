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
package cn.zhengcaiyun.idata.develop.service.job.impl;

import cn.zhengcaiyun.idata.develop.dto.job.spark.SparkJobDto;
import cn.zhengcaiyun.idata.develop.service.job.SparkJobService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author caizhedong
 * @date 2021-11-19 下午4:12
 */

@Service
public class SparkJobServiceImpl implements SparkJobService {

    @Override
    public SparkJobDto save(SparkJobDto sparkJobDto, String operator) {
        return null;
    }

    @Override
    public SparkJobDto find(Long jobId, Integer version) {
        return null;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        return null;
    }
}
