import type { VersionStatus } from '@/constants/datadev';
import type { Environments } from '@/constants/datasource';
import type { TaskListItem } from '@/types/tasks';
import { request } from 'umi';
import type { DefaultResponse } from './global';

/**
 * 查询版本发布记录
 */
export async function getTasks(params: {
  offset: number;
  limit: number;
  jobId?: number; // 作业id
  jobContentId?: string; // 作业内容id
  jobContentVersion?: string; // 作业内容版本号
  environment?: Environments; // 环境
  publishStatus?: VersionStatus; // 任务状态
  jobTypeCode?: string; // 作业类型
  dwLayerCode?: string; // 数仓分层
  submitOperator?: string; // 提交人
}) {
  return request<
    DefaultResponse & {
      data: { total: number; content: TaskListItem[] };
    }
  >('/api/p1/dev/jobs/publishRecords/page', {
    method: 'GET',
    params,
  });
}

/**
 * 发布版本
 */
export async function publishTask(data: { recordIds: number[] }) {
  return request<DefaultResponse & { data: boolean }>('/api/p1/dev/jobs/publishRecords/approve', {
    method: 'POST',
    data,
  });
}

/**
 * 驳回版本
 */
export async function rejectTask(data: { recordIds: number[] }) {
  return request<DefaultResponse & { data: boolean }>('/api/p1/dev/jobs/publishRecords/reject', {
    method: 'POST',
    data,
  });
}
