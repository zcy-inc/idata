import type { Key } from 'react';
import { request } from 'umi';
import type { Table } from '@/types/datapi';
import type {
  ConfiguredTaskListItem,
  DAG,
  DAGListItem,
  TaskConfig,
  Folder,
  KylinContent,
  MappedColumn,
  ScriptContent,
  SparkContent,
  SqlSparkContent,
  Task,
  TaskContent,
  TaskTable,
  TaskType,
  TaskVersion,
  TreeNode,
  DependenceTreeNode,
  TaskHistoryItem,
  UDF,
  CreateDIJobDto,
  DIJobBasicInfo,
  MergeSqlParamDto,
} from '@/types/datadev';
import type { PeriodRange, StatementState, TaskCategory, TaskTypes } from '@/constants/datadev';
import type { DefaultResponse } from './global';
import type { DataSourceTypes, Environments } from '@/constants/datasource';

/**
 * 标签 创建
 */
export async function createTag(data: {}) {
  return request('/api/p1/dev/labelDefine', { method: 'POST', data });
}

/**
 * 标签 查看
 */
export async function getLabel(params: { labelDefineId: number }) {
  return request(`/api/p1/dev/labelDefines/${params.labelDefineId}`, { method: 'GET', params });
}

/**
 * 标签 删除
 */
export async function deleteLabel(params: { labelCode: string }) {
  return request('/api/p1/dev/labelDefine', { method: 'DELETE', params });
}

/**
 * 枚举 获取names
 */
export async function getEnumNames() {
  return request('/api/p1/dev/enumNames', { method: 'GET' });
}

/**
 * 枚举 获取values
 */
export async function getEnumValues(params: { enumCode: string }) {
  return request<Tresponse<{ enumValue: string; valueCode: string }[]>>('/api/p1/dev/enumValues', {
    method: 'GET',
    params,
  });
}

/**
 * 枚举 查看
 */
export async function getEnum(params: { enumId: number }) {
  return request(`/api/p1/dev/enums/${params.enumId}`, { method: 'GET', params });
}

/**
 * 枚举 创建 / 编辑
 */
export async function createEnum(data: {}) {
  return request('/api/p1/dev/enum', { method: 'POST', data });
}

/**
 * 枚举 删除
 */
export async function deleteEnum(params: { enumCode: string }) {
  return request('/api/p1/dev/enum', { method: 'DELETE', params });
}

/**
 * 表 创建 / 编辑
 */
export async function createTable(data: {}) {
  return request('/api/p1/dev/tableInfo', { method: 'POST', data });
}

/**
 * 表 获取labels/columns
 */
export async function getTableLabels(params: {
  labelTag?: string;
  subjectType: 'TABLE' | 'COLUMN';
}) {
  return request('/api/p1/dev/labelDefines', { method: 'GET', params });
}

/**
 * 表 获取参考数据库
 */
export async function getTableReferDbs() {
  return request('/api/p1/dev/dbNames', { method: 'GET' });
}

/**
 * 表 获取参考表
 */
export async function getTableReferTbs(params: { labelValue?: string }) {
  return request<{ data: Table[] }>('/api/p1/dev/referTables', {
    method: 'GET',
    params,
  });
}

/**
 * 表 获取参考字段
 */
export async function getTableReferStr(params: any) {
  return request(`/api/p1/dev/columnInfos/${params.tableId}`, { method: 'GET', params });
}

/**
 * 表 数仓所属人
 */
export async function getDWOwner() {
  return request('/api/p1/uac/users', { method: 'GET' });
}

/**
 * 表 查看
 */
export async function getTable(params: { tableId: any }) {
  return request(`/api/p1/dev/tableInfo/${params.tableId}`, { method: 'GET' });
}

/**
 * 表 获取关系预览图
 */
export async function getTableRelations(params: { tableId: string }) {
  return request('/api/p0/dev/tableRelations', { method: 'GET', params });
}

/**
 * 表 删除
 */
export async function delTable(data: { tableId: any }) {
  return request(`/api/p1/dev/tableInfo/${data.tableId}`, { method: 'DELETE', data });
}

/**
 * 表 DDL模式
 */
export async function getDDL(params: { tableId: any }) {
  return request(`/api/p1/dev/tableDdl/${params.tableId}`, { method: 'GET', params });
}

/**
 * 表 同步MetaBase
 */
export async function postSyncMetabase(params: { tableId: any }) {
  return request(`/api/p1/dev/syncMetabaseInfo/${params.tableId}`, { method: 'POST', params });
}

