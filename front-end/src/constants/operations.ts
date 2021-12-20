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
