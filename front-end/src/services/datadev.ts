import type { Key } from 'react';
import { request } from 'umi';
import { Table } from '@/types/datapi';
import {
  DAG,
  DAGListItem,
  Folder,
  MappedColumn,
  Task,
  TaskConfig,
  TaskContent,
  TaskTable,
  TaskType,
  TaskVersion,
  TreeNode,
} from '@/types/datadev';
import { PeriodRange, TaskCategory, TaskTypes } from '@/constants/datadev';
import { DefaultResponse } from './global';
import { DataSourceTypes, Environments } from '@/constants/datasource';

// 标签 创建
export async function createTag(data: {}) {
  return request('/api/p1/dev/labelDefine', { method: 'POST', data });
}
/**
 * 标签 查看
 */
export async function getLabel(params: { labelDefineId: number }) {
  return request(`/api/p1/dev/labelDefines/${params.labelDefineId}`, { method: 'GET', params });
}
// 标签 删除
export async function deleteLabel(params: { labelCode: string }) {
  return request('/api/p1/dev/labelDefine', { method: 'DELETE', params });
}
// 枚举 获取names
export async function getEnumNames() {
  return request('/api/p1/dev/enumNames', { method: 'GET' });
}
// 枚举 获取values
export async function getEnumValues(params: { enumCode: string }) {
  return request('/api/p1/dev/enumValues', { method: 'GET', params });
}
// 枚举 查看
export async function getEnum(params: { enumId: number }) {
  return request(`/api/p1/dev/enums/${params.enumId}`, { method: 'GET', params });
}
// 枚举 创建/编辑
export async function createEnum(data: {}) {
  return request('/api/p1/dev/enum', { method: 'POST', data });
}
// 枚举 删除
export async function deleteEnum(params: { enumCode: string }) {
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
export async function getTableReferTbs(params: { labelValue?: string }) {
  return request<{ data: Table[] }>('/api/p1/dev/referTables', {
    method: 'GET',
    params,
  });
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
  return request(`/api/p1/dev/tableInfo/${params.tableId}`, { method: 'GET' });
}
// 表 获取关系预览图
export async function getTableRelations(params: { tableId: string }) {
  return request('/api/p0/dev/tableRelations', { method: 'GET', params });
}
// 表 删除
export async function delTable(data: { tableId: any }) {
  return request(`/api/p1/dev/tableInfo/${data.tableId}`, { method: 'DELETE', data });
}
// 表 DDL模式
export async function getDDL(params: { tableId: any }) {
  return request(`/api/p1/dev/tableDdl/${params.tableId}`, { method: 'GET', params });
}
// 表 同步MetaBase
export async function postSyncMetabase(params: { tableId: any }) {
  return request(`/api/p1/dev/syncMetabaseInfo/${params.tableId}`, { method: 'POST', params });
}
// 表 生成表结构
export async function getTableConstruct(data: { tableDdl: string; tableId: number }) {
  return request('/api/p1/dev/ddl/syncTableInfo', { method: 'POST', data });
}

/* ==================== DataDev ==================== */
/**
 * 获取功能性文件树
 */
export async function getFunctionTree() {
  return request<DefaultResponse & { data: TreeNode[] }>(
    '/api/p1/dev/compositeFolders/functions/tree',
    {
      method: 'GET',
    },
  );
}

/**
 * 获取完整的文件树
 */
export async function getTree(data?: { belongFunctions?: string[]; keyWord?: string }) {
  return request<DefaultResponse & { data: TreeNode[] }>('/api/p1/dev/compositeFolders/tree', {
    method: 'POST',
    data,
  });
}

/**
 * 获取平铺的文件夹列表
 */
export async function getFolders(params: { belong: string }) {
  return request<DefaultResponse & { data: Folder[] }>('/api/p1/dev/compositeFolders', {
    method: 'GET',
    params,
  });
}

/**
 * 创建文件夹
 */
export async function createFolder(data: Partial<Folder>) {
  return request<DefaultResponse & { data: Folder }>('/api/p1/dev/compositeFolders', {
    method: 'POST',
    data,
  });
}

/**
 * 编辑文件夹
 */
export async function updateFolder(data: Partial<Folder>) {
  return request<DefaultResponse & { data: Folder }>('/api/p1/dev/compositeFolders', {
    method: 'PUT',
    data,
  });
}

/**
 * 删除文件夹
 */
export async function deleteFolder(params: { id: number }) {
  return request<DefaultResponse & { data: Folder }>(`/api/p1/dev/compositeFolders/${params.id}`, {
    method: 'DELETE',
  });
}

/* ==================== DAG ==================== */
/**
 * 创建DAG
 */
export async function createDAG(data: {
  dagInfoDto: {
    name: string;
    dwLayerCode: string;
    folderId: string;
  };
  dagScheduleDto: {
    beginTime: string;
    endTime: string;
    periodRange: PeriodRange;
    triggerMode: string;
    cronExpression: string;
  };
}) {
  return request<DefaultResponse & { data: DAG }>('/api/p1/dev/dags', { method: 'POST', data });
}

/**
 * 编辑DAG
 */
export async function editDAG(data: {
  dagInfoDto: {
    name: string;
    dwLayerCode: string;
    folderId: string;
  };
  dagScheduleDto: {
    beginTime: string;
    endTime: string;
    periodRange: PeriodRange;
    triggerMode: string;
    cronExpression: string;
  };
}) {
  return request<DefaultResponse & { data: DAG }>('/api/p1/dev/dags', { method: 'PUT', data });
}

/**
 * 获取DAG
 */
export async function getDAG(params: { id: Key }) {
  return request<DefaultResponse & { data: DAG }>(`/api/p1/dev/dags/${params.id}`, {
    method: 'GET',
    params,
  });
}

/**
 * 删除DAG
 */
export async function deleteDAG(params: { id: Key }) {
  return request<DefaultResponse & { data: boolean }>(`/api/p1/dev/dags/${params.id}`, {
    method: 'DELETE',
    params,
  });
}

/**
 * 启用DAG
 */
export async function enableDAG(params: { id: Key }) {
  return request<DefaultResponse & { data: boolean }>(`/api/p1/dev/dags/${params.id}/enable`, {
    method: 'PUT',
    params,
  });
}

/**
 * 停用DAG
 */
export async function disableDAG(params: { id: Key }) {
  return request<DefaultResponse & { data: boolean }>(`/api/p1/dev/dags/${params.id}/disable`, {
    method: 'PUT',
    params,
  });
}

/* ==================== Task ==================== */
/**
 * 获取作业类型
 */
export async function getTaskTypes(params: { catalog: TaskCategory }) {
  return request<DefaultResponse & { data: TaskType[] }>('/api/p1/dev/jobs/types', {
    method: 'GET',
    params,
  });
}

/**
 * 新建作业
 */
export async function createTask(data: {
  name: string;
  jobType: TaskTypes;
  dwLayerCode: string;
  remark: string;
  folderId: number;
}) {
  return request<DefaultResponse & { data: Task }>('/api/p1/dev/jobs', {
    method: 'POST',
    data,
  });
}

/**
 * 获取作业
 */
export async function getTask(params: { id: number }) {
  return request<DefaultResponse & { data: Task }>(`/api/p1/dev/jobs/${params.id}`, {
    method: 'GET',
  });
}

/**
 * 编辑作业
 */
export async function editTask(data: Task) {
  return request<DefaultResponse & { data: Task }>('/api/p1/dev/jobs', {
    method: 'PUT',
    data,
  });
}

/**
 * 获取作业内容版本
 */
export async function getTaskVersions(params: { jobId: number }) {
  return request<DefaultResponse & { data: TaskVersion[] }>(
    `/api/p1/dev/jobs/${params.jobId}/di/versions`,
    {
      method: 'GET',
      params,
    },
  );
}

/**
 * 获取作业内容
 */
export async function getTaskContent(params: { jobId: number; version: number }) {
  return request<DefaultResponse & { data: TaskContent }>(
    `/api/p1/dev/jobs/${params.jobId}/di/contents/${params.version}`,
    {
      method: 'GET',
      params,
    },
  );
}

/**
 * 获取作业内容的表
 */
export async function getTaskTables(params: {
  dataSourceType: DataSourceTypes;
  dataSourceId: number;
}) {
  return request<DefaultResponse & { data: TaskTable[] }>('/api/p1/dev/jobTables', {
    method: 'GET',
    params,
  });
}

/**
 * 获取作业内容的表字段
 */
export async function getTaskTableColumns(params: {
  tableName: string;
  dataSourceType: DataSourceTypes;
  dataSourceId: number;
}) {
  return request<DefaultResponse & { data: MappedColumn[] }>('/api/p1/dev/jobTables/columns', {
    method: 'GET',
    params,
  });
}

/**
 * 暂停作业
 */
export async function disableTask(params: { id: number }) {
  return request<DefaultResponse & { data: boolean }>(`/api/p1/dev/jobs/${params.id}/disable`, {
    method: 'PUT',
  });
}

/**
 * 启用作业
 */
export async function enableTask(params: { id: number }) {
  return request<DefaultResponse & { data: boolean }>(`/api/p1/dev/jobs/${params.id}/enable`, {
    method: 'PUT',
  });
}

/**
 * 删除作业
 */
export async function deleteTask(params: { id: number }) {
  return request<DefaultResponse & { data: boolean }>(`/api/p1/dev/jobs/${params.id}`, {
    method: 'DELETE',
  });
}

/**
 * 保存作业
 */
export async function saveTask(data: TaskContent) {
  return request<DefaultResponse & { data: TaskContent }>(
    `/api/p1/dev/jobs/${data.jobId}/di/contents`,
    {
      method: 'POST',
      data,
    },
  );
}

/**
 * 提交作业
 */
export async function submitTask(params: {
  jobId: number;
  version: number;
  env: Environments;
  remark: string;
}) {
  return request<DefaultResponse & { data: TaskContent }>(
    `/api/p1/dev/jobs/${params.jobId}/di/contents/${params.version}/submit/${params.env}`,
    {
      method: 'POST',
    },
  );
}

/**
 * 获取作业配置列表
 */
export async function getTaskConfigs(params: { jobId: number; environment?: Environments }) {
  return request<DefaultResponse & { data: TaskConfig[] }>(
    `/api/p1/dev/jobs/${params.jobId}/execConfigs`,
    {
      method: 'GET',
      params,
    },
  );
}

/**
 * 获取DAG列表
 */
export async function getDAGList(params: { dwLayerCode: string }) {
  return request<DefaultResponse & { data: DAGListItem[] }>('/api/p1/dev/dags/info', {
    method: 'GET',
    params,
  });
}

/**
 * 获取运行队列
 */
export async function getExecuteQueues() {
  return request<DefaultResponse & { data: string[] }>('/api/p1/dev/executeQueues', {
    method: 'GET',
  });
}

/**
 * 保存作业配置列表
 */
export async function saveTaskConfig(data: { jobId: number; environment: Environments }) {
  return request<DefaultResponse & { data: TaskConfig[] }>(
    `/api/p1/dev/jobs/${data.jobId}/execConfigs`,
    {
      method: 'POST',
      data,
    },
  );
}
