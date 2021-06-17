import { Label, LabelTag } from '@tablemanage/type';

export interface TableLabel extends Label {
  tableId: number;
}
export interface ColumnLabel extends Label {
  tableId: number;
  columnName: string;
}
export interface ColumnInfo {
  id: number;
  tableId: number;
  columnIndex: number;
  columnName: string;
  columnLabels: ColumnLabel[];
}
export enum ERType {
  I2I = 'I2I',
  I2N = 'I2N',
  N2I = 'N2I',
  M2N = 'M2N',
}
export interface ForeignKey {
  id: number;
  tableId: number;
  columnName: string;
  referDbName: string;
  referTableName: string;
  referTableId: number;
  referColumnNames: string;
  erType: ERType;
}
export interface TableInfo {
  id: number;
  folderId?: number;
  tableName: string;
  tableLabels: TableLabel[];
  columnInfos: ColumnInfo[];
  foreignKeys: ForeignKey[];
}
