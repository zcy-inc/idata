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

package cn.zhengcaiyun.idata.map.dal.repo.impl;

import cn.zhengcaiyun.idata.map.bean.condition.ViewCountCond;
import cn.zhengcaiyun.idata.map.dal.dao.ViewCountDao;
import cn.zhengcaiyun.idata.map.dal.model.ViewCount;
import cn.zhengcaiyun.idata.map.dal.repo.ViewCountRepo;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_NO;
import static cn.zhengcaiyun.idata.map.dal.dao.ViewCountDao.selectList;
import static cn.zhengcaiyun.idata.map.dal.dao.ViewCountDynamicSqlSupport.VIEW_COUNT;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-19 14:13
 **/
@Repository
public class ViewCountRepoImpl implements ViewCountRepo {

    private final ViewCountDao countDao;

    @Autowired
    public ViewCountRepoImpl(ViewCountDao countDao) {
        this.countDao = countDao;
    }

    @Override
    public List<ViewCount> queryViewCount(ViewCountCond cond) {
        SelectStatementProvider selectStatement = select(selectList)
                .from(VIEW_COUNT)
                .where(VIEW_COUNT.userId, isEqualTo(cond.getUserId()).when(Objects::nonNull))
                .and(VIEW_COUNT.entitySource, isEqualTo(cond.getEntitySource()).when(StringUtils::isNotEmpty))
                .and(VIEW_COUNT.entityCode, isInWhenPresent(cond.getEntityCodes()))
                .and(VIEW_COUNT.del, isEqualTo(DEL_NO.val))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return countDao.selectMany(selectStatement);
    }

    @Override
    public Optional<ViewCount> queryUserViewCount(Long userId, String entitySource, String entityCode) {
        List<ViewCount> viewCounts = countDao.select(dsl -> dsl.where(VIEW_COUNT.userId, isEqualTo(userId),
                and(VIEW_COUNT.entitySource, isEqualTo(entitySource)),
                and(VIEW_COUNT.entityCode, isEqualTo(entityCode)),
                and(VIEW_COUNT.del, isEqualTo(DEL_NO.val)))
                .orderBy(VIEW_COUNT.id.descending()).limit(1));
        return isNotEmpty(viewCounts) ? Optional.of(viewCounts.get(0)) : Optional.empty();
    }

    @Override
    public List<ViewCount> queryEntityWithOrderedCount(Long userId, String entitySource, int limit) {
        return countDao.select(dsl -> dsl.where(VIEW_COUNT.userId, isEqualTo(userId),
                and(VIEW_COUNT.entitySource, isEqualTo(entitySource)),
                and(VIEW_COUNT.del, isEqualTo(DEL_NO.val)))
                .orderBy(VIEW_COUNT.viewCount.descending())
                .limit(limit).offset(0L));
    }

    @Override
    public int createViewCount(ViewCount viewCount) {
        return countDao.insert(viewCount);
    }

    @Override
    public int updateViewCount(Long id, long count) {
        return countDao.update(dsl -> dsl.set(VIEW_COUNT.viewCount).equalTo(count)
                .where(VIEW_COUNT.id, isEqualTo(id)));
    }

}
