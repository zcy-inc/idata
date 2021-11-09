import { request } from 'umi';

import type { TConfigType, IConfigs } from 'src/types/system-controller'
export const getConfigByType = (configType: TConfigType) => {
  return request<Tresponse<IConfigs[]>>('/api/p1/sys/configs', {
    params: { configType }
  });
}
