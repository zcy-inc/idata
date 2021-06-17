// enum
export enum TreeNodeType {
  FOLDER = 'FOLDER',
  TABLE = 'TABLE',
  LABEL = 'LABEL',
  ENUM = 'ENUM',
}
export enum LabelTag {
  STRING_LABEL = 'STRING_LABEL',
  BOOLEAN_LABEL = 'BOOLEAN_LABEL',
  ENUM_LABEL = 'ENUM_LABEL',
  ENUM_VALUE_LABEL = 'ENUM_VALUE_LABEL',
  ATTRIBUTE_LABEL = 'ATTRIBUTE_LABEL',
}

// const
export const rules = {
  require: [{ required: true, message: '必填' }],
};
