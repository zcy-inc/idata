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
import cn.zhengcaiyun.idata.commons.util.TreeNodeGenerator;
import cn.zhengcaiyun.idata.map.bean.condition.CategoryCond;
import cn.zhengcaiyun.idata.map.bean.condition.DataSearchCond;
import cn.zhengcaiyun.idata.map.bean.condition.ViewCountCond;
import cn.zhengcaiyun.idata.map.bean.dto.CategoryTreeNodeDto;
import cn.zhengcaiyun.idata.map.bean.dto.ColumnAttrDto;
import cn.zhengcaiyun.idata.map.bean.dto.DataEntityDto;
import cn.zhengcaiyun.idata.map.bean.dto.ViewCountDto;
import cn.zhengcaiyun.idata.map.facade.DataMapFacade;
import cn.zhengcaiyun.idata.map.manager.CategoryManager;
import cn.zhengcaiyun.idata.map.manager.DataEntityManager;
import cn.zhengcaiyun.idata.map.service.ViewCountService;
import cn.zhengcaiyun.idata.map.util.DataEntityUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
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
        checkArgument(StringUtils.isNotBlank(condition.getSource()), "需指定搜索数据类型");
        String keyword = condition.getKeyWord();
        if (isEmpty(keyword) || keyword.length() > 1000) return Page.newOne(Lists.newLinkedList(), 0);
        condition.setKeyWords(KeywordUtil.parseKeyword(keyword));
        condition.setKeyWord(null);

        // 查询数据实体，当前只包含数据实体code（唯一标识），排序分页后再查询实体其他数据
        List<DataEntityDto> entityDtoList = dataEntityManager.queryDataEntity(condition.getSource(), condition);
        if (isEmpty(entityDtoList)) return Page.newOne(Lists.newLinkedList(), 0);

        // 搜索结果数据量小于1000时，查询浏览次数带上实体code，减少查询量；大于1000时，直接查询全量浏览次数统计数据
        List<String> entityCodes = null;
        if (entityDtoList.size() <= 1000) {
            entityCodes = DataEntityUtil.getEntityCode(entityDtoList);
        }
        Map<String, Long> seqMap = getEntitySeq(condition.getSource(), entityCodes, viewCountService::queryViewCount);
        // 浏览次数添加到数据实体中
        List<DataEntityDto> seqEntityList = assembleSequence(entityDtoList, seqMap);
        // 排序后分页
        Page<DataEntityDto> entityPage = PaginationInMemory.of(seqEntityList).sort().paging(pageParam);
        // 根据分页结果查询数据实体全部数据
        List<DataEntityDto> entityWholeInfoList = dataEntityManager.getEntityWholeInfo(condition.getSource(), entityPage.getContent());
        if (condition.searchFromTable()) {
            entityWholeInfoList = pickEntityMatchColumn(entityWholeInfoList, condition.getKeyWords());
        }
        // entityWholeInfoList 中为分页后重新查询的数据对象，需要再次加上浏览次数排序（只排当页数据）
        List<DataEntityDto> seqEntityWholeInfoList = assembleSequence(entityWholeInfoList, seqMap);
        entityPage.setContent(seqEntityWholeInfoList.stream().sorted().collect(Collectors.toList()));
        return entityPage;
    }

    @Override
    public List<CategoryTreeNodeDto> getCategory(CategoryCond condition) {
        checkArgument(StringUtils.isNotBlank(condition.getCategoryType()), "需指定类别");
        List<CategoryTreeNodeDto> treeNodeDtoList = categoryManager.getCategoryTreeNode(condition.getCategoryType(), condition);
        return TreeNodeGenerator.withExpandedNodes(treeNodeDtoList).makeTree(() -> "", condition.getLevel());
    }

    private List<DataEntityDto> assembleSequence(List<DataEntityDto> entityDtoList,
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

    private List<DataEntityDto> pickEntityMatchColumn(List<DataEntityDto> entityWholeInfoList, List<String> keyWords) {
        if (ObjectUtils.isEmpty(entityWholeInfoList) || ObjectUtils.isEmpty(keyWords)) return entityWholeInfoList;

        for (DataEntityDto entityDto : entityWholeInfoList) {
            entityDto.putMoreAttr(DataEntityDto.more_table_match_column, pickMatchColumn((List<ColumnAttrDto>) entityDto.getMoreAttrs(DataEntityDto.more_table_column), keyWords));
        }
        return entityWholeInfoList;
    }

    private List<ColumnAttrDto> pickMatchColumn(List<ColumnAttrDto> dtoList, List<String> keyWords) {
        if (ObjectUtils.isEmpty(keyWords) || ObjectUtils.isEmpty(dtoList)) return Lists.newArrayList();

        List<ColumnAttrDto> pickList = Lists.newArrayList();
        for (ColumnAttrDto dto : dtoList) {
            boolean matchAllWords = false;
            for (String keyWord : keyWords) {
                if (StringUtils.isEmpty(keyWord))
                    continue;
                if ((StringUtils.isNotEmpty(dto.getName()) && dto.getName().indexOf(keyWord) >= 0)
                        || (StringUtils.isNotEmpty(dto.getNameEn()) && dto.getNameEn().indexOf(keyWord) >= 0)) {
                    matchAllWords = true;
                } else {
                    matchAllWords = false;
                    break;
                }
            }
            if (matchAllWords) pickList.add(dto);
        }
        return pickList;
    }

}

