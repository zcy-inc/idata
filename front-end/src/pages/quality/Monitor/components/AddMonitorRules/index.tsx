import React, { useEffect, useImperativeHandle, useState } from 'react';
import ProForm, { ProFormSelect, ProFormText, ProFormRadio, ProFormTimePicker, ProFormFieldSet } from '@ant-design/pro-form';
import { Form, Spin } from 'antd';
import type { FC } from 'react';
import styles from './index.less';
import { monitorObjList, ruleTypeList, alarmLevelList, compareOptions } from '@/constants/quality';
import { getTemplateList, getRecivers, getMonitorRule } from '@/services/quality';
import type { MonitorObj, RuleType } from '@/types/quality';
import { render } from 'react-dom';


const AddMonitorRule: FC<{id: number}> = ({id}, ref) => {
  const [form] = Form.useForm();
  const [templateList, setTemplateList] = useState<{label: string; value: string} []>([]);
  const [recivers, setRecivers] = useState<{label: string; value: number} []>([]);
  const [loading, setLoading] = useState(false);
  const [ruleName, setRuleName] = useState('');
  useImperativeHandle(ref, () => ({
    handleSubmit
  }));

  useEffect(() => {
    const init = async () => {
      setLoading(true);
      await getReciverList();
      if(id) {
        await getMonitorRule({id}).then(res => {
          form.setFieldsValue({
            ...res.data,
            alarmReceivers: res.data?.alarmReceivers?.split(',') || []
          });
          return getRuleNameOptions(res.data);
        });
      }
      setLoading(false);
    }
    init();
  }, [])

  const getReciverList = (name?: string) => {
    return getRecivers({name}).then(res => {
      setRecivers(res.data?.map((item: { nickname: any; }) => ({label: item.nickname, value: item.nickname})));
    })
  }

  const handleSubmit = () => {
    return form.validateFields()
  }

  // 获取规则名称列表
  const getRuleNameOptions = (values: { monitorObj: MonitorObj; ruleType: RuleType; }) => {
    getTemplateList({
      monitorObj: values.monitorObj,
      type: values.ruleType,
      curPage: 1,
      pageSize: 10000
    }).then(res => {
      setTemplateList(res.data.data.map(item => ({
        label: item.name,
        value: item.content
      })))
    })
  }

  const renderRuleContent = () => {
    console.log(ruleName);
    if(ruleName === 'table_output_time') {
      return <ProFormTimePicker
        label=" "
        rules={[{ required: true, message: '请选择表产出时间' }]}
        name="content"
        placeholder="请选择表产出时间"
      />
    } else if(ruleName === 'field_enum_content') {
      <ProFormText
        label=" "
        rules={[{ required: true, message: '请输入字段枚举值' }]}
        name="content"
        placeholder="请输入字段枚举值，用英文逗号隔开"
      />
    } else if(ruleName === 'field_enum_count') {
      <ProFormText
        label=" "
        rules={[{ required: true, message: '请输入字段枚举数量' }]}
        name="fixValue"
        placeholder="请输入字段枚举数量"
      />
    } else if(ruleName === 'field_data_range') {
      return <ProFormFieldSet
        name="list"
        label=" "
        transform={(value: any) => ({ rangeStart: value[0], rangeEnd: value[1] })}
      >
        <ProFormText width="md" />
        -
        <ProFormText width="md" />
      </ProFormFieldSet>
      
    } else if(ruleName === 'table_row') {
      return <>
      
      </>
    }
    return null;
  }

  return (
    <Spin spinning={loading}>
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
          fieldProps={{
            onChange: val => setRuleName(val)
          }}
        />
        {renderRuleContent()}
        <ProFormSelect
          name="alarmReceivers"
          label="告知人"
          tooltip="默认通知数仓表所属人"
          mode="multiple"
          options={recivers}
          placeholder="请选择"
        />
        <ProFormRadio.Group
          label="告警等级"
          name="alarmLevel"
          initialValue={1}
          options={alarmLevelList}
        />
      </ProForm>
    </Spin>
  );
};

export default AddMonitorRule;
