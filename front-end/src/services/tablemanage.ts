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
// 标签 创建
export async function createTag(data: {}) {
  return request('/api/p1/dev/labelDefine', { method: 'POST', data });
}
// 标签 查看
export async function getLabel(params: { labelCode: string }) {
  return request('/api/p1/dev/labelDefine', { method: 'GET', params });
}
// 标签 删除
export async function delLabel(params: { labelCode: string }) {
  return request('/api/p1/dev/labelDefine', { method: 'DELETE', params });
}
// 枚举 获取names
export async function getEnumNames(params?: {}) {
  return request('/api/p1/dev/enumNames', { method: 'GET', params });
}
// 枚举 获取values
export async function getEnumValues(params: { enumCode: string }) {
  return request('/api/p1/dev/enumValues', { method: 'GET', params });
}
// 枚举 查看
export async function getEnum(params: { enumCode: string }) {
  return request('/api/p1/dev/enum', { method: 'GET', params });
}
// 枚举 创建/编辑
export async function createEnum(data: {}) {
  return request('/api/p1/dev/enum', { method: 'POST', data });
}
// 枚举 删除
export async function delEnum(params: { enumCode: string }) {
  return request('/api/p1/dev/enum', { method: 'DELETE', params });
}
// 表 创建/编辑
export async function createTable(data: {}) {
  return request('/api/p1/dev/tableInfo', { method: 'POST', data });
}
// 表 获取labels/columns
export async function getTableLabels(params: {
  labelTag?: string;
  subjectType: 'TABLE' | 'COLUMN';
}) {
  return request('/api/p1/dev/labelDefines', { method: 'GET', params });
}
// 表 获取参考数据库
export async function getTableReferDbs() {
  return request('/api/p1/dev/dbNames', { method: 'GET' });
}
// 表 获取参考表
export async function getTableReferTbs(params: { labelValue: string }) {
  return request('/api/p1/dev/referTables', { method: 'GET', params });
}
// 表 获取参考字段
export async function getTableReferStr(params: { tableId: string }) {
  return request(`/api/p1/dev/columnInfos/${params.tableId}`, { method: 'GET', params });
}
// 表 数仓所属人
export async function getDWOwner() {
  return request('/api/p1/uac/users', { method: 'GET' });
}
// 表 查看
export async function getTable(params: { tableId: any }) {
  return request(`/api/p1/dev/tableInfo/${params.tableId}`, { method: 'GET', params });
}
// 表 获取关系预览图
export async function getTableRelations(params: { tableId: string }) {
  return request('/api/p0/dev/tableRelations', { method: 'GET', params });
}
// 表 删除
export async function delTable(data: { tableId: any }) {
  return request(`/api/p1/dev/tableInfo/${data.tableId}`, { method: 'DELETE', data });
}
