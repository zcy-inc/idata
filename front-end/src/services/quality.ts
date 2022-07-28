import type {
  MonitorItem,
  TableItem,
  MonitorRuleItem,
  TemplateItem,
  MonitorObj,
  RuleType,
  Category,
  OutputType,
  BaselineItem,
  LogItem
} from '@/types/quality';
import { request } from 'umi';
import type { DefaultResponse } from './global';

/* ========== 监控规则 ========== */
/**
 * 获取监控列表
 */
export async function getMonitorList(data: {tableName?: string; latestAlarmLevel?: number; curPage: number; pageSize: number; baselineId: number}) {
  return request<DefaultResponse & { data: {data: MonitorItem [], totalElements: number} }>('/api/dqc/monitorTable/getByPage', {
    method: 'POST',
    data
  });
}

/**
 * 添加监控规则
 */
 export async function addMonitor(data: {tableName: string | undefined; partitionExpr?: string; baselineId: number}) {
  return request<DefaultResponse & { data: MonitorItem }>('/api/dqc/monitorTable/add', {
    method: 'POST',
    data
  });
}

/**
 * 删除监控规则
 */
 export async function deleteMonitor(data: {id: number;  isBaseline: boolean}) {
  return request<DefaultResponse & { data: MonitorItem }>(`/api/dqc/monitorTable/del/${data.id}/${data.isBaseline}`, {
    method: 'GET',
  });
}

/**
 * 获取所有表
 */
 export async function getTables(params?: {tableName?: string; editTable?: string}) {
  return request<DefaultResponse & { data: TableItem [] }>('/api/dqc/getTables', {
    method: 'GET',
    params
  });
}

/**
 * 获取单个监控下的监控规则列表
 */
 export async function getMonitorRules(data: {tableName?: string; curPage: number; pageSize: number; baselineId: number}) {
  return request<DefaultResponse & { data: {data: MonitorRuleItem [], totalElements: number} }>('/api/dqc/monitorRule/getByPage', {
    method: 'POST',
    data
  });
}

/**
 * 获取监控表信息
 */
 export async function getMonitorInfo(params: {id?: string; }) {
  return request<DefaultResponse & { data: MonitorItem }>(`/api/dqc/monitorTable/get/${params.id}`, {
    method: 'GET',
  });
}

/**
 * 修改监控表信息
 */
 export async function editMonitorInfo(data: {id: string | number;baselineId: number; partitionExpr: string; }) {
  return request<DefaultResponse>('/api/dqc/monitorTable/update', {
    method: 'POST',
    data
  });
}
/**
 * 添加监控规则
 */
 export async function addMonitorRule(data: MonitorRuleItem) {
  return request<DefaultResponse>('/api/dqc/monitorRule/add', {
    method: 'POST',
    data
  });
}

/**
 * 　修改监控规则
 */
 export async function updateMonitorRule(data: MonitorRuleItem) {
  return request<DefaultResponse>('/api/dqc/monitorRule/update', {
    method: 'POST',
    data
  });
}

/**
 * 　删除监控规则
 */
 export async function removeMonitorRule(data: {id: number;}) {
  return request<DefaultResponse>(`/api/dqc/monitorRule/del/${data.id}`, {
    method: 'GET',
    data
  });
}

/**
 * 　启用、停用监控规则
 */
 export async function toggleMonitorRule(data: {id: number; status: number}) {
  return request<DefaultResponse>(`/api/dqc/monitorRule/setStatus/${data.id}/${data.status}`, {
    method: 'GET',
    data
  });
}

/**
 * 　试跑监控规则
 */
 export async function tryRunMonitorRule(data: {ruleId: number; baselineId: number | string}) {
  return request<DefaultResponse & { data: LogItem [] }>(`/api/dqc/monitorRule/tryRun/${data.baselineId}/${data.ruleId}`, {
    method: 'GET'
  });
}

/**
 * 　获取规则详情
 */
 export async function getMonitorRule(data: {id: number}) {
  return request<DefaultResponse & {data: MonitorRuleItem }>(`/api/dqc/monitorRule/get/${data.id}`, {
    method: 'GET',
  });
}

/**
 *  获取所有字段
 */
 export async function getFiledList(params: {tableName: string}) {
  return request<DefaultResponse & { data: {name: string} [] }>('/api/dqc/table/getColumns', {
    method: 'GET',
    params
  });
}

