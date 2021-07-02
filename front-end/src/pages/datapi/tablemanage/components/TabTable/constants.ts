export const InitialLabel = {
  labelName: '表名称',
  labelCode: 'tableName',
  labelRequired: 1,
  labelTag: 'STRING_LABEL',
};

export const InitialColumn = {
  labelName: '字段英文名',
  labelCode: 'columnName',
  labelRequired: 1,
  labelTag: 'STRING_LABEL',
};

export const EROps = [
  { label: '1:1', value: 'I2I' },
  { label: '1:N', value: 'I2N' },
  { label: 'N:1', value: 'N2I' },
  { label: 'M:N', value: 'M2N' },
];

export const ViewInitialColumns = [
  { title: '字段英文名', dataIndex: 'columnName', key: 'columnName' },
];

export const TransformBoolean = {
  true: '是',
  false: '否',
};

export const RadioOps = [
  { label: '是', value: 'true' },
  { label: '否', value: 'false' },
];
