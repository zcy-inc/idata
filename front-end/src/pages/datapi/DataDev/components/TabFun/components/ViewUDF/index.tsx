import React, { useEffect, useState } from 'react';
import { Descriptions } from 'antd';
import type { FC } from 'react';

import { Folder, UDF } from '@/types/datadev';
import { FolderBelong } from '@/constants/datadev';
import Title from '@/components/Title';
import { getFolders } from '@/services/datadev';

interface ViewUDFProps {
  data?: UDF;
}

const { Item } = Descriptions;

const ViewUDF: FC<ViewUDFProps> = ({ data }) => {
  const [folders, setFolders] = useState<Folder[]>([]);

  useEffect(() => {
    getFolders({ belong: FolderBelong.DEVFUN }).then((res) => setFolders(res.data));
  }, []);

  return (
    <div>
      <Title>函数信息</Title>
      <Descriptions colon={false} column={1}>
        <Item label="函数名称">{data?.udfName || '-'}</Item>
        <Item label="函数类型">{data?.udfType || '-'}</Item>
        <Item label="函数代码">
          <a href={data?.hdfsPath} download={data?.fileName}>
            下载
          </a>
        </Item>
        <Item label="返回类型">{data?.returnType || '-'}</Item>
        <Item label="返回值">{data?.returnSample || '-'}</Item>
        <Item label="目标文件夹">{folders.find((_) => _.id === data?.folderId)?.name || '-'}</Item>
        <Item label="描述">{data?.description || '-'}</Item>
        <Item label="命令格式">{data?.commandFormat || '-'}</Item>
        <Item label="示例">{data?.udfSample || '-'}</Item>
      </Descriptions>
    </div>
  );
};

export default ViewUDF;
