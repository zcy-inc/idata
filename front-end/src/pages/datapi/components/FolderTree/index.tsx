import React, { Fragment, useEffect, useRef, useState } from 'react';
import { Dropdown, Input, Menu, Tabs, Tree } from 'antd';
import { useBoolean } from 'ahooks';
import { useModel } from 'umi';
import type { FC, ChangeEvent } from 'react';
import styles from '../../tablemanage/index.less';

import IconFont from '@/components/IconFont';

export type Key = string | number;
export interface FolderTreeProps {
  actions: {
    showFolder: () => void;
  };
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

  const { setCurPath, tree, getTree, onCreateEnum, onCreateTable, onViewTree, showLabel } =
    useModel('tabalmanage', (ret) => ({
      setCurPath: ret.setCurPath,
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
    <Menu
      onClick={(node) => {
        const key = node.key;
        switch (key) {
          case 'folder':
            actions.showFolder();
            break;
          case 'tag':
            showLabel();
            break;
          case 'enum':
            onCreateEnum();
            break;
          case 'table':
          default:
            onCreateTable();
            break;
        }
      }}
    >
      <Menu.Item key="folder">新建文件夹</Menu.Item>
      <Menu.Item key="tag">新建标签</Menu.Item>
      <Menu.Item key="enum">新建枚举</Menu.Item>
      <Menu.Item key="table">新建表</Menu.Item>
    </Menu>
  );

  // 组装数据，并高亮检索结果
  const loop = (data: any[], parentId?: string): any => {
    return data.map((_) => {
      const { name, type, cid } = _;
      const i = name.indexOf(searchValue);
      const node = { ..._, key: cid };
      let title = name;
      let _type = type;
      // 判断文件夹类型的节点是否展开
      if (type === 'FOLDER' && expandedKeys.indexOf(cid) > -1) {
        _type = 'FOLDEROPEN';
      }
      // 给检索命中的title加高亮
      if (i > -1) {
        const beforeStr = name.substring(0, i);
        const afterStr = name.substring(i + searchValue?.length);
        title = (
          <span key="title">
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

  // 生成检索用的平铺
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
            type="icon-tianjia"
            className={styles['icon-plus']}
            onClick={() => setCurPath(null)}
          />
        </Dropdown>
      </div>
      <Tabs defaultActiveKey="TABLE" onChange={(key) => getTree(key)}>
        <TabPane tab="表" key="TABLE" />
        <TabPane tab="标签" key="LABEL" />
        <TabPane tab="枚举" key="ENUM" />
      </Tabs>
      <Dropdown overlay={menu} placement="bottomLeft" trigger={['contextMenu']}>
        <Tree
          blockNode
          onExpand={(keys) => onExpand(keys)}
          expandedKeys={expandedKeys}
          autoExpandParent={autoExpandParent}
          treeData={loop(tree)}
          onRightClick={({ event, node }) => {
            const _: any = node;
            const folderId =
              _.type === 'FOLDER' ? _.cid?.split('_')[1] : _.parentId?.split('_')[1] || null;
            setCurPath(`${folderId}`);
          }}
          onSelect={(selectedKeys, { node, selectedNodes }) => {
            const _: any = node;
            _.type !== 'FOLDER' && onViewTree(node);
          }}
        />
      </Dropdown>
    </Fragment>
  );
};

export default FolderTree;