/**
 * 表 生成表结构
 */
export async function getTableConstruct(data: { tableDdl: string; tableId: number }) {
  return request('/api/p1/dev/ddl/syncTableInfo', { method: 'POST', data });
}
// 表 同步Hive
export async function syncHive(data: { tableId: number }) {
  return request<DefaultResponse>(`/api/p1/dev/syncHiveInfo/${data.tableId}`, {
    method: 'POST',
    data,
  });
}

/* ==================== DataDev ==================== */
/**
 * 获取功能性文件树
 */
export async function getFunctionTree() {
  return request<DefaultResponse & { data: TreeNode[] }>(
    '/api/p1/dev/compositeFolders/functions/tree',
    {
      method: 'GET',
    },
  );
}

/**
 * 获取完整的文件树
 */
export async function getTree(data?: { belongFunctions?: string[]; keyWord?: string }) {
  return request<DefaultResponse & { data: TreeNode[] }>('/api/p1/dev/compositeFolders/tree', {
    method: 'POST',
    data,
  });
}

/**
 * 获取平铺的文件夹列表
 */
export async function getFolders(params: { belong: string }) {
  return request<DefaultResponse & { data: Folder[] }>('/api/p1/dev/compositeFolders/folders', {
    method: 'GET',
    params,
  });
}

/**
 * 创建文件夹
 */
export async function createFolder(data: Partial<Folder>) {
  return request<DefaultResponse & { data: Folder }>('/api/p1/dev/compositeFolders', {
    method: 'POST',
    data,
  });
}

/**
 * 编辑文件夹
 */
export async function updateFolder(data: Partial<Folder>) {
  return request<DefaultResponse & { data: Folder }>('/api/p1/dev/compositeFolders', {
    method: 'PUT',
    data,
  });
}

/**
 * 删除文件夹
 */
export async function deleteFolder(params: { id: number }) {
  return request<DefaultResponse & { data: Folder }>(`/api/p1/dev/compositeFolders/${params.id}`, {
    method: 'DELETE',
  });
}

/* ==================== DAG ==================== */
/**
 * 创建DAG
 */
export async function createDAG(data: {
  dagInfoDto: {
    name: string;
    dwLayerCode: string;
    folderId: string;
  };
  dagScheduleDto: {
    beginTime: string;
    endTime: string;
    periodRange: PeriodRange;
    triggerMode: string;
    cronExpression: string;
  };
}) {
  return request<DefaultResponse & { data: DAG }>('/api/p1/dev/dags', { method: 'POST', data });
}

/**
 * 编辑DAG
 */
export async function editDAG(data: {
  dagInfoDto: {
    name: string;
    dwLayerCode: string;
    folderId: string;
  };
  dagScheduleDto: {
    beginTime: string;
    endTime: string;
    periodRange: PeriodRange;
    triggerMode: string;
    cronExpression: string;
  };
}) {
  return request<DefaultResponse & { data: DAG }>('/api/p1/dev/dags', { method: 'PUT', data });
}

/**
 * 获取DAG
 */
export async function getDAG(params: { id: Key }) {
  return request<DefaultResponse & { data: DAG }>(`/api/p1/dev/dags/${params.id}`, {
    method: 'GET',
    params,
  });
}

/**
 * 删除DAG
 */
export async function deleteDAG(params: { id: Key }) {
  return request<DefaultResponse & { data: boolean }>(`/api/p1/dev/dags/${params.id}`, {
    method: 'DELETE',
    params,
  });
}

/**
 * 上线DAG
 */
export async function onlineDAG(params: { id: Key }) {
  return request<DefaultResponse & { data: boolean }>(`/api/p1/dev/dags/${params.id}/online`, {
    method: 'PUT',
    params,
  });
}

/**
 * 下线DAG
 */
export async function offlineDAG(params: { id: Key }) {
  return request<DefaultResponse & { data: boolean }>(`/api/p1/dev/dags/${params.id}/offline`, {
    method: 'PUT',
    params,
  });
}

/* ==================== Task ==================== */
/**
 * 获取作业类型
 */
export async function getTaskTypes(params: { catalog: TaskCategory }) {
  return request<DefaultResponse & { data: TaskType[] }>('/api/p1/dev/jobs/types', {
    method: 'GET',
    params,
  });
}

/**
 * 新建作业
 */
