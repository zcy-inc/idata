export interface RuleLayer {
  layerId: number;
  layerName: string;
  ruleDef: [];
}

export interface ObjectLabel {
  id: number;
  folderId: number;
  version: number;
  name: string;
  nameEn: string;
  objectType: string;
  remark: string;
  ruleLayers: RuleLayer[];
}
