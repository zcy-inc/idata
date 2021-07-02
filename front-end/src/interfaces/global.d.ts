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
  type: folderTreeNodeType.F_MENU;
  children: FolderTreeNode[];
}

export interface FolderNode extends TreeNode {
  filePermission: number; // 0~7的数字
  folderId: string;
  name: string;
  type: folderTreeNodeType;
  children: FolderNode[];
}

export type FolderTreeNode = MenuNode | FolderNode;

export type FolderDataNode = (MenuNode & DataNode) | (FolderNode & DataNode);