export async function createTask(data: {
  name: string;
  jobType: TaskTypes;
  dwLayerCode: string;
  remark: string;
  folderId: number;
}) {
  return request<DefaultResponse & { data: Task }>('/api/p1/dev/jobs', {
    method: 'POST',
    data,
  });
}

/**
 * 获取作业
 */
export async function getTask(params: { id: number }) {
  return request<DefaultResponse & { data: Task }>(`/api/p1/dev/jobs/${params.id}`, {
    method: 'GET',
  });
}

/**
 * 编辑作业
 */
export async function editTask(data: Task) {
  return request<DefaultResponse & { data: Task }>('/api/p1/dev/jobs', {
    method: 'PUT',
    data,
  });
}

/**
 * 获取作业内容版本
 */
export async function getTaskVersions(params: { jobId: number }) {
  return request<DefaultResponse & { data: TaskVersion[] }>(
    `/api/p1/dev/jobs/${params.jobId}/versions`,
    {
      method: 'GET',
      params,
    },
  );
}

/**
 * 获取作业内容
 */
export async function getTaskContent(params: { jobId: number; version: number }) {
  return request<DefaultResponse & { data: TaskContent }>(
    `/api/p1/dev/jobs/${params.jobId}/di/contents/${params.version}`,
    {
      method: 'GET',
      params,
    },
  );
}

/**
 * 获取作业内容的表
 */
export async function getTaskTables(params: {
  dataSourceType: DataSourceTypes;
  dataSourceId: number;
}) {
  return request<DefaultResponse & { data: TaskTable[] }>('/api/p1/dev/jobTables', {
    method: 'GET',
    params,
  });
}

/**
 * 获取作业内容的表字段
 */
export async function getTaskTableColumns(params: {
  tableName: string;
  dataSourceType: DataSourceTypes;
  dataSourceId: number;
}) {
  return request<DefaultResponse & { data: MappedColumn[] }>('/api/p1/dev/jobTables/columns', {
    method: 'GET',
    params,
  });
}

/**
 * 删除作业
 */
export async function deleteTask(params: { id: number }) {
  return request<DefaultResponse & { data: boolean }>(`/api/p1/dev/jobs/${params.id}`, {
    method: 'DELETE',
  });
}

/**
 * 保存作业
 */
export async function saveTask(data: TaskContent) {
  return request<DefaultResponse & { data: TaskContent }>(
    `/api/p1/dev/jobs/${data.jobId}/di/contents`,
    {
      method: 'POST',
      data,
    },
  );
}

/**
 * 提交作业
 */
export async function submitTask(
  params: {
    jobId: number;
    version: number;
    env: Environments;
  },
  data: {
    remark: string;
  },
) {
  return request<DefaultResponse & { data: TaskContent }>(
    `/api/p1/dev/jobs/${params.jobId}/versions/${params.version}/submit/${params.env}`,
    {
      method: 'POST',
      data,
    },
  );
}

/**
 * 获取DAG列表
 */
export async function getDAGList(params: { environment: Environments; dwLayerCode?: string }) {
  return request<DefaultResponse & { data: DAGListItem[] }>('/api/p1/dev/dags/info', {
    method: 'GET',
    params,
  });
}

/**
 * 获取运行队列
 */
export async function getExecuteQueues() {
  return request<DefaultResponse & { data: { name: string; code: string }[] }>(
    '/api/p1/dev/executeQueues',
    {
      method: 'GET',
    },
  );
}

/* ========== 数据开发 ========== */
/**
 * 获取数据开发的catalogs
 */
export async function getDataDevTypes() {
  return request<DefaultResponse & { data: TaskCategory[] }>(`/api/p1/dev/jobs/catalogs`, {
    method: 'GET',
  });
}

/**
 * 获取已配置的作业列表
 */
export async function getConfiguredTaskList(params: { environment: Environments }) {
  return request<DefaultResponse & { data: ConfiguredTaskListItem[] }>(
    `/api/p1/dev/jobs/environments/${params.environment}/jobs`,
    {
      method: 'GET',
    },
  );
}

// 获取已配置的作业列表（依赖的上游任务）
export async function getDependenceTaskList(params: { environment: Environments }) {
  return request<Tresponse<ConfiguredTaskListItem[]>>(
    `/api/p1/dev/jobs/environments/${params.environment}/jobs`,
    {
      method: 'GET',
    },
  ).then(({ data }) => {
    return data.map((item) => ({
      ...item,
      prevJobId: item.jobId,
      prevJobName: item.jobName,
      prevJobDagName: item.dagName,
      prevJobDagId: item.dagId,
    }));
  });
}

