import React, { useImperativeHandle, useState } from 'react';
import ProForm, { ProFormText } from '@ant-design/pro-form';
import { Form } from 'antd';
import type { FC } from 'react';
import TableSelect from '../TableSelect';
import styles from './index.less';

const { Item } = Form;
const AddMonitor: FC = ({}, ref) => {
  const [form] = Form.useForm();
  const [partitioned, setPartitioned] = useState(false);
  useImperativeHandle(ref, () => ({
    handleSubmit
  }))

  const handleSubmit = () => {
    return form.validateFields();
  }

  const handleTableChange = (_: string, p: boolean) => {
    setPartitioned(p)
  }

  return (
    <ProForm form={form} colon={false} className={styles.form} submitter={false} layout="horizontal">
      <Item
        name="tableName"
        label="适用表名"
        rules={[{ required: true, message: '请选择适用表名' }]}
      >
        <TableSelect onChange={handleTableChange} />
      </Item>
      {partitioned && <ProFormText
        name="partitionExpr"
        label="时间分区表达式"
        rules={[{ required: true, message: '请输入时间分区表达式' }]}
        placeholder="请选择"
      />}
      
    </ProForm>
  );
};

export default AddMonitor;
