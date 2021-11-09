export type TConfigType = "LDAP" | "DS" | "HIVE_METASTORE" | "HADOOP" | "HTOOL" | "LIVY";

export interface IConfigs{
  id: string;
  valueOne: Record<string,any>
}
export interface IDataSourceType extends IConfigItem {
  configValueKey: string;
};

export  interface IConfigItem{
  configValue?: string;
  configValueRemarks?: string;
}
