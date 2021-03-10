import { request } from 'umi';

// 退出登录
export async function outLogin() {
  return request('/api/p0/uc/signOut', { method: 'POST' });
}

export async function queryLogin(data: { username?: string; password?: string }) {
  return fetch('/api/v1/idata/auth/login', {
    body: JSON.stringify(data),
    headers: {
      'content-type': 'application/json',
    },
    method: 'POST',
  }).then((response) => response.json());
}