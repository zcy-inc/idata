export const NewObjectLabelOriginId = -1;

export const rules = [{ required: true, message: '请输入' }];

export const ObjectType = [
  { label: '供应商', value: 'supplier:LABEL' },
  { label: '采购单位', value: 'purchaserOrg:LABEL' },
];
export const ObjectTypeView = {
  'supplier:LABEL': '供应商',
  'purchaserOrg:LABEL': '采购单位',
};
export const ConditionMap = {
  equal: '等于',
  greater: '大于',
  less: '小于',
  greaterOrEqual: '大于等于',
  lessOrEqual: '小于等于',
  between: '介于两个值之间',
};
