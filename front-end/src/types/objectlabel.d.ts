export interface TreeNodeOption {
  id: number;
  folderName: string;
}
export interface TreeNode {
  cid: string;
  originId: number;
  parentId: number;
  name: string;
  type: TreeNodeType;
  folderId?: number;
  id?: number;
  children?: TreeNode[];
}
/* ========== ObjectLabel ========== */
export interface DimensionDef {
  dimensionCode: string | null;
  params: string[];
  dimensionName?: string;
}
export interface IndicatorDef {
  indicatorCode: string | null;
  condition: IndicatorCondition;
  params: number[] | string[];
  indicatorName?: string;
}
export interface Rule {
  ruleId: number;
  ruleName: string;
  dimensionDefs: DimensionDef[];
  indicatorDefs: IndicatorDef[];
}
export interface RuleDef {
  connector?: 'and' | 'or';
  rules: Rule[];
}
export interface RuleLayer {
  layerId: number;
  layerName: string;
  ruleDef: RuleDef;
}

export type ObjectType = 'supplier:LABEL' | 'purchaserOrg:LABEL';

export interface ObjectLabel {
  id: number;
  originId: number;
  folderId: number;
  name: string;
  nameEn: string;
  objectType: ObjectType;
  remark: string;
  editor: string;
  editTime: string;
  ruleLayers: RuleLayer[];
}
