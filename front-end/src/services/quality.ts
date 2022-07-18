import type {
  MonitorItem,
  TableItem,
  MonitorRuleItem,
  TemplateItem,
  MonitorObj,
  RuleType,
  Category,
  OutputType,
  BaselineItem
} from '@/types/quality';
import { request } from 'umi';
import type { DefaultResponse } from './global';

/* ========== 监控规则 ========== */
/**
 * 获取监控列表
 */
export async function getMonitorList(data: {tableName?: string; alarmLevel?: number; curPage: number; pageSize: number; baselineId: number}) {
  return request<DefaultResponse & { data: {data: MonitorItem [], totalElements: number} }>('/api/monitorTable/getByPage', {
    method: 'POST',
    data
  });
}

/**
 * 添加监控规则
 */
 export async function addMonitor(data: {tableName: string; partitionExpr?: string; baselineId: number}) {
  return request<DefaultResponse & { data: MonitorItem }>('/api/monitorTable/add', {
    method: 'POST',
    data
  });
}

/**
 * 删除监控规则
 */
 export async function deleteMonitor(data: {id: number;  isBaseline: boolean}) {
  return request<DefaultResponse & { data: MonitorItem }>(`/api/monitorTable/del/${data.id}/${data.isBaseline}`, {
    method: 'GET',
  });
}

/**
 * 获取所有表
 */
 export async function getTables(params?: {tableName?: string; editTable?: string}) {
  return request<DefaultResponse & { data: TableItem [] }>('/api/getTables', {
    method: 'GET',
    params
  });
}

/**
 * 获取单个监控下的监控规则列表
 */
 export async function getMonitorRules(data: {tableName?: string; curPage: number; pageSize: number; baselineId: number}) {
  return request<DefaultResponse & { data: {data: MonitorRuleItem [], totalElements: number} }>('/api/monitorRule/getByPage', {
    method: 'POST',
    data
  });
}

/**
 * 获取监控表信息
 */
 export async function getMonitorInfo(params: {id?: string; }) {
  return request<DefaultResponse & { data: MonitorItem }>(`/api/monitorTable/get/${params.id}`, {
    method: 'GET',
  });
}

/**
 * 修改监控表信息
 */
 export async function editMonitorInfo(data: {id: string;baselineId: number; partitionExpr: string; }) {
  return request<DefaultResponse>('/api/monitorTable/update', {
    method: 'POST',
    data
  });
}
/**
 * 添加监控规则
 */
 export async function addMonitorRule(data: MonitorRuleItem) {
  return request<DefaultResponse>('/api/monitorRule/add', {
    method: 'POST',
    data
  });
}

/**
 * 　修改监控规则
 */
 export async function updateMonitorRule(data: MonitorRuleItem) {
  return request<DefaultResponse>('/api/monitorRule/update', {
    method: 'POST',
    data
  });
}


/* ========== 规则模板库 ========== */
/**
 *  获取模板列表
 */
 export async function getTemplateList(data: {curPage: number, pageSize: number, monitorObj?: MonitorObj, type?: RuleType}) {
  return request<DefaultResponse & { data: {data: TemplateItem [], totalElements: number} } >('/api/monitorTemplate/getByPage', {
    method: 'POST',
    data
  });
}

/**
 *  增加模板
 */
 export async function addTemplate(
  data: {name: string, category: Category, monitorObj: MonitorObj, type: RuleType,content: string; outputType: OutputType}
) {
  return request<DefaultResponse & { data: TemplateItem } >('/api/monitorTemplate/add', {
    method: 'POST',
    data
  });
}

/**
 *  修改模板
 */
 export async function updateTemplate(
  data: {id?: number; name: string, category: Category, monitorObj: MonitorObj, type: RuleType,content: string; outputType: OutputType}
) {
  return request<DefaultResponse & { data: TemplateItem } >('/api/monitorTemplate/update', {
    method: 'POST',
    data
  });
}

/**
 *  获取模板
 */
 export async function getTemplate(params: {id: number;}) {
  return request<DefaultResponse & { data: TemplateItem } >(`/api/monitorTemplate/get/${params.id}`, {
    method: 'GET',
  });
}

/**
 *  删除模板
 */
 export async function removeTemplate(params: {id: number;}) {
  return request<DefaultResponse & { data: TemplateItem } >(`/api/monitorTemplate/del/${params.id}`, {
    method: 'GET',
  });
}

/**
 *  开启、停止模板
 */
 export async function toggleTemplate(params: {id: number;status: number;}) {
  return request<DefaultResponse & { data: TemplateItem } >(`/api/monitorTemplate/setStatus/${params.id}/${params.status}`, {
    method: 'GET',
  });
}

/* ========== 基线管理 ========== */
/**
 *  获取模板列表
 */
 export async function getBaselineList(data: {curPage: number; pageSize: number; name: string;}) {
  return request<DefaultResponse & { data: {data: BaselineItem [], totalElements: number} } >('/api/monitorBaseline/getByPage', {
    method: 'POST',
    data
  });
}

/**
 *  新增基线
 */
 export async function addBaseline(data: { name: string;}) {
  return request<DefaultResponse & { data: BaselineItem [] } >('/api/monitorBaseline/add', {
    method: 'POST',
    data
  });
}

/**
 *  更新基线
 */
 export async function updateBaseline(data: { name: string; id: number;}) {
  return request<DefaultResponse & { data: BaselineItem [] } >('/api/monitorBaseline/update', {
    method: 'POST',
    data
  });
}

/**
 *  获取基线信息
 */
 export async function getBaseline(data: { id: string;}) {
  return request<DefaultResponse & { data: BaselineItem } >(`/api/monitorBaseline/get/${data.id}`, {
    method: 'GET'
  });
}

/**
 *  删除基线
 */
 export async function deleteBaseline(data: { id: number;}) {
  return request<DefaultResponse & { data: BaselineItem } >(`/api/monitorBaseline/del/${data.id}`, {
    method: 'GET'
  });
}

/**
 *  卡其、停止基线
 */
 export async function toggleBaseline(data: { id: number; status: number;}) {
  return request<DefaultResponse & { data: BaselineItem} >(`/api/monitorBaseline/setStatus/${data.id}/${data.status}`, {
    method: 'GET'
  });
}