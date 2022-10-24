import type { authTypeEnum } from '@/constants/common';

export interface Tuser {
  id: number;
  password?: string;
  mobile: string;
  nickname: string;
  realName: string;
  username: string;
  roleCodes?: string[];
  roleNames?: string[];
  department?: string;
  editor?: string;
  sysAdmin?: number;
  authType?: authTypeEnum;
}

export interface CurrentUser {
  id: number;
  employeeId: string;
  avatar?: string;
  userName?: string;
  nickname?: string;
  realName?: string;
  mail?: string;
  mobile?: string;
  del?: boolean;
}

export interface CurrentGroup {
  id: number;
  employeeId: string;
  avatar?: string;
  userName?: string;
  nickname?: string;
  realName?: string;
  mail?: string;
  mobile?: string;
  del?: boolean;
}

export interface Tgroup {
  id?: string;
  name: string;
  ownerId: string;
  remark: string;
  relatedUsers: Array
}

export interface TableList {
  resourceType: string;
  db: string;
}
export interface authPolicyList {
  authResourceList: object[];
  effect: string;
  actionList: string[];
  resourceType: string;
}

export interface AuthorizationsInfo {
  authPolicyList: authPolicyList;
  id?: string;
  subjectId: string | number;
  subjectType: string;
}