import React, { Fragment, useEffect, useRef, useState } from 'react';
import { Dropdown, Input, Menu, message, Tabs, Tree, Modal } from 'antd';
import { useModel } from 'umi';
import type { FC, ChangeEvent, Key } from 'react';
import styles from '../../index.less';

import IconFont from '@/components/IconFont';
import CreateFolder from './components/CreateFolder';

import { deleteFolder } from '@/services/measure';
import { TreeNodeType } from '@/constants/datapi';
import { TreeNode as ITreeNode } from '@/types/datapi';

export interface FolderTreeProps {}
interface SearchTreeNode {
  key: string;
  name: string;
  parentId?: string;
}

const { TreeNode } = Tree;
const { TabPane } = Tabs;
const { confirm } = Modal;
const NodeTypeIcon = {
  FOLDER: <IconFont type="icon-wenjianjia" key="folder" />,
  FOLDEROPEN: <IconFont type="icon-wenjianjiazhankai" key="folderopen" />,
  LABEL: <IconFont type="icon-biaoqian1" key="dimension" />,
  ENUM: <IconFont type="icon-meiju" key="modifier" />,
  TABLE: <IconFont type="icon-biao" key="metric" />,
};

const FolderTree: FC<FolderTreeProps> = ({}) => {
  const [searchValue, setSearchValue] = useState('');
  const [expandedKeys, setExpandedKeys] = useState<Key[]>([]);
  const [autoExpandParent, setAutoExpandParent] = useState(true);
  const flatTree = useRef<SearchTreeNode[]>([]);

  const [visible, setVisible] = useState(false);

  const { tree, getTree, treeType, curNode, setCurNode, setFolderMode, viewTab, createTab } =
    useModel('measure', (_) => ({
      tree: _.tree,
      getTree: _.getTree,
      treeType: _.treeType,
      curNode: _.curNode,
      setCurNode: _.setCurNode,
      setFolderMode: _.setFolderMode,
      viewTab: _.viewTab,
      createTab: _.createTab,
    }));

  useEffect(() => {
    getTree(TreeNodeType.DIMENSION_LABEL);
  }, []);

  useEffect(() => {
    flat(tree);
  }, [tree]);

  const menu = (
    <Menu onClick={({ key }) => onMenuActions(key)}>
      <Menu.Item key="folder">新建文件夹</Menu.Item>
      <Menu.Item key="dimension">新建维度</Menu.Item>
      <Menu.Item key="modifier">新建修饰词</Menu.Item>
      <Menu.Item key="metric">新建指标</Menu.Item>
    </Menu>
  );

  const renderTypeMenu = () => {
    switch (treeType) {
      case TreeNodeType.DIMENSION_LABEL:
        return <Menu.Item key="dimension">新建维度</Menu.Item>;
      case TreeNodeType.MODIFIER_LABEL:
        return <Menu.Item key="modifier">新建修饰词</Menu.Item>;
      case TreeNodeType.METRIC_LABEL:
      default:
        return <Menu.Item key="metric">新建指标</Menu.Item>;
    }
  };

  const treeMenu = (
    <Menu onClick={({ key }) => onMenuActions(key)}>
      {curNode?.type === 'FOLDER' ? (
        <Fragment>
          {renderTypeMenu()}
          <Menu.Divider />
          <Menu.Item key="folder">新建文件夹</Menu.Item>
          <Menu.Item key="edit">编辑文件夹</Menu.Item>
          <Menu.Item key="delete">删除文件夹</Menu.Item>
        </Fragment>
      ) : null}
    </Menu>
  );

  // 新建文件夹/维度/修饰词/指标
  const onMenuActions = (key: Key) => {
    switch (key) {
      case 'folder':
        setFolderMode('create');
        setVisible(true);
        break;
      case 'edit':
        setFolderMode('edit');
        setVisible(true);
        break;
      case 'delete':
        onDeleteFolder();
        break;
      case 'dimension':
        createTab(TreeNodeType.DIMENSION_LABEL);
        break;
      case 'modifier':
        createTab(TreeNodeType.MODIFIER_LABEL);
        break;
      case 'metric':
        createTab(TreeNodeType.METRIC_LABEL);
        break;
      default:
        break;
    }
  };

  const onDeleteFolder = () =>
    confirm({
      title: '您确定要删除该文件夹吗？',
      autoFocusButton: null,
      onOk: () =>
        deleteFolder({ folderId: curNode?.folderId })
          .then((res) => {
            if (res.success) {
              message.success('删除文件夹成功');
              getTree(treeType);
            }
          })
          .catch((err) => {}),
    });

  // 组装数据，并高亮检索结果
  const loop = (data: ITreeNode[], parentId?: string): any => {
    const n = data.length;
    return data.map((_, i) => {
      const { name, type, cid } = _;
      const _i = name.indexOf(searchValue);
      const node: any = { ..._, key: cid };
      const clsFolderRoot = (!parentId && type === 'FOLDER' && styles['folder-root']) || '';
      const clsFolderMargin = ((type === 'FOLDER' || i === n - 1) && styles['folder-margin']) || '';
      let _type = type as string;
      let title = (
        <span key="title" className={clsFolderRoot}>
          {name}
        </span>
      );
      // 判断文件夹类型的节点是否展开以赋值icon类型
      if (type === 'FOLDER' && expandedKeys.indexOf(cid) > -1) {
        _type = 'FOLDEROPEN';
      }
      // 给检索命中的title加高亮
      if (_i > -1) {
        const beforeStr = name.substring(0, _i);
        const afterStr = name.substring(_i + searchValue?.length);
        title = (
          <span key="title" className={clsFolderRoot}>
            {beforeStr}
            <span className={styles['search-match']}>{searchValue}</span>
            {afterStr}
          </span>
        );
      }
      node.className = clsFolderMargin;
      node.title = [NodeTypeIcon[_type], title];
      parentId && (node.parentId = parentId);

      return _.children ? (
        <TreeNode {...node}>{loop(_.children, cid)}</TreeNode>
      ) : (
        <TreeNode {...node} />
      );
    });
  };

  // 将树平铺用以检索
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
    setExpandedKeys(keys as Key[]);
    setSearchValue(value);
    setAutoExpandParent(true);
  };

  const onExpand = (keys: Key[] = []) => {
    setExpandedKeys(keys);
    setAutoExpandParent(false);
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
            onClick={() => setCurNode(null)}
          />
        </Dropdown>
      </div>
      <Tabs activeKey={treeType} onChange={(key) => getTree(key as TreeNodeType)}>
        <TabPane tab="维度" key={TreeNodeType.DIMENSION_LABEL} />
        <TabPane tab="修饰词" key={TreeNodeType.MODIFIER_LABEL} />
        <TabPane tab="指标" key={TreeNodeType.METRIC_LABEL} />
      </Tabs>
      <Dropdown overlay={treeMenu} placement="bottomLeft" trigger={['contextMenu']}>
        <Tree
          blockNode
          onExpand={(keys) => onExpand(keys)}
          expandedKeys={expandedKeys}
          autoExpandParent={autoExpandParent}
          onRightClick={({ node }: any) => {
            const parentId = node.parentId?.split('_')[1] || null;
            const folderId = `${node.folderId}`;
            setCurNode({ ...node, folderId, parentId });
          }}
          onSelect={(selectedKeys, { node }: any) => node.type !== 'FOLDER' && viewTab(node)}
        >
          {loop(tree)}
        </Tree>
      </Dropdown>
      {visible && <CreateFolder visible={visible} onCancel={() => setVisible(false)} />}
    </Fragment>
  );
};

export default FolderTree;
