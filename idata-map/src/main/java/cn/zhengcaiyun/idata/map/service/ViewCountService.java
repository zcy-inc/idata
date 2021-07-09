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

package cn.zhengcaiyun.idata.map.service;

import cn.zhengcaiyun.idata.map.dto.ViewCountDto;

import java.util.List;

/**
 * @description: 用户浏览次数统计
 * @author: yangjianhua
 * @create: 2021-07-09 14:12
 **/
public interface ViewCountService {

    /**
     * 增加次数，默认加1
     *
     * @param userId       用户编号，0表示全局所有用户的统计数据
     * @param entitySource 实体记录数据源：数仓表（table） or 数据指标（indicator）
     * @param entityCode   实体记录唯一标识
     * @param operator
     * @return 记录id
     */
    Long increase(Long userId, String entitySource, String entityCode, String operator);

    /**
     * 查询浏览次数前N名的业务记录
     *
     * @param entitySource 实体记录数据源：数仓表（table） or 数据指标（indicator）
     * @param limit        查询记录数
     * @param offset       默认为0
     * @return 浏览次数对象
     */
    List<ViewCountDto> getTopCountEntity(String entitySource, Long limit, Long offset);

    /**
     * 查询指定用户浏览次数前N名的业务记录
     *
     * @param userId       用户编号
     * @param entitySource 实体记录数据源：数仓表（table） or 数据指标（indicator）
     * @param limit        查询记录数
     * @param offset       默认为0
     * @return 浏览次数对象
     */
    List<ViewCountDto> getTopCountEntityForUser(Long userId, String entitySource, Long limit, Long offset);
}
