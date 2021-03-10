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
  nickName?: string;
  realName?: string;
  mail?: string;
  mobile?: string;
  del?: boolean;
}
