import { Environments } from '@/constants/datasource';
import { PublishStatus } from '@/constants/task';

export interface TaskListItem {
  id: number;
  jobId: number;
  jobName: string;
  jobContentId: number;
  jobContentVersion: number;
  jobTypeCode: string;
  dwLayerCode: string;
  dwLayerValue: string;
  environment: Environments;
  publishStatus: PublishStatus;
  submitRemark: string;
  approveOperator: string;
  approveTime: string;
  approveRemark: string;
}
