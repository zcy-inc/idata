export const wsStorageKey = 'workspace__idata__pro';
// export const tokenStorageKey = 'Admin-Token';
// export const userStorageKey = 'USER_INFO';
// export const hasUserManageStorageKey = 'HAS_USER_MANAGE';

export const mainPublicPath = '/idata/';

export const defaultWs = 'zcy'; // 默认环境

export enum authTypeEnum {
  LDAP = 'LDAP',
  REGISTER = 'REGISTER',
}

export enum folderTreeNodeType {
  MENU = 'F_MENU',
  FOLDER = 'F_ICON',
}

export enum operatorEnum {
  BETWEEN = 'between',
  GT = 'gt',
  GE = 'ge',
  LT = 'lt',
  LE = 'le',
  EQ = 'eq',
  NE = 'ne',
}

export const operatorMap = new Map([
  [operatorEnum.BETWEEN, '区间'],
  [operatorEnum.GT, '>'],
  [operatorEnum.GE, '>='],
  [operatorEnum.LT, '<'],
  [operatorEnum.LE, '<='],
  [operatorEnum.EQ, '='],
  [operatorEnum.NE, '<'],
]);

// 请求白名单，不需要添加工作区前缀
export const reqUrlWhiteList = [
  /^\/api\/v1\/idata\/auth/,
  /^\/api\/v1\/idata\/users/,
  /^\/api\/v1\/idata\/user/,
  /^\/api\/v1\/idata\/workspaces/,
];
