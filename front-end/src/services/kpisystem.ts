import { request } from 'umi';

// 树 获取
export async function getFolderTree(params: {
  devTreeType: 'DIMENSION_LABEL' | 'MODIFIER_LABEL' | 'METRIC_LABEL';
}) {
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
export async function getDimension(params: { dimensionId: string }) {
  return request(`/api/p1/dev/dimension/${params.dimensionId}`, { method: 'GET', params });
}
// 维度 更新
export async function createDimension(data: { dimensionId?: string }) {
  return request(`/api/p1/dev/dimension`, { method: 'POST', data });
}
// 维度 删除
export async function deleteDimension(params: { dimensionId: string }) {
  return request(`/api/p1/dev/dimension/${params.dimensionId}`, { method: 'DELETE', params });
}

/* ========== 修饰词 ========== */
// 修饰词 获取
export async function getModifier(params: { modifierId: string }) {
  return request(`/api/p1/dev/modifier/${params.modifierId}`, { method: 'GET', params });
}
// 修饰词 创建 / 更新
export async function updateModifier(data: { modifierId?: string }) {
  return request(`/api/p1/dev/modifier`, { method: 'POST', data });
}
// 修饰词 删除
export async function deleteModifier(params: { modifierId: string }) {
  return request(`/api/p1/dev/modifier/${params.modifierId}`, { method: 'DELETE', params });
}

/* ========== 指标 ========== */
// 指标 获取
export async function getMetric(params: { metricId: string }) {
  return request(`/api/p1/dev/metric/${params.metricId}`, { method: 'GET', params });
}
// 指标 创建 / 更新
export async function updateMetric(data: { metricId?: string }) {
  return request(`/api/p1/dev/metric`, { method: 'POST', data });
}
// 指标 删除
export async function deleteMetric(params: { metricId: string }) {
  return request(`/api/p1/dev/metric/${params.metricId}`, { method: 'DELETE', params });
}
