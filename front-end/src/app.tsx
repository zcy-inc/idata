import React from 'react';
import { PageLoading } from '@ant-design/pro-layout';
import { notification } from 'antd';
import type { Settings as LayoutSettings } from '@ant-design/pro-layout';
import { history } from 'umi';
import type { RequestConfig, RunTimeLayoutConfig } from 'umi';
import type { CurrentUser } from '@/interfaces/user';
import type { ResponseError } from 'umi-request';
import { RightContent } from '@/components';
import { skip2Login } from '@/utils/utils';
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
      if (!initialState?.currentUser) {
        skip2Login();
      }
    },
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    ...initialState?.settings,
  };
};

const codeMessage = {
  200: '服务器成功返回请求的数据。',
  201: '新建或修改数据成功。',
  202: '一个请求已经进入后台排队（异步任务）。',
  204: '删除数据成功。',
  400: '发出的请求有错误，服务器没有进行新建或修改数据的操作。',
  401: '用户没有权限（令牌、用户名、密码错误）。',
  403: '用户得到授权，但是访问是被禁止的。',
  404: '发出的请求针对的是不存在的记录，服务器没有进行操作。',
  405: '请求方法不被允许。',
  406: '请求的格式不可得。',
  410: '请求的资源被永久删除，且不会再得到的。',
  422: '当创建一个对象时，发生一个验证错误。',
  500: '服务器发生错误，请检查服务器。',
  502: '网关错误。',
  503: '服务不可用，服务器暂时过载或维护。',
  504: '网关超时。',
};

/**
 * 异常处理程序
 */
const errorHandler = (error: ResponseError) => {
  const { response, data } = error;
  if (response && response.status) {
    const errorText = codeMessage[response.status] || response.statusText;
    const { status, url } = response;

    notification.error({
      message: `请求错误 ${status}: ${url}`,
      description: errorText,
    });
  }

  if (!response) {
    notification.error({
      description: data.msg,
      message: '出错啦！',
    });
  }
  throw error;
};

export const request: RequestConfig = {
  errorHandler,
  errorConfig: {
    adaptor: (resData) => {
      return {
        ...resData,
        errorMessage: resData.msg,
        showType: 2, // 错误提示方式.0 silent; 1 message.warn; 2 message.error; 4 notification; 9 page
      };
    },
  },
};
