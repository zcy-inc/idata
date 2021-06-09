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

export const EROPs = [
  { label: '1:1', value: 'I2I' },
  { label: '1:N', value: 'I2N' },
  { label: 'N:1', value: 'N2I' },
  { label: 'M:N', value: 'M2N' },
];

export const ViewInitialColumns = [
  { title: '字段英文名', dataIndex: 'columnName', key: 'columnName' },
];
