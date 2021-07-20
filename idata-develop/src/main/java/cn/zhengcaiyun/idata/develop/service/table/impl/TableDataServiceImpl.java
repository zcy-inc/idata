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

import cn.zhengcaiyun.idata.connector.api.DataFetchApi;
import cn.zhengcaiyun.idata.connector.bean.dto.TableDataDto;
import cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDao;
import cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDao;
import cn.zhengcaiyun.idata.develop.dal.model.DevTableInfo;
import cn.zhengcaiyun.idata.develop.service.table.TableDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static cn.zhengcaiyun.idata.develop.dal.dao.DevLabelDynamicSqlSupport.devLabel;
import static cn.zhengcaiyun.idata.develop.dal.dao.DevTableInfoDynamicSqlSupport.devTableInfo;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-20 14:04
 **/
@Service
public class TableDataServiceImpl implements TableDataService {

    @Autowired
    private DataFetchApi dataFetchApi;
    @Autowired
    private DevLabelDao devLabelDao;
    @Autowired
    private DevTableInfoDao devTableInfoDao;

    private final String DB_NAME_LABEL = "dbName:LABEL";

    @Override
    public TableDataDto getTableData(Long tableId) {
        DevTableInfo tableInfo = devTableInfoDao.selectOne(c -> c.where(devTableInfo.id, isEqualTo(tableId),
                and(devTableInfo.del, isNotEqualTo(1))))
                .orElseThrow(() -> new IllegalArgumentException("表不存在"));
        return dataFetchApi.getTableData(getDbName(tableId), tableInfo.getTableName(), 10, 0);
    }

    private String getDbName(Long tableId) {
        return devLabelDao.selectOne(c -> c
                .where(devLabel.del, isNotEqualTo(1), and(devLabel.labelCode, isEqualTo(DB_NAME_LABEL)),
                        and(devLabel.tableId, isEqualTo(tableId))))
                .get().getLabelParamValue();
    }
}
