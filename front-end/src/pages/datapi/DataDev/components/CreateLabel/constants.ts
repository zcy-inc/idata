export const rules = [{ required: true, message: '必填' }];
export const SubTypeOps = [
  { label: '表', value: 'TABLE' },
  { label: '字段', value: 'COLUMN' },
];
export const IsReqOps = [
  { label: '是', value: 1 },
  { label: '否', value: 0 },
];
// 标签类型
export const LabelTagOps = {
  STRING_LABEL: '文本',
  BOOLEAN_LABEL: '布尔',
  USER_LABEL: '用户标签',
  ENUM_LABEL: '范围标签',
  ENUM_VALUE_LABEL: '枚举标签',
  ATTRIBUTE_LABEL: '属性标签',
};
// 当标签类型不等于 ATTRIBUTE_LABEL 时, 需要 labelParamType 参数
// 当 labelTag 不等于 ENUM_VALUE_LABEL 时，为下文定义当常量
// 当 labelTag 等于 ENUM_VALUE_LABEL 时, 为10位随机小写字符串
export const TagToParamMap = {
  STRING_LABEL: 'STRING',
  BOOLEAN_LABEL: 'BOOLEAN',
  USER_LABEL: 'WHOLE',
  ENUM_LABEL: 'ENUM',
  // ENUM_VALUE_LABEL: 'random(10)',
};
export const EditorBoolOps = [
  { label: 'True', value: 'true' },
  { label: 'False', value: 'false' },
];
export const initialEnumTypeCols = [
  { title: '枚举值', dataIndex: 'enumValue', key: 'enumValue' },
  { title: '父级枚举值', dataIndex: 'parentValue', key: 'parentValue' },
];
