import { request } from 'umi';

export async function getHrefUrl() {
  return request<{ data: Record<string, string> }>('/api/v1/idata/hrefUrl');
}

export async function getSystemFeatureTree() {
  return request('/api/p0/sys/systemFeatureTree');
}

export async function getSystemFolderTree() {
  return request('/api/p0/sys/systemFolderTree');
}