/**
 * 获取数据开发配置信息
 */
export async function getDataDevConfig(params: { jobId: number; environment: Environments }) {
  return request<DefaultResponse & { data: TaskConfig }>(
    `/api/p1/dev/jobs/${params.jobId}/environments/${params.environment}/configs`,
    {
      method: 'GET',
    },
  );
}

/**
 * 保存数据开发配置信息
 */
export async function saveTaskConfig(
  params: { jobId: number; environment: Environments },
  data: TaskConfig,
) {
  return request<DefaultResponse & { data: TaskConfig }>(
    `/api/p1/dev/jobs/${params.jobId}/environments/${params.environment}/configs`,
    {
      method: 'POST',
      data,
    },
  );
}

/**
 * 暂停作业
 */
export async function pauseTask(params: { id: number; environment: Environments }) {
  return request<DefaultResponse & { data: boolean }>(
    `/api/p1/dev/jobs/${params.id}/environments/${params.environment}/pause`,
    {
      method: 'PUT',
      params,
    },
  );
}

/**
 * 恢复作业
 */
export async function resumeTask(params: { id: number; environment: Environments }) {
  return request<DefaultResponse & { data: boolean }>(
    `/api/p1/dev/jobs/${params.id}/environments/${params.environment}/resume`,
    {
      method: 'PUT',
      params,
    },
  );
}

/**
 * 单次运行作业
 */
export async function runTask(params: { id: number; environment: Environments }) {
  return request<DefaultResponse & { data: boolean }>(
    `/api/p1/dev/jobs/${params.id}/environments/${params.environment}/run`,
    {
      method: 'POST',
      params,
    },
  );
}

/**
 * 保存Sql作业内容(done)
 */
export async function saveSqlSpark(params: { jobId: number }, data: SqlSparkContent) {
  return request<DefaultResponse & { data: SqlSparkContent }>(
    `/api/p1/dev/jobs/${params.jobId}/sql/contents`,
    {
      method: 'POST',
      data,
    },
  );
}

/**
 * 获取Sql作业内容(done)
 */
export async function getSqlSpark(params: { jobId: number; version: number }) {
  return request<DefaultResponse & { data: SqlSparkContent }>(
    `/api/p1/dev/jobs/${params.jobId}/sql/contents/${params.version}`,
    {
      method: 'GET',
      params,
    },
  );
}

/**
 * 保存Spark作业内容
 */
export async function saveSpark(params: { jobId: number }, data: SparkContent) {
  return request<DefaultResponse & { data: SparkContent }>(
    `/api/p1/dev/jobs/${params.jobId}/spark/contents`,
    {
      method: 'POST',
      data,
    },
  );
}

/**
 * 获取Spark作业内容
 */
export async function getSpark(params: { jobId: number; version: number }) {
  return request<DefaultResponse & { data: SparkContent }>(
    `/api/p1/dev/jobs/${params.jobId}/spark/contents/${params.version}`,
    {
      method: 'GET',
      params,
    },
  );
}

/**
 * 获取Spark作业内容
 */
export async function uploadJar(params: { jobId: number }, data: FormData) {
  return request<DefaultResponse & { data: string }>(
    `/api/p1/dev/jobs/${params.jobId}/spark/uploadFile`,
    {
      method: 'POST',
      params,
      data,
    },
  );
}

/**
 * 保存Script作业内容
 */
export async function saveScript(params: { jobId: number }, data: ScriptContent) {
  return request<DefaultResponse & { data: ScriptContent }>(
    `/api/p1/dev/jobs/${params.jobId}/script/contents`,
    {
      method: 'POST',
      data,
    },
  );
}

/**
 * 获取Script作业内容
 */
export async function getScript(params: { jobId: number; version: number }) {
  return request<DefaultResponse & { data: ScriptContent }>(
    `/api/p1/dev/jobs/${params.jobId}/script/contents/${params.version}`,
    {
      method: 'GET',
      params,
    },
  );
}

/**
 * 保存Kylin作业内容
 */
export async function saveKylin(params: { jobId: number }, data: KylinContent) {
  return request<DefaultResponse & { data: KylinContent }>(
    `/api/p1/dev/jobs/${params.jobId}/kylin/contents`,
    {
      method: 'POST',
      data,
    },
  );
}

/**
 * 获取Kylin作业内容
 */
