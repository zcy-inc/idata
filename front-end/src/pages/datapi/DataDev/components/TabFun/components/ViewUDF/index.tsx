import React from 'react';
import { Descriptions } from 'antd';
import type { FC } from 'react';
import { UDF } from '@/types/datadev';
import { FolderBelong } from '@/constants/datadev';
import Title from '@/components/Title';
import { getWorkspacePrefix } from '@/utils/utils';

import { useFolderNames } from '../../../../../hooks/useFolderNames';

interface ViewUDFProps {
  data?: UDF;
}

const { Item } = Descriptions;

const ViewUDF: FC<ViewUDFProps> = ({ data }) => {
  const { folderNames } = useFolderNames([FolderBelong.DEVFUN], data?.folderId);
  const renderDownload = () => {
    let workspace = getWorkspacePrefix();
    if (workspace) {
      workspace = `/${workspace}`;
    }

    return (
      <a href={`${location.origin}${workspace}/api/p1/dev/udf/download/${data?.id}`} download>
        下载
      </a>
    );
  };

  return (
    <div>
      <Title>函数信息</Title>
      <Descriptions colon={false} column={1}>
        <Item label="函数名称">{data?.udfName || '-'}</Item>
        <Item label="函数类型">{data?.udfType || '-'}</Item>
        <Item label="函数代码">
          <div style={{ position: 'relative', width: '100%', height: 28 }}>
            {renderDownload()}
            <span
              style={{
                color: 'rgba(0, 0, 0, 0.45)',
                position: 'absolute',
                left: 0,
                top: 22,
                display: 'inline-block',
                width: '100%',
                whiteSpace: 'nowrap',
                overflow: 'hidden',
                textOverflow: 'ellipsis',
              }}
            >{`hdfs路径: ${data?.hdfsPath || '-'}`}</span>
          </div>
        </Item>
        <Item label="返回类型">{data?.returnType || '-'}</Item>
        <Item label="返回值">{data?.returnSample || '-'}</Item>
        <Item label="目标文件夹">{folderNames}</Item>
        <Item label="描述">{data?.description || '-'}</Item>
        <Item label="命令格式">{data?.commandFormat || '-'}</Item>
        <Item label="示例">{data?.udfSample || '-'}</Item>
      </Descriptions>
    </div>
  );
};

export default ViewUDF;
