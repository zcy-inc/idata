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
  F_MENU = 'F_MENU',
  R_DW_DESIGN_DIR = 'R_DW_DESIGN_DIR',
  R_JOB_MANAGE_DIR = 'R_JOB_MANAGE_DIR',
  R_RESOURCE_MANAGE_DIR = 'R_RESOURCE_MANAGE_DIR',
  R_FUNCTION_MANAGE_DIR = 'R_FUNCTION_MANAGE_DIR',
  R_API_DEVELOP_DIR = 'R_API_DEVELOP_DIR',
  R_DATA_DEVELOP_DW_DIR = 'R_DATA_DEVELOP_DW_DIR',
  R_DATA_DEVELOP_DAG_DIR = 'R_DATA_DEVELOP_DAG_DIR',
  R_DATA_DEVELOP_DI_DIR = 'R_DATA_DEVELOP_DI_DIR',
  R_DATA_DEVELOP_DD_DIR = 'R_DATA_DEVELOP_DD_DIR',
}

export const folderTypes = [
  folderTreeNodeType.R_API_DEVELOP_DIR,
  folderTreeNodeType.R_DW_DESIGN_DIR,
  folderTreeNodeType.R_FUNCTION_MANAGE_DIR,
  folderTreeNodeType.R_JOB_MANAGE_DIR,
  folderTreeNodeType.R_RESOURCE_MANAGE_DIR,
  folderTreeNodeType.R_DATA_DEVELOP_DW_DIR,
  folderTreeNodeType.R_DATA_DEVELOP_DAG_DIR,
  folderTreeNodeType.R_DATA_DEVELOP_DI_DIR,
  folderTreeNodeType.R_DATA_DEVELOP_DD_DIR,
];

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
