package cn.zhengcaiyun.idata.label.compute.sql.transform;

import cn.zhengcaiyun.idata.label.compute.dto.DimensionDto;
import cn.zhengcaiyun.idata.label.compute.dto.IndicatorDto;
import cn.zhengcaiyun.idata.label.compute.sql.model.*;
import cn.zhengcaiyun.idata.label.compute.sql.model.condition.BaseCondition;
import cn.zhengcaiyun.idata.label.compute.sql.model.condition.EqualTo;
import cn.zhengcaiyun.idata.label.compute.sql.model.condition.InThe;
import cn.zhengcaiyun.idata.label.dto.label.rule.DimensionDefDto;
import cn.zhengcaiyun.idata.label.dto.label.rule.IndicatorDefDto;
import cn.zhengcaiyun.idata.label.dto.label.rule.LabelRuleDto;
import cn.zhengcaiyun.idata.label.enums.ObjectTypeEnum;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * @description:
 * @author: yangjianhua
 * @create: 2021-06-24 19:56
 **/
@Component
public class SqlTranslator {
    public String translate(LabelRuleDto ruleDto, String objectType, Long limit, Long offset) {
        checkArgument(ruleDto != null, "标签规则为空.");
        Optional<ObjectTypeEnum> optionalObjectTypeEnum = ObjectTypeEnum.getEnum(objectType);
        checkState(optionalObjectTypeEnum.isPresent(), "标签主体不正确.");

        List<IndicatorDto> indicatorDtoList = getIndicatorInfo(ruleDto.getIndicatorDefs());
        checkState(!CollectionUtils.isEmpty(indicatorDtoList), "未获取到指标数据.");
        List<DimensionDto> dimensionDtoList = getDimensionInfo(ruleDto.getDimensionDefs());
        checkState(!CollectionUtils.isEmpty(dimensionDtoList), "未获取到维度数据.");

        QueryModel queryModel = translateToModel(indicatorDtoList.get(0), dimensionDtoList,
                optionalObjectTypeEnum.get(), limit, offset);
        return queryModel.renderSql();
    }

    private QueryModel translateToModel(IndicatorDto indicatorDto, List<DimensionDto> dimensionDtoList,
                                        ObjectTypeEnum objectTypeEnum, Long limit, Long offset) {
        //维度名
        String dimCombineName = combineDimensionName(dimensionDtoList);
        // 表
        TableModel tableModel = TableModel.of(indicatorDto.getTable());
        // 查询字段
        BaseColumn objIdColumn = ColumnModel.of(objectTypeEnum.getObjectIdFiled(), tableModel, "主体编号");
        BaseColumn objNameColumn = ColumnModel.of(objectTypeEnum.getObjectNameFiled(), tableModel, "主体名称");
        // 指标函数列
        BaseColumn indicatorFuncColumn = getIndicatorColumn(indicatorDto, tableModel, dimCombineName);
        SelectModel selectModel = SelectModel.of().addColumn(objIdColumn, objNameColumn, indicatorFuncColumn);

        //where 语句模型
        Optional<WhereModel> whereModelOptional = buildWhereModel(indicatorDto, dimensionDtoList, tableModel);
        // group by 语句
        GroupByModel groupByModel = GroupByModel.of().addColumn(objIdColumn, objNameColumn);
        //having 语句
        Optional<BaseCondition<Long>> havingCondition = ConditionFactory.createCondition(indicatorDto.getIndicatorCondition(),
                indicatorFuncColumn, indicatorDto.getIndicatorParams());
        HavingModel havingModel = null;
        if (havingCondition.isPresent())
            havingModel = HavingModel.of(havingCondition.get());

        // order by
        OrderByModel orderByModel = OrderByModel.of().addDescColumn(indicatorFuncColumn);

        //paging
        PagingModel pagingModel = PagingModel.of(limit, offset);
        return new QueryModel.Builder(selectModel, tableModel)
                .where(whereModelOptional.orElse(null))
                .group(groupByModel)
                .having(havingModel)
                .order(orderByModel)
                .paging(pagingModel)
                .build();
    }

    private BaseColumn getIndicatorColumn(IndicatorDto indicatorDto, TableModel tableModel, String prefixAliasName) {
        String aliasName = prefixAliasName + indicatorDto.getName();
        ColumnModel indicatorColumn = ColumnModel.of(indicatorDto.getColumn(), tableModel, aliasName);
        Optional<BaseColumn> columnOptional = FunctionColumnFactory.createFunctionColumn(indicatorDto.getFunction(), indicatorColumn, aliasName);
        checkState(columnOptional.isPresent(), "指标函数不正确");
        return columnOptional.get();
    }

    private String combineDimensionName(List<DimensionDto> dimensionDtoList) {
        StringBuilder builder = new StringBuilder();
        for (DimensionDto dto : dimensionDtoList) {
            builder.append(dto.getName());
        }
        return builder.toString();
    }

    private Optional<WhereModel> buildWhereModel(IndicatorDto indicatorDto, List<DimensionDto> dimensionDtoList, TableModel tableModel) {
        WhereModel whereModel = null;
        IndicatorDto.DecorateWordDto decorateWordDto = indicatorDto.getDecorateWord();
        if (!Objects.isNull(decorateWordDto)) {
            checkState(StringUtils.isNotEmpty(decorateWordDto.getColumn()), "修饰词不正确");
            checkState(!CollectionUtils.isEmpty(decorateWordDto.getParams()), "修饰词不正确");

            InThe<String> inThe = InThe.of(ColumnModel.of(decorateWordDto.getColumn(), tableModel),
                    decorateWordDto.getParams().toArray(new String[0]));
            whereModel = WhereModel.of(inThe);
        }
        if (!CollectionUtils.isEmpty(dimensionDtoList)) {
            for (DimensionDto dimensionDto : dimensionDtoList) {
                checkState(StringUtils.isNotEmpty(dimensionDto.getColumn()), "维度不正确");
                checkState(ObjectUtils.isNotEmpty(dimensionDto.getDimensionParams()), "维度不正确");
                EqualTo<String> equalTo = EqualTo.of(ColumnModel.of(dimensionDto.getColumn(), tableModel),
                        dimensionDto.getDimensionParams());
                if (whereModel == null) {
                    whereModel = WhereModel.of(equalTo);
                } else {
                    whereModel.and(equalTo);
                }
            }
        }
        return Optional.ofNullable(whereModel);
    }

    private List<IndicatorDto> getIndicatorInfo(List<IndicatorDefDto> indicatorDefs) {
        checkArgument(!CollectionUtils.isEmpty(indicatorDefs), "未选择指标.");
        List<String> indicatorCodes = indicatorDefs.stream()
                .map(defDto -> defDto.getIndicatorCode())
                .collect(Collectors.toList());
        // 从指标系统获取指标数据，转换为 IndicatorDto
        return null;
    }

    private List<DimensionDto> getDimensionInfo(List<DimensionDefDto> dimensionDefs) {
        checkArgument(!CollectionUtils.isEmpty(dimensionDefs), "未选择维度.");
        List<String> dimensionCodes = dimensionDefs.stream()
                .map(defDto -> defDto.getDimensionCode())
                .collect(Collectors.toList());
        // 从指标系统获取维度数据，转换为 DimensionDto
        return null;
    }
}
