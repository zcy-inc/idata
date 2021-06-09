export const SubjectTypeMap = { TABLE: '表', COLUMN: '字段' };
export const LabelTagMap = {
  STRING_LABEL: '文本标签',
  BOOLEAN_LABEL: '布尔标签',
  USER_LABEL: '用户标签',
  ENUM_LABEL: '范围标签',
  ENUM_VALUE_LABEL: '枚举标签',
  ATTRIBUTE_LABEL: '属性标签',
};
export const LabelRequiredMap = { 0: '否', 1: '是' };
export const ExLabelTag = ['ENUM_VALUE_LABEL', 'ATTRIBUTE_LABEL'];
export const ExLabelTagTitle = { ENUM_VALUE_LABEL: '枚举值', ATTRIBUTE_LABEL: '属性值' };
export const InitialColumns = [
  { title: '枚举值', dataIndex: 'enumValue', key: 'enumValue' },
  {
    title: '父级枚举值',
    dataIndex: 'parentValue',
    key: 'parentValue',
    render: (_: any) => _ || '-',
  },
];
export const AttributeTypeMap = {
  STRING: '文本',
  BOOLEAN: '布尔',
};
