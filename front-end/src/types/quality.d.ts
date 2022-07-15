export interface MonitorItem {
  id: number;
  tableName: string;
  comment?: string;
  partitionExpr?: string;
}

export interface TableItem {
  tableName: string;
  partitioned: boolean;
}

export interface MonitorRuleItem {
  name: string;
}