import React, { useEffect, useImperativeHandle, useState } from 'react';
import ProForm, { ProFormSwitch } from '@ant-design/pro-form';
import { Form } from 'antd';
import type { FC } from 'react';

const AddMonitor: FC<{ id: number }> = ({ id }, ref) => {
  const [form] = Form.useForm();
  useImperativeHandle(ref, () => ({
    handleSubmit
  }))

  const handleSubmit = () => {
    return form.validateFields();
  }

  return (
    <ProForm form={form} colon={false} submitter={false} layout="horizontal">
      <ProFormSwitch name="forceInit" label="表强制初始化" fieldProps={{ defaultChecked: true }} />
    </ProForm>
  );
};

export default AddMonitor;
