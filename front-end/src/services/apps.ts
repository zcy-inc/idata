import { request } from 'umi';
export interface IApp {
  id: string;
  appName: string;
  appFeatures?: Array<{
    featureCode: string;
    featureName: string;
  }>;
  featureCodes: string;
  editor?: Array<string>;
  description: string;
}

export interface IFeature {
  id: string;
  featureName: string;
  featureCode: string;
}

export async function findApps(params: PaginatedParams) {
  return request<{ 
    success: Boolean;
    msg: string;
    data: {
      total: number;
      content: Array<IApp>;
    }; }>('/api/p1/uac/apps', { params });
}

export async function add(data: IApp) {
  return request<{ 
    success: Boolean;
    msg: string;
    }>('/api/p1/uac/app', {
      method: 'POST',
      data,
    });
}

export async function update(data: IApp) {
  return request<{ 
    success: Boolean;
    msg: string;
    }>('/api/p1/uac/app', {
      method: 'PUT',
      data,
    });
}

export async function getFeaturesByCodes(params: {
  featureCodes: string;
}) {
  return request<{ 
    success: Boolean;
    msg: string;
    data: Array<IFeature>; 
  }>('/api/p1/sys/features', { params });
}