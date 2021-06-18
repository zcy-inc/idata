import { TreeNodeType, LabelRequired, LabelTag, ERType } from '@/constants/tablemanage';

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
export interface Label {
  id: number;
  labelName: string;
  labelCode: string;
  labelParamType: string;
  labelParamValue: string;
  labelTag: LabelTag;
  enumNameOrValue?: string;
  labelRequired: LabelRequired;
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

export interface TableLable extends Label {
  tableId: number;
  columnName: string;
  enumValues: EnumValue[];
}
export interface ColumnLabel extends TableLable {
  columnType: string;
  columnIndex: number;
  columnComment: string;
  columnLabels: TableLable[];
  pk: boolean;
}
export interface ForeignKey {
  id: number;
  tableId: number;
  tableName: string;
  columnNames: string;
  referDbName: string;
  referTableName: string;
  referTableId: number;
  referColumnNames: string;
  erType: ERType;
}
export interface User {
  id: number;
  username: string;
  nickname: string;
  realName: string;
}
export interface Table {
  id: number;
  folderId?: number;
  creator: string;
  tableName: string;
  tableLabels: TableLable[];
  columnInfos: ColumnLabel[];
  foreignKeys: ForeignKey[];
}
