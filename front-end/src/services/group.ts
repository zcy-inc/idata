import { request } from 'umi';
// import { authTypeEnum } from '@/constants/common';
import type {
  Tuser,
  CurrentUser,
  CurrentGroup,
  Tgroup,
  TableList,
  AuthorizationsInfo,
} from '@/interfaces/group.d';

// // 退出登录
// export async function outLogin() {
//   return request('/api/p1/uac/signOut', { method: 'POST' });
// }

// // 退出
// export async function queryLogin(data: { username?: string; password?: string }) {
//   return fetch('/api/p0/uac/signIn', {
//     body: JSON.stringify(data),
//     headers: {
//       'content-type': 'application/json',
//     },
//     method: 'POST',
//   }).then((response) => response.json());
// }

// // 获取当前用户
// export async function queryCurrent() {
//   return request<{ data: CurrentUser }>('/api/p1/uac/currentUser');
// }

// // 注册
// export async function queryRegister(data: any) {
//   return request('/api/p0/uac/register', {
//     method: 'POST',
//     data: { authType: authTypeEnum.REGISTER, ...data },
//   });
// }

// export async function resetUserPassword(userId: number) {
//   return request<{ data: number; success: boolean }>(`/api/p1/uac/resetUserPassword/${userId}`, {
//     method: 'PUT',
//   });
// }

export async function getUserFeatureTree(userId: number) {
  return request(`/api/p1/uac/userFeatureTree/${userId}`);
}

export async function getUserFolderTree(userId: number) {
  return request(`/api/p1/uac/userFolderTree/${userId}`);
}

// 获取用户下拉列表
export async function queryUserList() {
  return request<{ data: CurrentUser }>('/api/p1/uac/users/KeyValList');
}

// 获取用户组下拉列表
export async function queryGroupList() {
  return request<{ data: CurrentGroup }>('/api/p1/uac/groups/KeyValList');
}

// 分页查询用户组
export async function getGroupPaging(params: PaginatedParams<{ name?: string }>) {
  return request<{ data: PaginatedData<Tuser> }>('/api/p1/uac/groups/paging', {
    method: 'POST',
    data: { ...params },
  });
}

// 新建用户组
export async function addGroup(params: Tgroup) {
  return request('/api/p1/uac/groups', {
    method: 'POST',
    data: { ...params },
  });
}

// 编辑用户组
export async function editGroup(params: Tgroup) {
  return request(`/api/p1/uac/groups/${params.id}`, {
    method: 'PUT',
    data: { ...params },
  });
}

// 删除用户组
export async function deleteGroup(id: number) {
  return request(`/api/p1/uac/groups/${id}`, {
    method: 'DELETE',
  });
}

// 获取授权资源列表
export async function queryTablesList(params: TableList) {
  return request(`/api/p1/uac/authResource/resourceTypes/${params.resourceType}`, {
    method: 'POST',
    data: { dbName: params.db },
  });
}

// 根据主体查询授权
export async function getAuthorizationsInfo(subjectType: string, subjectId: string | number) {
  return request(`/api/p1/uac/authorizations/subjectTypes/${subjectType}/subjectIds/${subjectId}`);
}

// 新增授权
export async function addAuthorizations(params: AuthorizationsInfo) {
  return request('/api/p1/uac/authorizations', {
    method: 'POST',
    data: { ...params },
  });
}

// 编辑授权
export async function editAuthorizations(params: AuthorizationsInfo) {
  return request(`/api/p1/uac/authorizations/${params.id}`, {
    method: 'PUT',
    data: { ...params },
  });
}
