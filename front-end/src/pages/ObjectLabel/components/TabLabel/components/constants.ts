import { RuleLayer } from '@/types/objectlabel';

export const InitialLayer: RuleLayer = {
  layerId: Date.now(),
  layerName: '分层1',
  ruleDef: {
    rules: [
      {
        ruleId: Date.now(),
        ruleName: '规则1',
        indicatorDefs: [{ indicatorCode: null, condition: null, params: [] }],
        dimensionDefs: [{ dimensionCode: null, params: [] }],
      },
    ],
  },
};

export const ConditionOptions = [
  { label: '等于', value: 'equal' },
  { label: '大于', value: 'greater' },
  { label: '小于', value: 'less' },
  { label: '大于等于', value: 'greaterOrEqual' },
  { label: '小于等于', value: 'lessOrEqual' },
  { label: '介于两个值之间', value: 'between' },
];