export async function getKylin(params: { jobId: number; version: number }) {
  return request<DefaultResponse & { data: KylinContent }>(
    `/api/p1/dev/jobs/${params.jobId}/kylin/contents/${params.version}`,
    {
      method: 'GET',
      params,
    },
  );
}

/**
 * 查询日志runQuerySql
 */
export async function runQuery(data: {
  querySource: string;
  sessionKind: string;
  udfIds?: number[];
  externalTables?: string;
}) {
  return request<
    DefaultResponse & {
      data: {
        sessionId: number;
        statementId: number;
      };
    }
  >('/api/p1/dev/jobs/runQuery', {
    method: 'POST',
    data,
  });
}

/**
 * 查询结果runQueryResult
 */
export async function runQueryResult(params: {
  sessionId: number;
  sessionKind: string;
  statementId: number;
  from?: number;
  size?: number;
}) {
  return request<
    DefaultResponse & {
      data: {
        queryRunLog: {
          sessionId: number;
          state: string;
          from: number;
          total: number;
          log: string[];
        };
        sessionId: number;
        statementId: number;
        statementState: StatementState;
        outputStatus: string;
        resultSet: { [key: string]: string }[];
        pythonResults?: string;
      };
    }
  >('/api/p1/dev/jobs/runQueryResult', {
    method: 'GET',
    params,
  });
}

/**
 * 获取作业历史
 */
export async function getTaskHistory(data: {
  condition: {
    id: number;
  };
  pageNum: number;
  pageSize: number;
}) {
  return request<
    DefaultResponse & {
      data: {
        content: TaskHistoryItem[];
        pageNum: number;
        pageSize: number;
        pages: number;
        total: number;
      };
    }
  >(`/api/p1/dev/jobs/history/page`, {
    method: 'POST',
    data,
  });
}

/**
 * 获取作业的依赖树
 */
export async function getDependenceTree(params: {
  id: number;
  env: Environments;
  name?: string;
  preLevel: number;
  nextLevel: number;
  searchJobId?: number;
}) {
  return request<DefaultResponse & { data: DependenceTreeNode }>(
    `/api/p1/dev/jobs/dependency/${params.id}/tree`,
    {
      method: 'GET',
      params,
    },
  );
}

/**
 * 获取作业的依赖树节点的平铺列表
 */
export async function getDependenceTreeNodeList(params: {
  searchName: string;
  env: Environments;
  jobId: number;
}) {
  return request<DefaultResponse & { data: { id: number; name: string }[] }>(
    '/api/p1/dev/jobs/dependency/list',
    {
      method: 'GET',
      params,
    },
  );
}

/**
 * 重跑作业
 */
export async function rerunTask(params: { id: number; env: Environments; runPost?: boolean }) {
  return request<DefaultResponse & { data: boolean }>(
    `/api/p1/dev/jobs/dependency/${params.id}/rerun`,
    {
      method: 'GET',
      params,
    },
  );
}

/**
 * 查看作业最后运行时间
 */
export async function getTaskLastTime(params: { id: number }) {
  return request<DefaultResponse & { data: string }>(
    `/api/p1/dev/jobs/dependency/${params.id}/latestRuntime`,
    {
      method: 'GET',
      params,
    },
  );
}

/**
 * 查看作业日志
 */
export async function getTaskRunningLog(params: { id: number; env: Environments; taskId: number }) {
  return request<DefaultResponse & { data: string }>(
    `/api/p1/dev/jobs/dependency/${params.id}/running/log`,
    {
      method: 'GET',
      params,
    },
  );
}

/* ========== Dev.Fun(UDF) ========== */
/**
 * 获取函数详情
 */
export async function getUDF(params: { id: number }) {
  return request<DefaultResponse & { data: UDF }>(`/api/p1/dev/udf/${params.id}`, {
    method: 'GET',
    params,
  });
}

/**
 * 创建函数
 */
export async function createUDF(data: UDF) {
  return request<DefaultResponse & { data: UDF }>('/api/p1/dev/udf', {
    method: 'POST',
    data,
  });
}

/**
 * 更新函数
 */
export async function updateUDF(data: UDF) {
  return request<DefaultResponse & { data: UDF }>('/api/p1/dev/udf', {
    method: 'PUT',
    data,
  });
}

/**
 * 删除函数
 */
export async function deleteUDF(params: { id: number }) {
  return request<DefaultResponse & { data: boolean }>(`/api/p1/dev/udf/${params.id}`, {
    method: 'DELETE',
  });
}

