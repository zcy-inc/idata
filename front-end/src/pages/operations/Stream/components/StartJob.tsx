import React, { useEffect, useImperativeHandle, useState } from 'react';
import ProForm, { ProFormSelect } from '@ant-design/pro-form';
import { Form } from 'antd';
import { getForceList } from '@/services/operations'
import type { FC } from 'react';

const AddMonitor: FC<{id: number}> = ({id}, ref) => {
  const [form] = Form.useForm();
  const [options, setOption] = useState([]);
  useImperativeHandle(ref, () => ({
    handleSubmit
  }))

  useEffect(() => {
    getForceList({id}).then(res => {
      setOption(res.data?.map((item: string) => ({label: item, value: item})))
    })
  }, []);

  const handleSubmit = () => {
    return form.validateFields();
  }
 

  return (
    <ProForm form={form} colon={false}  submitter={false} layout="horizontal">
     <ProFormSelect
        name="forceInitTables"
        label="表强制初始化"
        placeholder="请选择"
        options={options}
        fieldProps={{
          mode: 'multiple'
        }}
      />
    </ProForm>
  );
};

export default AddMonitor;
