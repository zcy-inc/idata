import React, { useState } from 'react';
import { Tabs, Tree, TreeProps } from 'antd';
import type { DataNode } from 'antd/es/tree';
import { debounce } from 'lodash';
import { getAllParentsKey, getWorkspacePrefix } from '@/utils/utils';
import type { FeatureTreeNode, FolderNode } from '@/interfaces/global';
import UnderLinedSearch from './components/SearchInput/UnderLinedSearch';
import FeatureList from '../FeatureList';
import FolderList from '../FolderList';
import styles from './index.less';

const { TabPane } = Tabs;
const { TreeNode } = Tree;

export interface AuthSettingProps {
  readonly?: boolean; // 是否为查看
  style?: React.CSSProperties;
  featureTree?: DataNode[];
  folderTree?: DataNode[];
  onFeatureTreeSelect?: TreeProps['onSelect'];
  onFolderTreeSelect?: TreeProps['onSelect'];
  featureList?: FeatureTreeNode[];
  folderList?: FolderNode[];
  onFeatureEnableChange?: (node: FeatureTreeNode) => void;
  onFolderAuthChange?: (node: FolderNode) => void;
}

const AuthSetting: React.FC<AuthSettingProps> = ({
  readonly,
  style,
  featureTree = [],
  folderTree = [],
  featureList,
  folderList,
  onFeatureTreeSelect,
  onFolderTreeSelect,
  onFeatureEnableChange,
  onFolderAuthChange,
}) => {
  const [activeKey, setActiveKey] = useState('1');
  const [autoExpandParent, setAutoExpandParent] = useState(false);
  const [featureExpandedKeys, setFeatureExpandedKeys] = useState<React.Key[]>([]);
  const [floderExpandedKeys, setFolderExpandedKeys] = useState<React.Key[]>([]);
  const handleChange = debounce((e: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target;
    if (activeKey === '1' && featureTree) {
      const expandKeys = getAllParentsKey(
        featureTree,
        (node) => node.key,
        (node) => {
          if (typeof node.title === 'string') {
            return node.title.indexOf(value) > -1;
          }
          return false;
        },
      );
      setAutoExpandParent(true);
      setFeatureExpandedKeys(expandKeys);
    } else if (activeKey === '2' && folderTree) {
      const expandKeys = getAllParentsKey(
        folderTree,
        (node) => node.key,
        (node) => {
          if (typeof node.title === 'string') {
            return node.title.indexOf(value) > -1;
          }
          return false;
        },
      );
      setAutoExpandParent(true);
      setFolderExpandedKeys(expandKeys);
    }
  }, 300);

  const onFeatureExpand = (expandedKeys: React.Key[]) => {
    setFeatureExpandedKeys(expandedKeys);
    setAutoExpandParent(false);
  };
  const onFolderExpand = (expandedKeys: React.Key[]) => {
    setFolderExpandedKeys(expandedKeys);
    setAutoExpandParent(false);
  };
  const placeholder = activeKey === '1' ? '请输入功能名称' : '请输入资源名称';

  // 组装数据
  const loop = (data: any[], treeType: 'feature' | 'folder'): any => {
    return data.map((_) => {
      if (!_.featureCode) {
        return null;
      }
      const { name, type, parentCode, featureCode, cid } = _;
      const clsFolderRoot = !parentCode && type === 'F_MENU' ? 'folder-root' : '';
      let title = (
        <span key="title" className={clsFolderRoot}>
          {name}
        </span>
      );

      const workspaceString = sessionStorage.getItem('workspace');
      let workspacePrefix = '';
      
      if (workspaceString) {
        try {
          const workspaceJson = JSON.parse(workspaceString);
          workspacePrefix = workspaceJson.urlPath || '';
        } catch (e) {}
      }

      let key = '';
      if (workspacePrefix === 'zcy') {
        key = featureCode;
      } else {
        key = treeType === 'feature' ? featureCode : cid;
      }

      const node: any = { ..._, key };
      node.className = clsFolderRoot;
      node.title = title;
      parentCode && (node.parentId = parentCode);

      return _.children ? (
        <TreeNode {...node}>{loop(_.children, treeType)}</TreeNode>
      ) : (
        <TreeNode {...node}></TreeNode>
      );
    });
  };

  return (
    <div className={styles.wrap} style={style}>
      <div className={styles.left}>
        <UnderLinedSearch key={activeKey} placeholder={placeholder} onChange={handleChange} />
        <Tabs activeKey={activeKey} onChange={setActiveKey}>
          <TabPane tab="功能" key="1">
            <Tree
              className={`${styles.featureTree} folder-tree`}
              autoExpandParent={autoExpandParent}
              onExpand={onFeatureExpand}
              expandedKeys={featureExpandedKeys}
              onSelect={onFeatureTreeSelect}
            >
              {loop(featureTree, 'feature')}
            </Tree>
          </TabPane>
          <TabPane tab="资源" key="2">
            <Tree
              className={`${styles.folderTree} folder-tree`}
              autoExpandParent={autoExpandParent}
              expandedKeys={floderExpandedKeys}
              onExpand={onFolderExpand}
              onSelect={onFolderTreeSelect}
            >
              {loop(folderTree, 'folder')}
            </Tree>
          </TabPane>
        </Tabs>
      </div>
      <div className={styles.right}>
        {activeKey === '1' && Array.isArray(featureList) && (
          <FeatureList readonly={readonly} data={featureList} onChange={onFeatureEnableChange} />
        )}
        {activeKey === '2' && Array.isArray(featureList) && (
          <FolderList readonly={readonly} data={folderList} onChange={onFolderAuthChange} />
        )}
      </div>
    </div>
  );
};

export default AuthSetting;
