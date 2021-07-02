import React from 'react';
import { PageLoading } from '@ant-design/pro-layout';
import type { Settings as LayoutSettings } from '@ant-design/pro-layout';
import { history } from 'umi';
import type { RequestConfig, RunTimeLayoutConfig } from 'umi';
import type { CurrentUser } from '@/interfaces/user';
import type { Context } from 'umi-request';
import { RightContent } from '@/components';
import { skip2Login } from '@zcy-data/idata-utils';
import { getSystemState } from '@/services/global';
import { queryCurrent } from '@/services/user';
import defaultSettings from '../config/defaultSettings';

const fetchCurrentUser = async () => {
  try {
    const { data: currentUser } = await queryCurrent();
    return currentUser;
  } catch (error) {
    skip2Login({ redirect: true });
  }
  return undefined;
};

/**
 * 获取用户信息比较慢的时候会展示一个 loading
 */
export const initialStateConfig = {
  loading: <PageLoading />,
};

export async function getInitialState(): Promise<{
  settings?: LayoutSettings;
  systemState?: { registerEnable?: boolean };
  currentUser?: CurrentUser;
}> {
  const { data: systemState } = await getSystemState();
  // 如果是登录页面，不执行
  if (history.location.pathname !== '/login') {
    const currentUser = await fetchCurrentUser();
    return {
      currentUser,
      systemState,
      settings: defaultSettings,
    };
  }
  return {
    systemState,
    settings: defaultSettings,
  };
}

export const layout: RunTimeLayoutConfig = ({ initialState }) => {
  return {
    rightContentRender: () => <RightContent />,
    disableContentMargin: false,
    onPageChange: () => {
      // 如果没有登录，重定向到 login
      // if (!initialState?.currentUser) {
      //   skip2Login();
      // }
    },
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    ...initialState?.settings,
  };
};

const middleware = async (ctx: Context, next: () => void) => {
  await next();
  const { code } = ctx.res;
  if (code === '401') {
    skip2Login({ redirect: true, redirectUrl: window.location.href });
    return;
  }
};

export const request: RequestConfig = {
  errorConfig: {
    adaptor: (resData) => {
      return {
        ...resData,
        errorMessage: resData.msg,
        showType: 2, // 错误提示方式.0 silent; 1 message.warn; 2 message.error; 4 notification; 9 page
      };
    },
  },
  middlewares: [middleware],
};
