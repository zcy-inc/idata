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

import cn.zhengcaiyun.idata.develop.dto.label.EnumDto;
import cn.zhengcaiyun.idata.develop.dto.table.TableInfoDto;
import cn.zhengcaiyun.idata.merge.data.service.ModelMigrationService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author caizhedong
 * @date 2022-01-13 上午9:59
 */

@Service
public class ModelMigrationServiceImpl implements ModelMigrationService {

    @Override
    public EnumDto syncBizProcess() {
        return null;
    }

    @Override
    public List<TableInfoDto> syncTable(Long oldTableId) {
        return null;
    }

    @Override
    public Boolean syncForeignKey() {
        return null;
    }
}
