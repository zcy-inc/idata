import { ColumnLabel } from './datapi';

export interface DefaultResponse {
  success: boolean;
  code: string;
  msg: string;
  causeMsg: string;
}

export interface IMenuItem {
  name: string;
  enable: boolean;
  featureCode: string;
  children: IMenuItem[];
}

export interface IAutocompletionTipConfigs {
  basicAutocompletionTips: string[];
  dbTableNames: string[];
  columns: IAutocompletionTipConfigsColumn[];
}

export interface IAutocompletionTipConfigsColumn {
  id: number;
  pk: boolean;
  dbName: string;
  tableId: number;
  tableName: string;
  columnName: string;
  columnType: string;
  columnIndex: number;
  columnDescription: string;
  columnComment: string;

  securityLevel: string;
  partitionedColumn: string;
  columnLabels: ColumnLabel[];
}

export interface GlobalEnvironment {
  id: number;
  name: string; // 工作空间名称
  code: string; // 工作空间code
  urlPath: string; // 工作空间url path前缀
}
