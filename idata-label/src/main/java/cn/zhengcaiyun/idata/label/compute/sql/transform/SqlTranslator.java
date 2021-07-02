package cn.zhengcaiyun.idata.label.compute.sql.transform;

import cn.zhengcaiyun.idata.label.compute.MeasureApiAgent;
import cn.zhengcaiyun.idata.label.compute.metadata.DimensionMetadata;
import cn.zhengcaiyun.idata.label.compute.metadata.IndicatorMetadata;
import cn.zhengcaiyun.idata.label.compute.metadata.ObjectMetadata;
import cn.zhengcaiyun.idata.label.compute.sql.model.*;
import cn.zhengcaiyun.idata.label.compute.sql.model.condition.BaseCondition;
import cn.zhengcaiyun.idata.label.dto.label.rule.DimensionDefDto;
import cn.zhengcaiyun.idata.label.dto.label.rule.IndicatorDefDto;
import cn.zhengcaiyun.idata.label.dto.label.rule.LabelRuleDto;
import cn.zhengcaiyun.idata.label.enums.ObjectTypeEnum;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
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

    private final MeasureApiAgent measureApiAgent;
    private final DimensionTranslator dimensionTranslator;
    private final IndicatorTranslator indicatorTranslator;

    @Autowired
    public SqlTranslator(DimensionTranslator dimensionTranslator, IndicatorTranslator indicatorTranslator, MeasureApiAgent measureApiAgent) {
        this.dimensionTranslator = dimensionTranslator;
        this.indicatorTranslator = indicatorTranslator;
        this.measureApiAgent = measureApiAgent;
    }

    public String translate(LabelRuleDto ruleDto, String objectType, Long limit, Long offset) {
        checkArgument(ruleDto != null, "标签规则为空.");
        Optional<ObjectTypeEnum> optionalObjectTypeEnum = ObjectTypeEnum.getEnum(objectType);
        checkState(optionalObjectTypeEnum.isPresent(), "标签主体不正确.");
        checkArgument(!CollectionUtils.isEmpty(ruleDto.getIndicatorDefs()), "未选择指标");

        IndicatorMetadata indicatorMetadata = getIndicatorMetadata(ruleDto.getIndicatorDefs().get(0));
        List<DimensionMetadata> dimensionMetadataList = getDimensionMetadata(ruleDto.getDimensionDefs(), indicatorMetadata);
        ObjectMetadata objectMetadata = getObjectMetadata(optionalObjectTypeEnum.get());

        QueryModel queryModel = translateToModel(objectMetadata, indicatorMetadata,
                dimensionMetadataList, limit, offset);
        return joinObjectInfo(queryModel, objectMetadata, optionalObjectTypeEnum.get());
    }

    /**
     * join主体表，查询主体名称，元数据信息不足，暂时硬编码sql
     *
     * @param queryModel
     * @param objectMetadata
     * @param objectTypeEnum
     * @return
     */
    private String joinObjectInfo(QueryModel queryModel, ObjectMetadata objectMetadata, ObjectTypeEnum objectTypeEnum) {
        String subSql = queryModel.renderSql();
        String sql = "select d." + objectTypeEnum.getObjectNameFiled() + " as \"主体名称\", t.* "
                + "from ("
                + subSql
                + ") t inner join " + objectMetadata.getOriginTable() + " d"
                + " on t.\"主体编号\" = d." + objectMetadata.getColumn();
        return sql;
    }

    private QueryModel translateToModel(ObjectMetadata objectMetadata,
                                        IndicatorMetadata indicatorMetadata,
                                        List<DimensionMetadata> dimensionMetadataList,
                                        Long limit, Long offset) {
        // 表
        TableModel tableModel = indicatorTranslator.getTable(indicatorMetadata);
        // 查询列
        SelectModel selectModel = buildSelectModel(objectMetadata, indicatorMetadata, dimensionMetadataList, tableModel);

        //BaseColumn objNameColumn = ColumnModel.of(objectTypeEnum.getObjectNameFiled(), tableModel, "主体名称");
        //where 语句模型
        Optional<WhereModel> whereModelOptional = buildWhereModel(indicatorMetadata, dimensionMetadataList, tableModel);
        // group by 语句
        GroupModel groupModel = buildGroupModel(objectMetadata, dimensionMetadataList, tableModel);
        //having 语句
        HavingModel havingModel = buildHavingModel(indicatorMetadata, tableModel);
        //paging
        PagingModel pagingModel = PagingModel.of(limit, offset);

        return new QueryModel.Builder(selectModel, tableModel)
                .where(whereModelOptional.orElse(null))
                .group(groupModel)
                .having(havingModel)
                .paging(pagingModel)
                .build();
    }

    private HavingModel buildHavingModel(IndicatorMetadata indicatorMetadata,
                                         TableModel tableModel) {
        // 维度描述组合，作为指标列别名前缀
        Optional<BaseCondition<Long>> indCondOptional = indicatorTranslator.toIndicatorCondition(indicatorMetadata, tableModel);
        checkState(indCondOptional.isPresent(), "指标不正确");
        return HavingModel.of(indCondOptional.get());
    }

    private GroupModel buildGroupModel(ObjectMetadata objectMetadata,
                                       List<DimensionMetadata> dimensionMetadataList,
                                       TableModel tableModel) {
        // 查询主体列
        BaseColumn objIdColumn = ColumnModel.of(objectMetadata.getColumn(), tableModel);
        // 查询维度列
        Optional<List<BaseColumn>> dimColumnOptional = dimensionTranslator.toColumns(dimensionMetadataList, tableModel);
        GroupModel groupModel = GroupModel.of().addColumn(objIdColumn);
        if (dimColumnOptional.isPresent()) {
            List<BaseColumn> dimColumns = dimColumnOptional.get();
            dimColumns.stream().forEach(groupModel::addColumn);
        }
        return groupModel;
    }

    private Optional<WhereModel> buildWhereModel(IndicatorMetadata indicatorMetadata,
                                                 List<DimensionMetadata> dimensionMetadataList,
                                                 TableModel tableModel) {
        WhereModel whereModel = null;
        // 修饰词暂时放在where条件下，当多指标（表）时，可以放到select条件中
        List<BaseCondition<String>> conditionList = Lists.newArrayList();
        Optional<List<BaseCondition<String>>> dwCondOptional = indicatorTranslator.toDecorateWordCondition(indicatorMetadata.getDecorateWords(), tableModel);
        if (dwCondOptional.isPresent()) {
            conditionList.addAll(dwCondOptional.get());
        }
        Optional<List<BaseCondition<String>>> dimCondOptional = dimensionTranslator.toConditions(dimensionMetadataList, tableModel);
        if (dimCondOptional.isPresent()) {
            conditionList.addAll(dimCondOptional.get());
        }
        for (BaseCondition<String> dimCond : conditionList) {
            if (whereModel == null) {
                whereModel = WhereModel.of(dimCond);
            } else {
                whereModel.and(dimCond);
            }
        }
        return Optional.ofNullable(whereModel);
    }

    private SelectModel buildSelectModel(ObjectMetadata objectMetadata,
                                         IndicatorMetadata indicatorMetadata,
                                         List<DimensionMetadata> dimensionMetadataList,
                                         TableModel tableModel) {
        // 查询主体列
        BaseColumn objIdColumn = ColumnModel.of(objectMetadata.getColumn(), tableModel, "\"主体编号\"");
        // 维度描述组合，作为指标列别名前缀
        String dimCombineDesc = dimensionTranslator.combineDimDesc(dimensionMetadataList);
        // 指标函数列
        BaseColumn indicatorFuncColumn = indicatorTranslator.toFunctionColumn(indicatorMetadata, tableModel, dimCombineDesc);
        return SelectModel.of().addColumn(objIdColumn, indicatorFuncColumn);
    }

    private IndicatorMetadata getIndicatorMetadata(IndicatorDefDto indicatorDefDto) {
        // 从指标系统获取指标数据，转换为 IndicatorDto
        Optional<IndicatorMetadata> optional = measureApiAgent.getIndicatorMetadata(indicatorDefDto.getIndicatorCode());
        checkState(optional.isPresent(), "未获取到可用的指标数据");
        IndicatorMetadata metadata = optional.get();
        metadata.setIndicatorCondition(indicatorDefDto.getCondition());
        metadata.setIndicatorParams(indicatorDefDto.getParams());
        return metadata;
    }

    private List<DimensionMetadata> getDimensionMetadata(List<DimensionDefDto> dimensionDefs, IndicatorMetadata indicatorMetadata) {
        checkArgument(!CollectionUtils.isEmpty(dimensionDefs), "未选择维度.");
        List<String> dimensionCodes = dimensionDefs.stream()
                .map(defDto -> defDto.getDimensionCode())
                .collect(Collectors.toList());
        // 从指标系统获取维度数据，转换为 DimensionDto
        Optional<Map<String, DimensionMetadata>> optional = measureApiAgent.getDimensionMetadata(dimensionCodes, indicatorMetadata);
        checkState(optional.isPresent(), "未获取到可用的维度数据");
        Map<String, DimensionMetadata> metadataMap = optional.get();
        List<DimensionMetadata> metadataList = Lists.newArrayList();
        for (DimensionDefDto dimensionDefDto : dimensionDefs) {
            DimensionMetadata metadata = metadataMap.get(dimensionDefDto.getDimensionCode());
            checkState(!Objects.isNull(metadata), "未获取到可用的维度数据");
            metadata.setDimensionParams(dimensionDefDto.getParams());
            metadataList.add(metadata);
        }
        return metadataList;
    }

    private ObjectMetadata getObjectMetadata(ObjectTypeEnum objectTypeEnum) {
        checkArgument(objectTypeEnum != null, "未选择标签主体.");
        // 从指标系统获取维度数据，转换为 ObjectDto
        Optional<ObjectMetadata> optional = measureApiAgent.getObjectMetadata(objectTypeEnum.getType());
        checkState(optional.isPresent(), "未获取到标签主体数据");
        return optional.get();
    }

    public static void main(String[] args) {
        SqlTranslator sqlTranslator = new SqlTranslator(new DimensionTranslator(), new IndicatorTranslator(), null);
        System.out.println(sqlTranslator.mockSQL());
    }

    public String mockSQL() {
        IndicatorMetadata indicatorDto = new IndicatorMetadata();
        IndicatorMetadata.DecorateWordMetadata decorateWordDto_1 = new IndicatorMetadata.DecorateWordMetadata();
        decorateWordDto_1.setColumn("trade_status_name");
        decorateWordDto_1.setParams(Lists.newArrayList("已完成", "无效"));
        IndicatorMetadata.DecorateWordMetadata decorateWordDto_2 = new IndicatorMetadata.DecorateWordMetadata();
        decorateWordDto_2.setColumn("order_status_name");
        decorateWordDto_2.setParams(Lists.newArrayList("已完成", "已备案"));

        indicatorDto.setCode("trade_amount_1");
        indicatorDto.setName("交易总额");
        indicatorDto.setTable("dwd.dwd_trade_order_detail");
        indicatorDto.setColumn("item_amount");
        indicatorDto.setFunction("AGGREGATOR_SUM:ENUM_VALUE");
        indicatorDto.setDecorateWords(Lists.newArrayList(decorateWordDto_1, decorateWordDto_2));
        indicatorDto.setIndicatorCondition("greaterOrEqual");
        indicatorDto.setIndicatorParams(new Long[]{30000L});

        List<DimensionMetadata> dimensionDtoList = Lists.newArrayList();
        DimensionMetadata dimensionDto_1 = new DimensionMetadata();
        dimensionDto_1.setCode("dim-1");
        dimensionDto_1.setName("市级区划");
        dimensionDto_1.setTable("dwd.dwd_trade_order_detail");
        dimensionDto_1.setColumn("district_city_code");
        dimensionDto_1.setDimensionParams(new String[]{"330600"});
        dimensionDtoList.add(dimensionDto_1);
        DimensionMetadata dimensionDto_2 = new DimensionMetadata();
        dimensionDto_2.setCode("dim-2");
        dimensionDto_2.setName("业务模块");
        dimensionDto_2.setTable("dwd.dwd_trade_order_detail");
        dimensionDto_2.setColumn("biz_module");
        dimensionDto_2.setDimensionParams(new String[]{"网上超市"});
        dimensionDtoList.add(dimensionDto_2);

        ObjectTypeEnum objectTypeEnum = ObjectTypeEnum.supplier;
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setCode("object-1");
        objectMetadata.setName("主体");
        objectMetadata.setTable("dwd.dwd_trade_order_detail");
        objectMetadata.setColumn("supplier_org_id");
        objectMetadata.setOriginTable("dim.dim_supplier");

        Long limit = 1L;
        Long offset = null;

        QueryModel queryModel = translateToModel(objectMetadata,
                indicatorDto, dimensionDtoList, limit, offset);
        String sql = joinObjectInfo(queryModel, objectMetadata, objectTypeEnum);
        return sql;
    }

}
