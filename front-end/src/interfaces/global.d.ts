import type { folderTreeNodeType } from '@/constants/common';
import { DataNode } from 'antd/es/tree';

export interface TreeNode {
  children?: TreeItem[];
  [index: string]: any;
}

export interface FeatureTreeNode extends TreeNode {
  name: string;
  featureCode: string;
  enable?: boolean;
  children?: FeatureTreeNode[];
}

export interface MenuNode extends TreeNode {
  featureCode: string;
  name: string;
  type: folderTreeNodeType.MENU;
  children: FolderTreeNode[];
}

export interface FolderNode extends TreeNode {
  filePermission: string; //二进制数字位表示：001 读，010 写，100 删，可以组合加和；十进制读写
  folderId: string;
  name: string;
  type: folderTreeNodeType.FOLDER;
  children: FolderNode[];
}

export type FolderTreeNode = MenuNode | FolderNode;

export type FolderDataNode = (MenuNode & DataNode) | (FolderNode & DataNode);
