import { request } from 'umi';

import type { TSubjectType, ILabelDefines } from 'src/types/labelController'
export const findDefines = (subjectType: TSubjectType) => {
  return request<Tresponse<ILabelDefines[]>>('/api/p1/dev/labelDefines', {
    params: {
      subjectType
    }
  });
}
export const defineLabel = (label: ILabelDefines) => {
  return request<Tresponse<ILabelDefines[]>>('/api/p1/dev/labelDefine', {
    method: 'POST',
    data: label
  });
}
