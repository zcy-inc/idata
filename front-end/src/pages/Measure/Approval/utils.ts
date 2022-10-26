import type { ColumnsType } from 'antd/lib/table/Table';
import { ApprovalListItem } from '@/types/measure';
import moment from 'moment';

// 已处理表格
export const defaultColumns: ColumnsType<ApprovalListItem> = [
  { title: '指标名称', key: 'dwLayerValue', dataIndex: 'dwLayerValue' },
  { title: '指标类型', key: 'jobName', dataIndex: 'jobName' },
  // { title: '主题域', key: 'jobContentVersionDisplay', dataIndex: 'jobContentVersionDisplay' },
  // { title: '业务过程', key: 'environment', dataIndex: 'environment' },
  { title: '提交人', key: 'creator', dataIndex: 'creator' },
  {
    title: '提交备注',
    key: 'submitRemark',
    dataIndex: 'submitRemark',
    render: (_) => _ || '-',
  },
  {
    title: '提交时间',
    key: 'createTime',
    dataIndex: 'createTime',
    render: (_) => moment(_).format('YYYY-MM-DD HH:mm:ss'),
  },
  {
    title: '审批人',
    key: 'approveOperator',
    dataIndex: 'approveOperator',
  },
  {
    title: '审批结果',
    key: 'approveResult',
    dataIndex: 'approveResult',
  },
  {
    title: '审批时间',
    key: 'approveTime',
    dataIndex: 'approveTime',
    render: (_) => _ ? moment(_).format('YYYY-MM-DD HH:mm:ss') : '',
  },
];

// 待处理表格
export const defaultWaitColumns: ColumnsType<ApprovalListItem> = [
  { title: '指标名称', key: 'dwLayerValue', dataIndex: 'dwLayerValue' },
  { title: '指标类型', key: 'jobName', dataIndex: 'jobName' },
  // { title: '主题域', key: 'jobContentVersionDisplay', dataIndex: 'jobContentVersionDisplay' },
  // { title: '业务过程', key: 'environment', dataIndex: 'environment' },
  { title: '提交人', key: 'creator', dataIndex: 'creator' },
  {
    title: '提交备注',
    key: 'submitRemark',
    dataIndex: 'submitRemark',
    render: (_) => _ || '-',
  },
  {
    title: '提交时间',
    key: 'createTime',
    dataIndex: 'createTime',
    render: (_) => moment(_).format('YYYY-MM-DD HH:mm:ss'),
  },
];