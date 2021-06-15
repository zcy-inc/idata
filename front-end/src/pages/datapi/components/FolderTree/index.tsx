import React, { Fragment, useEffect, useRef, useState } from 'react';
import { Dropdown, Input, Menu, message, Tabs, Tree, Modal } from 'antd';
import { useBoolean } from 'ahooks';
import { useModel } from 'umi';
import type { FC, ChangeEvent } from 'react';
import styles from '../../tablemanage/index.less';

import IconFont from '@/components/IconFont';
import { deleteFolder } from '@/services/tablemanage';

export type Key = string | number;
export interface FolderTreeProps {
  actions: { showFolder: () => void };
}
export interface TreeNode {
  name: string;
  type: string;
  folderId: string;
  parentId: string;
  children?: TreeNode[];
  [key: string]: any;
}
export interface FlatTreeNode {
  key: string;
  name: string;
  parentId?: string;
}

const { TabPane } = Tabs;
const { confirm } = Modal;
const NodeTypeIcon = {
  FOLDER: <IconFont type="icon-wenjianjia" key="folder" />,
  FOLDEROPEN: <IconFont type="icon-wenjianjiazhankai" key="folderopen" />,
  LABEL: <IconFont type="icon-biaoqian1" key="label" />,
  ENUM: <IconFont type="icon-meiju" key="enum" />,
  TABLE: <IconFont type="icon-biao" key="table" />,
};

