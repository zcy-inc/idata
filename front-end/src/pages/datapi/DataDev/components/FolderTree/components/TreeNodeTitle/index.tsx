import React, { useState } from 'react';
import { IconFont } from '@/components';
import type { FC } from 'react';
import { FolderBelong, FolderTypes } from '@/constants/datadev';
import { TreeNode } from '@/types/datadev';
import { Dropdown, Menu } from 'antd';

interface TreeNodeTitleProps {
  node: TreeNode;
  title: any;
  onAction: (key: string, node?: TreeNode) => void;
}

const MapFolderIcon = {
  [FolderBelong.DESIGN]: 'icon-shucangsheji',
  [FolderBelong.DESIGNTABLE]: 'icon-biao1',
  [FolderBelong.DESIGNLABEL]: 'icon-biaoqian',
  [FolderBelong.DESIGNENUM]: 'icon-meijuleixing',
  [FolderBelong.DAG]: 'icon-DAG',
  [FolderBelong.DI]: 'icon-shujujicheng',
  [FolderBelong.DEV]: 'icon-shujukaifa',
  [FolderBelong.DEVJOB]: 'icon-zuoye',
  [FolderTypes.FOLDER]: 'icon-wenjianjia',
};

const TreeNodeTitle: FC<TreeNodeTitleProps> = ({ node, title, onAction }) => {
  const [isHover, setIsHover] = useState(false);

  const renderPrimaryMenu = () => {
    switch (node.belong) {
      case FolderBelong.DESIGNTABLE:
        return <Menu.Item key="CreateTable">新建表</Menu.Item>;
      case FolderBelong.DESIGNLABEL:
        return <Menu.Item key="CreateLabel">新建标签</Menu.Item>;
      case FolderBelong.DESIGNENUM:
        return <Menu.Item key="CreateEnum">新建枚举</Menu.Item>;
      case FolderBelong.DAG:
        return <Menu.Item key="CreateDAG">新建DAG</Menu.Item>;
      case FolderBelong.DI:
        return <Menu.Item key="CreateDI">新建数据集成</Menu.Item>;
      case FolderBelong.DEVJOB:
        return <Menu.Item key="CreateJob">新建任务</Menu.Item>;
      default:
        return null;
    }
  };

  const renderDropdownMenu = () => {
    switch (node.type) {
      case FolderTypes.FUNCTION:
        return (
          <Menu onClick={({ key }) => onAction(key, node)}>
            {renderPrimaryMenu()}
            <Menu.Divider />
            <Menu.Item key="CreateFolder">新建文件夹</Menu.Item>
          </Menu>
        );
      case FolderTypes.FOLDER:
        return (
          <Menu onClick={({ key }) => onAction(key, node)}>
            {renderPrimaryMenu()}
            <Menu.Divider />
            <Menu.Item key="CreateFolder">新建文件夹</Menu.Item>
            <Menu.Item key="EditFolder">编辑文件夹</Menu.Item>
            <Menu.Item key="DeleteFolder">删除文件夹</Menu.Item>
          </Menu>
        );
      default:
        return <Menu />;
    }
  };

  const renderIcon = () => {
    // 在hover状态下、不是根目录节点、不是文件节点，这三个条件下显示图标
    if (
      isHover &&
      node.belong !== FolderBelong.DESIGN &&
      node.belong !== FolderBelong.DI &&
      node.belong !== FolderBelong.DEV &&
      node.type !== FolderTypes.RECORD
    ) {
      return (
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <div>
            <IconFont
              type={
                MapFolderIcon[node.type === FolderTypes.FOLDER ? FolderTypes.FOLDER : node.belong]
              }
            />
            {title}
          </div>
          <Dropdown overlay={renderDropdownMenu()} placement="bottomLeft" trigger={['click']}>
            <IconFont type="icon-gengduo2" />
          </Dropdown>
        </div>
      );
    } else {
      return (
        <>
          <IconFont
            type={
              MapFolderIcon[node.type === FolderTypes.FOLDER ? FolderTypes.FOLDER : node.belong]
            }
          />
          {title}
        </>
      );
    }
  };

  return (
    <div onMouseEnter={() => setIsHover(true)} onMouseLeave={() => setIsHover(false)}>
      {renderIcon()}
    </div>
  );
};

export default TreeNodeTitle;
