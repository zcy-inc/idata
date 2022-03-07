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

package cn.zhengcaiyun.idata.develop.service.job;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.commons.enums.DataSourceTypeEnum;
import cn.zhengcaiyun.idata.commons.enums.DriverTypeEnum;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIJobContentContentDto;

import java.util.List;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-28 14:07
 **/
public interface DIJobContentService {

    DIJobContentContentDto save(Long jobId, DIJobContentContentDto contentDto, Operator operator);

    DIJobContentContentDto get(Long jobId, Integer version);

    /**
     * 生成mergeSql
     * @param columnList select的列
     * @param keyColumns 主见keys，逗号分隔
     * @param sourceTable 源数据表，可能是多张，按正则解析
     * @param destTable 目标表（带库名）
     * @param typeEnum 驱动类型 @see cn.zhengcaiyun.idata.commons.enums.DriverTypeEnum
     * @param days 天数，近几天
     * @return
     * @throws IllegalArgumentException
     */
    String generateMergeSql(List<String> columnList, String keyColumns, String sourceTable, String destTable, DataSourceTypeEnum typeEnum, int days) throws IllegalArgumentException;
}
