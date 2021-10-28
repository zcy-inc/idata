import React, { useCallback, useEffect, useRef, useState } from 'react';
import { Dropdown, Input, Menu, message, Tree, Modal, Popover, Empty } from 'antd';
import { useModel } from 'umi';
import type { FC, Key } from 'react';
import styles from './index.less';

import IconFont from '@/components/IconFont';
import { deleteFolder, getFunctionTree } from '@/services/datadev';
import { TreeNode as Treenode } from '@/types/datadev';
import { FolderBelong, FolderTypes } from '@/constants/datadev';

import CreateFolder from './components/CreateFolder';
import TreeNodeTitle from './components/TreeNodeTitle';
import IconCreate from './components/IconCreate';
import IconFilter from './components/IconFilter';

const { TreeNode } = Tree;
const { confirm } = Modal;
interface FolderListItem {
  key: string;
  title: string;
}

const FolderTree: FC = () => {
  const [functionTree, setFunctionTree] = useState<Treenode[]>([]);
  const [visible, setVisible] = useState(false);

  const [expandedKeys, setExpandedKeys] = useState<Key[]>([]);
  const [autoExpandParent, setAutoExpandParent] = useState(false);

  const {
    tree,
    getTreeWrapped,
    setFolderMode,
    curNode,
    setCurNode,
    belongFunctions,
    setBelongFunctions,
    keyWord,
    setKeyWord,
    onCreateEnum,
    onCreateTable,
    onViewTree,
    showLabel,
    onCreateDAG,
    setVisibleTask,
    setCurLabel,
    folderList,
  } = useModel('datadev', (_) => ({
    tree: _.tree,
    getTreeWrapped: _.getTreeWrapped,
    setFolderMode: _.setFolderMode,
    curNode: _.curNode,
    setCurNode: _.setCurNode,
    belongFunctions: _.belongFunctions,
    setBelongFunctions: _.setBelongFunctions,
    keyWord: _.keyWord,
    setKeyWord: _.setKeyWord,
    onCreateEnum: _.onCreateEnum,
    onCreateTable: _.onCreateTable,
    onViewTree: _.onViewTree,
    showLabel: _.showLabel,
    onCreateDAG: _.onCreateDAG,
    setVisibleTask: _.setVisibleTask,
    setCurLabel: _.setCurLabel,
    folderList: _.folderList,
  }));

  useEffect(() => {
    getTreeWrapped();
    getFunctionTree()
      .then((res) => setFunctionTree(res.data))
      .catch((err) => {});
  }, []);

  const menu = (
    <Menu onClick={({ key }) => onAction(key)}>
      <Menu.Item key="CreateTable">
        <IconFont style={{ marginRight: 8 }} type="icon-xinjianwenjianjia1" />
        新建表
      </Menu.Item>
      <Menu.Item key="CreateLabel">
        <IconFont style={{ marginRight: 8 }} type="icon-xinjianbiaoqian1" />
        新建标签
      </Menu.Item>
      <Menu.Item key="CreateEnum">
        <IconFont style={{ marginRight: 8 }} type="icon-xinjianmeiju" />
        新建枚举
      </Menu.Item>
      <Menu.Item key="CreateDAG">
        <IconFont style={{ marginRight: 8 }} type="icon-xinjianDAG" />
        新建DAG
      </Menu.Item>
      <Menu.Item key="CreateJob">
        <IconFont style={{ marginRight: 8 }} type="icon-xinjianzuoye" />
        新建任务
      </Menu.Item>
    </Menu>
  );

  // 新建文件夹/标签/枚举/表
  const onAction = (key: Key, node?: Treenode) => {
    switch (key) {
      case 'CreateFolder':
        setCurNode(node);
        setFolderMode('create');
        setVisible(true);
        break;
      case 'EditFolder':
        setCurNode(node);
        setFolderMode('edit');
        setVisible(true);
        break;
      case 'DeleteFolder':
        setCurNode(node);
        onDeleteFolder();
        break;
      case 'CreateTable':
        setCurNode(node);
        onCreateTable();
        break;
      case 'CreateLabel':
        setCurNode(node);
        setCurLabel(-1);
        showLabel();
        break;
      case 'CreateEnum':
        setCurNode(node);
        onCreateEnum();
        break;
      case 'CreateDAG':
        setCurNode(node);
        onCreateDAG();
        break;
      case 'CreateJob':
        setCurNode(node);
        setVisibleTask(true);
        break;

      default:
        break;
    }
  };

  const onDeleteFolder = () =>
    confirm({
      title: '您确定要删除该文件夹吗？',
      autoFocusButton: null,
      onOk: () => {
        console.log(curNode);

        if (curNode) {
          deleteFolder({ id: curNode.id }).then((res) => {
            if (res.success) {
              message.success('删除文件夹成功');
              getTreeWrapped();
            }
          });
        }
      },
    });

  const isRootFolder = useCallback((belong: FolderBelong) => {
    if (
      belong === FolderBelong.DESIGN ||
      belong === FolderBelong.DAG ||
      belong === FolderBelong.DI ||
      belong === FolderBelong.DEV
    ) {
      return true;
    } else {
      return false;
    }
  }, []);

  // 为了加上样式所以使用TreeNode，不然可以用treeData属性
  const loop = (data: Treenode[]): any => {
    const n = data.length;
    return data.map((_, i) => {
      let tmp: any = _.name;
      // 按组件的属性赋值key
      _.key = _.cid;
      // 给node加上样式
      if (isRootFolder(_.belong) || i === n - 1) {
        _.className = 'folder-margin';
      }
      const index = _.name.indexOf(keyWord);
      if (index > -1) {
        const beforeStr = _.name.substr(0, index);
        const afterStr = _.name.substr(index + keyWord.length);
        tmp = (
          <span>
            {beforeStr}
            <span style={{ color: 'red' }}>{keyWord}</span>
            {afterStr}
          </span>
        );
      }
      // 给title加上样式
      let title = <span className={_.parentId ? '' : 'folder-root'}>{tmp}</span>;
      // 给type不为FolderTypes.RECORD的节点加上icon
      if (_.type === FolderTypes.RECORD) {
        _.title = title;
      } else {
        _.title = <TreeNodeTitle node={_} title={title} onAction={onAction} />;
      }

      return _.children ? <TreeNode {..._}>{loop(_.children)}</TreeNode> : <TreeNode {..._} />;
    });
  };

  // 渲染筛选树
  const loopBelongTree = (data: Treenode[]) =>
    data.map((_) => {
      _.title = _.name;
      _.key = _.belong;
      return _.children ? (
        <TreeNode {..._}>{loopBelongTree(_.children)}</TreeNode>
      ) : (
        <TreeNode {..._} />
      );
    });

  // 获取父节点的key
  const getParentKey = (key: string, tree: Treenode[]): string => {
    let parentKey = '';
    for (let i = 0; i < tree.length; i++) {
      const node = tree[i];
      if (node.children) {
        if (node.children.some((item) => item.cid === key)) {
          parentKey = node.cid;
        } else if (getParentKey(key, node.children)) {
          parentKey = getParentKey(key, node.children);
        }
      }
    }
    return parentKey;
  };

  return (
    <div className="folder-tree">
      <div className="search">
        <Input
          className="search-input"
          placeholder="请输入关键字进行搜索"
          prefix={<IconFont type="icon-sousuo" />}
          onKeyDown={(e) => {
            if (e.key === 'Enter') {
              getTreeWrapped();
              const tmpKeys = !!keyWord
                ? folderList.current
                    .map((item) => {
                      if (item.title.indexOf(keyWord) > -1) {
                        return getParentKey(item.key, tree);
                      }
                      return null;
                    })
                    .filter((item, i, self) => item && self.indexOf(item) === i)
                : [];
              setExpandedKeys(tmpKeys as Key[]);
              setAutoExpandParent(true);
            }
          }}
          onChange={({ target: { value } }) => setKeyWord(value)}
        />
        <Dropdown overlay={menu} placement="bottomLeft" trigger={['click']}>
          <IconCreate onClick={() => setCurNode(null)} />
        </Dropdown>
        <Popover
          content={
            functionTree.length ? (
              <Tree
                blockNode
                checkable
                defaultExpandAll
                defaultCheckedKeys={belongFunctions}
                onCheck={(checked, { halfCheckedKeys }) => {
                  let checkedKeys = checked as string[];
                  if (Array.isArray(halfCheckedKeys)) {
                    checkedKeys = checkedKeys.concat(halfCheckedKeys as string[]);
                  }
                  setBelongFunctions(checkedKeys);
                }}
              >
                {loopBelongTree(functionTree)}
              </Tree>
            ) : (
              <Empty />
            )
          }
          placement="bottomLeft"
          trigger="click"
        >
          <div>
            <IconFilter />
          </div>
        </Popover>
      </div>
      {tree.length ? (
        <div className={styles.tree} style={{ marginTop: 16, height: '100%' }}>
          <Tree
            blockNode
            onSelect={(selectedKeys, { node, ...props }) => {
              // 节点的浮窗菜单点击时会触发onSelect，不知道为什么。
              // 但是这个时候 selectedKeys.length === 0
              // 可以通过这个来判断是选中节点还是浮窗点击。
              if (selectedKeys.length) {
                const nodeForTSLint: any = node;
                if (nodeForTSLint.type === FolderTypes.RECORD) {
                  onViewTree(nodeForTSLint);
                }
              }
            }}
            expandedKeys={expandedKeys}
            autoExpandParent={autoExpandParent}
            onExpand={(keys) => {
              setExpandedKeys(keys);
              setAutoExpandParent(false);
            }}
          >
            {loop(tree)}
          </Tree>
        </div>
      ) : (
        <Empty style={{ marginTop: 56 }} />
      )}
      {visible && <CreateFolder visible={visible} onCancel={() => setVisible(false)} />}
    </div>
  );
};

export default FolderTree;
