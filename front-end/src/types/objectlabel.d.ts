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
/* ========== ObjectLabel ========== */
export interface DimensionDef {
  dimensionCode: string;
  params: string[];
}
export interface IndicatorDef {
  indicatorCode: string;
  condition: IndicatorCondition;
  params: number[];
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
export interface ObjectLabel {
  id: number;
  folderId: number;
  name: string;
  nameEn: string;
  objectType: string;
  remark: string;
  editor: string;
  editTime: string;
  ruleLayers: RuleLayer[];
}
