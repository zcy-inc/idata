import { request } from 'umi';

// 树 获取
export async function getFolderTree(params: { devTreeType: 'TABLE' | 'LABEL' | 'ENUM' }) {
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
