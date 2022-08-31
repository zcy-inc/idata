import { DataSourceTypes, Environments } from '@/constants/datasource';

export interface DataSourceItem {
  id: number;
  type: DataSourceTypes;
  name: string;
  envList: string[];
  remark: string;
  dbConfigList: DBConfigList[];
  editor: string;
  editTime: string;
}

export interface ActualTableItem {
  id?: number;
  srcTable: {
    inputMode: 'S' | 'M';
    rawTable: string | undefined;
    tableIdxBegin?: string;
    tableIdxEnd?: string;
  };
  destTable: string | undefined;
  tableCdcPropList: {key: string; value: string} []
}

export interface CSVItem extends DataSourceItem {
  fileName: string;
}

export interface DBConfigList {
  env: Environments;
  dbName: string;
  username: string;
  password: string;
  host: string;
  port: number;
  schema: string;
}
