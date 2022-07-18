import React, { useImperativeHandle } from 'react';
import ProForm, { ProFormText } from '@ant-design/pro-form';
import { Form } from 'antd';
import type { FC } from 'react';


const AddBaseline: FC = ({}, ref) => {
  const [form] = Form.useForm();

  useImperativeHandle(ref, () => ({
    handleSubmit
  }))

  const handleSubmit = () => {
    return form.validateFields()
  }


  return (
    <ProForm form={form} colon={false} submitter={false} layout="horizontal">
      <ProFormText
        name="name"
        label="基线名称"
        rules={[{ required: true, message: '请输入基线名称' }]}
        placeholder="请选择"
      />
    </ProForm>
  );
};

export default AddBaseline;
