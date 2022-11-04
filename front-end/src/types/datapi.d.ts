import { TreeNodeType, LabelRequired, LabelTag, ERType } from '@/constants/datapi';

export interface TreeNodeOption {
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
  enumName?: string;
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
  tableName: string;
  columnName: string;
  enumValues: EnumValue[];
}
export interface ColumnLabel extends TableLable {
  columnType: string;
  columnIndex: number;
  columnComment: string;
  columnLabels: TableLable[];
  pk: boolean;
  enableCompare?: boolean;
  hiveDiff?: boolean;
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

export const HivechangeType = {
  1:'字段新增',
  2:'字段删除',
  3:'字段修改'
}

export interface HivechangeContentInfo {
  // id: number;
  columnNameBefore: string;
  changeDescription: string;
  columnNameAfter: string;
  changeType: HivechangeType;
}

/* ========== measure ========== */
export interface MetricModifier {
  modifierName: string;
  modifierCode: string;
  enumValues: string[];
  enumValueCodes: string[];
  modifierAttribute: LabelAttribute;
}
export interface Dimension {
  id: numebr;
  folderId?: numebr;
  labelName: string;
  labelTag: LabelTag;
  labelCode: string;
  labelAttributes: LabelAttribute[];
  measureLabels: TableLable[];
  specialAttribute: {
    degradeDim: boolean;
    aggregatorCode: string;
    complexMetricFormula: string;
    modifiers: MetricModifier[];
    atomicMetricCode: string;
    atomicMetricName: string;
  };
  enName?: string;
}
export interface Modifier extends Dimension {}
export interface Metric extends Dimension {
  dimensions: Dimension[];
  modifiers: MetricModifier[];
  deriveMetrics: {
    labelName: string;
    bizProcessValue: string;
  }[];
}
