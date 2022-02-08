export enum FolderTypes {
  FUNCTION = 'FUNCTION', // 功能性文件夹
  FOLDER = 'FOLDER', // 普通文件夹
  RECORD = 'RECORD', // 业务数据
}

export enum FolderBelong {
  DESIGN = 'DESIGN', // 数仓设计
  DESIGNTABLE = 'DESIGN.TABLE', // 数仓设计-表
  // DESIGNLABEL = 'DESIGN.LABEL', // 数仓设计-标签
  // DESIGNENUM = 'DESIGN.ENUM', // 数仓设计-枚举
  DAG = 'DAG', // DAG
  DI = 'DI', // 数据集成
  DEV = 'DEV', // 数据开发
  DEVJOB = 'DEV.JOB', // 数据开发-作业
  DEVFUN = 'DEV.FUN', // 数据开发-函数
}

export enum PeriodRange {
  YEAR = 'year',
  MONTH = 'month',
  WEEK = 'week',
  DAY = 'day',
  HOUR = 'hour',
  MINUTE = 'minute',
}

export enum TriggerMode {
  INTERVAL = 'interval',
  POINT = 'point',
}

export enum TaskCategory {
  DI = 'DI', // DI：数据集成作业（获取数据集成分类下的作业类型：离线作业、实时作业）
  SCRIPT = 'SCRIPT',
  KYLIN = 'KYLIN',
  SPARK = 'SPARK',
  SQL = 'SQL',
}

export enum TaskTypes {
  DI_BATCH = 'DI_BATCH', // 离线同步
  DI_STREAM = 'DI_STREAM', // 实时同步
  SQL_SPARK = 'SQL_SPARK',
  SPARK_PYTHON = 'SPARK_PYTHON',
  SPARK_JAR = 'SPARK_JAR',
  SCRIPT_PYTHON = 'SCRIPT_PYTHON',
  SCRIPT_SHELL = 'SCRIPT_SHELL',
  KYLIN = 'KYLIN',
}

export enum SrcReadMode {
  ALL = 'all', // 全量
  INCREMENTAL = 'incremental', // 增量
}

export enum DestWriteMode {
  INIT = 'init', // 新建表
  OVERWRITE = 'overwrite', // 覆盖表
  APPEND = 'append',
}

export enum VersionStatus {
  EDITING = 0, // 编辑中
  TO_BE_PUBLISHED = 1, // 待发布
  PUBLISHED = 2, // 已发布
  REJECTED = 4, // 已驳回
  ARCHIVED = 9, // 已归档
}

export const VersionStatusDisplayMap = {
  [VersionStatus.EDITING]: '编辑中',
  [VersionStatus.TO_BE_PUBLISHED]: '待发布',
  [VersionStatus.PUBLISHED]: '已发布',
  [VersionStatus.REJECTED]: '已驳回',
  [VersionStatus.ARCHIVED]: '已归档',
};

export enum EnvRunningState {
  PAUSED = 0,
  NORMAL = 1,
}

export enum SchRerunMode {
  ALWAYS = 'always', // 皆可重跑
  FAILED = 'failed', // 失败后可重跑
  NEVER = 'never', // 皆不可重跑
}

export enum SchTimeOutStrategy {
  ALARM = 'alarm',
  FAIL = 'fail',
}

export enum SchPriority {
  LOW = 1,
  MIDDLE = 2,
  HIGH = 3,
}

export enum StatementState {
  WAITING = 'waiting',
  RUNNING = 'running',
  AVAILABLE = 'available',
  ERROR = 'error',
  CANCELLING = 'cancelling',
  CANCELED = 'cancelled',
}

export enum JobStatus {
  READY = 1,
  RUNNING = 2,
  FAIL = 6,
  SUCCESS = 7,
  OTHER = -1,
}

export enum AutoCompletionLangs {
  SQL = 'SQL',
  PYTHON = 'PYTHON',
}

export enum ExecEngine {
  SPARK = 'SPARK',
  SQOOP = 'SQOOP',
  KYLIN = 'KYLIN',
}
