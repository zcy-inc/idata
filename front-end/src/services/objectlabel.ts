import { ObjectLabel } from '@/types/objectlabel';
import { request } from 'umi';

// 树 获取
export async function getFolderTree() {
  return request('/api/p1/lab/labFolder/tree', { method: 'GET', params: {} });
}
// 树 获取平铺的文件夹目录
export async function getFolders(params?: {}) {
  return request('/api/p1/lab/labFolders', { method: 'GET', params });
}
// 文件夹 创建
export async function createFolder(data: { folderName: string; parentId: string }) {
  return request('/api/p1/lab/labFolder', { method: 'POST', data });
}
// 文件夹 更新
export async function updateFolder(data: { folderName: string; parentId: string; id: string }) {
  return request('/api/p1/lab/labFolder', { method: 'PUT', data });
}
// 文件夹 删除
export async function deleteFolder(params: { id: string }) {
  return request(`/api/p1/lab/labFolder/${params.id}`, { method: 'DELETE', params });
}

/* ========== 标签 ========== */
// 标签 获取
export async function getObjectLabel(params: { id: number }) {
  return request<{ data: ObjectLabel }>(`/api/p1/lab/objectLabel/${params.id}`, {
    method: 'GET',
    params,
  });
}
// 标签 创建
export async function createObjectLabel(data: {}) {
  return request('/api/p1/lab/objectLabel', { method: 'POST', data });
}
// 标签 更新
export async function updateObjectLabel(data: {}) {
  return request('/api/p1/lab/objectLabel', { method: 'PUT', data });
}
// 标签 删除
export async function deleteObjectLabel(params: { id: number }) {
  return request(`/api/p1/lab/objectLabel/${params.id}`, { method: 'DELETE', params });
}
// 标签 分层 获取
export async function getObjectLabelLayer(params: { id: number; layerId: number }) {
  return request(`/api/api/p1/lab/objectLabel/${params.id}/layer/${params.layerId}/queryData`, {
    method: 'GET',
    params,
  });
}
// 标签 分层 导出
export async function exportObjectLabel(params: { id: number; layerId: number }) {
  return request(`/api/api/p1/lab/objectLabel/${params.id}/layer/${params.layerId}/exportData`, {
    method: 'GET',
    params,
  });
}