/**
 *  获取告知人
 */
 export async function getRecivers(params: {name?: string}) {
  return request<DefaultResponse & { data: {nickname: string} [] }>('/api/dqc/user/getList', {
    method: 'GET',
    params
  });
}

/**
 *  获取日志
 */
export async function getLogs(params: {
  ruleId?: string | number;
  baselineId?: string | number;
  days?: string
}) {
  return request<DefaultResponse & { data: LogItem [] }>('/api/dqc/monitorHistory/get', {
    method: 'GET',
    params
  });
}

/**
 *  获取当前表负责人
 */
 export async function getTableOwners(params: {tableName: string}) {
  return request<DefaultResponse & { data: LogItem [] }>('/api/dqc/table/getOwners', {
    method: 'GET',
    params
  });
}


/* ========== 规则模板库 ========== */
/**
 *  获取模板列表
 */
 export async function getTemplateList(data: {curPage: number, pageSize: number, monitorObj?: MonitorObj, type?: RuleType, status: 0 | 1}) {
  return request<DefaultResponse & { data: {data: TemplateItem [], totalElements: number} } >('/api/dqc/monitorTemplate/getByPage', {
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
  return request<DefaultResponse & { data: TemplateItem } >('/api/dqc/monitorTemplate/add', {
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
  return request<DefaultResponse & { data: TemplateItem } >('/api/dqc/monitorTemplate/update', {
    method: 'POST',
    data
  });
}

/**
 *  获取模板
 */
 export async function getTemplate(params: {id: number;}) {
  return request<DefaultResponse & { data: TemplateItem } >(`/api/dqc/monitorTemplate/get/${params.id}`, {
    method: 'GET',
  });
}

/**
 *  删除模板
 */
 export async function removeTemplate(params: {id: number;}) {
  return request<DefaultResponse & { data: TemplateItem } >(`/api/dqc/monitorTemplate/del/${params.id}`, {
    method: 'GET',
  });
}

/**
 *  开启、停止模板
 */
 export async function toggleTemplate(params: {id: number;status: number;}) {
  return request<DefaultResponse & { data: TemplateItem } >(`/api/dqc/monitorTemplate/setStatus/${params.id}/${params.status}`, {
    method: 'GET',
  });
}
/**
 *  开启、停止模板
 */
 export async function getUseInfo() {
  return request<DefaultResponse & { data: {nickname: string} } >('/api/dqc/user/getCur', {
    method: 'GET',
  });
}

/* ========== 基线管理 ========== */
/**
 *  获取模板列表
 */
 export async function getBaselineList(data: {curPage: number; pageSize: number; name: string;}) {
  return request<DefaultResponse & { data: {data: BaselineItem [], totalElements: number} } >('/api/dqc/monitorBaseline/getByPage', {
    method: 'POST',
    data
  });
}

/**
 *  新增基线
 */
 export async function addBaseline(data: { name: string;}) {
  return request<DefaultResponse & { data: BaselineItem [] } >('/api/dqc/monitorBaseline/add', {
    method: 'POST',
    data
  });
}

/**
 *  更新基线
 */
 export async function updateBaseline(data: { name: string; id: number;}) {
  return request<DefaultResponse & { data: BaselineItem [] } >('/api/dqc/monitorBaseline/update', {
    method: 'POST',
    data
  });
}

/**
 *  获取基线信息
 */
 export async function getBaseline(data: { id: string;}) {
  return request<DefaultResponse & { data: BaselineItem } >(`/api/dqc/monitorBaseline/get/${data.id}`, {
    method: 'GET'
  });
}

/**
 *  删除基线
 */
 export async function deleteBaseline(data: { id: number;}) {
  return request<DefaultResponse & { data: BaselineItem } >(`/api/dqc/monitorBaseline/del/${data.id}`, {
    method: 'GET'
  });
}

/**
 *  开启、停止基线
 */
 export async function toggleBaseline(data: { id: number; status: number;}) {
  return request<DefaultResponse & { data: BaselineItem} >(`/api/dqc/monitorBaseline/setStatus/${data.id}/${data.status}`, {
    method: 'GET'
  });
}

/**
 *  开启、停止基线
 */
 export async function getBaselineTables(data: { id: string | number; }) {
  return request<DefaultResponse & { data: TableItem []} >(`api/dqc/monitorTable/tables/get/${data.id}`, {
    method: 'GET'
  });
}