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
