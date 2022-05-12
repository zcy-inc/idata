import {
  FolderBelong,
  FolderTypes,
  PeriodRange,
  SchRerunMode,
  SrcReadMode,
  DestWriteMode,
  TaskCategory,
  TaskTypes,
  TriggerMode,
  VersionStatus,
  SchTimeOutStrategy,
  SchPriority,
  JobStatus,
  DIJobType,
  DISyncMode,
} from '@/constants/datadev';
import { DataSourceTypes, Environments } from '@/constants/datasource';

// 树的节点类型
export interface TreeNode {
  cid: string; // type_belong_id
  id: number; // 可能存在重复，用作树的唯一标识符时请使用cid
  name: string;
  type: FolderTypes;
  belong: FolderBelong;
  parentCid?: string;
  parentId?: number;
  children?: TreeNode[];
  // 以下是Antd树组件需要的属性
  className?: string;
  title?: any;
  key?: string;
}

// 平铺文件夹的类型
export interface Folder {
  id: number;
  name: string;
  type: FolderTypes;
  belong: FolderBelong;
  parentId?: number;
}

export interface DAG {
  dagInfoDto: {
    id: number;
    name: string;
    environment: Environments;
    dwLayerCode: string;
    status: 0 | 1; // 0 停用，1 启用
    remark: string;
    folderId: number;
  };
  dagScheduleDto: {
    id: number;
    dagId: number;
    beginTime: string;
    endTime: string;
    periodRange: PeriodRange;
    triggerMode: TriggerMode;
    cronExpression: string;
  };
}

export interface TaskType {
  code: TaskTypes;
  catalog: TaskCategory;
  name: string;
  language?: string;
}

export interface Task {
  id: number;
  name: string;
  jobType: TaskTypes;
  dwLayerCode: string;
  status: 0 | 1; // 0 停用，1 启用
  remark: string;
  folderId: number;
  creator: string;
  language?: TaskTypes;
}

export interface TaskVersion {
  jobId: number;
  version: number;
  versionDisplay: string;
  versionStatus: VersionStatus;
  environment: string;
  envRunningState: 0 | 1; // 0 暂停；1 恢复
}

export interface TaskContent {
  id: number;
  jobId: number; // 作业id
  version: number; // 作业版本号
  versionDisplay: number; // 显示用的作业版本号
  srcDataSourceType: DataSourceTypes; // 数据来源-数据源类型
  srcDataSourceId: numebr; // 数据来源-数据源id
  srcTables: string; // 数据来源-表
  srcReadFilter: string; // 数据来源-过滤条件
  srcReadShardKey: string; // 数据来源-切分键
  srcShardingNum: number; // 数据来源-分片数量（并行度）
  srcReadMode: SrcReadMode; // 数据来源-读取模式
  destDataSourceType: DataSourceTypes; // 数据去向-数据源类型
  destDataSourceId: number; // 数据去向-数据源id
  destTable: string; // 数据去向-数仓表的表名
  destBeforeWrite: string; // 数据去向-写入前语句
  destAfterWrite: string; // 数据去向-写入后语句
  destWriteMode: DestWriteMode; //数据去向-写入模式
  srcCols: MappedColumn[];
  destCols: MappedColumn[];
  contentHash: string; // 作业内容hash，查询作业内容时返回，保存新的作业内容时需将该值一起提交，用于判断作业内容是否有变更
}

export interface MappedColumn {
  name: string;
  primaryKey: boolean;
  dataType?: string;
  mappingSql?: string;
  mappedColumn?: MappedColumn;
}

export interface TaskTable {
  tableName: string;
}

