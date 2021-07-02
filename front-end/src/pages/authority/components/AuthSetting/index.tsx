import React, { useState } from 'react';
import { Tabs, Tree, TreeProps } from 'antd';
import type { DataNode } from 'antd/es/tree';
import { debounce } from 'lodash';
import { UnderLinedSearch } from '@/components';
import { getAllParentsKey } from '@/utils/utils';
import type { FeatureTreeNode, FolderNode } from '@/interfaces/global';
import FeatureList from '../FeatureList';
import FolderList from '../FolderList';
import styles from './index.less';

const { TabPane } = Tabs;

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
  featureTree,
  folderTree,
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
  return (
    <div className={styles.wrap} style={style}>
      <div className={styles.left}>
        <UnderLinedSearch key={activeKey} placeholder={placeholder} onChange={handleChange} />
        <Tabs activeKey={activeKey} onChange={setActiveKey}>
          <TabPane tab="功能" key="1">
            <Tree
              className={styles.featureTree}
              treeData={featureTree}
              autoExpandParent={autoExpandParent}
              onExpand={onFeatureExpand}
              expandedKeys={featureExpandedKeys}
              onSelect={onFeatureTreeSelect}
            />
          </TabPane>
          <TabPane tab="资源" key="2">
            <Tree
              className={styles.folderTree}
              autoExpandParent={autoExpandParent}
              expandedKeys={floderExpandedKeys}
              treeData={folderTree}
              onExpand={onFolderExpand}
              onSelect={onFolderTreeSelect}
            />
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
