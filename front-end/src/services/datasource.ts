import { DataSourceTypes, Environments } from '@/constants/datasource';
import { CSVItem, DataSourceItem, DBConfigList } from '@/types/datasource';
import { request } from 'umi';
import { DefaultResponse } from './global';

/**
 * 获取数据源类型
 */
export async function getDataSourceTypes() {
  return request<DefaultResponse & { data: DataSourceTypes[] }>('/api/p1/das/datasources/types', {
    method: 'GET',
  });
}

/**
 * 获取环境
 */
export async function getEnvironments() {
  return request<DefaultResponse & { data: Environments[] }>('/api/p1/dev/environments', {
    method: 'GET',
  });
}

/**
 * 删除数据源
 */
export async function deleteDataSource(params: { id: number }) {
  return request(`/api/p1/das/datasources/${params.id}`, { method: 'DELETE' });
}

/**
 * 获取数据源列表
 */
export async function getDataSourceList(params: {
  type?: DataSourceTypes;
  name?: string;
  env?: Environments;
  limit: number;
  offset: number;
}) {
  return request<{
    data: {
      content: DataSourceItem[];
      total: number;
    };
  }>('/api/p1/das/datasources', { method: 'GET', params });
}

/**
 * 获取文件型数据源列表
 */
export async function getCSVDataSourceList(params: {
  type?: DataSourceTypes;
  name?: string;
  env?: Environments;
  limit: number;
  offset: number;
}) {
  return request<{
    data: { content: CSVItem[]; total: number };
  }>('/api/p1/das/datasources/files', { method: 'GET', params });
}

/**
 * 创建数据源
 */
export async function createDataSource(data: {
  type: DataSourceTypes;
  name: string;
  envList: string[];
  remark: string;
  dbConfigList: Partial<DBConfigList>[];
}) {
  return request('/api/p1/das/datasources', { method: 'POST', data });
}

/**
 * 修改数据源
 */
export async function updateDataSource(data: {
  id: number;
  type: DataSourceTypes;
  name: string;
  envList: string[];
  remark: string;
  dbConfigList: Partial<DBConfigList>[];
}) {
  return request('/api/p1/das/datasources', { method: 'PUT', data });
}

/**
 * 创建csv数据源
 */
export async function createDataSourceCSV(data: {
  type: DataSourceTypes;
  name: string;
  envList: Environments[];
  fileName: string;
  remark?: string;
}) {
  return request('/api/p1/das/datasources/files', { method: 'POST', data });
}

/**
 * 连通性测试
 */
export async function testConnection(
  params: { dataSourceType: DataSourceTypes },
  data: {
    env?: string;
    dbName?: string;
    username?: string;
    password?: string;
    host: string;
    port: string;
    schema?: string;
  },
) {
  return request<{ data: boolean }>('/api/p1/das/datasources/test', {
    method: 'POST',
    params,
    data,
  });
}

/**
 * 上传csv文件
 */
export async function postCSV(
  params: {
    destTableName: string;
    environments: Environments;
  },
  data: FormData,
) {
  return request('/api/p1/das/datasources/files/upload', {
    method: 'POST',
    params,
    data,
  });
}

/**
 * 预览csv数据
 */
export async function getCSVPreview(data: {
  type: DataSourceTypes;
  name: string;
  envList: Environments[];
  fileName: string;
}) {
  return request<{
    data: {
      meta: {
        columnName: string;
        columnType: string;
        columnComment: string;
        dataType: string;
      }[];
      data: string[][];
      total: number;
    };
  }>('/api/p1/das/datasources/files/preview', { method: 'POST', data });
}
