import { request } from 'umi';

import type { TConfigType, IConfigs } from 'src/types/system-controller'
export const getConfigByType = (configType: TConfigType) => {
  return request<Tresponse<IConfigs[]>>('/api/p1/sys/configs', {
    params: { configType }
  });
}
export const editSystemConfig = (config: IConfigs[]) => {
  return request<Tresponse>('/api/p1/sys/configs', {
    method: "PUT",
    data: config
  });
}
export const checkConfigConnection = (config: IConfigs[]) => {
  return request<Tresponse>('/api/p1/sys/configs', {
    method: "PUT",
    data: config
  });
}
