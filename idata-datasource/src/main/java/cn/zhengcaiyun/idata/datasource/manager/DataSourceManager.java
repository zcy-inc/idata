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

package cn.zhengcaiyun.idata.datasource.manager;

import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.connector.api.MetadataQueryApi;
import cn.zhengcaiyun.idata.datasource.bean.dto.DbConfigDto;
import cn.zhengcaiyun.idata.datasource.spi.DataSourceUsageSupplier;
import cn.zhengcaiyun.idata.datasource.spi.DataSourceUsageSupplierFactory;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-16 11:06
 **/
@Component
public class DataSourceManager {

    private final MetadataQueryApi metadataQueryApi;
    private final DataSourceUsageSupplierFactory dataSourceUsageSupplierFactory;

    @Autowired
    public DataSourceManager(MetadataQueryApi metadataQueryApi,
                             DataSourceUsageSupplierFactory dataSourceUsageSupplierFactory) {
        this.metadataQueryApi = metadataQueryApi;
        this.dataSourceUsageSupplierFactory = dataSourceUsageSupplierFactory;
    }

    public Boolean testConnectionWithJDBC(DataSourceTypeEnum dataSourceType, DbConfigDto dto) {
        return metadataQueryApi.testConnection(dataSourceType, dto.getHost(), dto.getPort(), dto.getUsername(), dto.getPassword(),
                dto.getDbName(), dto.getSchema());
    }

    public boolean checkInUsing(DataSourceTypeEnum dataSourceType, Long dataSourceId) {
        List<DataSourceUsageSupplier> suppliers = dataSourceUsageSupplierFactory.getSuppliers();
        if (ObjectUtils.isEmpty(suppliers)) return false;

        for (DataSourceUsageSupplier supplier : suppliers) {
            boolean inUsing = supplier.inUsing(dataSourceType, dataSourceId);
            if (inUsing) return true;
        }
        return false;
    }
}
