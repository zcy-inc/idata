import React, { useImperativeHandle, useState } from 'react';
import ProForm, { ProFormSelect, ProFormText, ProFormRadio } from '@ant-design/pro-form';
import { Form } from 'antd';
import type { FC } from 'react';
import styles from './index.less';
import { monitorObjList, ruleTypeList, alarmLevelList } from '@/constants/quality';
import { getTemplateList } from '@/services/quality';
import type { MonitorObj, RuleType, TemplateItem } from '@/types/quality';


const AddMonitorRule: FC = ({}, ref) => {
  const [form] = Form.useForm();
  const [templateList, setTemplateList] = useState<{label: string; value: number} []>([]);

  useImperativeHandle(ref, () => ({
    handleSubmit
  }))

  const handleSubmit = () => {
    return form.validateFields()
  }

  const getRuleNameOptions = (values: { monitorObj: MonitorObj; ruleType: RuleType; }) => {
    console.log(values);
    getTemplateList({
      monitorObj: values.monitorObj,
      type: values.ruleType,
      curPage: 1,
      pageSize: 10000
    }).then(res => {
      setTemplateList(res.data.data.map(item => ({
        label: item.name,
        value: item.templateId
      })))
    })
  }


  return (
    <ProForm form={form} colon={false} className={styles.form} submitter={false} layout="horizontal">
      <ProFormRadio.Group
        label="监控对象"
        name="monitorObj"
        initialValue="table"
        options={monitorObjList}
        fieldProps={{
          onChange: e => getRuleNameOptions({monitorObj: e.target.value, ruleType: form.getFieldsValue().ruleType})
        }}
      />
      <ProFormSelect
        label="规则类型"
        name="ruleType"
        options={ruleTypeList}
        placeholder="请选择"
        rules={[{ required: true, message: '请选择规则类型' }]}
        fieldProps={{
          onChange: val => getRuleNameOptions({monitorObj: form.getFieldsValue().monitorObj, ruleType: val})
        }}
      />
      <ProFormSelect
        name="name"
        label="规则名称"
        rules={[{ required: true, message: '请选择规则名称' }]}
        options={templateList}
        placeholder="请选择"
      />
       <ProFormSelect
        name="alarmReceiver"
        label="告知人"
        tooltip="默认通知数仓表所属人"
        mode="multiple"
        options={templateList}
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

export default AddMonitorRule;