const FolderTree: FC<FolderTreeProps> = ({ actions }) => {
  const [searchValue, setSearchValue] = useState('');
  const [expandedKeys, setExpandedKeys] = useState<any[]>([]);
  const [autoExpandParent, { setTrue: autoExpandON, setFalse: autoExpandOFF }] = useBoolean(true);
  const flatTree = useRef<FlatTreeNode[]>([]);

  const {
    setFolderMode,
    curFolder,
    setCurFolder,
    curTreeType,
    tree,
    getTree,
    onCreateEnum,
    onCreateTable,
    onViewTree,
    showLabel,
  } = useModel('tabalmanage', (ret) => ({
    setFolderMode: ret.setFolderMode,
    curFolder: ret.curFolder,
    setCurFolder: ret.setCurFolder,
    curTreeType: ret.curTreeType,
    tree: ret.tree,
    getTree: ret.getTree,
    onCreateEnum: ret.onCreateEnum,
    onCreateTable: ret.onCreateTable,
    onViewTree: ret.onViewTree,
    showLabel: ret.showLabel,
  }));

  useEffect(() => {
    getTree('TABLE');
  }, []);

  useEffect(() => {
    flat(tree);
  }, [tree]);

  const menu = (
    <Menu onClick={({ key }) => onMenuActions(key)}>
      <Menu.Item key="folder">新建文件夹</Menu.Item>
      <Menu.Item key="label">新建标签</Menu.Item>
      <Menu.Item key="enum">新建枚举</Menu.Item>
      <Menu.Item key="table">新建表</Menu.Item>
    </Menu>
  );
  const renderPrimaryMenu = () => {
    switch (curTreeType) {
      case 'TABLE':
        return <Menu.Item key="table">新建表</Menu.Item>;
      case 'LABEL':
        return <Menu.Item key="label">新建标签</Menu.Item>;
      case 'ENUM':
      default:
        return <Menu.Item key="enum">新建枚举</Menu.Item>;
    }
  };
  const treeMenu = (
    <Menu onClick={({ key }) => onMenuActions(key)}>
      {renderPrimaryMenu()}
      <Menu.Divider />
      <Menu.Item key="folder">新建文件夹</Menu.Item>
      {curFolder?.type === 'FOLDER' && (
        <Fragment>
          <Menu.Item key="edit">编辑文件夹</Menu.Item>
          <Menu.Item key="delete">删除文件夹</Menu.Item>
        </Fragment>
      )}
    </Menu>
  );

  // 新建文件夹/标签/枚举/表
  const onMenuActions = (key: Key) => {
    switch (key) {
      case 'folder':
        setFolderMode('create');
        actions.showFolder();
        break;
      case 'label':
        showLabel();
        break;
      case 'enum':
        onCreateEnum();
        break;
      case 'table':
        onCreateTable();
        break;
      case 'edit':
        setFolderMode('edit');
        actions.showFolder();
        break;
      case 'delete':
        confirm({
          title: '您确定要删除该文件夹吗？',
          onOk: () =>
            deleteFolder({ folderId: curFolder.folderId })
              .then((res) => {
                if (res.success) {
                  message.success('删除文件夹成功');
                  getTree(curTreeType);
                }
              })
              .catch((err) => {}),
        });
        break;
      default:
        break;
    }
  };

  // 组装数据，并高亮检索结果
  const loop = (data: any[], parentId?: string): any => {
    const n = data.length;
    return data.map((_, i) => {
      const { name, type, cid } = _;
      const _i = name.indexOf(searchValue);
      const node = { ..._, key: cid };
      let title = name;
      let _type = type;
      // 判断文件夹类型的节点是否展开以赋值icon类型
      if (type === 'FOLDER' && expandedKeys.indexOf(cid) > -1) {
        _type = 'FOLDEROPEN';
      }
      // 给检索命中的title加高亮
      if (_i > -1) {
        const beforeStr = name.substring(0, _i);
        const afterStr = name.substring(_i + searchValue?.length);
        title = (
          <span
            key="title"
            className={`${(type === 'FOLDER' || i === n - 1) && styles['folder-margin']} ${
              !parentId && type === 'FOLDER' && styles['folder-root']
            }`}
          >
            {beforeStr}
            <span className={styles['search-match']}>{searchValue}</span>
            {afterStr}
          </span>
        );
      }
      node.title = [NodeTypeIcon[_type], title];
      parentId && (node.parentId = parentId);

      return _.children ? { ...node, children: loop(_.children, cid) } : { ...node };
    });
  };

  // 将树平铺, 用以检索
  const flat = (data: any[], parentId?: string) => {
    for (let i = 0; i < data.length; i++) {
      const node = data[i];
      const { name, cid } = node;
      flatTree.current.push({ name, key: cid, parentId: parentId });
      if (node.children) {
        flat(node.children, node.cid);
      }
    }
  };

  // 检索树
  const onFilterTree = ({ target: { value } }: ChangeEvent<HTMLInputElement>) => {
    if (!value) {
      setExpandedKeys([]);
      setSearchValue('');
      return;
    }
    const keys = flatTree.current
      .map((node) => (node.name.indexOf(value) > -1 ? node.parentId : ''))
      .filter((_) => !!_);
    setExpandedKeys(keys);
    setSearchValue(value);
    autoExpandON();
  };

  const onExpand = (keys: Key[] = []) => {
    setExpandedKeys(keys);
    autoExpandOFF();
  };

  return (
    <Fragment>
      <div className={styles.search}>
        <Input
          className={styles['search-input']}
          placeholder="请输入关键字进行搜索"
          prefix={<IconFont type="icon-sousuo" />}
          onChange={onFilterTree}
        />
        <Dropdown overlay={menu} placement="bottomLeft" trigger={['click']}>
          <IconFont
            type="icon-xinjian"
            className={styles['icon-plus']}
            onClick={() => setCurFolder(null)}
          />
        </Dropdown>
      </div>
      <Tabs activeKey={curTreeType} onChange={(key) => getTree(key)}>
        <TabPane tab="表" key="TABLE" />
        <TabPane tab="标签" key="LABEL" />
        <TabPane tab="枚举" key="ENUM" />
      </Tabs>
      <Dropdown overlay={treeMenu} placement="bottomLeft" trigger={['contextMenu']}>
        <Tree
          blockNode
          onExpand={(keys) => onExpand(keys)}
          expandedKeys={expandedKeys}
          autoExpandParent={autoExpandParent}
          treeData={loop(tree)}
          onRightClick={({ node }) => {
            const _: any = node;
            const parentId = _.parentId?.split('_')[1] || null;
            const folderId = `${_.folderId}`;
            setCurFolder({ ...node, folderId, parentId });
          }}
          onSelect={(selectedKeys, { node }) => {
            const _: any = node;
            _.type !== 'FOLDER' && onViewTree(node);
          }}
        />
      </Dropdown>
    </Fragment>
  );
};

export default FolderTree;
