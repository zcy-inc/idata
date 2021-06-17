import { TreeNodeType, LabelTag } from '@/constants/tablemanage';

export interface FLatTreeNode {
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
export interface Label {
  id: number;
  labelName: string;
  labelCode: string;
  labelParamType: string;
  labelParamValue: string;
  labelTag: LabelTag;
  enumNameOrValue?: string;
}
export interface EnumValue {
  id: number;
  valueCode: string;
  enumCode: string;
  enumValue: string;
  parentValue?: string;
  enumAttributes: [];
}
export interface EnumName {
  id: number;
  folderId?: number;
  enumCode: string;
  enumName: string;
  enumValues: EnumValue[];
}
export interface LabelAttribute {
  attributeKey: string;
  attributeType: string;
  attributeValue: string;
  enumNameOrValue?: string;
  enumValue?: string;
}
export interface Enum {
  id: number;
  folderId?: number;
  creator: string;
  enumName: string;
  enumCode: string;
  enumValues: EnumValue[];
}
