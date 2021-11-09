import type { IDataSourceType, IConfigItem, IConnection } from 'src/types/system-controller'
export function dataToList(data: Record<string, IConfigItem>) {
  return Object.keys(data).map(configValueKey => {
    const { configValue, configValueRemarks } = data[configValueKey];
    return {
      configValueKey,
      configValue,
      configValueRemarks
    }
  })
}

function listReducer(config: Record<string, IConfigItem>, item: IDataSourceType) {
  config[item.configValueKey] = {
    configValue: item.configValue,
    configValueRemarks: item.configValueRemarks,
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
