export enum FolderTypes {
  FUNCTION = 'FUNCTION', // 功能性文件夹
  FOLDER = 'FOLDER', // 普通文件夹
  RECORD = 'RECORD', // 业务数据
}

export enum FolderBelong {
  DESIGN = 'DESIGN', // 数仓设计
  DESIGNTABLE = 'DESIGN.TABLE', // 数仓设计-表
  DESIGNLABEL = 'DESIGN.LABEL', // 数仓设计-标签
  DESIGNENUM = 'DESIGN.ENUM', // 数仓设计-枚举
  DAG = 'DAG', // DAG
  DI = 'DI', // 数据集成
  DEV = 'DEV', // 数据开发
  DEVJOB = 'DEV.JOB', // 数据开发-作业
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
}

export enum TaskTypes {
  DI_BATCH = 'DI_BATCH', // 离线同步
  DI_STREAM = 'DI_STREAM', // 实时同步
}

export enum SrcReadMode {
  ALL = 'all', // 全量
  INCREMENTAL = 'incremental', // 增量
}

export enum SrcWriteMode {
  INIT = 'init', // 新建表
  OVERRIDE = 'override', // 覆盖表
}

export enum VersionStatus {
  EDITING = 0,
  TO_BE_PUBLISHED = 1,
  PUBLISHED = 2,
  REJECTED = 4,
  ARCHIVED = 9,
}

export enum SchRerunMode {
  ALWAYS = 'always', // 皆可重跑
  FAILED = 'failed', // 失败后可重跑
  NEVER = 'never', // 皆不可重跑
}
