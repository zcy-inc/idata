export type TConfigType = "LDAP" | "DS" | "HIVE_METASTORE" | "HADOOP" | "HTOOL" | "LIVY";

export interface IConfigs{
  valueOne: Record<string,any>
}
export type TDataSourceType = {
  configValueKey: string;
  configValue?: string;
  configValueRemarks?: string;
};
