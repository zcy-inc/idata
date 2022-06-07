import React, { useMemo, useEffect, useState } from 'react';
import { Dropdown, Input, Menu, message, Tree, Modal, Popover, Empty, Tooltip } from 'antd';
import { useModel } from 'umi';
import type { FC, Key } from 'react';
import { formatTreeData, highlightText } from '@/utils/utils';
import { usePersistFn } from '@/hooks';
import styles from './index.less';
import { IconFont, TreeTitle } from '@/components';
import { Operation } from '@/components/TreeTitle';
import { deleteFolder, getFunctionTree, deleteTask, jobCopy } from '@/services/datadev';
import { TreeNode as Treenode } from '@/types/datadev';
import { FolderTypes, funcFolderIconMap, FolderBelong } from '@/constants/datadev';
import CreateFolder from './components/CreateFolder';
import IconCreate from './components/IconCreate';
import IconFilter from './components/IconFilter';

import IconBatch from './components/IconBatch';
import BatchOpetate from './components/BatchOpetate';
import showDialog from '@/utils/showDialog';
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
    // curNode,
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
    onRemovePane
  } = useModel('datadev', (_) => ({
    tree: _.tree,
    onRemovePane: _.onRemovePane,
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

  // 标签/枚举/表
  const onAction = (key: Key, node?: Treenode) => {
    setCurNode(node);
    switch (key) {
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
        setVisibleTask(true);
        break;
      case 'CreateDev':
        setVisibleDev(true);
        break;
      case 'CreateFun':
        onCreateFun();
        break;
    }
  };

  const onCreateFolder = (node: Treenode) => {
    setCurNode(node);
    setFolderMode('create');
    setVisible(true);
  };

  const onEditFolder = (node: Treenode) => {
    setCurNode(node);
    setFolderMode('edit');
    setVisible(true);
  };

  // 删除文件夹
  const onDeleteFolder = (node: Treenode) =>
    confirm({
      title: '您确定要删除该文件夹吗？',
      autoFocusButton: null,
      onOk: () => {
        if (node) {
          deleteFolder({ id: node.id }).then((res) => {
            if (res.success) {
              message.success('删除文件夹成功');
              getTreeWrapped();
            }
          });
        }
      },
    });

  const calcOperation = usePersistFn((belong: FolderBelong, type: FolderTypes, node: Treenode) => {
    let operations: Operation[] = [];
    switch (belong) {
      case FolderBelong.DESIGN:
      case FolderBelong.DEV:
        return [];
      case FolderBelong.DESIGNTABLE:
        operations.push({
          label: '新建表',
          key: 'CreateTable',
          onClick: () => onAction('CreateTable', node),
        });
        break;
      case FolderBelong.DAG:
        operations.push({
          label: '新建DAG',
          key: 'CreateDAG',
          onClick: () => onAction('CreateDAG', node),
        });
        break;
      case FolderBelong.DI:
        operations.push({
          label: '新建DI',
          key: 'CreateDI',
          onClick: () => onAction('CreateDI', node),
        });
        break;
      case FolderBelong.DEVJOB:
        operations.push({
          label: '新建作业',
          key: '新建作业',
          onClick: () => onAction('CreateDev', node),
        });
        break;
      case FolderBelong.DEVFUN:
        operations.push({
          label: '新建函数',
          key: '新建函数',
          onClick: () => onAction('CreateFun', node),
        });
        break;
    }
    operations.push({ type: 'divider' });
    switch (type) {
      case FolderTypes.FUNCTION:
        operations.push({
          label: '新建文件夹',
          key: '新建文件夹',
          onClick: () => onCreateFolder(node),
        });
        break;
      case FolderTypes.FOLDER:
        operations.push(
          {
            label: '新建文件夹',
            key: '新建文件夹',
            onClick: () => onCreateFolder(node),
          },
          {
            label: '编辑文件夹',
            key: '编辑文件夹',
            onClick: () => onEditFolder(node),
          },
          {
            label: '删除文件夹',
            key: '删除文件夹',
            onClick: () => onDeleteFolder(node),
          },
        );
        break;
      case FolderTypes.RECORD:
        if([FolderBelong.DAG, FolderBelong.DI, FolderBelong.DEVJOB, FolderBelong.DEV, FolderBelong.DEVFUN].includes(belong)) {
          operations = [{
            label: '删除',
            key: 'delete',
            onClick: () => {
              confirm({
                title: '删除任务',
                content: '您确认要删除该任务吗？',
                autoFocusButton: null,
                onOk: () =>
                  deleteTask({ id: node.id })
                    .then((res) => {
                      if (res.success) {
                        message.success('删除成功');
                        onRemovePane(node.cid);
                        getTreeWrapped();
                      }
                    })
                    .catch((err) => {}),
              });
            },
          },
          {
            label: '复制',
            key: 'copy',
            onClick: () => {
              jobCopy({
                jobIds: [node.id],
                destFolderId: node.parentId
              }).then(() => {
                message.success('复制成功！');
                getTreeWrapped();
              })
            },
          }];
        } else {
          operations = [];
        }
      
    }
    return operations;
  });

  const treeData = useMemo(
    () =>
      formatTreeData(tree, (node) => {
        const { name, parentId, type, cid, belong } = node;
        const titleText = (
          <span
            style={{ fontWeight: parentId ? 'normal' : 'bold' }}
            dangerouslySetInnerHTML={{ __html: highlightText({ text: name, keyWord }) }}
          ></span>
        );
        let title = null;
        // 给type不为FolderTypes.RECORD的节点加上icon
        switch (type) {
          case FolderTypes.FUNCTION:
            title = (
              <TreeTitle
                icon={funcFolderIconMap[belong]}
                text={titleText}
                operations={calcOperation?.(belong, type, node)}
              />
            );
            break;
          case FolderTypes.FOLDER:
            title = (
              <TreeTitle
                icon="icon-wenjianjia"
                text={titleText}
                operations={calcOperation?.(belong, type, node)}
              />
            );
            break;
          case FolderTypes.RECORD:
            title = <TreeTitle onClick={() => onViewTree(node)} text={titleText} operations={calcOperation?.(belong, type, node)} />;
            break;
        }
        return {
          ...node,
          key: cid,
          title,
        };
      }),
    [tree, keyWord, calcOperation],
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

  const showBatchOperate = () => {
    showDialog('批量处理', {
      hideFooter: true,
      formProps: {
        getTreeWrapped
      },
      modalProps: {
        width: 980,
        wrapClassName: styles['batch-operate-modal']
      }
    },BatchOpetate);
  }

  return (
    <div className="folder-tree">
      <div className={styles['operation-list']}>
        <Dropdown overlay={menu} placement="bottomLeft" trigger={['click']}>
          <Tooltip title="新建">
            <IconCreate onClick={() => setCurNode(null)} />
          </Tooltip>
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
          <div style={{display: 'inline'}}>
          <Tooltip title="过滤">
            <IconFilter />
            </Tooltip>
          </div>
        </Popover>
        <Tooltip title="批量处理">
          <IconBatch onClick={showBatchOperate} />
        </Tooltip>
      </div>
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
      </div>
      {tree?.length ? (
        <div className={styles.tree} style={{ marginTop: 16, height: '100%' }}>
          <Tree
            treeData={treeData}
            blockNode
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
