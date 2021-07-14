import { LabelTag, TreeNodeType } from '@/constants/datapi';
import { request } from 'umi';

// 树 获取
export async function getFolderTree(params: { devTreeType: TreeNodeType; treeNodeName?: string }) {
  return request('/api/p1/dev/devFolderTree', { method: 'GET', params });
}
// 树 获取平铺的文件夹目录
export async function getFolders(params?: {}) {
  return request('/api/p1/dev/devFolders', { method: 'GET', params });
}
// 文件夹 创建
export async function createFolder(data: { folderName: string; parentId: string }) {
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

/* ========== 指标 ========== */
// 指标 获取
export async function getMetric(params: { metricCode: string }) {
  return request(`/api/p1/dev/metric`, { method: 'GET', params });
}
// 指标 创建 / 更新
export async function createMetric(data: { labelCode?: string; [key: string]: any }) {
  return request(`/api/p1/dev/metric`, { method: 'POST', data });
}
// 指标 删除
export async function deleteMetric(params: { metricCode: string }) {
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
