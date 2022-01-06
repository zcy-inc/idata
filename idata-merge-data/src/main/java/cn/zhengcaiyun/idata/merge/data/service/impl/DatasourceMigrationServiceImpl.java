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

import cn.zhengcaiyun.idata.datasource.service.DataSourceService;
import cn.zhengcaiyun.idata.merge.data.dal.old.OldIDataDao;
import cn.zhengcaiyun.idata.merge.data.dto.MigrateResultDto;
import cn.zhengcaiyun.idata.merge.data.service.DatasourceMigrationService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-01-06 09:43
 **/
@Service
public class DatasourceMigrationServiceImpl implements DatasourceMigrationService {

    @Autowired
    private OldIDataDao oldIDataDao;
    @Autowired
    private DataSourceService dataSourceService;

    @Override
    public List<MigrateResultDto> migrate() {
        // 查询旧版IData数据
        List<String> dataJsonList = fetchOldData();
        // 处理旧版数据，组装新版IData数据

        // 调用新版server接口，新增数据
//        dataSourceService.addDataSource();

        // 返回迁移失败的数据 MigrateResultDto
        //DataSourceDto dto, Operator operator;
        return null;
    }

    private List<String> fetchOldData() {
        List<String> columns = Lists.newArrayList("id", "type", "name", "description", "host", "port", "db_name",
                "db_user", "db_psw", "operator", "owner", "status");
        String filter = "is_del = false and host is not null and host != ''";
        return oldIDataDao.queryJsonStringList("metadata.datasource", columns, filter);
    }

}
