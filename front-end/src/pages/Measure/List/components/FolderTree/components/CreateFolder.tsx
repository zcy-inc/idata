import React, { useEffect, useState, useImperativeHandle, useRef } from 'react';
import ProForm, { ProFormText, ProFormCascader } from '@ant-design/pro-form';
import type { ProFormInstance } from '@ant-design/pro-form';
import { Form } from 'antd';

import { createFolder, updateFolder, getFolderTree } from '@/services/measure';
import { TreeNodeOption } from '@/types/datapi';
import { TreeNodeType } from '@/constants/datapi';

export interface CreateFolderProps {
  visible: boolean;
  onCancel: () => void;
}
interface FlatTreeNodeOption extends TreeNodeOption {
  label: string;
  value: string;
}

const rules = [{ required: true, message: '必填' }];

const CreateFolder = ({ node }: any, ref: React.Ref<unknown> | undefined) => {
  const [folders, setFolders] = useState<FlatTreeNodeOption[]>([]);
  const formRef = useRef<
    ProFormInstance<{
      folderName: string;
      parentId: string;
    }>
  >();
  const [form] = Form.useForm();
  useImperativeHandle(ref, () => ({
    handleSubmit
  }));
  useEffect(() => {
    if(node.folderId) {
      form.setFieldsValue({
        folderName: node.name,
        parentId: node.parentId
      })
    }
      getTreeData();
  }, []);

  const getTreeData = (treeNodeName?: string) => {
    getFolderTree({ devTreeType: TreeNodeType.METRIC_LABEL, treeNodeName }).then((res) => {
      setFolders(res.data);
    });
  }

  const handleSubmit = () => {
    return form.validateFields().then((values: any) => {
      if(node.folderId) {
        return update(values);
      }
      return create(values);
    })
  }

  const update = (form: { folderName: string; parentId: string; }) => {
    const params = {
      folderName: form.folderName,
      parentId: form.parentId[form.parentId.length - 1],
      id: node.folderId
    };
    return updateFolder(params);
  }

  const create = (form: { folderName: string; parentId: string; }) => {
    const params = {
      folderName: form.folderName,
      parentId: form.parentId[form.parentId.length - 1],
      folderType: TreeNodeType.METRIC_LABEL
    };
    return createFolder(params);
  }

  return (
    <ProForm
      form={form}
      formRef={formRef}
      colon={false}
      labelCol={{ span: 5 }}
      layout="horizontal"
      submitter={false}
    >
      <ProFormText name="folderName" label="文件夹名称" placeholder="请输入名称" rules={rules} />
      <ProFormCascader
        name="parentId"
        label="位置"
        placeholder="根目录"
        fieldProps={{
          options: folders,
          changeOnSelect: true,
          fieldNames: { label: 'name', value: 'folderId', children: 'children' }
        }} />
    </ProForm>
  );
};

export default CreateFolder;
