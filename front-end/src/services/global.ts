import type { IMenuItem, IAutocompletionTipConfigs } from '@/types/common';
import { request } from 'umi';

export interface DefaultResponse {
  success: boolean;
  code: string;
  msg: string;
  causeMsg: string;
}

export async function getSystemFeatureTree() {
  return request('/api/p1/sys/featureTree');
}

export async function getSystemFolderTree() {
  return request('/api/p1/sys/folderTree');
}

export async function getSystemState() {
  return request('/api/p0/sys/state');
}

/**
 * 获取左侧菜单树（根据当前权限）
 */
export async function getSideMenu() {
  return request<DefaultResponse & { data: IMenuItem[] }>('/api/p1/uac/currentFeatureTree', {
    method: 'GET',
  });
}

/**
 * 获取monaco编辑器的提示列表
 */
export async function getAutocompletionTipConfigs(params: {
  autocompletionType: 'SQL' | 'PYTHON';
}) {
  return request<DefaultResponse & { data: IAutocompletionTipConfigs }>(
    '/api/p1/dev/jobs/autocompletionTipConfigs',
    {
      method: 'GET',
      params,
    },
  );
}
