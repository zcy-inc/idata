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

package cn.zhengcaiyun.idata.develop.validator;

import cn.zhengcaiyun.idata.develop.dto.job.di.DIStreamJobContentDto;
import cn.zhengcaiyun.idata.develop.dto.job.di.DIStreamJobTableDto;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2022-08-17 17:46
 **/
public class DIStreamJobContentValidator {

    public static void checkContent(DIStreamJobContentDto contentDto) {
        checkArgument(StringUtils.isNotBlank(contentDto.getSrcDataSourceType()), "来源数据源类型为空");
        checkArgument(Objects.nonNull(contentDto.getSrcDataSourceId()), "来源数据源为空");
        checkArgument(StringUtils.isNotBlank(contentDto.getDestDataSourceType()), "去向数据源类型为空");
        checkArgument(Objects.nonNull(contentDto.getDestDataSourceId()), "去向数据源为空");
        checkArgument(Objects.nonNull(contentDto.getEnableSharding()), "是否开启分表参数不正确");

        List<DIStreamJobTableDto> tableDtoList = contentDto.getTableDtoList();
        checkArgument(CollectionUtils.isNotEmpty(tableDtoList), "同步表配置为空");
        Set<String> srcTableSet = Sets.newHashSet();
        for (DIStreamJobTableDto tableDto : tableDtoList) {
            checkArgument(StringUtils.isNotBlank(tableDto.getSrcTable()), "同步表配置 - 来源表名为空");
            checkArgument(StringUtils.isNotBlank(tableDto.getDestTable()), "同步表配置 - 去向表名为空");
            checkArgument(srcTableSet.add(tableDto.getSrcTable()), "同步表配置 - 同一来源表多次配置");
        }
    }
}
