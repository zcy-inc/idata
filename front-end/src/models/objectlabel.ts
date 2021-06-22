import { useState } from 'react';
import type { ReactNode } from 'react';

import { getFolderTree } from '@/services/objectlabel';

interface ITab {
  key: string;
  title: string | ReactNode;
  [key: string]: any;
}

export default () => {
  // tree
  const [tree, setTree] = useState([]);
  const [curNode, setCurNode] = useState<any>(null);
  // 当点击节点为文件夹且是新建的时候, 需要该变量来赋值name值
  const [folderMode, setFolderMode] = useState<'create' | 'edit'>('create');
  // workbench
  const [tabs, setTabs] = useState<ITab[]>([]);
  const [activeTab, setActiveTab] = useState('');

  // 获取树
  const getTree = () => {
    getFolderTree().then((res) => {
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
    let lastIndex = tabs.findIndex((_) => _.key === key) - 1;

    if (curPanes.length && curActiveKey === key) {
      curActiveKey = curPanes[lastIndex > 0 ? lastIndex : 0].key;
    }
    setTabs(curPanes);
    setActiveTab(curActiveKey);
  };

  return {
    // tree
    tree,
    getTree,
    curNode,
    setCurNode,
    // create or edit folder
    folderMode,
    setFolderMode,
    // workbench
    tabs,
    activeTab,
    setActiveTab,
    addTab,
    removeTab,
  };
};
