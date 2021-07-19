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

package cn.zhengcaiyun.idata.map.facade.impl;

import cn.zhengcaiyun.idata.commons.pojo.Page;
import cn.zhengcaiyun.idata.commons.pojo.PageParam;
import cn.zhengcaiyun.idata.commons.util.KeywordUtil;
import cn.zhengcaiyun.idata.commons.util.PaginationInMemory;
import cn.zhengcaiyun.idata.commons.util.TreeNodeHelper;
import cn.zhengcaiyun.idata.map.bean.condition.CategoryCond;
import cn.zhengcaiyun.idata.map.bean.condition.DataSearchCond;
import cn.zhengcaiyun.idata.map.bean.condition.ViewCountCond;
import cn.zhengcaiyun.idata.map.bean.dto.CategoryTreeNodeDto;
import cn.zhengcaiyun.idata.map.bean.dto.DataEntityDto;
import cn.zhengcaiyun.idata.map.bean.dto.ViewCountDto;
import cn.zhengcaiyun.idata.map.facade.DataMapFacade;
import cn.zhengcaiyun.idata.map.manager.CategoryManager;
import cn.zhengcaiyun.idata.map.manager.DataEntityManager;
import cn.zhengcaiyun.idata.map.service.ViewCountService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-07-14 13:35
 **/
@Component
public class DataMapFacadeImpl implements DataMapFacade {

    private final DataEntityManager dataEntityManager;
    private final CategoryManager categoryManager;
    private final ViewCountService viewCountService;

    @Autowired
    public DataMapFacadeImpl(DataEntityManager dataEntityManager, CategoryManager categoryManager, ViewCountService viewCountService) {
        this.dataEntityManager = dataEntityManager;
        this.categoryManager = categoryManager;
        this.viewCountService = viewCountService;
    }

    @Override
    public Page<DataEntityDto> searchEntity(DataSearchCond condition, PageParam pageParam) {
        String keyword = condition.getKeyWord();
        if (isEmpty(keyword) || keyword.length() > 1000) return Page.newOne(Lists.newLinkedList(), 0);
        condition.setKeyWords(KeywordUtil.parseKeyword(keyword));
        condition.setKeyWord(null);

        List<DataEntityDto> entityDtoList = dataEntityManager.getDataEntity(condition.getSource(), condition);
        if (isEmpty(entityDtoList)) return Page.newOne(Lists.newLinkedList(), 0);

        // 数据量小于1000时，查询浏览次数带上实体code，减少查询量；大于1000时，直接查询全量浏览次数统计数据
        List<String> entityCodes = null;
        if (entityDtoList.size() <= 1000) {
            entityCodes = getEntityCode(entityDtoList);
        }
        Map<String, Long> seqMap = getEntitySeq(condition.getSource(), entityCodes, viewCountService::queryViewCount);
        List<DataEntityDto> seqEntityList = sequencedEntity(entityDtoList, seqMap);
        return PaginationInMemory.of(seqEntityList).paging(pageParam);
    }

    @Override
    public List<CategoryTreeNodeDto> getCategory(CategoryCond condition) {
        List<CategoryTreeNodeDto> treeNodeDtoList = categoryManager.getCategoryTreeNode(condition.getCategoryType(), condition);
        return TreeNodeHelper.withExpandedNodes(treeNodeDtoList).makeTree(() -> "");
    }

    public List<DataEntityDto> sequencedEntity(List<DataEntityDto> entityDtoList,
                                               Map<String, Long> seqMap) {
        entityDtoList.stream()
                .forEach(entityDto -> entityDto.setViewCount(seqMap.get(entityDto.getEntityCode())));
        return entityDtoList;
    }

    private Map<String, Long> getEntitySeq(String entitySource, List<String> entityCodes, Function<ViewCountCond, List<ViewCountDto>> sequenceProvider) {
        Map<String, Long> seqMap = Maps.newHashMap();
        ViewCountCond countCond = new ViewCountCond.Builder()
                .userId(0L).entitySource(entitySource).entityCodes(entityCodes)
                .build();
        List<ViewCountDto> viewCounts = sequenceProvider.apply(countCond);
        if (ObjectUtils.isNotEmpty(viewCounts))
            viewCounts.stream()
                    .forEach(viewCount -> seqMap.put(viewCount.getEntityCode(), viewCount.getViewCount()));
        return seqMap;
    }

    private List<String> getEntityCode(List<DataEntityDto> entityDtoList) {
        return entityDtoList.stream()
                .map(entityDto -> entityDto.getEntityCode())
                .collect(Collectors.toList());
    }

}
