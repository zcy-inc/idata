package cn.zhengcaiyun.idata.label.compute.sql.transform;

import cn.zhengcaiyun.idata.label.compute.metadata.IndicatorMetadata;
import cn.zhengcaiyun.idata.label.compute.sql.model.BaseColumn;
import cn.zhengcaiyun.idata.label.compute.sql.model.ColumnModel;
import cn.zhengcaiyun.idata.label.compute.sql.model.TableModel;
import cn.zhengcaiyun.idata.label.compute.sql.model.condition.BaseCondition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
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

    public Optional<BaseCondition<String>> toDecorateWordCondition(IndicatorMetadata.DecorateWordMetadata metadata, TableModel tableModel) {
        if (Objects.isNull(metadata)) return Optional.empty();

        checkArgument(StringUtils.isNotEmpty(metadata.getColumn()), "修饰词不正确");
        checkArgument(!CollectionUtils.isEmpty(metadata.getParams()), "修饰词不正确");
        ColumnModel column = ColumnModel.of(metadata.getColumn(), tableModel);
        BaseCondition<String> inThe = column.inThe(metadata.getParams().toArray(new String[0]));
        return Optional.of(inThe);
    }

    public Optional<BaseCondition<Long>> toIndicatorCondition(IndicatorMetadata metadata, TableModel tableModel) {
        BaseColumn column = toFunctionColumn(metadata, tableModel, null);
        return column.withCondition(metadata.getIndicatorCondition(), metadata.getIndicatorParams());
    }
}
