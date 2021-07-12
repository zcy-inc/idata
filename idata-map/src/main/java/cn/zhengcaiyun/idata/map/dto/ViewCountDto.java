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

package cn.zhengcaiyun.idata.map.dto;

import cn.zhengcaiyun.idata.commons.dto.BaseDto;

/**
 * @description: 浏览记录次数dto
 * @author: yangjianhua
 * @create: 2021-07-09 14:22
 **/
public class ViewCountDto extends BaseDto {
    /**
     * 主键
     */
    private Long id;

    /**
     * 实体记录唯一标识
     */
    private String entityCode;

    /**
     * 实体记录唯一标识
     */
    private String entityName;

    /**
     * 实体记录数据源：数仓表（table） or 数据指标（indicator）
     */
    private String entitySource;

    /**
     * 用户编号，0表示全局所有用户的统计数据
     */
    private Long userId;

    /**
     * 浏览次数
     */
    private Long viewCount;
}
