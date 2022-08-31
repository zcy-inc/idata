import React, { FC } from 'react';
import ActualContent from '@/pages/datapi/DataDev/components/TabTaskActual/components/Content';

export interface CreateFolderProps {
  visible: boolean;
  onCancel: () => void;
}

const Detail:FC<{id: number; version: string}> = ({ id, version }, ref: any) => {

  return (
    <ActualContent jobId={id} version={version} isView />
  );
};

export default Detail;
