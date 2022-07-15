import type {
  MonitorItem,
  TableItem,
  MonitorRuleItem
} from '@/types/quality';
import { request } from 'umi';
import type { DefaultResponse } from './global';

/* ========== 监控规则 ========== */
/**
 * 获取监控列表
 */
export async function getMonitorList(data: {tableName?: string; alarmLevel?: number; curPage: number; pageSize: number; baselineId: number}) {
  return request<DefaultResponse & { data: {data: MonitorItem [], totalElements: number} }>('/api/monitorTable/getByPage', {
    method: 'POST',
    data
  });
}

/**
 * 添加监控规则
 */
 export async function addMonitor(data: {tableName: string; partitionExpr?: string; baselineId: number}) {
  return request<DefaultResponse & { data: MonitorItem }>('/api/monitorTable/add', {
    method: 'POST',
    data
  });
}

/**
 * 删除监控规则
 */
 export async function deleteMonitor(data: {id: number;  isBaseline: boolean}) {
  return request<DefaultResponse & { data: MonitorItem }>(`/api/monitorTable/del/${data.id}/${data.isBaseline}`, {
    method: 'GET',
  });
}

/**
 * 获取所有表
 */
 export async function getTables(params?: {tableName?: string; editTable?: string}) {
  return request<DefaultResponse & { data: TableItem [] }>('/api/getTables', {
    method: 'GET',
    params
  });
}

/**
 * 获取单个监控下的监控规则列表
 */
 export async function getMonitorRules(data: {tableName?: string; curPage: number; pageSize: number; baselineId: number}) {
  return request<DefaultResponse & { data: {data: MonitorRuleItem [], totalElements: number} }>('/api/monitorRule/getByPage', {
    method: 'POST',
    data
  });
}

/**
 * 获取监控表信息
 */
 export async function getMonitorInfo(params: {id?: string; }) {
  return request<DefaultResponse & { data: MonitorItem }>(`/api/monitorTable/get/${params.id}`, {
    method: 'GET',
  });
}

/**
 * 修改监控表信息
 */
 export async function editMonitorInfo(data: {id: string;baselineId: number; partitionExpr: string; }) {
  return request<DefaultResponse>('/api/monitorTable/update', {
    method: 'POST',
  });
}






