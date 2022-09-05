import React from 'react';
import { Skeleton } from 'antd';
import { useRequest } from 'ahooks';
import type { FC } from 'react';
import { IPane } from '@/models/datadev';
import { getDIJobBasicInfo } from '@/services/datadev';
import TaskBasic from './components/TaskBasic';

export interface TabTaskProps {
  pane: IPane;
}


const TabTask: FC<TabTaskProps> = ({ pane }) => {
  const { id: jobId } = pane;
  // 获取DI基础信息
  const { data: basicInfo, refresh: refreshBasicInfo, loading: basicInfoLoading } = useRequest(() =>
    getDIJobBasicInfo(jobId),
  );

  return (
    <Skeleton loading={basicInfoLoading}>
      { !basicInfoLoading && basicInfo && <TaskBasic pane={pane} basicInfo={basicInfo} refreshBasicInfo={refreshBasicInfo} />}
    </Skeleton>
  );
};

export default TabTask;
