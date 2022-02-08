import type { IDataSourceType } from '@/types/system-controller'
import type { ProColumns } from '@ant-design/pro-table';
export const columns: ProColumns<IDataSourceType>[] = [
  {
    title: '参数名称',
    dataIndex: 'configValueKey',
    width: '280px',
    editable: false
  },
  {
    title: '值',
    dataIndex: 'configValue',
    formItemProps: {
      rules: [
        {
          required: true,
          whitespace: true,
          message: '此项是必填项',
        }]
    },
  },
  {
    title: '备注',
    width: '360px',
    dataIndex: 'configValueRemarks',
  },
];
