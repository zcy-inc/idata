import React, { FC } from 'react';
import { useRequest } from 'ahooks';
import { Form } from 'antd';
import { FolderBelong } from '@/constants/datadev';
import { getSpecifiedFunctionTree } from '@/services/datadev';
import { CascaderSelect, CascaderSelectProps } from '@/components';

export const FolderFormItem: FC<{ belongFunctions: FolderBelong[] } & CascaderSelectProps> = ({
  belongFunctions,
  ...rest
}) => {
  const { data: folderTree = [] } = useRequest(
    () => getSpecifiedFunctionTree({ belongFunctions }),
    {
      refreshDeps: [belongFunctions],
    },
  );
  return (
    <Form.Item name="folderId" label="目标文件夹" rules={[{ required: true, message: '请选择' }]}>
      <CascaderSelect placeholder="请选择" size="large" options={folderTree} {...rest} />
    </Form.Item>
  );
};

export const DAGFolderFormItem: FC<CascaderSelectProps> = (props) => {
  return <FolderFormItem belongFunctions={[FolderBelong.DAG]} {...props} />;
};

export const DIFolderFormItem: FC<CascaderSelectProps> = (props) => {
  return <FolderFormItem belongFunctions={[FolderBelong.DI]} {...props} />;
};

export const DEVJOBFolderFormItem: FC<CascaderSelectProps> = (props) => {
  return <FolderFormItem belongFunctions={[FolderBelong.DEVJOB]} {...props} />;
};

export const DEVFUNFolderFormItem: FC<CascaderSelectProps> = (props) => {
  return <FolderFormItem belongFunctions={[FolderBelong.DEVFUN]} {...props} />;
};
