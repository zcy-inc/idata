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

import cn.zhengcaiyun.idata.commons.enums.TreeNodeTypeEnum;
import cn.zhengcaiyun.idata.commons.pojo.TreeNodeDto;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-09-18 17:08
 **/
public class TreeNodeFilter<N extends TreeNodeDto> {
    private final List<N> treeNodes;

    private TreeNodeFilter(List<N> treeNodes) {
        this.treeNodes = treeNodes;
    }

    /**
     * 传入所有节点的集合，创建helper类
     *
     * @param treeNodes
     * @return
     */
    public static <N extends TreeNodeDto> TreeNodeFilter withTreeNodes(List<N> treeNodes) {
        return new TreeNodeFilter(treeNodes);
    }

    public static class FilterCondition<T extends TreeNodeDto> {
        private final String keyWord;
        private final Long id;

        private final Pattern keyWordPattern;
        private final Matcher keyWordMatcher;

        public FilterCondition(String keyWord, Long id) {
            this.keyWord = keyWord;
            this.id = id;
            this.keyWordPattern = Pattern.compile(keyWord);
            // 重用 Matcher，不可用于多线程场景
            this.keyWordMatcher = this.keyWordPattern.matcher("");
        }

        public boolean match(T node) {
            if (TreeNodeTypeEnum.RECORD.name().equals(node.getType()) && Objects.nonNull(id)) {
                if (id.equals(node.getId())) return true;
            }

            keyWordMatcher.reset(node.getName());
            if (keyWordMatcher.matches()) return true;

            return false;
        }
    }

    public List<N> filterTree(final FilterCondition<N> condition) {
        if (CollectionUtils.isEmpty(treeNodes)) return treeNodes;

        List<N> filteredNodes = Lists.newArrayList();
        for (N node : treeNodes) {
            N filteredNode = filterNode(node, condition);
            if (filteredNode != null) filteredNodes.add(filteredNode);
        }
        return filteredNodes;
    }

    private N filterNode(final N node, final FilterCondition<N> condition) {
        // 当前文件夹或文件匹配，则不再过滤下级节点，直接返回当前节点及所有下级节点
        if (!condition.match(node)) {
            // 当前文件夹或文件不匹配，则继续过滤下级节点
            List<N> childrenNodes = node.getChildren();
            if (childrenNodes == null || childrenNodes.size() == 0) {
                // 没有下级节点，则删除该节点
                return null;
            }
            List<N> filteredChildrenNodes = null;
            for (N childrenNode : childrenNodes) {
                N filteredChildrenNode = filterNode(childrenNode, condition);
                if (filteredChildrenNode != null) {
                    if (filteredChildrenNodes == null) {
                        filteredChildrenNodes = Lists.newArrayList();
                    }
                    // 匹配的子节点重新加入集合
                    filteredChildrenNodes.add(filteredChildrenNode);
                }
            }
            if (filteredChildrenNodes == null || filteredChildrenNodes.size() == 0) {
                // 所有子节点都不匹配，则删除当前节点
                return null;
            }
            // 重新设置经过过滤的子节点
            node.setChildren(filteredChildrenNodes);
        }
        return node;
    }

}
