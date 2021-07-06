// enum
export enum TreeNodeType {
  FOLDER = 'FOLDER',
  TABLE = 'TABLE',
  LABEL = 'LABEL',
  ENUM = 'ENUM',
  DIMENSION_LABEL = 'DIMENSION',
  MODIFIER_LABEL = 'MODIFIER',
  METRIC_LABEL = 'METRIC',
}
export enum LabelRequired {
  NOT_REQUIRED = 0,
  REQUIRED = 1,
}
export enum LabelTag {
  STRING_LABEL = 'STRING_LABEL',
  BOOLEAN_LABEL = 'BOOLEAN_LABEL',
  ENUM_LABEL = 'ENUM_LABEL',
  ENUM_VALUE_LABEL = 'ENUM_VALUE_LABEL',
  ATTRIBUTE_LABEL = 'ATTRIBUTE_LABEL',
  USER_LABEL = 'USER_LABEL',
  DIMENSION_LABEL = 'DIMENSION_LABEL',
  DIMENSION_LABEL_DISABLE = 'DIMENSION_LABEL_DISABLE',
  MODIFIER_LABEL = 'MODIFIER_LABEL',
  MODIFIER_LABEL_DISABLE = 'MODIFIER_LABEL_DISABLE',
  ATOMIC_METRIC_LABEL = 'ATOMIC_METRIC_LABEL',
  ATOMIC_METRIC_LABEL_DISABLE = 'ATOMIC_METRIC_LABEL_DISABLE',
  DERIVE_METRIC_LABEL = 'DERIVE_METRIC_LABEL',
  DERIVE_METRIC_LABEL_DISABLE = 'DERIVE_METRIC_LABEL_DISABLE',
  COMPLEX_METRIC_LABEL = 'COMPLEX_METRIC_LABEL',
  COMPLEX_METRIC_LABEL_DISABLE = 'COMPLEX_METRIC_LABEL_DISABLE',
}
export enum ERType {
  I2I = 'I2I',
  I2N = 'I2N',
  N2I = 'N2I',
  M2N = 'M2N',
}

// const
export const rules = {
  require: [{ required: true, message: '必填' }],
};
export const BooleanOptions = [
  { label: '是', value: true },
  { label: '否', value: false },
];

// 维度："enName", "dimensionId", "dimensionDefine"，"comment"
// 修饰词："enName", "modifierEnum", "modifierDefine"，"comment"
// 指标："enName", "metricId", "bizTypeCode", "metricDefine"，"comment"
export const KpiLabelsMap = {
  enName: '英文别名',
  comment: '备注',
  dimensionId: '维度ID',
  dimensionDefine: '定义',
  modifierEnum: '枚举值',
  modifierDefine: '定义',
  metricId: '指标ID',
  bizTypeCode: '业务过程',
  metricDefine: '定义',
};
export enum AggregatorCodeMap {
  SUM = 'AGGREGATOR_SUM:ENUM_VALUE',
  AVG = 'AGGREGATOR_AVG:ENUM_VALUE',
  MAX = 'AGGREGATOR_MAX:ENUM_VALUE',
  MIN = 'AGGREGATOR_MIN:ENUM_VALUE',
  CNT = 'AGGREGATOR_CNT:ENUM_VALUE',
  CNTD = 'AGGREGATOR_CNTD:ENUM_VALUE',
}
export const AggregatorCodeTransform = {
  [AggregatorCodeMap.SUM]: '求和',
  [AggregatorCodeMap.AVG]: '求平均',
  [AggregatorCodeMap.MAX]: '最大值',
  [AggregatorCodeMap.MIN]: '最小值',
  [AggregatorCodeMap.CNT]: '计数',
  [AggregatorCodeMap.CNTD]: '去重计数',
};
export const AggregatorCodeOptions = [
  { label: '求和', value: AggregatorCodeMap.SUM },
  { label: '求平均', value: AggregatorCodeMap.AVG },
  { label: '最大值', value: AggregatorCodeMap.MAX },
  { label: '最小值', value: AggregatorCodeMap.MIN },
  { label: '计数', value: AggregatorCodeMap.CNT },
  { label: '去重计数', value: AggregatorCodeMap.CNTD },
];
