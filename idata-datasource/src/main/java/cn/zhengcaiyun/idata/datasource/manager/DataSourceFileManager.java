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

import cn.zhengcaiyun.idata.commons.enums.EnvEnum;
import cn.zhengcaiyun.idata.connector.spi.hdfs.HdfsService;
import cn.zhengcaiyun.idata.connector.spi.livy.LivyService;
import cn.zhengcaiyun.idata.connector.spi.livy.enums.LivySessionKindEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-17 10:44
 **/
@Component
public class DataSourceFileManager {

    public static final Logger LOGGER = LoggerFactory.getLogger(DataSourceFileManager.class);

    private final HdfsService hdfsService;
    private final LivyService livyService;

    @Autowired
    public DataSourceFileManager(HdfsService hdfsService, LivyService livyService) {
        this.hdfsService = hdfsService;
        this.livyService = livyService;
    }

    public Boolean importSourceFileData(InputStream fileStream, String originFileName, String destTableName, List<EnvEnum> envList) {
        String hdfsFilePath = hdfsService.uploadFileToCsv(fileStream, originFileName);
        for (EnvEnum envEnum : envList) {
            String tableName = EnvEnum.prod == envEnum ? destTableName : (envEnum.name() + "_" + destTableName);
            String code = String.format("spark.read.option(\"header\", true).option(\"inferSchema\", true)"
                            + ".csv(\"%s\").write.mode(\"overwrite\").saveAsTable(\"%s\")",
                    hdfsFilePath, tableName);
            livyService.submitSparkCode(code, LivySessionKindEnum.spark);
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException ex) {
                LOGGER.warn("spark 导入数据失败", ex);
            }
        }
        return Boolean.TRUE;
    }
}
