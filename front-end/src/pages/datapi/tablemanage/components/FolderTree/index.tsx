import React, { Fragment, useEffect, useRef, useState } from 'react';
import { Dropdown, Input, Menu, message, Tabs, Tree, Modal } from 'antd';
import { useModel } from 'umi';
import { debounce } from 'lodash';
import type { FC, ChangeEvent, Key } from 'react';
import styles from '../../index.less';

import IconFont from '@/components/IconFont';
import CreateFolder from '../CreateFolder';

import { deleteFolder } from '@/services/tablemanage';
import { TreeNode as ITreeNode } from '@/types/datapi';
import { TreeNodeType } from '@/constants/datapi';

interface SearchTreeNode {
  key: string;
  name: string;
  parentId?: string;
}
type TreeNodeIcon = 'FOLDER' | 'FOLDEROPEN' | 'LABEL' | 'ENUM' | 'TABLE';

const { TreeNode } = Tree;
const { TabPane } = Tabs;
const { confirm } = Modal;
const NodeTypeIcon = {
  FOLDER: <IconFont type="icon-wenjianjia" key="folder" />,
  FOLDEROPEN: <IconFont type="icon-wenjianjiazhankai" key="folderopen" />,
  LABEL: <IconFont type="icon-biaoqian1" key="label" />,
  ENUM: <IconFont type="icon-meiju" key="enum" />,
  TABLE: <IconFont type="icon-biao" key="table" />,
};

const FolderTree: FC = () => {
  // const [search, setSearch] = useState('');
  const [expandedKeys, setExpandedKeys] = useState<Key[]>([]);
  const [autoExpand, setAutoExpand] = useState(true);

  const [visible, setVisible] = useState(false);

  const flatTree = useRef<SearchTreeNode[]>([]);

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
  } = useModel('tablemanage', (ret) => ({
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
    getTree(TreeNodeType.TABLE);
  }, []);

  useEffect(() => {
    flat(tree);
  }, [tree]);

  const menu = (
    <Menu onClick={({ key }) => onMenuActions(key)}>
      <Menu.Item key="folder">新建文件夹</Menu.Item>
      <Menu.Item key="table">新建表</Menu.Item>
      <Menu.Item key="label">新建标签</Menu.Item>
      <Menu.Item key="enum">新建枚举</Menu.Item>
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
      {curFolder?.type === 'FOLDER' ? (
        <Fragment>
          {renderPrimaryMenu()}
          <Menu.Divider />
          <Menu.Item key="folder">新建文件夹</Menu.Item>
          <Menu.Item key="edit">编辑文件夹</Menu.Item>
          <Menu.Item key="delete">删除文件夹</Menu.Item>
        </Fragment>
      ) : null}
    </Menu>
  );

  // 新建文件夹/标签/枚举/表
  const onMenuActions = (key: Key) => {
    switch (key) {
      case 'folder':
        setFolderMode('create');
        setVisible(true);
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
        setVisible(true);
        break;
      case 'delete':
        confirm({
          title: '您确定要删除该文件夹吗？',
          autoFocusButton: null,
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
  const loop = (data: ITreeNode[], parentId?: string): any => {
    const n = data.length;
    return data.map((_, i) => {
      const { name, type, cid } = _;
      // const _i = name.indexOf(search);
      const clsFolderRoot =
        (!parentId && type === TreeNodeType.FOLDER && styles['folder-root']) || '';
      const clsFolderMargin =
        ((type === TreeNodeType.FOLDER || i === n - 1) && styles['folder-margin']) || '';
      let iconType: TreeNodeIcon = type as TreeNodeIcon;
      let title = (
        <span key="title" className={clsFolderRoot}>
          {name}
        </span>
      );
      // 判断文件夹类型的节点是否展开以赋值icon类型
      if (type === TreeNodeType.FOLDER && expandedKeys.indexOf(cid) > -1) {
        iconType = 'FOLDEROPEN';
      }
      // 给检索命中的title加高亮
      // if (_i > -1) {
      //   const pre = name.substring(0, _i);
      //   const suf = name.substring(_i + search?.length);
      //   title = (
      //     <span key="title" className={clsFolderRoot}>
      //       {pre}
      //       <span className={styles['search-match']}>{search}</span>
      //       {suf}
      //     </span>
      //   );
      // }

      const node: any = { ..._, key: cid };
      node.className = clsFolderMargin;
      node.title = [NodeTypeIcon[iconType], title];
      parentId && (node.parentId = parentId);

      return _.children ? (
        <TreeNode {...node}>{loop(_.children, cid)}</TreeNode>
      ) : (
        <TreeNode {...node} />
      );
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
    getTree(curTreeType, value);
    // if (!value) {
    //   setExpandedKeys([]);
    //   setSearch('');
    //   return;
    // }
    // const keys = flatTree.current
    //   .filter((_) => _.name.indexOf(value) > -1 && _.parentId)
    //   .map((_) => _.parentId);

    // setExpandedKeys(keys as Key[]);
    // setSearch(value);
    // setAutoExpand(true);
  };

  const deFilterTree = debounce(onFilterTree, 500);

  const onExpand = (keys: Key[]) => {
    setExpandedKeys(keys);
    setAutoExpand(false);
  };

  return (
    <Fragment>
      <div className={styles.search}>
        <Input
          className={styles['search-input']}
          placeholder="请输入关键字进行搜索"
          prefix={<IconFont type="icon-sousuo" />}
          onChange={deFilterTree}
        />
        <Dropdown overlay={menu} placement="bottomLeft" trigger={['click']}>
          <IconFont
            type="icon-xinjian"
            className={styles['icon-plus']}
            onClick={() => setCurFolder(null)}
          />
        </Dropdown>
      </div>
      <Tabs activeKey={curTreeType} onChange={(key) => getTree(key as TreeNodeType)}>
        <TabPane tab="表" key={TreeNodeType.TABLE} />
        <TabPane tab="标签" key={TreeNodeType.LABEL} />
        <TabPane tab="枚举" key={TreeNodeType.ENUM} />
      </Tabs>
      <Dropdown overlay={treeMenu} placement="bottomLeft" trigger={['contextMenu']}>
        <Tree
          blockNode
          onExpand={onExpand}
          expandedKeys={expandedKeys}
          autoExpandParent={autoExpand}
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
        >
          {loop(tree)}
        </Tree>
      </Dropdown>
      {visible && <CreateFolder visible={visible} onCancel={() => setVisible(false)} />}
    </Fragment>
  );
};

export default FolderTree;
