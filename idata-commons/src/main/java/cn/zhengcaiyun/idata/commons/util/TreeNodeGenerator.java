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

package cn.zhengcaiyun.idata.commons.util;

import cn.zhengcaiyun.idata.commons.pojo.TreeNodeDto;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.MultimapBuilder;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @description: 工具类，创建TreeNodeDto树
 * example: TreeNodeGenerator.withExpandedNodes(treeNodeDtoList).makeTree(() -> "0");
 * @author: yangjianhua
 * @create: 2021-07-15 17:18
 * @see TreeNodeDto
 **/
public class TreeNodeGenerator<N extends TreeNodeDto> {
    private final List<N> expandedNodes;

    private TreeNodeGenerator(List<N> expandedNodes) {
        this.expandedNodes = expandedNodes;
    }

    /**
     * 传入所有节点的集合，创建helper类
     *
     * @param expandedNodes
     * @return
     */
    public static <N extends TreeNodeDto> TreeNodeGenerator withExpandedNodes(List<N> expandedNodes) {
        return new TreeNodeGenerator(expandedNodes);
    }

    /**
     * 传入父节点id Supplier
     *
     * @param parentIdProvider
     * @return
     */
    public List<N> makeTree(Supplier<String> parentIdProvider) {
        return makeTree(parentIdProvider, null);
    }

    /**
     * 传入父节点id Supplier
     *
     * @param parentIdProvider
     * @return
     */
    public List<N> makeTree(Supplier<String> parentIdProvider, final Integer maxLevel) {
        if (CollectionUtils.isEmpty(expandedNodes)) return Lists.newArrayList();

        ListMultimap<String, N> treeListMultimap = MultimapBuilder.hashKeys().arrayListValues().build();
        expandedNodes.stream().forEach(nodeDto -> treeListMultimap.put(nodeDto.getParentId().toString(), nodeDto));

        int curLevel = 1;
        List<N> treeList = makeTree(parentIdProvider.get(), null, treeListMultimap, curLevel, maxLevel);
        return treeList == null ? Lists.newArrayList() : treeList;
    }

    private List<N> makeTree(String parentId, N parentNode, ListMultimap<String, N> listMultimap, int curLevel, final Integer maxLevel) {
        if (Objects.nonNull(maxLevel) && curLevel > maxLevel) {
            return null;
        }
        List<N> nodeDtoList = listMultimap.get(parentId);
        if (CollectionUtils.isEmpty(nodeDtoList)) return null;

        String parentCid;
        if (Objects.isNull(parentNode)) {
            // 最上层节点没有 parentNode，特殊处理生成 parentCid
            parentCid = "##top##node_" + parentId;
        } else {
            parentCid = parentNode.getCid();
        }
        for (N nodeDto : nodeDtoList) {
            nodeDto.setParentCid(parentCid);
            nodeDto.setChildren(makeTree(nodeDto.getId().toString(), nodeDto, listMultimap, curLevel + 1, maxLevel));
        }
        return nodeDtoList;
    }
}
