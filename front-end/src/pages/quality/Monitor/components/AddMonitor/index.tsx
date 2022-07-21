import React, { useImperativeHandle } from 'react';
import ProForm, { ProFormText } from '@ant-design/pro-form';
import { Form } from 'antd';
import type { FC } from 'react';
import TableSelect from '../TableSelect';
import styles from './index.less';


const AddMonitor: FC = ({}, ref) => {
  const [form] = Form.useForm();

  useImperativeHandle(ref, () => ({
    handleSubmit
  }))

  const handleSubmit = () => {
    return form.validateFields();
  }

  return (
    <ProForm form={form} colon={false} className={styles.form} submitter={false} layout="horizontal">
      <TableSelect />
      <ProFormText
        name="partitionExpr"
        label="时间分区表达式"
        rules={[{ required: true, message: '请输入时间分区表达式' }]}
        placeholder="请选择"
      />
    </ProForm>
  );
};

export default AddMonitor;
