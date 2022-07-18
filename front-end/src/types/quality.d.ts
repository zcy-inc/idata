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

export type MonitorObj = 'table' | 'field';
export type RuleType = 'system' | 'template' | 'custom';
export type AlarmLevel = 1 | 2 | 3;
export type Category = 'timely' | 'accuracy' | 'integrity';
export type OutputType = 1 | 2;
export type Status = 0 | 1;
export interface MonitorRuleItem {
  id?: number;
  baselineId: number;
  name: string;
  monitorObj: MonitorObj;
  templateId: 2 | -1;
  tableName: string;
  ruleType: RuleType;
  alarmLevel: AlarmLevel;
  alarmReceivers: string;
}

export interface TemplateItem {
  id?: number;
  type: RuleType;
  tableName: string;
  name: string;
  ruleType: RuleType;
  category: Category;
  status: Status;
  outputType: OutputType;
  templateId: 2 | -1;
  monitorObj: MonitorObj;
  alarmLevel: AlarmLevel;
  alarmReceivers: string;
  content?: string;
  rangeStart?: number;
  rangeEnd?: number;
}