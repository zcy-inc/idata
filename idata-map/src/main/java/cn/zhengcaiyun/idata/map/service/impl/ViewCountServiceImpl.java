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

package cn.zhengcaiyun.idata.map.service.impl;

import cn.zhengcaiyun.idata.commons.context.Operator;
import cn.zhengcaiyun.idata.map.dal.dao.ViewCountDao;
import cn.zhengcaiyun.idata.map.dal.model.ViewCount;
import cn.zhengcaiyun.idata.map.dto.ViewCountDto;
import cn.zhengcaiyun.idata.map.service.ViewCountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_NO;
import static cn.zhengcaiyun.idata.map.dal.dao.ViewCountDynamicSqlSupport.VIEW_COUNT;
import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.mybatis.dynamic.sql.SqlBuilder.and;
import static org.mybatis.dynamic.sql.SqlBuilder.isEqualTo;

/**
 * @description: 用户浏览次数统计
 * @author: yangjianhua
 * @create: 2021-07-09 14:50
 **/
@Service
public class ViewCountServiceImpl implements ViewCountService {

    @Autowired
    private ViewCountDao countDao;

    @Override
    public Long increase(Long userId, String entitySource, String entityCode, String operator) {
        checkArgument(isNotEmpty(userId), "用户编号不能为空");
        checkArgument(isNotEmpty(entitySource), "请求参数不能为空");
        checkArgument(isNotEmpty(entityCode), "请求参数不能为空");
        List<ViewCount> viewCounts = countDao.select(dsl -> dsl.where(VIEW_COUNT.userId, isEqualTo(userId),
                and(VIEW_COUNT.entitySource, isEqualTo(entitySource)),
                and(VIEW_COUNT.entityCode, isEqualTo(entityCode)),
                and(VIEW_COUNT.del, isEqualTo(DEL_NO.val)))
                .orderBy(VIEW_COUNT.id.descending()).limit(1));
        ViewCount viewCount;
        if (isEmpty(viewCounts)) {
            viewCount = createViewCount(userId, entitySource, entityCode, operator);
        } else {
            viewCount = viewCounts.get(0);
            increaseViewCount(viewCount.getId(), viewCount.getViewCount());
        }
        return viewCount.getId();
    }

    @Override
    public List<ViewCountDto> getTopCountEntity(String entitySource, Integer topNum) {
        checkArgument(isNotEmpty(entitySource), "请求参数不能为空");
        // 查询数据源下全局排行，userId传0L
        List<ViewCount> viewCountList = queryTopByViewCount(0L, entitySource, firstNonNull(topNum, 10));
        return toDto(viewCountList);
    }

    @Override
    public List<ViewCountDto> getTopCountEntityForUser(String entitySource, Integer topNum, Operator operator) {
        checkArgument(isNotEmpty(entitySource), "请求参数不能为空");
        List<ViewCount> viewCountList = queryTopByViewCount(operator.getId(), entitySource, firstNonNull(topNum, 10));
        return toDto(viewCountList);
    }

    private List<ViewCountDto> toDto(List<ViewCount> viewCountList) {
        return viewCountList.stream().map(this::toDto).collect(Collectors.toList());
    }

    private ViewCountDto toDto(ViewCount viewCount) {
        ViewCountDto dto = new ViewCountDto();
        BeanUtils.copyProperties(viewCount, dto);

        //todo 补充 entityName
        return dto;
    }

    private List<ViewCount> queryTopByViewCount(Long userId, String entitySource, Integer topNum) {
        return countDao.select(dsl -> dsl.where(VIEW_COUNT.userId, isEqualTo(userId),
                and(VIEW_COUNT.entitySource, isEqualTo(entitySource)),
                and(VIEW_COUNT.del, isEqualTo(DEL_NO.val)))
                .orderBy(VIEW_COUNT.viewCount.descending())
                .limit(topNum).offset(0L));
    }

    private int increaseViewCount(Long id, long increasedCount) {
        return countDao.update(dsl -> dsl.set(VIEW_COUNT.viewCount).equalTo(increasedCount)
                .where(VIEW_COUNT.id, isEqualTo(id)));
    }

    private ViewCount createViewCount(Long userId, String entitySource, String entityCode, String operator) {
        ViewCount viewCount = new ViewCount();
        viewCount.setEntitySource(entitySource);
        viewCount.setEntityCode(entityCode);
        viewCount.setUserId(userId);
        viewCount.setViewCount(1L);
        viewCount.setCreator(operator);
        viewCount.setDel(DEL_NO.val);
        viewCount.setEditor(operator);
        countDao.insert(viewCount);
        return viewCount;
    }

}
