export type TConfigType = "LDAP" | "DS" | "HIVE_METASTORE" | "HADOOP" | "HTOOL" | "LIVY";

export interface IConfigs{
  id: string;
  type: string;
  valueOne: Record<string,any>
}
export interface IDataSourceType extends IConfigItem {
  configValueKey?: string;
};

export  interface IConfigItem{
  id?: React.Key
  configValue?: string;
  configValueRemarks?: string;
}

export interface IConnection{
    connectionType: TConfigType
    connectionUri?: string
    token?: string
    username?: string
    password?: string
}
