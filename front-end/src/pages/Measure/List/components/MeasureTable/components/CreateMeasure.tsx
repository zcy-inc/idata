import React, { useEffect, useState, useImperativeHandle, useRef } from 'react';
import ProForm, { ProFormSelect } from '@ant-design/pro-form';
import type { ProFormInstance } from '@ant-design/pro-form';
import { Form } from 'antd';

import { getFolders } from '@/services/measure';
import { TreeNodeOption } from '@/types/datapi';

export interface CreateFolderProps {
  visible: boolean;
  onCancel: () => void;
}
interface FlatTreeNodeOption extends TreeNodeOption {
  label: string;
  value: string;
}

const rules = [{ required: true, message: '必填' }];
const MetricTypeOps = [
  { label: '原子指标', value: 'ATOMIC_METRIC_LABEL' },
  { label: '派生指标', value: 'DERIVE_METRIC_LABEL' },
];

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
    getFolders()
      .then((res) => {
        const fd = res.data?.map((_: any) => ({
          label: _.folderName,
          value: `${_.id}`,
        }));
        setFolders(fd);
      })
      .catch((err) => {});
  }, []);

  const handleSubmit = () => {
    return form.validateFields();
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
      <ProFormSelect name="labelTag" label="指标类型" rules={rules} options={MetricTypeOps} />
      <ProFormSelect
        name="parentId"
        label="位置"
        placeholder="根目录"
        options={folders}
        rules={rules}
      />
    </ProForm>
  );
};

export default CreateFolder;
