import React, { FC } from 'react';
import { useRequest } from 'ahooks';
import { Form } from 'antd';
import { FolderBelong } from '@/constants/datadev';
import { getSpecifiedFunctionTree } from '@/services/datadev';
import { CascaderSelect, CascaderSelectProps } from '@/components';

export const cascaderSelect: FC<{ belongFunctions: FolderBelong[] } & CascaderSelectProps> = ({
  belongFunctions,
  includeFunFolders = false,
  placeholder="请选择",
  ...rest
}) => {
  const { data: folderTree = [] } = useRequest(
    () => getSpecifiedFunctionTree({ belongFunctions, includeFunFolders }),
    {
      refreshDeps: [belongFunctions],
    },
  );
  return (
      <CascaderSelect placeholder={placeholder} size="large" options={folderTree} {...rest} />
  );
};

export const FolderFormItem: FC<{ belongFunctions: FolderBelong[] } & CascaderSelectProps> = (props) => {
  return (
    <Form.Item name="folderId" label={props?.label || "目标文件夹"} rules={[{ required: true, message: '请选择' }]}>
      {cascaderSelect(props)}
    </Form.Item>
  )
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

// v1.7 编辑：“位置”允许选择层级文件夹
export const DESIGNTABLEFolderFormItem: FC<CascaderSelectProps> = (props) => {
  return <FolderFormItem belongFunctions={[FolderBelong.DESIGNTABLE]} {...props} />;
};

// v1.7 新建：“位置”允许选择层级文件夹
export const LOCATIONFolderFormItem: FC<CascaderSelectProps> = (props) => {
  return <Form.Item name="parentId" label="位置" rules={[{ required: true, message: '请选择根目录' }]}>
    {cascaderSelect({...props, includeFunFolders:false, placeholder:"根目录" })}
  </Form.Item>
};