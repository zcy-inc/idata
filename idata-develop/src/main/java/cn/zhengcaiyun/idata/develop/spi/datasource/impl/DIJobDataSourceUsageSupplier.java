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

package cn.zhengcaiyun.idata.develop.spi.datasource.impl;

import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.datasource.spi.DataSourceUsageSupplier;
import cn.zhengcaiyun.idata.develop.dal.repo.job.DIJobContentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-10-14 10:49
 **/
@Component
public class DIJobDataSourceUsageSupplier implements DataSourceUsageSupplier {

    private final DIJobContentRepo diJobContentRepo;

    @Autowired
    public DIJobDataSourceUsageSupplier(DIJobContentRepo diJobContentRepo) {
        this.diJobContentRepo = diJobContentRepo;
    }

    @Override
    public boolean inUsing(DataSourceTypeEnum dataSourceType, Long dataSourceId) {
        long count = diJobContentRepo.countByDataSource(dataSourceId);
        return count > 0;
    }
}
