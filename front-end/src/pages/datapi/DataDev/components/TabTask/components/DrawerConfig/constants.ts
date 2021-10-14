import { SchRerunMode } from '@/constants/datadev';
import type { ProColumns } from '@ant-design/pro-table';

interface DataSourceType {}

export const columnsParent: ProColumns<DataSourceType>[] = [
  { title: '父节点输出任务名称', dataIndex: 'taskName', key: 'taskName', width: '30%' },
  { title: '父节点输出任务表名', dataIndex: 'tableName', key: 'tableName', width: '30%' },
  {
    title: '责任人',
    dataIndex: 'user',
    key: 'user',
    editable: (text, record, dataIndex) => false,
  },
  {
    title: '来源',
    dataIndex: 'from',
    key: 'from',
    editable: (text, record, dataIndex) => false,
  },
  { title: '操作', key: 'option', valueType: 'option' },
];

export const columnsOutput: ProColumns<DataSourceType>[] = [
  { title: '输出名称', dataIndex: 'taskName', key: 'taskName', width: '30%' },
  { title: '输出表名', dataIndex: 'tableName', key: 'tableName', width: '30%' },
  {
    title: '责任人',
    dataIndex: 'user',
    key: 'user',
    editable: (text, record, dataIndex) => false,
  },
  {
    title: '来源',
    dataIndex: 'from',
    key: 'from',
    editable: (text, record, dataIndex) => false,
  },
  { title: '操作', key: 'option', valueType: 'option' },
];

export const restartOptions = [
  { label: '皆可重跑', value: SchRerunMode.ALWAYS },
  { label: '失败重跑', value: SchRerunMode.FAILED },
  { label: '皆不重跑', value: SchRerunMode.NEVER },
];

export const concurrentOptions = [...Array(20).keys()].map((_) => ({
  label: _ + 1,
  value: _ + 1,
}));
