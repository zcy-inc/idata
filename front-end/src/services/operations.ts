import type { TaskTypes } from '@/constants/datadev';
import type { ClusterState } from '@/constants/operations';
import type {
  Cluster,
  ClusterListItem,
  ConsumeResourceItem,
  ConsumeTimeItem,
  JobHistoryItem,
  JobHistoryGanttItem,
  OperationLine,
  OperationOverview,
  Overhang,
  ScheduleListItem,
  UsageOverview,
  DagItem,
  EnumItem,
  StreamListItem
} from '@/types/operations';
import { request } from 'umi';
import type { DefaultResponse } from './global';

/* ========== 运维看板 ========== */
/**
 * 获取当前cpu的使用情况
 */
export async function getUsageOverview() {
  return request<DefaultResponse & { data: UsageOverview }>('/api/p1/ops/dashboard/usageOverview', {
    method: 'GET',
  });
}

/**
 * 获取作业调度信息的列表
 */
export async function getScheduleList(data: {
  pageSize: number;
  pageNum: number;
  condition: {
    state: number; // 成功:7 / 队列:1 / 运行中:2 / 失败:6 / 其他:-1
  };
}) {
  return request<
    DefaultResponse & {
      data: {
        total: number;
        content: ScheduleListItem[];
      };
    }
  >('/api/p1/ops/dashboard/page/jobSchedule', {
    method: 'POST',
    data,
  });
}

/**
 * 获取作业集群信息的列表
 */
export async function getClusterList(data: {
  pageSize: number;
  pageNum: number;
  condition: {
    state: number; // 成功:7 / 队列:1 / 运行中:2 / 失败:6 / 其他:-1
  };
}) {
  return request<
    DefaultResponse & {
      data: {
        total: number;
        content: ClusterListItem[];
      };
    }
  >('/api/p1/ops/dashboard/page/yarn', {
    method: 'POST',
    data,
  });
}

/**
 * 获取作业调度信息（总数、饼图）
 */
export async function getScheduleOverview() {
  return request<DefaultResponse & { data: OperationOverview }>(
    '/api/p1/ops/dashboard/jobSchedule/overview',
    {
      method: 'GET',
    },
  );
}

/**
 * 获取作业集群信息（总数、饼图）
 */
export async function getClusterOverview() {
  return request<DefaultResponse & { data: OperationOverview }>(
    '/api/p1/ops/dashboard/yarn/overview',
    {
      method: 'GET',
    },
  );
}

/**
 * 获取作业 调度/集群 信息（折线图）
 */
export async function getOperationLine(params: { scope: 'ds' | 'yarn' }) {
  return request<DefaultResponse & { data: OperationLine }>('/api/p1/ops/dashboard/job/stackLine', {
    method: 'GET',
    params,
  });
}

/**
 * 获取作业耗时TOP10
 */
export async function getTimeTop10(params: { recentDays: 1 | 7 | 30 }) {
  return request<DefaultResponse & { data: ConsumeTimeItem[] }>(
    '/api/p1/ops/dashboard/rank/timeConsume',
    {
      method: 'GET',
      params,
    },
  );
}

/**
 * 获取作业耗资源TOP10
 */
export async function getResourceTop10(params: { recentDays: 1 | 7 | 30 }) {
  return request<DefaultResponse & { data: ConsumeResourceItem[] }>(
    '/api/p1/ops/dashboard/rank/resourceConsume',
    {
      method: 'GET',
      params,
    },
  );
}

/* ========== 作业历史 ========== */
/**
 * 获取作业历史
 */
export async function getJobHistory(data: {
  pageNum: number;
  pageSize: number;
  condition: {
    startDateBegin: string;
    startDateEnd: string;
    finishDateBegin: string;
    finishDateEnd: string;
    jobName: string;
    jobStatus: string;
  };
}) {
  return request<
    DefaultResponse & {
      data: {
        content: JobHistoryItem[];
        total: number;
        pageNum: number;
        pageSize: number;
        pages: number;
      };
    }
  >('/api/p1/ops/dashboard/page/jobHistory', {
    method: 'POST',
    data,
  });
}

