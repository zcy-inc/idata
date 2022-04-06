import React, { useEffect, useRef, useState } from 'react';
import { Dropdown, Input, Menu, message, Tree, Modal } from 'antd';
import { debounce } from 'lodash';
import type { FC, ChangeEvent, Key } from 'react';

import IconFont from '@/components/IconFont';
import CreateFolder from './components/CreateFolder';

import { deleteFolder, getFolderTree } from '@/services/measure';
import { TreeNodeType } from '@/constants/datapi';
import { TreeNode as ITreeNode } from '@/types/datapi';
import showDialog from '@/utils/showDialog';
export interface FolderTreeProps {
  onChange: (node: any) => void
}
interface SearchTreeNode {
  key: string;
  name: string;
  parentId?: string;
}

const { TreeNode } = Tree;
const { confirm } = Modal;
const NodeTypeIcon = {
  FOLDER: <IconFont type="icon-wenjianjia" key="folder" />,
  FOLDEROPEN: <IconFont type="icon-wenjianjiazhankai" key="folderopen" />,
  LABEL: <IconFont type="icon-biaoqian1" key="dimension" />,
  ENUM: <IconFont type="icon-meiju" key="modifier" />,
  TABLE: <IconFont type="icon-biao" key="metric" />,
};

const FolderTree: FC<FolderTreeProps> = ({onChange}) => {
  const [expandedKeys, setExpandedKeys] = useState<Key[]>([]);
  const [autoExpandParent, setAutoExpandParent] = useState(true);
  const flatTree = useRef<SearchTreeNode[]>([]);
  const [tree, setTree] = useState([]);
  const [curNode, setCurNode] = useState<any>(null);
  const [selectedKeys, setSelectedKeys] = useState<React.Key []>([]);

  useEffect(() => {
    getTreeData();
  }, []);

  useEffect(() => {
    flat(tree);
  }, [tree]);

  const treeMenu = (
    <Menu onClick={({ key }) => onMenuActions(key)}>
      <Menu.Divider />
      <Menu.Item key="folder">新建文件夹</Menu.Item>
      <Menu.Item key="edit">编辑文件夹</Menu.Item>
      <Menu.Item key="delete">删除文件夹</Menu.Item>
    </Menu>
  );

  const getTreeData = (treeNodeName?: string) => {
    getFolderTree({ devTreeType: TreeNodeType.METRIC_LABEL, treeNodeName }).then((res) => {
      setTree(res.data);
      if(res.data.length) {
        setSelectedKeys([res.data[0].cid]);
      }
    });
  }

  const editTreeItem = (node = { folderId: ''}) => {
    const isEdit = !!node.folderId;
    showDialog(`${isEdit ? '编辑文件夹' : '新建文件夹'}`, {
      modalProps: {
        width: 540
      },
      formProps: {
        node
      },
      beforeConfirm: (dialog, form, done) => {
        dialog.showLoading();
        form.handleSubmit().then(() => {
          message.success(`文件夹${isEdit ? '编辑': '新建'}成功`);
          getTreeData();
          done();
        }).finally(() => {
          dialog.hideLoading();
        })
      },
    }, CreateFolder)
  }

  // 新建文件夹/维度/修饰词/指标
  const onMenuActions = (key: Key) => {
    switch (key) {
      case 'folder':
        editTreeItem();
        break;
      case 'edit':
        editTreeItem(curNode);
        break;
      case 'delete':
        onDeleteFolder();
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
              getTreeData();
            }
          })
          .catch((err) => {}),
  });

  // 组装数据，并高亮检索结果
  const loop = (data: ITreeNode[], parentId?: string): any => {
    const n = data.length;
    return data.map((_, i) => {
      const { name, type, cid } = _;
      const node: any = { ..._, key: cid };
      const clsFolderRoot = (!parentId && type === 'FOLDER' && 'folder-root') || '';
      const clsFolderMargin = ((type === 'FOLDER' || i === n - 1) && 'folder-margin') || '';
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
      node.className = clsFolderMargin;
      node.title = [NodeTypeIcon[_type], title];
      parentId && (node.parentId = parentId);

      return _.children ? (
        <TreeNode key={_.folderId} {...node}>{loop(_.children, cid)}</TreeNode>
      ) : (
        <TreeNode key={_.folderId} {...node} />
      );
    });
  };

  // 将树平铺用以检索
  const flat = (data: any[], parentId?: string) => {
    for (let i = 0; i < data.length; i++) {
      const node = data[i];
      const { name, cid } = node;
      // flatTree暂时不知道做什么用途
      flatTree.current.push({ name, key: cid, parentId: parentId });
      if (node.children) {
        flat(node.children, node.cid);
      }
    }
  };

  // 检索树
  const onFilterTree = ({ target: { value } }: ChangeEvent<HTMLInputElement>) => {
    getTreeData(value);
  };

  const deFilterTree = debounce(onFilterTree, 500);

  const onExpand = (keys: Key[] = []) => {
    setExpandedKeys(keys);
    setAutoExpandParent(false);
  };

  const handleSelect = (selectedKes: React.Key[], info: any) => {
    console.log('onChange',info);
    setSelectedKeys(selectedKes);
    if(info.node.props.type === 'FOLDER') {
      onChange && onChange(info.node.props);
    }
  }
  return (
    <div className="folder-tree">
      <div className="search">
        <Input
          className="search-input"
          placeholder="请输入关键字进行搜索"
          prefix={<IconFont type="icon-sousuo" />}
          onChange={deFilterTree}
        />
        <IconFont
          type="icon-xinjian1"
          className="icon-plus"
          onClick={() => editTreeItem()}
        />
      </div>
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
          selectedKeys={selectedKeys}
          onSelect={handleSelect}
        >
          {loop(tree)}
        </Tree>
      </Dropdown>
    </div>
  );
};

export default FolderTree;
