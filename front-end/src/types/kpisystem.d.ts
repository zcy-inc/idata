export type TreeNodeType = 'DIMENSION_LABEL' | 'MODIFIER_LABEL' | 'METRIC_LABEL';

export interface FlatTreeNode {
  id: number;
  folderName: string;
}
export interface TreeNode {
  cid: string;
  parentId: number;
  name: string;
  type: TreeNodeType;
  folderId?: number;
  fileCode?: number;
  children?: TreeNode[];
}
export interface TargetDefine {}
export interface Dimension {
  targetDefine: TargetDefine;
  targetLabels: Label[];
}
