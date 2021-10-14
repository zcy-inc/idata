import {
  FolderBelong,
  FolderTypes,
  PeriodRange,
  SchRerunMode,
  SrcReadMode,
  SrcWriteMode,
  TaskCategory,
  TaskTypes,
  TriggerMode,
  VersionStatus,
} from '@/constants/datadev';
import { DataSourceTypes, Environments } from '@/constants/datasource';

// 树的节点类型
export interface TreeNode {
  cid: string; // type + belong + id
  id: number; // 可能存在重复，用作树的唯一标识符时请使用cid
  name: string;
  type: FolderTypes;
  belong: FolderBelong;
  parentCid?: string;
  parentId?: number;
  children?: TreeNode[];
  // 以下是树组件需要的属性
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
}

export interface TaskVersion {
  jobId: number;
  version: number;
  versionStatus: VersionStatus;
  environment: string;
}

export interface TaskContent {
  id: number;
  jobId: number; // 作业id
  version: number; // 作业版本号
  srcDataSourceType: DataSourceTypes; // 数据来源-数据源类型
  srcDataSourceId: numebr; // 数据来源-数据源id
  srcTables: string; // 数据来源-表
  srcReadFilter: string; // 数据来源-过滤条件
  srcReadShardKey: string; // 数据来源-切分键
  srcReadMode: SrcReadMode; // 数据来源-读取模式
  destDataSourceType: DataSourceTypes; // 数据去向-数据源类型
  destDataSourceId: number; // 数据去向-数据源id
  destTable: string; // 数据去向-数仓表的表名
  destBeforeWrite: string; // 数据去向-写入前语句
  destAfterWrite: string; // 数据去向-写入后语句
  destWriteMode: SrcWriteMode; //数据去向-写入模式
  srcCols: MappedColumn[];
  destCols: MappedColumn[];
  contentHash: string; // 作业内容hash，查询作业内容时返回，保存新的作业内容时需将该值一起提交，用于判断作业内容是否有变更
}

export interface MappedColumn {
  name: string;
  dataType: string;
  primaryKey: boolean;
  mappedColumn?: MappedColumn;
}

export interface TaskTable {
  tableName: string;
}

export interface TaskConfig {
  id: number;
  jobId: number;
  environment: Environments;
  schDagId: number; // 调度配置-dag编号
  schRerunMode: SchRerunMode; // 调度配置-重跑配置
  schTimeOut: number; // 调度配置-超时时间
  schDryRun: number; // 调度配置-是否空跑
  execQueue: string; // 运行配置-队列
  execMaxParallelism: number; // 运行配置-作业最大并发数，配置为0时表示使用默认并发数
  execWarnLevel: string; // 运行配置-告警等级 时光的接口获取
}

export interface DAGListItem {
  id: number;
  name: string;
  dwLayerCode: string;
  status: 0 | 1;
  remark: string;
  folderId: string;
}
