import { Environments } from '@/constants/datasource';
import { PublishStatus } from '@/constants/task';
export interface MetricListItem {
  metricId?: string; // 指标id
  modifierId?: string; // 修饰词id
  labelName: string; // 指标名称
  labelCode: string; // 指标code
  labelTag: string; // 指标类型 + 指标状态
  domain: string; // 主题域
  metricDefine: string; // 业务口径
  bizProcessValue: string; // 业务过程
  metricDeadline: string; // 截止生效日期
  creator: string; // 创建人
  editor: string; // 更新人
  editTime: string; // 最近更新时间
  folderName: string; // 所属文件夹
}

export interface MetricFloderItem {
  folderId: string;
  pos: number [];
}

export interface MetricDetail {
  labelCode?: string;
  metricId: string;
  labelName: string;
  labelTag: string;
  measureLabels: {tableName:string}[];
  creator: string;
  metricDeadline?: string;
  enName?: string;
  bizProcessCodeName?: string;
  domainCodeName?: string;
  bizProcessCode?: string;
  metricDefine?: string;
  comment?: string;
  aggregatorCode?: string;
  modifiers?: Record<string, any> [];
  dimTables?: Record<string, any> [];
  atomicMetric: Record<string, any> [];
  timeAttribute: Record<string, any> [];
  calculableType?: string;
}

export interface ApprovalListItem {
  id: number;
  jobId: number;
  jobName: string;
  jobContentId: number;
  jobContentVersion: number;
  jobTypeCode: string;
  dwLayerCode: string;
  dwLayerValue: string;
  environment: Environments;
  publishStatus: PublishStatus;
  submitRemark: string;
  approveOperator?: string;
  approveTime?: string;
  approveRemark?: string;
}