/**
 * 函数代码文件上传
 */
export async function uploadUDFCodeFile(data: FormData) {
  return request<
    DefaultResponse & {
      data: {
        relativePath: string;
        fileName: string;
      };
    }
  >('/api/p1/hdfs/upload/udf', {
    method: 'POST',
    data,
  });
}

/**
 * 函数代码文件下载
 */
export async function getUDFCodeFile(params: { id: number }) {
  return request(`/api/p1/dev/udf/download/${params.id}`, {
    method: 'GET',
  });
}

/**
 * 自定义函数列表获取
 */
export async function getUDFList() {
  return request<DefaultResponse & { data: UDF[] }>('/api/p1/dev/udfs', {
    method: 'GET',
  });
}

/**
 * 获取DI任务类型下拉列表
 */
export async function getDIJobTypes() {
  return request<Tresponse<{ name: string; value: string }[]>>(
    '/api/p1/dev/jobs/di/meta/job-type',
    {
      method: 'GET',
    },
  ).then(({ data = [] }) => data.map(({ name, value }) => ({ label: name, value })));
}

/**
 * 获取DI同步类型下拉列表
 */
export async function getDISyncMode(params: { jobType: string }) {
  return request<Tresponse<{ name: string; value: string }[]>>(
    '/api/p1/dev/jobs/di/meta/sync-mode',
    {
      method: 'GET',
      params,
    },
  ).then(({ data = [] }) => data.map(({ name, value }) => ({ label: name, value })));
}

/**
 * 新增DI
 */
export async function createDIJob(data: CreateDIJobDto) {
  return request<Tresponse>('/api/p1/dev/jobs/di', { method: 'POST', data });
}

/**
 * 获取DI基础信息
 */
export async function getDIJobBasicInfo(jobId: number) {
  return request<Tresponse<DIJobBasicInfo>>(`/api/p1/dev/jobs/di/${jobId}`).then(
    ({ data }) => data,
  );
}

/**
 * 保存DI作业基础信息
 */
export async function saveDIJobBasicInfo(data: unknown) {
  return request('/api/p1/dev/jobs/di', {
    method: 'PUT',
    data,
  });
}

/**
 * 获取DI作业内容
 */
export async function getDIJobContent({ jobId, version }: { jobId: number; version: number }) {
  return request(`/api/p1/dev/jobs/${jobId}/di/contents/${version}`);
}

/**
 * 保存DI作业内容
 */
export async function saveDIJobContent(data: any) {
  const { jobId } = data;
  return request(`/api/p1/dev/jobs/${jobId}/di/contents`, {
    method: 'POST',
    data,
  });
}

/**
 * 自动生成mergeSql
 */
export async function genMergeSQL(data: MergeSqlParamDto) {
  return request(`/api/p1/dev/jobs/di/generate/merge-sql`, {
    method: 'POST',
    data,
  });
}

/**
 * 获取 kafka topic 下拉列表
 */
export async function getKafkaTopics(params: { dataSourceId: number }) {
  return request<Tresponse<string[]>>('/api/p1/das/kafka/topics', {
    params,
  });
}

/**
 * 获取数据源的数据库
 */
export async function getDbNames(params: { dataSourceId: number }) {
  return request<Tresponse<string[]>>('/api/p1/das/dbNames', {
    params,
  }).then(({ data }) => data);
}

/**
 * 根据数据源和库名查询表名
 */
export async function getTableNames(params: { dataSourceId: number; dbName?: string }) {
  return request<Tresponse<string[]>>('/api/p1/das/tableNames', { params }).then(
    ({ data }) => data,
  );
}

/**
 * 获取表结构
 */
export async function getColumns(params: {
  dataSourceId: number;
  dbName?: string;
  tableName: string;
}) {
  return request('/api/p1/das/columns', {
    params,
  });
}

/**
 * 生成 SQL_FLINK 模板
 * @param params
 * @returns
 */
export async function genFlinkTemplate(data: {
  flinkSourceConfigs?: unknown[];
  flinkSinkConfigs?: unknown[];
}) {
  return request('/api/p1/dev/jobs/sql/flink/template', {
    method: 'POST',
    data,
  }).then(({ data }) => data);
}

export async function getWriteModeEnum(params: {
  writeMode: 'DiEnum' | 'BackFlowEnum' | 'SqlEnum';
}) {
  return request<Tresponse<string[]>>('/api/p1/dictionary/enum/writeMode', {
    params,
  });
}
