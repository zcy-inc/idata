// enum
export enum TreeNodeType {
  FOLDER = 'FOLDER',
  TABLE = 'TABLE',
  LABEL = 'LABEL',
  ENUM = 'ENUM',
  DIMENSION_LABEL = 'DIMENSION_LABEL',
  MODIFIER_LABEL = 'MODIFIER_LABEL',
  METRIC_LABEL = 'METRIC_LABEL',
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
  ATOMIC_METRIC_LABEL = 'ATOMIC_METRIC_LABEL',
  DERIVE_METRIC_LABEL = 'DERIVE_METRIC_LABEL',
  COMPLEX_METRIC_LABEL = 'COMPLEX_METRIC_LABEL',
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
  { label: '是', value: 'true' },
  { label: '否', value: 'false' },
];
