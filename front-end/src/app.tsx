import React from 'react';
import { MenuDataItem, PageLoading } from '@ant-design/pro-layout';
import { history, Link } from 'umi';
import { cloneDeep } from 'lodash';
import { RightContent } from '@/components';
import { skip2Login } from '@zcy-data/idata-utils';
import { getSideMenu, getSystemState } from '@/services/global';
import { queryCurrent } from '@/services/user';
import defaultSettings from '../config/defaultSettings';
import type { RequestConfig, RunTimeLayoutConfig } from 'umi';
import type { CurrentUser } from '@/interfaces/user';
import type { Context } from 'umi-request';
import type { Settings as LayoutSettings } from '@ant-design/pro-layout';

import IconFont from '@/components/IconFont';
import { IMenuItem } from './types/common';

const fetchCurrentUser = async () => {
  try {
    const { data: currentUser } = await queryCurrent();
    return currentUser;
  } catch (error) {
    skip2Login({ redirect: true });
  }
  return undefined;
};

const renderIcon = (src: string) => (
  <img src={src} alt="icon" style={{ marginRight: 6, height: 16, width: 16 }} />
);

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
  console.log('getInitialState');
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
    menu: {
      params: {},
      request: async (params, defaultMenuData) => {
        // 这里引用原对象去修改时不生效的
        const clone = cloneDeep(defaultMenuData);
        const res = await getSideMenu();
        const { data } = res;
        const renderMenuRouter = (config: MenuDataItem[], list: IMenuItem[] = []) => {
          config.forEach((item) => {
            if (item.featureCode) {
              const i = list.findIndex((_) => _.featureCode === item.featureCode);
              if (i > -1) {
                if (item.routes) {
                  renderMenuRouter(item.routes, list[i].children);
                }
              } else {
                item.hideInMenu = true;
              }
            }
          });
        };
        renderMenuRouter(clone, data);
        return clone;
      },
    },
    menuProps: {
      expandIcon: (props) => {
        return props.isOpen ? (
          <IconFont style={{ fontSize: 12 }} type="icon-xiala-jihuo" />
        ) : (
          <IconFont style={{ fontSize: 12 }} type="icon-xiala-changgui" />
        );
      },
    },
    subMenuItemRender: (subMenu, defaultDom) => {
      let icon = location.hash.includes(subMenu.path as string)
        ? renderIcon(subMenu.iconActive)
        : renderIcon(subMenu.iconDefault);
      return (
        <div className="ant-submenu-box">
          {icon}
          {defaultDom}
        </div>
      );
    },
    menuItemRender: (item, defaultDom) => {
      const path = item.path || '';
      let icon = null;
      if (path === '/objectLabel') {
        icon = location.hash.includes(path)
          ? renderIcon(item.iconActive)
          : renderIcon(item.iconDefault);
      }
      return (
        <div
          className={
            path.split('/').length > 2 ? 'ant-menu-item-child-padding' : 'ant-menu-item-sub-padding'
          }
        >
          {icon}
          <Link to={item.path as string}>{defaultDom}</Link>
        </div>
      );
    },
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
  if (code === '403') {
    history.replace('/');
    return;
  }
};

export const request: RequestConfig = {
  errorConfig: {
    adaptor: (resData) => {
      return {
        ...resData,
        errorMessage: resData.msg,
        showType: 0, // 错误提示方式.0 silent; 1 message.warn; 2 message.error; 4 notification; 9 page
      };
    },
  },
  middlewares: [middleware],
};
