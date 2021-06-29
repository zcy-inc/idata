package cn.zhengcaiyun.idata.label.compute.sql.transform;

import cn.zhengcaiyun.idata.label.compute.metadata.DimensionMetadata;
import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;
import cn.zhengcaiyun.idata.label.compute.sql.model.ColumnModel;
import cn.zhengcaiyun.idata.label.compute.sql.model.TableModel;
import cn.zhengcaiyun.idata.label.compute.sql.model.condition.BaseCondition;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
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
 * @create: 2021-06-29 14:37
 **/
@Component
public class DimensionTranslator {

    public Optional<List<BaseColumn>> toColumns(List<DimensionMetadata> dimensionMetadataList, TableModel tableModel) {
        if (CollectionUtils.isEmpty(dimensionMetadataList)) return Optional.empty();

        List<BaseColumn> columnList = Lists.newArrayList();
        for (DimensionMetadata metadata : dimensionMetadataList) {
            columnList.add(toColumn(metadata, tableModel));
        }
        return Optional.of(columnList);
    }

    public BaseColumn toColumn(DimensionMetadata metadata, TableModel tableModel) {
        checkArgument(StringUtils.isNotEmpty(metadata.getColumn()), "维度不正确");
        return ColumnModel.of(metadata.getColumn(), tableModel, ColumnAliasUtil.aliasWithQuotes(metadata.getName()));
    }

    public Optional<List<BaseCondition<String>>> toConditions(List<DimensionMetadata> dimensionMetadataList, TableModel tableModel) {
        if (CollectionUtils.isEmpty(dimensionMetadataList)) return Optional.empty();

        List<BaseCondition<String>> conditions = Lists.newArrayList();
        for (DimensionMetadata metadata : dimensionMetadataList) {
            BaseColumn column = toColumn(metadata, tableModel);
            checkState(ObjectUtils.isNotEmpty(metadata.getDimensionParams()), "维度不正确");
            conditions.add(column.equalTo(metadata.getDimensionParams()[0]));
        }
        return Optional.of(conditions);
    }

    public String combineDimDesc(List<DimensionMetadata> dimensionMetadataList) {
        if (CollectionUtils.isEmpty(dimensionMetadataList)) return "";

        StringBuilder builder = new StringBuilder();
        int count = 0;
        for (DimensionMetadata metadata : dimensionMetadataList) {
            if (count > 0) {
                builder.append("且");
            }

            builder.append(metadata.getName());
            if (ObjectUtils.isNotEmpty(metadata.getDimensionParams())) {
                builder.append("是");
                builder.append(metadata.getDimensionParams()[0]);
            }
            count++;
        }
        builder.append("的");
        return builder.toString();
    }
}
