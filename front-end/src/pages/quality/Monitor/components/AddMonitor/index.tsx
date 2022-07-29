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
        tooltip="当前只支持日期形式的分区，示例：若分区格式为yyyyMMdd，表为t+1的数据，则分区表达式填写为${yyyyMMdd-1}"
        name="partitionExpr"
        label="时间分区表达式"
        rules={[{ required: true, message: '请输入时间分区表达式' }]}
        placeholder="请选择"
      />}
      
    </ProForm>
  );
};

export default AddMonitor;
