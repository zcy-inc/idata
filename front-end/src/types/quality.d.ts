import { Moment } from 'moment';

export interface MonitorItem {
  id: number;
  tableName: string;
  comment?: string;
  partitionExpr?: string;
}

export interface TableItem {
  id: number;
  tableName: string | undefined;
  partitioned: boolean;
  partitionExpr: string;
  showEdit: boolean;
}

export type MonitorObj = 'table' | 'field';
export type RuleType = 'system' | 'template' | 'custom';
export type AlarmLevel = 1 | 2 | 3;
export type Category = 'timely' | 'accuracy' | 'integrity';
export type OutputType = 1 | 2;
export type Status = 0 | 1;
export interface MonitorRuleItem {
  id: number;
  baselineId: number;
  name: string;
  monitorObj: MonitorObj;
  templateId: number;
  status: Status;
  content?: string | Moment;
  tableName: string;
  ruleType: RuleType;
  rangeStart?: number;
  rangeEnd?: number;
  alarmLevel: AlarmLevel;
  outputType?: number;
  alarmReceivers: string;
}

export interface TemplateItem {
  id: number;
  type: RuleType;
  tableName: string;
  name: string;
  content: string;
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
  creator?: string;
}

export interface LogItem {
  createTime: string;
  comment: string;
  tableName: string;
  ruleName: string;
  alarm: number;
  ruleType: RuleType;
  ruleValue: string;
  dataValue: number | string;
  alarmLevel?: AlarmLevel
  content?: string;
  rangeStart?: string;
  rangeEnd?: string;
  fixValue?: string;
  compareType?: string;
}

export interface BaselineItem {
  id: number;
  name: string;
  status: Status;
  creator: string;
  createTime: string;
  ruleCount: number;
  tableCount: number;
}