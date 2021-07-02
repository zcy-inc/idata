import { useState } from 'react';
import type { ReactNode, Key } from 'react';

import { getFolderTree } from '@/services/objectlabel';
import { TreeNode } from '@/types/objectlabel';

interface ITab {
  key: Key;
  title: string | ReactNode;
  originId: number;
  [key: string]: any;
}
const newObjectLabel = {
  title: '新建数据标签',
  key: 'newObjectLabel',
  originId: -1,
  mode: 'edit',
};

export default () => {
  // tree
  const [tree, setTree] = useState([]);
  const [curNode, setCurNode] = useState<any>(null);
  // 当点击节点为文件夹且是新建的时候, 需要该变量来赋值name值
  const [folderMode, setFolderMode] = useState<'create' | 'edit'>('create');
  // workbench
  const [tabs, setTabs] = useState<ITab[]>([]);
  const [activeTab, setActiveTab] = useState('');
  // editRules
  const [editLayers, setEditLayers] = useState([]);

  // 获取树
  const getTree = () => {
    getFolderTree().then((res) => {
      setTree(res.data);
    });
  };

  // 点击文件查看的时候增加一个tab
  const viewTab = (node: TreeNode) => {
    const { name, id, type } = node;
    if (!tabs.some((_) => _.key === `${id}`)) {
      setTabs([...tabs, { key: `${id}`, originId: id as number, title: name, type, mode: 'view' }]);
    }
    setActiveTab(`${id}`);
  };

  // 增加一个tab
  const createTab = () => {
    if (!tabs.some((_) => _.key === 'newObjectLabel')) {
      setTabs([...tabs, newObjectLabel]);
    }
    setActiveTab('newObjectLabel');
  };

  // 关闭一个tab
  const removeTab = (key: Key) => {
    const curPanes = tabs.filter((_) => _.key !== key);
    let curActiveKey = activeTab;
    let lastIndex = tabs.findIndex((_) => _.key === key) - 1;

    if (curPanes.length && curActiveKey === key) {
      curActiveKey = curPanes[lastIndex > 0 ? lastIndex : 0].key as string;
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
    viewTab,
    createTab,
    removeTab,
    // editRules
    editLayers,
    setEditLayers,
  };
};
