import { useState } from 'react';
import type { ReactNode } from 'react';

import { getFolderTree } from '@/services/kpisystem';

interface ITab {
  key: string;
  title: string | ReactNode;
  [key: string]: any;
}

export default () => {
  // tree
  const [tree, setTree] = useState([]);
  const [treeType, setTreeType] = useState('');
  const [curNode, setCurNode] = useState<any>(null);
  // workbench
  const [tabs, setTabs] = useState<ITab[]>([]);
  const [activeTab, setActiveTab] = useState('');

  // 获取树
  const getTree = (devTreeType: any) => {
    setTreeType(devTreeType);
    getFolderTree({ devTreeType }).then((res) => {
      setTree(res.data);
    });
  };
  // 增加一个tab
  const addTab = (node: any) => {
    const { cid, name, fileCode, type } = node;
    if (!tabs.some((_) => _.key === cid)) {
      setTabs([...tabs, { key: cid, title: name, code: fileCode, type, mode: 'view' }]);
    }
    setActiveTab(cid);
  };
  // 关闭一个tab
  const removeTab = (key: any) => {
    const curPanes = tabs.filter((_) => _.key !== key);
    let curActiveKey = activeTab;
    let lastIndex = tabs.length - 1;

    tabs.find((_, i) => {
      if (_.key === key) {
        lastIndex = i - 1;
        return true;
      }
      return false;
    });
    if (curPanes.length && curActiveKey === key) {
      curActiveKey = curPanes[lastIndex > 0 ? lastIndex : 0].key;
    }
    setTabs(curPanes);
    setActiveTab(curActiveKey);
  };

  return {
    // tree
    tree,
    treeType,
    getTree,
    curNode,
    setCurNode,
    // workbench
    tabs,
    activeTab,
    setActiveTab,
    addTab,
    removeTab,
  };
};
