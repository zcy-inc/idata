import { Modal, message } from 'antd';
import { reqUrlWhiteList, wsStorageKey, defaultWs } from '@/constants/common';
import { cloneDeep, omit } from 'lodash';
import type { TreeNode } from '@/interfaces/global';

/* eslint no-useless-escape:0 import/prefer-default-export:0 */
const reg = /(((^https?:(?:\/\/)?)(?:[-;:&=\+\$,\w]+@)?[A-Za-z0-9.-]+(?::\d+)?|(?:www.|[-;:&=\+\$,\w]+@)[A-Za-z0-9.-]+)((?:\/[\+~%\/.\w-_]*)?\??(?:[-\+=&;%@.\w_]*)#?(?:[\w]*))?)$/;

export const isUrl = (path: string): boolean => reg.test(path);

export const isAntDesignPro = (): boolean => {
  if (ANT_DESIGN_PRO_ONLY_DO_NOT_USE_IN_YOUR_PRODUCTION === 'site') {
    return true;
  }
  return window.location.hostname === 'preview.pro.ant.design';
};

// 给官方演示站点用，用于关闭真实开发环境不需要使用的特性
export const isAntDesignProOrDev = (): boolean => {
  const { NODE_ENV } = process.env;
  if (NODE_ENV === 'development') {
    return true;
  }
  return isAntDesignPro();
};

export const boolToInt = (bool: boolean) => {
  if (bool) {
    return 1;
  }
  return 0;
};

export const getDeleteFn = <T = any>(
  service: (...args: any) => Promise<Tresponse<T>>,
  cb?: (result?: Tresponse<T>) => void,
) => {
  return (...args: any) =>
    new Promise((resolve, reject) => {
      Modal.confirm({
        title: '提示',
        content: '确定要删除吗？',
        onOk: async () => {
          try {
            const res = await service(...args);
            message.success('删除成功');
            cb?.(res);
            resolve(res);
          } catch (e) {
            reject(e);
          }
        },
      });
    });
};

export const getSuccessReqFn = (msg: string) => async <R>(
  service: () => Promise<Tresponse<R>>,
  cb?: (result?: Tresponse<R>) => void,
) => {
  const res = await service();
  message.success(msg);
  cb?.(res);
  return res;
};

// 从本地存储获取当前工作区
export const getCachedWs = () => {
  const cachedWs = localStorage.getItem(wsStorageKey);
  if (typeof cachedWs === 'string' && cachedWs !== '') {
    return cachedWs;
  }
  return defaultWs;
};

// 将工作区存储到本地
export const setWsCache = (ws: string) => {
  localStorage.setItem(wsStorageKey, ws);
};

export const getReqUrl = (url: string) => {
  if (reqUrlWhiteList.some((i) => i.test(url))) {
    return url;
  }
  const ws = getCachedWs();
  return `/${ws}${url}`;
};

export const formatTreeData = <T extends TreeNode<T>>(
  roots: T[],
  nodeFormatter?: (node: T) => TreeNode,
) => {
  const holder = cloneDeep(roots);
  const loop = (nodes: T[]) => {
    if (Array.isArray(nodes)) {
      for (let i = 0; i < nodes.length; i++) {
        const node = nodes[i];
        if (Array.isArray(node.children)) {
          loop(node.children);
        }
        if (nodeFormatter) {
          Object.assign(node, nodeFormatter(node));
        }
      }
    }
  };
  loop(holder);
  return holder;
};

// 根据指定的层级剪切树
export const cutTreeData = <T extends TreeNode<T>>(roots: T[], level: number) => {
  const holder = cloneDeep(roots);
  const stack: TreeNode[] = [];
  const loop = (nodes: T[]) => {
    nodes?.forEach((node) => {
      stack.push(node);
      if (stack.length >= level) {
        delete node.children;
      }
      if (Array.isArray(node.children)) {
        loop(node.children);
      } else {
        stack.pop();
      }
    });
  };
  loop(holder);
  return holder;
};

// 根据条件获取树中节点
export const getTreeNode = <T extends TreeNode<T>>(roots: T[], condition: (node: T) => boolean) => {
  const holder = cloneDeep(roots);
  let going = true;
  let target;
  const loop = (nodes: T[]) => {
    if (!going) {
      return;
    }
    for (let i = 0; i < nodes.length; i++) {
      const node = nodes[i];
      if (condition(node)) {
        going = false;
        target = node;
        return;
      }
      if (Array.isArray(node.children)) {
        loop(node.children);
      }
    }
  };
  loop(holder);
  return target as T | undefined;
};

// 获取所有满足指定条件的子节点列表
export const getFlattenChildren = <T extends TreeNode<T>>(
  root: T,
  condition: (node: T) => boolean,
) => {
  const arr: T[] = [];
  const loop = (nodes: T[]) => {
    if (Array.isArray(nodes)) {
      for (let i = 0; i < nodes.length; i++) {
        const node = nodes[i];
        if (condition(node)) {
          arr.push(node);
        }
        if (Array.isArray(node.children)) {
          loop(node.children);
        }
      }
    }
  };

  if (root && Array.isArray(root.children)) {
    loop(root.children);
  }
  return arr;
};

// 更新某个树中某个节点
export const updateTreeNode = <T extends TreeNode<T>>(
  roots: T[],
  condition: (node: T) => boolean,
  updater: (node: T) => any,
) => {
  const holder = cloneDeep(roots);
  let going = true;

  const loop = (nodes: T[]) => {
    if (!going) {
      return;
    }
    for (let i = 0; i < nodes.length; i++) {
      let node = nodes[i];
      if (condition(node)) {
        going = false;
        nodes[i] = updater(node);
        return;
      }
      if (Array.isArray(node.children)) {
        loop(node.children);
      }
    }
  };
  loop(holder);
  return holder;
};

// 树结构扁平化
export const flatTreeData = <T extends TreeNode<T>>(roots: T[], getKey: (node: T) => React.Key) => {
  const holder = cloneDeep(roots);
  const arr: (Omit<T, 'children'> & { pid?: React.Key })[] = [];
  const loop = (nodes: T[], pid?: React.Key) => {
    if (Array.isArray(nodes)) {
      for (let i = 0; i < nodes.length; i++) {
        const node = nodes[i];
        const partial = omit(node, ['children']);
        arr.push({ ...partial, pid });
        if (Array.isArray(node.children)) {
          loop(node.children, getKey(node));
        }
      }
    }
  };
  loop(holder);
  return arr;
};

// 获取满足指定条件的所有节点的父节点key列表
export const getAllParentsKey = <T extends TreeNode<T>>(
  roots: T[],
  getKey: (node: T) => React.Key,
  condition: (node: Omit<T, 'children'> & { pid?: React.Key }) => boolean,
) => {
  const flattenList = flatTreeData(roots, getKey);
  const arr: React.Key[] = [];
  for (let i = 0; i < flattenList.length; i++) {
    const current = flattenList[i];
    if (condition(current) && typeof current.pid !== 'undefined') {
      arr.push(current.pid);
    }
  }
  return arr;
};

// 获取树中指定节点的所有父节点
export function getTreeParents<T extends TreeNode>(
  roots: Array<T>,
  getKey: (node: T) => React.Key,
  key: React.Key,
) {
  const stack: Array<T> = [];
  let going = true;

  const walker = (array: Array<T>) => {
    array.forEach((item) => {
      if (!going) return;
      stack.push(item);
      if (getKey(item) === key) {
        going = false;
      } else if (item.children) {
        walker(item.children as T[]);
      } else {
        stack.pop();
      }
    });
    if (going) stack.pop();
  };

  walker(roots);
  return stack;
}

export const getWorkspacePrefix = () => {
  const workspaceString = sessionStorage.getItem('workspace');
  let workspace = '';
  if (workspaceString) {
    try {
      const workspaceJson = JSON.parse(workspaceString);
      workspace = workspaceJson.urlPath || '';
      if (workspaceJson.urlPath === 'zcy') {
        workspace = '';
      }
    } catch (e) {
      workspace = '';
    }
  }

  return workspace;
};

/**
 * combine request url
 */
export const getRequestUrl = (url: string) => {
  const workspace = getWorkspacePrefix();
  if (workspace) {
    return `/${workspace}${url}`;
  }
  return url;
};

/**
 * 复制指定文本到粘贴板
 * @param text
 */
export const copy = (text: string) => {
  const textarea = document.createElement('textarea');
  // 选中
  textarea.style.position = 'absolute';
  textarea.style.clipPath = 'polygon(0px 0px,0px 0px,0px 0px,0px 0px)';
  // 赋值
  textarea.value = text;
  document.body.appendChild(textarea);
  textarea.select();
  // 复制
  document.execCommand('copy', true);

  document.body.removeChild(textarea);
};

// 字符串高亮
export const highlightText = ({
  text,
  keyWord,
  color = 'red',
}: {
  text: string;
  keyWord: string;
  color?: string;
}) => {
  const index = text.indexOf(keyWord);
  if (index > -1) {
    const beforeStr = text.substring(0, index);
    const afterStr = text.substring(index + keyWord.length);
    return `${beforeStr}<span style="color: ${color}">${keyWord}</span>${afterStr}`;
  }
  return text;
};
