export enum JobOperationStatus {
  SUCCESS = 'success',
  READY = 'ready',
  RUNNING = 'running',
  FAILURE = 'failure',
  OTHER = 'other',
}

export enum ClusterState {
  NEW = 'NEW',
  NEW_SAVING = 'NEW_SAVING',
  SUBMITTED = 'SUBMITTED',
  ACCEPTED = 'ACCEPTED',
  RUNNING = 'RUNNING',
  FINISHED = 'FINISHED',
  FAILED = 'FAILED',
  KILLED = 'NEKILLEDW',
}

export const statusList = [
 {label: "待启动", value: 0},
 {label: "启动中", value: 1},
 {label: "运行中", value: 2},
 {label: "已失败", value: 7},
 {label: "已停止", value: 8},
 {label: "已下线", value: 9},
]
