import React, { FC } from 'react';
import ActualContent from '@/pages/datapi/DataDev/components/TabTaskActual/components/Content';
import { Spin } from 'antd';
import { useRequest } from 'ahooks';
import { getDIJobBasicInfo } from '@/services/datadev';
export interface CreateFolderProps {
  jobId: number;
  version: string
}

const Detail:FC<CreateFolderProps> = ({ jobId, version }, ref: any) => {
  // 获取DI基础信息
  const { data: basicInfo, loading } = useRequest(() =>
   getDIJobBasicInfo(jobId)
 );
  return (
    <Spin spinning={loading}>
     {basicInfo && <ActualContent jobId={jobId} version={version} isView basicInfo={basicInfo} />}
    </Spin>
  );
};

export default Detail;
