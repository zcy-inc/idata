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
package cn.zhengcaiyun.idata.develop.service.table.impl;

import cn.zhengcaiyun.idata.develop.service.table.ForeignKeyService;
import cn.zhengcaiyun.idata.dto.develop.table.ForeignKeyDto;

import java.util.List;

/**
 * @author caizhedong
 * @date 2021-05-28 15:36
 */
public class ForeignKeyServiceImpl implements ForeignKeyService {

    @Override
    public List<ForeignKeyDto> getForeignKeys(Long tableId) {
        return null;
    }

    @Override
    public ForeignKeyDto create(ForeignKeyDto foreignKeyDto, String creator) {
        return null;
    }

    @Override
    public ForeignKeyDto edit(ForeignKeyDto foreignKeyDto, String editor) {
        return null;
    }

    @Override
    public boolean delete(Long foreignKeyId, String editor) {
        return false;
    }
}
