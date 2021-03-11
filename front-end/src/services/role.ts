import { request } from 'umi';
import type { Trole } from '@/interfaces/role';

export async function getRoleList(params: PaginatedParams) {
  return request<{ data: PaginatedData<Trole> }>('/api/p1/uac/roles', { params });
}

export async function deleteRole(roleId: number) {
  return request(`/api/p1/uac/role/${roleId}`, { method: 'DELETE' });
}

export async function getRoleFeatureTree(roleId: number) {
  return request(`/api/p1/uac/roleFeatureTree/${roleId}`);
}

export async function getRoleFolderTree(roleId: number) {
  return request(`/api/p1/uac/roleFolderTree/${roleId}`);
}

export async function createRole(data: { featureTree: any; folderTree: any; roleName: string }) {
  return request('/api/p1/uac/role', { method: 'POST', data });
}

export async function editRole(data: {
  featureTree: any;
  folderTree: any;
  roleName: string;
  id: number;
}) {
  return request('/api/p1/uac/role', {
    method: 'PUT',
    data,
  });
}
