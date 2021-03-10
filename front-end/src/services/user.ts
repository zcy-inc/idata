import { request } from 'umi';
import type { Tuser, CurrentUser } from '@/interfaces/user.d';

export async function queryCurrent() {
  return request<{ data: CurrentUser }>('/api/p1/uc/userInfo');
}

export async function getUserList(params: PaginatedParams<{ name?: string }>) {
  return request<{ data: PaginatedData<Tuser> }>('/api/p1/uac/users', { params });
}

export async function deleteUser(userId: number) {
  return request(`/api/p1/uac/user/${userId}`, {
    method: 'DELETE',
  });
}

export async function addUser(
  user: Pick<Tuser, 'realName' | 'mobile' | 'roleCodes' | 'department' | 'nickname' | 'password'>,
) {
  return request('/api/p1/uac/user', {
    method: 'POST',
    data: user,
  });
}

export async function editUser(
  user: Pick<Tuser, 'realName' | 'mobile' | 'roleCodes' | 'department'>,
) {
  return request('/api/p1/uac/user', {
    method: 'PUT',
    data: user,
  });
}

export async function resetUserPassword(userId: number) {
  return request<{ data: number; success: boolean }>(`/api/p1/uac/resetUserPassword/${userId}`, {
    method: 'PUT',
  });
}

export async function getUserFeatureTree(userId: number) {
  return request(`/api/p1/uac/userFeatureTree/${userId}`);
}

export async function getUserFolderTree(userId: number) {
  return request(`/api/p1/uac/userFolderTree/${userId}`);
}