export interface TaskConfig {
  executeConfig: {
    id?: number;
    jobId: number;
    environment: Environments;
    schDagId: number; // 调度配置-dag编号
    schRerunMode: SchRerunMode; // 调度配置-重跑配置
    schTimeOut: number; // 调度配置-超时时间
    schDryRun: number | string[]; // 调度配置-是否空跑
    execQueue: string; // 运行配置-队列
    execMaxParallelism: number; // 运行配置-作业最大并发数，配置为0时表示使用默认并发数
    execWarnLevel: string; // 运行配置-告警等级 时光的接口获取
    schTimeOutStrategy: SchTimeOutStrategy; // 调度配置-超时策略
    schPriority: SchPriority; // 调度配置-优先级
    execDriverMem: number; // 运行配置-驱动器内存
    execWorkerMem: number; // 运行配置-执行器内存
    runningState?: 0 | 1; // 作业运行状态（环境级），0：暂停运行；1：恢复运行
  };
  dependencies?: {
    id?: number;
    jobId: number;
    environment: Environments;
    prevJobId: number; // 上游作业id
    prevJobDagId: string; // 上游作业所属dag id
  }[];
  output?: {
    id?: number;
    jobId: number;
    environment: Environments;
    destDataSourceType: string; // 数据去向-数据源类型
    destDataSourceId: number; // 数据去向-数据源id
    destTable: string; // 数据去向-目标表
    destWriteMode: DestWriteMode; // 数据去向-写入模式，override，upsert
  };
}

export interface DAGListItem {
  id: number;
  name: string;
  dwLayerCode: string;
  status: 0 | 1;
  remark: string;
  folderId: string;
}

/* ========== 数据开发 ========== */
export interface ConfiguredTaskListItem {
  jobId: number;
  jobName: string;
  dwLayerCode: string;
  dagId: number;
  dagName: string;
}

export interface DependenciesJob extends ConfiguredTaskListItem {
  prevJobId: number;
  prevJobDagId: number;
  prevJobDagName: string;
  prevJobName: string;
  environment: Environments;
}

export interface SqlSparkContent {
  jobId: number;
  jobType: TaskTypes;
  sourceSql: string;
  externalTables: string;
  id?: number;
  editable?: number;
  version?: number;
  udfIds?: string;
}

export interface SparkContent {
  jobId: number;
  jobType: TaskTypes;
  appArguments: {
    argumentValue: string;
    argumentRemark: string;
  }[]; // 动态表单列表参数
  id?: number;
  version?: number;
  editable?: number;
  mainClass?: string; // 执行类
  pythonResource?: string; // python代码
  resourceHdfsPath?: string; // 上传文件返回的路径
}

export interface ScriptContent {
  jobId: number;
  jobType: TaskTypes;
  sourceResource: string; // 代码
  id?: number;
  editable?: number;
  version?: number;
  scriptArguments?: {
    argumentValue: string;
    argumentRemark: string;
  }[]; // 参数
}

export interface KylinContent {
  jobId: number;
  cubeName: string; // cube名称
  buildType: string; // 构建类型
  startTime?: number; // 生效时间
  endTime?: number; // 失效时间
  id?: number;
  editable?: number;
  version?: number;
}

export interface TaskHistoryItem {
  id: number;
  jobId: number;
  applicationId: string;
  avgMemory: number;
  avgVcores: number;
  createTime: string;
  duration: number;
  finalStatus: string;
  startTime: string;
  finishTime: string;
  businessLogsUrl: string;
}

export interface DependenceTreeNode {
  jobId: number;
  jobName: string;
  jobStatus: JobStatus;
  lastRunTime: string;
  nextLevel?: number;
  prevLevel?: number;
  children?: DependenceTreeNode[];
  taskId: number;
  relation?: 'prev' | 'next';
}

export interface UDF {
  commandFormat: string;
  sourceName?: string;
  description: string;
  fileName: string;
  folderId: number;
  hdfsPath: string;
  returnSample: string;
  returnType: string;
  udfName: string;
  udfSample: string;
  udfType: string;
  id?: number;
}

export interface CreateDIJobDto {
  jobType: DIJobType;
  syncMode: DISyncMode;
  name: string;
  dwLayerCode: string;
  folderId: number;
  remark?: string;
}

export interface DIJobBasicInfo {
  id: number;
  jobType: DIJobType;
  syncMode: DISyncMode;
  name: string;
  dwLayerCode: string;
  folderId: number;
  creator: string;
  status: 0 | 1; // 0 停用，1 启用
  remark?: string;
}

export interface MergeSqlParamDto {
  selectColumns: string;
  keyColumns: string;
  sourceTable: string;
  destTable: string;
  srcReadMode: string;
  dataSourceType: string;
  days?: number;
}

export interface DIJobContent {
  id: save;
}
