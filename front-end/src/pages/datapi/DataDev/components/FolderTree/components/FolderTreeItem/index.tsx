import React, { useState, useImperativeHandle } from 'react';
import { Tree } from 'antd';
import type { TreeNode } from '@/types/datadev';
import _ from 'lodash';
import { TreeTitle } from '@/components';
import './index.less';
export default ({data}: {data : TreeNode []}, ref: React.Ref<unknown> | undefined) => {
  const [selectedFolder, setSelectedFolder] = useState<string | number>('');
  const [expandedKeys, setExpandedKeys] = useState<(string | number) []>([data[0].id]);
  useImperativeHandle(ref, () => ({
    selectedFolder
  }))
  
  const getTreeNode =( data: TreeNode []): Record<string, any> [] => {
    return data.map((item) => {
      if (item.type !== 'RECORD') {
        let children = item.children || [];
        children = children.filter(child => child.type !== 'RECORD');
        return {
          title: <TreeTitle icon="icon-wenjianjia" text={item.name} />,
          key: item.id,
          children: getTreeNode(children)
        };
      }
      return {}
    })
  };
  return  <Tree
    className='folder-tree-item'
    treeData={getTreeNode(data)}
    expandedKeys={expandedKeys}
    onExpand={key => setExpandedKeys(key)}
    onSelect={(selectedKeys,) => {
      if (selectedKeys.length) {
       setSelectedFolder(selectedKeys[0])
      }
    }}
  />
}