import React, { useMemo, useEffect, useState } from 'react';
import { Dropdown, Input, Menu, message, Tree, Modal, Popover, Empty } from 'antd';
import { useModel } from 'umi';
import type { FC, Key } from 'react';
import { formatTreeData, highlightText } from '@/utils/utils';
import styles from './index.less';

import IconFont from '@/components/IconFont';
import { deleteFolder, getFunctionTree } from '@/services/datadev';
import { TreeNode as Treenode } from '@/types/datadev';
import { FolderTypes } from '@/constants/datadev';

import CreateFolder from './components/CreateFolder';
import TreeNodeTitle from './components/TreeNodeTitle';
import IconCreate from './components/IconCreate';
import IconFilter from './components/IconFilter';

const { confirm } = Modal;

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
    onCreateFun,
    setVisibleTask,
    setVisibleDev,
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
    onCreateFun: _.onCreateFun,
    setVisibleTask: _.setVisibleTask,
    setVisibleDev: _.setVisibleDev,
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
      <Menu.Item key="CreateDI">
        <IconFont style={{ marginRight: 8 }} type="icon-xinjianDI" />
        新建DI
      </Menu.Item>
      <Menu.Item key="CreateDev">
        <IconFont style={{ marginRight: 8 }} type="icon-xinjianzuoye" />
        新建作业
      </Menu.Item>
      <Menu.Item key="CreateFun">
        <IconFont style={{ marginRight: 8 }} type="icon-xinjianhanshu" />
        新建函数
      </Menu.Item>
    </Menu>
  );

  // 新建文件夹/标签/枚举/表
  const onAction = (key: Key, node?: Treenode) => {
    switch (key) {
      case 'CreateFolder':
        setFolderMode('create');
        setVisible(true);
        break;
      case 'EditFolder':
        setFolderMode('edit');
        setVisible(true);
        break;
      case 'DeleteFolder':
        onDeleteFolder();
        break;
      case 'CreateTable':
        onCreateTable();
        break;
      case 'CreateLabel':
        setCurLabel(-1);
        showLabel();
        break;
      case 'CreateEnum':
        onCreateEnum();
        break;
      case 'CreateDAG':
        onCreateDAG();
        break;
      case 'CreateDI':
        setCurNode(node);
        setVisibleTask(true);
        break;
      case 'CreateDev':
        setCurNode(node);
        setVisibleDev(true);
        break;
      case 'CreateFun':
        onCreateFun();
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

  const treeData = useMemo(
    () =>
      formatTreeData(tree, (node) => {
        const { name, parentId, type, cid } = node;
        const titleText = (
          <span
            style={{ fontWeight: parentId ? 'normal' : 'bold' }}
            dangerouslySetInnerHTML={{ __html: highlightText({ text: name, keyWord }) }}
          ></span>
        );
        let title = null;
        // 给type不为FolderTypes.RECORD的节点加上icon
        if (type === FolderTypes.RECORD) {
          title = titleText;
        } else {
          title = <TreeNodeTitle node={node} title={titleText} onAction={onAction} />;
        }
        return {
          ...node,
          key: cid,
          title,
        };
      }),
    [tree, keyWord],
  );

  // 功能性文件树
  const functiontreeData = formatTreeData(functionTree, (node) => ({
    ...node,
    title: node.name,
    key: node.belong,
  }));

  // 获取父节点的key
  const getParentKey = (key: string, tree: Treenode[] = []): string => {
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
            functionTree?.length ? (
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
                treeData={functiontreeData}
              />
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
      {tree?.length ? (
        <div className={styles.tree} style={{ marginTop: 16, height: '100%' }}>
          <Tree
            blockNode
            treeData={treeData}
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
          />
        </div>
      ) : (
        <Empty style={{ marginTop: 56 }} />
      )}
      {visible && <CreateFolder visible={visible} onCancel={() => setVisible(false)} />}
    </div>
  );
};

export default FolderTree;
