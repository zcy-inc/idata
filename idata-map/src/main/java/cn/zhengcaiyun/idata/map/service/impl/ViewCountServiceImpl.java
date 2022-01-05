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
import cn.zhengcaiyun.idata.map.bean.condition.ViewCountCond;
import cn.zhengcaiyun.idata.map.bean.dto.DataEntityDto;
import cn.zhengcaiyun.idata.map.bean.dto.ViewCountDto;
import cn.zhengcaiyun.idata.map.constant.Constants;
import cn.zhengcaiyun.idata.map.dal.model.ViewCount;
import cn.zhengcaiyun.idata.map.dal.repo.ViewCountRepo;
import cn.zhengcaiyun.idata.map.manager.DataEntityManager;
import cn.zhengcaiyun.idata.map.service.ViewCountService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static cn.zhengcaiyun.idata.commons.enums.DeleteEnum.DEL_NO;
import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

/**
 * @description: 用户浏览次数统计
 * @author: yangjianhua
 * @create: 2021-07-09 14:50
 **/
@Service
public class ViewCountServiceImpl implements ViewCountService {

    private final ViewCountRepo viewCountRepo;
    private final DataEntityManager dataEntityManager;

    @Autowired
    public ViewCountServiceImpl(ViewCountRepo viewCountRepo, DataEntityManager dataEntityManager) {
        this.viewCountRepo = viewCountRepo;
        this.dataEntityManager = dataEntityManager;
    }

    @Override
    public Long increase(Long userId, String entitySource, String entityCode, String operator) {
        checkArgument(isNotEmpty(userId), "用户编号不能为空");
        checkArgument(isNotEmpty(entitySource), "请求参数不能为空");
        checkArgument(isNotEmpty(entityCode), "请求参数不能为空");
        Optional<ViewCount> viewCountOptional = viewCountRepo.queryUserViewCount(userId, entitySource, entityCode);
        ViewCount viewCount;
        if (viewCountOptional.isPresent()) {
            viewCount = viewCountOptional.get();
            viewCountRepo.updateViewCount(viewCount.getId(), viewCount.getViewCount() + 1);
        } else {
            viewCount = assembleViewCount(userId, entitySource, entityCode, operator);
            viewCountRepo.createViewCount(viewCount);
        }
        return viewCount.getId();
    }

    @Override
    public List<ViewCountDto> getTopCountEntity(String entitySource, Integer topNum) {
        checkArgument(isNotEmpty(entitySource), "请求参数不能为空");
        // 查询数据源下全局排行，userId传0L
        List<ViewCount> viewCountList = viewCountRepo.queryEntityWithOrderedCount(0L, entitySource, firstNonNull(topNum, 10));
        if (isEmpty(viewCountList)) return Lists.newArrayList();

        List<String> entityCodes = viewCountList.stream()
                .map(ViewCount::getEntityCode).collect(Collectors.toList());
        Map<String, DataEntityDto> entityNameMap = dataEntityManager.getDataEntityMap(entitySource, entityCodes);
        return assembleEntityName(copyToDto(viewCountList), entityNameMap);
    }

    @Override
    public List<ViewCountDto> getUserTopCountEntity(String entitySource, Integer topNum, Operator operator) {
        checkArgument(isNotEmpty(entitySource), "请求参数不能为空");
        List<ViewCount> viewCountList = viewCountRepo.queryEntityWithOrderedCount(operator.getId(), entitySource, firstNonNull(topNum, 10));
        if (isEmpty(viewCountList)) return Lists.newArrayList();

        List<String> entityCodes = viewCountList.stream()
                .map(ViewCount::getEntityCode).collect(Collectors.toList());
        Map<String, DataEntityDto> entityNameMap = dataEntityManager.getDataEntityMap(entitySource, entityCodes);
        return assembleEntityName(copyToDto(viewCountList), entityNameMap);
    }

    @Override
    public List<ViewCountDto> queryViewCount(ViewCountCond cond) {
        List<ViewCount> viewCountList = viewCountRepo.queryViewCount(cond);
        if (ObjectUtils.isEmpty(viewCountList))
            return Lists.newLinkedList();
        return copyToDto(viewCountList);
    }

    private List<ViewCountDto> copyToDto(List<ViewCount> viewCountList) {
        return viewCountList.stream().map(this::copyToDto).collect(Collectors.toList());
    }

    private ViewCountDto copyToDto(ViewCount viewCount) {
        ViewCountDto dto = new ViewCountDto();
        BeanUtils.copyProperties(viewCount, dto);
        return dto;
    }

    public List<ViewCountDto> assembleEntityName(List<ViewCountDto> countDtoList, Map<String, DataEntityDto> entityNameMap) {
        return countDtoList.stream().map(countDto -> {
            DataEntityDto entityDto = entityNameMap.get(countDto.getEntityCode());
            String entityName = isNotEmpty(entityDto) ? entityDto.getEntityName() : null;
            countDto.setEntityName(firstNonNull(entityName, ""));
            return countDto;
        }).collect(Collectors.toList());
    }

    private ViewCount assembleViewCount(Long userId, String entitySource, String entityCode, String operator) {
        ViewCount viewCount = new ViewCount();
        viewCount.setEntitySource(entitySource);
        viewCount.setEntityCode(entityCode);
        viewCount.setUserId(userId);
        viewCount.setViewCount(Constants.VIEW_COUNT_START);
        viewCount.setCreator(operator);
        viewCount.setDel(DEL_NO.val);
        viewCount.setEditor(operator);
        return viewCount;
    }

}
