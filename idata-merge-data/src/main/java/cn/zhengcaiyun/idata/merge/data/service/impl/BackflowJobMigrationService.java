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

package cn.zhengcaiyun.idata.merge.data.service.impl;

import cn.zhengcaiyun.idata.merge.data.service.JobMigrationService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-06 11:42
 **/
public class BackflowJobMigrationService implements JobMigrationService {
    @Override
    public List<String> migrateBaseInfo() {
        return null;
    }

    @Override
    public List<String> migrateConfigInfo() {
        return null;
    }

    @Override
    public List<String> migrateContentInfo() {
        return null;
    }

    @Override
    public List<String> migratePublishInfo() {
        return null;
    }
}
