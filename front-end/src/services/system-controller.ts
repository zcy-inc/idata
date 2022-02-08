import { request } from 'umi';

import type { TConfigType, IConfigs, IConnection } from 'src/types/system-controller'
export const getConfigByType = (configType: TConfigType) => {
  return request<Tresponse<IConfigs[]>>('/api/p1/sys/configs', {
    params: { configType }
  });
}
export const editSystemConfig = (config: IConfigs[]) => {
  return request<Tresponse>('/api/p1/sys/config', {
    method: "PUT",
    data: config
  });
}
export const checkConfigConnection = (connection: IConnection) => {
  return request<Tresponse<boolean>>('/api/p1/sys/checkConnection', {
    method: "POST",
    data: connection
  });
}
