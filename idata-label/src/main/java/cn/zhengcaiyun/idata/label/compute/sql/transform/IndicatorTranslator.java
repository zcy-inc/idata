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
package cn.zhengcaiyun.idata.label.compute.sql.transform;

import cn.zhengcaiyun.idata.label.compute.metadata.IndicatorMetadata;
import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;
import cn.zhengcaiyun.idata.label.compute.sql.model.ColumnModel;
import cn.zhengcaiyun.idata.label.compute.sql.model.TableModel;
import cn.zhengcaiyun.idata.label.compute.sql.model.condition.BaseCondition;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-29 15:07
 **/
@Component
public class IndicatorTranslator {

    public TableModel getTable(IndicatorMetadata metadata) {
        checkArgument(StringUtils.isNotEmpty(metadata.getTable()), "指标不正确");
        return TableModel.of(metadata.getTable());
    }

    public BaseColumn toFunctionColumn(IndicatorMetadata metadata, TableModel tableModel, String prefixAliasName) {
        String aliasName = ColumnAliasUtil.aliasWithQuotes(prefixAliasName + metadata.getName());
        ColumnModel indicatorColumn = ColumnModel.of(metadata.getColumn(), tableModel, aliasName);
        Optional<BaseColumn> columnOptional = FunctionColumnFactory.createFunctionColumn(metadata.getFunction(), indicatorColumn, aliasName);
        checkState(columnOptional.isPresent(), "指标函数不正确");
        return columnOptional.get();
    }

    public Optional<List<BaseCondition<String>>> toDecorateWordCondition(List<IndicatorMetadata.DecorateWordMetadata> metadataList, TableModel tableModel) {
        if (CollectionUtils.isEmpty(metadataList)) return Optional.empty();

        List<BaseCondition<String>> conditionList = Lists.newLinkedList();
        for (IndicatorMetadata.DecorateWordMetadata metadata : metadataList) {
            checkArgument(StringUtils.isNotEmpty(metadata.getColumn()), "修饰词不正确");
            checkArgument(!CollectionUtils.isEmpty(metadata.getParams()), "修饰词不正确");
            ColumnModel column = ColumnModel.of(metadata.getColumn(), tableModel);
            BaseCondition<String> inThe = column.inThe(metadata.getParams().toArray(new String[0]));
            conditionList.add(inThe);
        }
        return Optional.of(conditionList);
    }

    public Optional<BaseCondition<Long>> toIndicatorCondition(IndicatorMetadata metadata, TableModel tableModel) {
        BaseColumn column = toFunctionColumn(metadata, tableModel, null);
        return column.withCondition(metadata.getIndicatorCondition(), metadata.getIndicatorParams());
    }
}
