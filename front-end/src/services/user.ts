import { request } from 'umi';
import { authTypeEnum } from '@/constants/common';
import type { Tuser, CurrentUser } from '@/interfaces/user.d';

// 退出登录
export async function outLogin() {
  return request('/api/p1/uac/signOut', { method: 'POST' });
}

// 退出
export async function queryLogin(data: { username?: string; password?: string }) {
  return fetch('/api/p0/uac/signIn', {
    body: JSON.stringify(data),
    headers: {
      'content-type': 'application/json',
    },
    method: 'POST',
  }).then((response) => response.json());
}

// 获取当前用户
export async function queryCurrent() {
  return request<{ data: CurrentUser }>('/api/p1/uac/currentUser');
}

// 注册
export async function queryRegister(data: any) {
  return request('/api/p0/uac/register', { method: 'POST', data: { authType: authTypeEnum.REGISTER, ...data } });
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
    data: { authType: authTypeEnum.REGISTER, ...user },
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
