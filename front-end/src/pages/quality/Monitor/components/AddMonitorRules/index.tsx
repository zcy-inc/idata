import React, { useImperativeHandle } from 'react';
import ProForm, { ProFormSelect, ProFormText, ProFormRadio } from '@ant-design/pro-form';
import { Form } from 'antd';
import type { FC } from 'react';
import styles from './index.less';
import { monitorObjList, ruleTypeList, alarmLevelList } from '@/constants/quality'


const EditMonitor: FC = ({}, ref) => {
  const [form] = Form.useForm();

  useImperativeHandle(ref, () => ({
    handleSubmit
  }))

  const handleSubmit = () => {
    return form.validateFields()
  }


  return (
    <ProForm form={form} colon={false} className={styles.form} submitter={false} layout="horizontal">
      <ProFormRadio.Group
        label="监控对象"
        name="monitorObj"
        initialValue="table"
        options={monitorObjList}
      />
      <ProFormSelect
        label="规则类型"
        name="ruleType"
        options={ruleTypeList}
        placeholder="请选择"
        rules={[{ required: true, message: '请选择规则类型' }]}
      />
      <ProFormSelect
        name="name"
        label="规则名称"
        rules={[{ required: true, message: '请选择规则名称' }]}
        options={[]}
        placeholder="请选择"
      />
       <ProFormSelect
        name="alarmReceiver"
        label="告知人"
        options={[]}
        placeholder="请选择"
      />
       <ProFormRadio.Group
        label="告警等级"
        name="monitorObj"
        initialValue={1}
        options={alarmLevelList}
      />
    </ProForm>
  );
};

export default EditMonitor;
