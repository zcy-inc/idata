import { LabelTag, TreeNodeType } from '@/constants/datapi';
import { request } from 'umi';
import type { VersionStatus } from '@/constants/datadev';
import type { Environments } from '@/constants/datasource';
import type { ApprovalListItem } from '@/types/measure';
import type { DefaultResponse } from './global';

// 树 获取
export async function getFolderTree(params: { devTreeType: TreeNodeType; treeNodeName?: string }) {
  return request('/api/p1/dev/devFolderTree', { method: 'GET', params });
}
// 树 获取平铺的文件夹目录
export async function getFolders(params?: {}) {
  return request('/api/p1/dev/devFolders', { method: 'GET', params });
}

// 文件夹 创建
export async function createFolder(data: {
  folderName: string;
  parentId: string;
  folderType: TreeNodeType;
}) {
  return request('/api/p1/dev/devFolder', { method: 'POST', data });
}
// 文件夹 更新
export async function updateFolder(data: { folderName: string; parentId: string; id: string }) {
  return request('/api/p1/dev/devFolder', { method: 'PUT', data });
}
// 文件夹 删除
export async function deleteFolder(params: { folderId: string }) {
  return request(`/api/p1/dev/devFolder/${params.folderId}`, { method: 'DELETE', params });
}

/* ========== 维度 ========== */
// 维度 获取
export async function getDimension(params: { dimensionCode: string }) {
  return request(`/api/p1/dev/dimension`, { method: 'GET', params });
}
// 维度 创建 / 更新
export async function createDimension(data: { labelCode?: string; [key: string]: any }) {
  return request(`/api/p1/dev/dimension`, { method: 'POST', data });
}
// 维度 删除
export async function deleteDimension(params: { dimensionCode: string }) {
  return request(`/api/p1/dev/dimension`, { method: 'DELETE', params });
}
// 维度 停用 / 启用
export async function switchDimension(params: { dimensionCode: string; labelTag: LabelTag }) {
  return request(`/api/p1/dev/dimension/disableOrAbleDimension`, { method: 'POST', params });
}

/* ========== 修饰词 ========== */
// 修饰词 获取
export async function getModifier(params: { modifierCode: string }) {
  return request(`/api/p1/dev/modifier`, { method: 'GET', params });
}
// 修饰词 获取 (根据条件过滤)
export async function getModifiers(params: { modifierTableIds: string }) {
  return request(`/api/p1/dev/modifiers`, { method: 'GET', params });
}
// 修饰词 创建 / 更新
export async function createModifier(data: { labelCode?: string; [key: string]: any }) {
  return request(`/api/p1/dev/modifier`, { method: 'POST', data });
}
// 修饰词 删除
export async function deleteModifier(params: { modifierCode: string }) {
  return request(`/api/p1/dev/modifier`, { method: 'DELETE', params });
}
// 修饰词 停用 / 启用
export async function switchModifier(params: { modifierCode: string; labelTag: LabelTag }) {
  return request(`/api/p1/dev/modifier/disableOrAbleModifier`, { method: 'POST', params });
}
// 获取所有表
export async function getTables() {
  return request(`/api/p1/dev/tables`, { method: 'GET' });
}

// 获取表枚举值
export async function getTableEnums(params: any) {
  return request(`/api/p1/dev/measure/queryModifierValues`, { method: 'POST', params });
}

/* ========== 指标 ========== */
// 指标 单条信息获取
export async function getMetric(params?: {}) {
  return request('/api/p1/dev/metric', { method: 'GET', params });
}
// 指标 列表获取
export async function getMetrics(params: any) {
  return request(`/api/p1/dev/metrics`, { method: 'GET', params });
}
// 时间周期 获取
export async function getTableInfo(params: any) {
  return request(`/api/p1/dev/tableDateColumns`, { method: 'GET', params });
}
// 纬度 获取
export async function getForeignKeyTables(params: any) {
  return request(`/api/p1/dev/foreignKeyTables`, { method: 'GET', params });
}
// 指标 创建 / 更新
export async function createMetric(data: { labelCode?: string; [key: string]: any }) {
  return request(`/api/p1/dev/metric`, { method: 'POST', data });
}
// 指标 删除
export async function deleteMetric(params: { modifierCode: string }) {
  return request(`/api/p1/dev/metric`, { method: 'DELETE', params });
}
// 指标 停用 / 启用
export async function switchMetric(params: { metricCode: string; labelTag: LabelTag }) {
  return request(`/api/p1/dev/metric/disableOrAbleMetric`, { method: 'POST', params });
}
// 指标 配置派生指标时获取原子指标
export async function getAtomicMetrics(params: { atomicMetricCode: string }) {
  return request(`/api/p1/dev/modifiersByAtomic`, { method: 'GET', params });
}
// 指标 生成SQL
export async function generateSQL(params: { metricCode: string }, data: any) {
  return request(`/api/p1/dev/metricSql`, { method: 'POST', params, data });
}
// 获取指标列表
export async function getMeasures(params: any) {
  return request(`/api/p1/dev/measures`, { method: 'GET', params });
}

// 指标 发布
export async function indexPublish(params: { metricCode: string; remark?: string }) {
  return request(`/api/p1/dev/metric/publish`, { 
    method: 'POST',
    data: params
  });
}

// 指标 撤销
export async function indexRetreat(params: { metricCode: string; remark?: string }) {
  return request(`/api/p1/dev/metric/retreat`, { 
    method: 'POST',
    data: params
  });
}


/* ========== 指标审批 ========== */
/**
 * 指标审批列表
 */
 export async function getIndexList(params: {
  offset: number;
  limit: number;
  statusList: string[];
  metricNamePattern?: string;
  metricTag?: string;
  submitOperatorName?: string;
}) {
  return request<
    DefaultResponse & {
      data: { total: number; content: ApprovalListItem[] };
    }
  >('/api/p1/dev/metric/approvals/paging', {
    method: 'POST',
    data: params,
  });
}

/**
 * 指标发布
 */
export async function publishIndex(data: { ids: number[] }) {
  return request<DefaultResponse & { data: boolean }>(`/api/p1/dev/metric/approvals/approve`, {
    method: 'PUT',
    data,
  });
}

/**
 * 指标驳回
 */
export async function rejectIndex(data: {
  recordIds: number[]
}) {
  return request<DefaultResponse & { data: boolean }>(`/api/p1/dev/metric/approvals/${data?.recordIds[0]}/reject`, {
    method: 'PUT',
  });
}
