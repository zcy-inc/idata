import type { IDataSourceType, IConfigItem, IConnection } from 'src/types/system-controller'
import { v4 as uuidv4 } from 'uuid';
export function dataToList(data: Record<string, IConfigItem>) {
  return Object.keys(data).map(configValueKey => {
    const { configValue, configValueRemarks } = data[configValueKey];
    return {
      id:uuidv4(),
      configValueKey,
      configValue,
      configValueRemarks
    }
  })
}
function listReducer(config: Record<string, IConfigItem>, item: IDataSourceType) {
  if(item.configValueKey){
    config[item.configValueKey] = {
      configValue: item.configValue,
      configValueRemarks: item.configValueRemarks,
    }
  }
  return config;
}
export function listToData(list: IDataSourceType[]) {
  return list.reduce(listReducer, {})
}
export function configToConnection(type: IConnection["connectionType"], config: Record<string, IConfigItem>) {
  return {
    connectionType: type,
    connectionUri: config?.url?.configValue,
    token: config?.token?.configValue,
    username: config?.username?.configValue,
    password: config?.password?.configValue
  }
}
