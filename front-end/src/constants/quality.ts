export const alarmLevelList = [
  {value: 1, label: '一般'},
  {value: 2, label: '重要'},
  {value: 3, label: '严重'},
];

export const monitorObjList = [
  {value: 'field', label: '表'},
  {value: 'table', label: '字段'},
];

export const ruleTypeList = [
  {value: 'system', label: '内置规则'},
  {value: 'template', label: '模版规则'},
  {value: 'custom', label: '自定义规则'},
];

export enum operatorEnum {
  BETWEEN = 'between',
  GT = 'gt',
  GE = 'ge',
  LT = 'lt',
  LE = 'le',
  EQ = 'eq',
  NE = 'ne',
}

export const operatorMap = new Map([
  [operatorEnum.BETWEEN, '区间'],
  [operatorEnum.GT, '>'],
  [operatorEnum.GE, '>='],
  [operatorEnum.LT, '<'],
  [operatorEnum.LE, '<='],
  [operatorEnum.EQ, '='],
  [operatorEnum.NE, '<'],
]);

// 内置规则类型
export enum templateTypeEnum {
  COLUMN_NUMERICAL_RANGE = 'COLUMN_NUMERICAL_RANGE', // 字段数据值范围有效
  COLUMN_ENUM_NUM = 'COLUMN_ENUM_NUM', // 枚举值个数有效
  COLUMN_ENUM_CONTENT = 'COLUMN_ENUM_CONTENT', // 枚举值内容有效
  TABLE_PK_UNIQUE = 'TABLE_PK_UNIQUE', // 主键唯一
  COLUMN_EMPTY = 'COLUMN_EMPTY', // 字段值为空串或null
  TABLE_ROWS_DECLINE = 'TABLE_ROWS_DECLINE', // 表行数减少（仅针对增量表）
  TABLE_ROWS_FLUCTUATION = 'TABLE_ROWS_FLUCTUATION', // 表行数波动
  TABLE_NOT_EMPTY = 'TABLE_NOT_EMPTY', // 表不为空
  TABLE_DATA_CREATE_TIME = 'TABLE_DATA_CREATE_TIME', // 数据产出时间
  CUSTOM_SQL = 'CUSTOM_SQL', // 自定义sql
}

// 监测对象
export enum granularityEnum {
  TABLE = 'TABLE',
  COLUMN = 'COLUMN',
}

export const granularityMap = new Map([
  [granularityEnum.TABLE, '表'],
  [granularityEnum.COLUMN, '字段'],
]);

// 规则类型
export enum alarmSourceTypeEnum {
  BUILT_IN = 'built_in',
  CUSTOM_EVENT = 'custom_event',
}

export const alarmSourceTypeMap = new Map([
  [alarmSourceTypeEnum.BUILT_IN, '内置规则'],
  [alarmSourceTypeEnum.CUSTOM_EVENT, '自定义规则'],
]);

export enum outputTypeEnum {
  WHOLE = 'WHOLE',
}

export const outputTypeMap = new Map([[outputTypeEnum.WHOLE, '数值']]);

// 监控指标
export enum metricTypeEnum {
  ACCURACY = 'accuracy',
  INTEGRITY = 'integrity',
  TIMELINESS = 'timeliness',
}

export const metricTypeMap = new Map([
  [metricTypeEnum.ACCURACY, '准确性'],
  [metricTypeEnum.INTEGRITY, '完整性'],
  [metricTypeEnum.TIMELINESS, '时效性'],
]);
