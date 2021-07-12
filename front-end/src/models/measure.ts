import { useState } from 'react';
import type { ReactNode } from 'react';

import { getFolderTree } from '@/services/measure';
import { TreeNodeType } from '@/constants/datapi';
import { TreeNode } from '@/types/datapi';

interface ITab {
  key: string;
  title: string | ReactNode;
  [key: string]: any;
}
const newDimension = {
  title: '新建维度',
  key: 'newDimension',
  code: 'newDimension',
  type: TreeNodeType.DIMENSION_LABEL,
  mode: 'edit',
};
const newModifier = {
  title: '新建修饰词',
  key: 'newModifier',
  code: 'newModifier',
  type: TreeNodeType.MODIFIER_LABEL,
  mode: 'edit',
};
const newMetric = {
  title: '新建指标',
  key: 'newMetric',
  code: 'newMetric',
  type: TreeNodeType.METRIC_LABEL,
  mode: 'edit',
};

export default () => {
  // tree
  const [tree, setTree] = useState([]);
  const [treeType, setTreeType] = useState<TreeNodeType>(TreeNodeType.DIMENSION_LABEL);
  const [curNode, setCurNode] = useState<any>(null);
  // 当点击节点为文件夹且是新建的时候, 需要该变量来赋值name值
  const [folderMode, setFolderMode] = useState<'create' | 'edit'>('create');
  // workbench
  const [tabs, setTabs] = useState<ITab[]>([]);
  const [activeTab, setActiveTab] = useState('');

  // 获取树
  const getTree = (devTreeType: TreeNodeType) => {
    setTreeType(devTreeType);
    getFolderTree({ devTreeType }).then((res) => {
      setTree(res.data);
    });
  };

  // 点击文件查看的时候增加一个tab
  const viewTab = (node: TreeNode) => {
    const { cid, name, fileCode, type } = node;
    if (!tabs.some((_) => _.key === cid)) {
      setTabs([
        ...tabs,
        {
          key: cid,
          title: name,
          code: fileCode,
          type: type.split('_')[0],
          mode: 'view',
        },
      ]);
    }
    setActiveTab(cid);
  };

  // 新建维度 / 修饰词 / 指标
  const createTab = (type: TreeNodeType) => {
    const key = Date.now().toString();
    if (type === TreeNodeType.DIMENSION_LABEL) {
      setTabs([...tabs, { ...newDimension, key }]);
    }
    if (type === TreeNodeType.MODIFIER_LABEL) {
      setTabs([...tabs, { ...newModifier, key }]);
    }
    if (type === TreeNodeType.METRIC_LABEL) {
      setTabs([...tabs, { ...newMetric, key }]);
    }
    setActiveTab(key);
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

  // 新建完成后替换该Tab的key
  const replaceTab = (oldKey: string, newKey: string, title: string, treeType: TreeNodeType) => {
    let targetI = tabs.findIndex((_) => _.key === oldKey);
    let newTab: ITab = {
      key: newKey,
      code: newKey.split('_')[1],
      title: title,
      mode: 'view',
      type: tabs[targetI].type,
    };
    tabs[targetI] = newTab;
    getTree(treeType);
    setActiveTab(newKey);
    setTabs([...tabs]);
  };

  return {
    // tree
    tree,
    treeType,
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
    replaceTab,
  };
};
