import type { TaskTypes } from '@/constants/datadev';
import type { ClusterState } from '@/constants/operations';
import { RuleLayer } from './objectlabel';

/* ========== 运维看板 ========== */
export interface UsageOverview {
  allocatedMem: number; // 已分配内存，单位：MB
  totalMem: number; // 总内存，单位：MB
  memUsageRate: string; // 内存使用率
  allocatedVCores: number; // 已分配核数
  totalVCores: number; // 总核数
  vCoreUsageRate: string;
}

export interface ScheduleListItem {
  jobId: number;
  jobName: string;
  jobStatus: string;
  taskId: number;
  environment: string;
}
export interface DagItem {
  id: number;
  name: string;
}

export interface EnumItem {
  id: number;
  enumValue: string;
}

export interface ClusterListItem {
  jobId: number;
  jobName: string;
  jobStatus: string;
  businessLogsUrl: string;
}

export interface OperationOverview {
  total: number;
  success: number;
  ready: number;
  running: number;
  failure: number;
  other: number;
  nameValueResponseList: {
    name: string;
    value: number;
  }[];
}

export interface OperationLine {
  xAxis: string[];
  yAxisList: {
    name: string;
    yAxis: string[];
  }[];
}

export interface ConsumeTimeItem {
  jobId: number;
  jobName: string; // 任务名称
  duration: string; // 作业持续时间(min)
  startTime: string; // 开始时间
  finishTime: string; // 结束时间
  avgDuration: number; // 平均内存
  avgDurationStr: string; // 平均执行时长(min)
  businessLogsUrl: string; // application master container url地址
}

export interface ConsumeResourceItem {
  jobId: number;
  jobName: string; // 任务名称
  avgVcores: number; // 作业平均消耗cpu虚拟核数
  avgMemory: number; // 作业平均消耗内存（MB）
  startTime: string; // 开始时间
  finishTime: string; // 结束时间
  avgDuration: number; // 平均内存
  businessLogsUrl: string; // application master container url地址
}

/* ========== 作业历史 ========== */
export interface JobHistoryItem {
  id: number;
  createTime: number;
  jobId: number; // 作业id
  jobName: string; // 作业id
  startTime: string; // 作业开始时间
  finishTime: string; // 作业结束时间
  duration: number; // 作业持续时间（ms）
  finalStatus: string; // 作业最终状态
  avgVcores: number; // 作业平均消耗cpu虚拟核数
  avgMemory: number; // 作业平均消耗内存（GB）
  applicationId: string; // yarn的application
  user: string; // 启动应用的user
  businessLogsUrl: string; // application master container url地址
  layer: string; // 数仓分层
  businessStatus: string; // 等待运行 1 / 运行中 2 / 失败 6 / 成功 7 / 其他 -1
}

/* ========== 作业历史 ========== */
export interface JobHistoryGanttItem {
  id: number;
  createTime: number;
  jobId: number; // 作业id
  jobName: string; // 作业id
  startTime: string; // 作业开始时间
  children: Record<string, any> [], // 作业单次运行势力
  finishTime: string; // 作业结束时间
  duration: number; // 作业持续时间（ms）
  finalStatus: string; // 作业最终状态
  avgVcores: number; // 作业平均消耗cpu虚拟核数
  avgMemory: number; // 作业平均消耗内存（GB）
  applicationId: string; // yarn的application
  user: string; // 启动应用的user
  amContainerLogsUrl: string; // application master container url地址
  layer: string; // 数仓分层
  businessStatus: string; // 等待运行 1 / 运行中 2 / 失败 6 / 成功 7 / 其他 -1
}

/* ========== 作业历史 ========== */
export interface JobHistoryGanttItem {
  id: number;
  createTime: number;
  jobId: number; // 作业id
  jobName: string; // 作业id
  startTime: string; // 作业开始时间
  children: Record<string, any> [], // 作业单次运行势力
  finishTime: string; // 作业结束时间
  duration: number; // 作业持续时间（ms）
  finalStatus: string; // 作业最终状态
  avgVcores: number; // 作业平均消耗cpu虚拟核数
  avgMemory: number; // 作业平均消耗内存（GB）
  applicationId: string; // yarn的application
  user: string; // 启动应用的user
  amContainerLogsUrl: string; // application master container url地址
  layer: string; // 数仓分层
  businessStatus: string; // 等待运行 1 / 运行中 2 / 失败 6 / 成功 7 / 其他 -1
}

/* ========== 作业历史 ========== */
export interface JobHistoryGanttItem {
  id: number;
  createTime: number;
  jobId: number; // 作业id
  jobName: string; // 作业id
  startTime: string; // 作业开始时间
  children: Record<string, any> [], // 作业单次运行势力
  finishTime: string; // 作业结束时间
  duration: number; // 作业持续时间（ms）
  finalStatus: string; // 作业最终状态
  avgVcores: number; // 作业平均消耗cpu虚拟核数
  avgMemory: number; // 作业平均消耗内存（GB）
  applicationId: string; // yarn的application
  user: string; // 启动应用的user
  amContainerLogsUrl: string; // application master container url地址
  layer: string; // 数仓分层
  businessStatus: string; // 等待运行 1 / 运行中 2 / 失败 6 / 成功 7 / 其他 -1
}

/* ========== 集群监控 ========== */
export interface Cluster {
  appId: number; // 集群应用 id
  appName: string; // 集群应用 name
  jobId: number; // 作业id
  user: string; // 集群应用 user
  queue: string; // 集群应用队列
  state: ClusterState; // 集群应用状态
  progress: number; // 集群应用运行进度
  applicationType: string; // 集群应用类型
  startedTime: string; // 集群应用开始时间
  allocatedMem: number; // 集群应用总分配内存，单位：GB
  allocatedVCores: number; // 集群应用总分配核数
}

/* ========== 任务监控 ========== */
export interface Overhang {
  id: number;
  name: string;
  jobType: TaskTypes;
  dwLayerCode: string;
  status: 0 | 1; // 状态，1启用，0停用
  remark: string;
  creator: string;
}