/**
 * 获取作业甘特图数据
 */
 export async function getJobGantt(data: {
  pageNum: number;
  pageSize: number;
  condition: {
    startDateBegin: string;
    startDateEnd: string;
    finishDateBegin: string;
    finishDateEnd: string;
    jobName: string;
    jobStatus: string;
  };
}) {
  return request<
    DefaultResponse & {
      data: {
        content: JobHistoryGanttItem[];
        total: number;
        pageNum: number;
        pageSize: number;
        pages: number;
      };
    }
  >('/api/p1/ops/dashboard/page/gantt/jobHistory', {
    method: 'POST',
    data,
  });
}

/**
 * 获取dag列表
 */
 export async function getDagList() {
  return request<
  DefaultResponse & {
    data: DagItem [];
  }
>('/api/p1/dev/dags/info', {
    method: 'GET',
  });
}
/**
 * 获取数仓分层列表
 */
 export async function getEnumgList() {
  return request<
  DefaultResponse & {
    data: EnumItem [];
  }
>('/api/p1/dev/enumValues', {
    method: 'GET',
    params: {
      enumCode: 'dwLayerEnum:ENUM'
    }
  });
}

/* ========== 集群监控 ========== */
/**
 * 获取集群列表
 */
export async function getClusters(params: { state: ClusterState }) {
  return request<DefaultResponse & { data: Cluster[] }>('/api/p1/ops/clusters/apps', {
    method: 'GET',
    params,
  });
}

/**
 * 获取集群列表
 */
export async function stopCluster(params: { appId: number }) {
  return request<DefaultResponse & { data: Cluster[] }>(
    `/api/p1/ops/clusters/apps/${params.appId}/stop`,
    {
      method: 'PUT',
      params,
    },
  );
}

/* ========== 任务监控 ========== */
/**
 * 获取任务列表
 */
export async function getOverhang(params: {
  name: string;
  jobType: TaskTypes;
  offset: number;
  limit: number;
}) {
  return request<
    DefaultResponse & {
      data: {
        fetchTime: string;
        overhangJobDtoPage: {
          total: number;
          content: Overhang[];
        };
      };
    }
  >('/api/p1/dev/jobs/overhangPage', {
    method: 'GET',
    params,
  });
}

// 获取实时作业列表
export async function getStreamJobs(data: {
  jobNamePattern: string;
  owner: string;
  statusList: string [];
  offset: number;
  limit: number;
}) {
  return request<
    DefaultResponse& {
     data: {
      content: StreamListItem [];
      total: number
     }
    }
  >('/api/p1/opt/stream/instances/page', {
    method: 'POST',
    data,
  });
}

// 启动作业
export async function startJob(data: {
  id: number;
  initDITables: string[];
  forceInit: boolean;
}) {
  const {id , ...rest} = data;
  return request(`/api/p1/opt/stream/instances/${id}/start`, {
    method: 'POST',
    data: rest
  });
}

// 停止作业
export async function stopJob(data: {
  id: number;
}) {
  return request(`/api/p1/opt/stream/instances/${data.id}/stop`, {
    method: 'POST'
  });
}

// 停止作业
export async function destoryJob(data: {
  id: number;
}) {
  return request(`/api/p1/opt/stream/instances/${data.id}/destroy`, {
    method: 'POST'
  });
}

// 查询强制初始化表集合
export async function getForceList(data: {
  id: number;
}) {
  return request(`/api/p1/opt/stream/instances/${data.id}/forceInitTables`, {
    method: 'GET'
  });
}

// 查询启动参数配置
export async function startParamConfig(data: {
  id: number;
}) {
  return request(`/api/p1/opt/stream/instances/${data.id}/startParamConfig`, {
    method: 'GET'
  });
}
