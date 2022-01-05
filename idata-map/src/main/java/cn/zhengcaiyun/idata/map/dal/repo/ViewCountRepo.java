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

package cn.zhengcaiyun.idata.map.dal.repo;

import cn.zhengcaiyun.idata.map.bean.condition.ViewCountCond;
import cn.zhengcaiyun.idata.map.dal.model.ViewCount;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-19 14:11
 **/
public interface ViewCountRepo {

    /**
     * 查询业务实体浏览次数数据
     *
     * @param cond 查询条件
     * @return
     */
    List<ViewCount> queryViewCount(ViewCountCond cond);

    /**
     * 查询用户级业务实体浏览次数数据
     *
     * @param userId       用户id
     * @param entitySource 业务实体所属源
     * @param entityCode   业务实体唯一标识
     * @return
     */
    Optional<ViewCount> queryUserViewCount(Long userId, String entitySource, String entityCode);

    /**
     * 查询业务实体，根据count排序（desc）
     *
     * @param userId       用户id
     * @param entitySource 业务实体所属源
     * @param limit        限制条数
     * @return
     */
    List<ViewCount> queryEntityWithOrderedCount(Long userId, String entitySource, int limit);

    /**
     * 创建业务实体浏览次数记录
     *
     * @param viewCount
     * @return
     */
    int createViewCount(ViewCount viewCount);

    /**
     * 更新指定记录的浏览次数
     *
     * @param id
     * @param count
     * @return
     */
    int updateViewCount(Long id, long count);
}
