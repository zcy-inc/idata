import React, { useEffect, useImperativeHandle, useState } from 'react';
import ProForm, { ProFormSelect, ProFormText, ProFormRadio, ProFormTimePicker, ProFormFieldSet } from '@ant-design/pro-form';
import { Form, Spin } from 'antd';
import type { FC } from 'react';
import styles from './index.less';
import { monitorObjList, ruleTypeList, alarmLevelList, compareOptions, compareObjOptions, outputTypeList } from '@/constants/quality';
import { getTemplateList, getRecivers, getMonitorRule, getFiledList, getTableOwners } from '@/services/quality';
import type { MonitorObj, RuleType } from '@/types/quality';


const AddMonitorRule: FC<{id: number; tableName: string}> = ({id, tableName}, ref) => {
  const [form] = Form.useForm();
  const [templateList, setTemplateList] = useState<{label: string; value: string} []>([]);
  const [recivers, setRecivers] = useState<{label: string; value: number} []>([]);
  const [loading, setLoading] = useState(false);
  const [formValues, setFormValues] = useState<Record<string, any>>({});
  const [fieldOptions, setFieldOptions] = useState<{label: string; value: string} []>([]);
  useImperativeHandle(ref, () => ({
    handleSubmit
  }));

  useEffect(() => {
    const init = async () => {
      setLoading(true);
      await getReciverList();
      if(id) {
        await getMonitorRule({id}).then(res => {
          const values = {
            ...res.data,
            alarmReceivers: res.data?.alarmReceivers?.split(',') || []
          }
          form.setFieldsValue(values);
          return onFormChanges(values);
        });

      } else {
        await getTableOwners({tableName}).then(res => {
          form.setFieldsValue({
            alarmReceivers: res.data
          })
        })
      }
      setLoading(false);
    }
    init();
  }, [])

  // 获取接受人列表
  const getReciverList = (name?: string) => {
    return getRecivers({name}).then(res => {
      setRecivers(res.data?.map((item: { nickname: any; }) => ({label: item.nickname, value: item.nickname})));
    })
  }

  const handleSubmit = () => {
    return form.validateFields()
  }

  const onFormChanges = (values: { monitorObj: MonitorObj; ruleType: RuleType; }) => {
    let newFormValues = {
      ...formValues,
      ...values
    }
    // 获取规则名称列表
    if(values.monitorObj || values.ruleType && newFormValues.ruleType &&  newFormValues.monitorObj && newFormValues.ruleType !== 'custom') {
      getTemplateList({
        monitorObj: newFormValues.monitorObj,
        type: newFormValues.ruleType,
        curPage: 1,
        pageSize: 10000
      }).then(res => {
        setTemplateList(res.data.data.map(item => ({
          label: item.name,
          value: item.content
        })))
      })
    }

    // 获取字段选择下拉列表
    if((values.ruleType === 'system' && newFormValues.monitorObj === 'field') || (values.monitorObj === 'field' && newFormValues.ruleType === 'system')) {
      getFiledList({tableName}).then(res => {
        setFieldOptions(res.data?.map(item => ({label: item.name, value: item.name})) || []);
      })
    }
    setFormValues(newFormValues)
  }

  // 模板规则渲染
  const renderTemplateRules = () => {
    const checkType = formValues.checkType;
    return <>
    <ProFormSelect
      label=" "
      name="compareType"
      options={compareOptions}
      placeholder="请选择"
      rules={[{ required: true, message: '必填' }]}
    />
    <ProFormSelect
      label=" "
      name="checkType"
      options={compareObjOptions}
      placeholder="请选择"
      rules={[{ required: true, message: '必填' }]}
    />
      {checkType === 'abs'?
        <ProFormText
          placeholder="请选择"
          name="fixValue"
          label=" "
          rules={[{ required: true, message: '必填' }]}
        /> : checkType === 'pre_period' ?
        <ProFormFieldSet
          name="pre_period"
          label=" "
          transform={(value: any) => ({ rangeStart: value[0], rangeEnd: value[1] })}
        >
          <ProFormText
            placeholder="请选择"
            rules={[{ required: true, message: '必填' }]}
            fieldProps={{
              suffix: "%"
            }}
          />
          <span style={{lineHeight: '34px'}}>~</span>
          <ProFormText
            placeholder="请选择"
            rules={[{ required: true, message: '必填' }]}
            fieldProps={{
              suffix: "%"
            }}
          />
      </ProFormFieldSet> : null
    }
  </>
  }

  // 内置规则渲染
  const renderSystemRules = () => {
    const ruleName = formValues.name;
    if(ruleName === 'table_output_time') {
      return <ProFormTimePicker
        label=" "
        rules={[{ required: true, message: '请选择表产出时间' }]}
        name="content"
        placeholder="请选择表产出时间"
      />
    } else if(ruleName === 'field_enum_content') {
      return <ProFormText
        label=" "
        rules={[{ required: true, message: '请输入字段枚举值' }]}
        name="content"
        placeholder="请输入字段枚举值，用英文逗号隔开"
      />
    } else if(ruleName === 'field_enum_count') {
      return <ProFormText
        label=" "
        rules={[{ required: true, message: '请输入字段枚举数量' }]}
        name="fixValue"
        placeholder="请输入字段枚举数量"
      />
    } else if(ruleName === 'field_data_range') {
      return <ProFormFieldSet
        name="field_data_range"
        label=" "
        transform={(value: any) => ({ rangeStart: value[0], rangeEnd: value[1] })}
      >
        <ProFormText
          placeholder="请选择"
          rules={[{ required: true, message: '必填' }]}
        />
        <span style={{lineHeight: '34px'}}>-</span>
        <ProFormText
          placeholder="请选择"
          rules={[{ required: true, message: '必填' }]}
        />
      </ProFormFieldSet>
      
    } else if(ruleName === 'table_row') {
      return renderTemplateRules();
    }
    return null;
  }

  const renderRuleContent = () => {
    const ruleType = formValues.ruleType;
    if(ruleType === 'system') {
      return <>
        {formValues.monitorObj === 'field' && <ProFormSelect
            name="fieldName"
            label="字段名称"
            rules={[{ required: true, message: '请选择字段名称' }]}
            options={fieldOptions}
            placeholder="请选择"
          />
        }
        <ProFormSelect
          name="name"
          label="规则名称"
          rules={[{ required: true, message: '请选择规则名称' }]}
          options={templateList}
          placeholder="请选择"
        />
        {renderSystemRules()}
      </>
    } else if(ruleType === 'template') {
      return <>
        <ProFormSelect
          name="name"
          label="规则名称"
          rules={[{ required: true, message: '请选择规则名称' }]}
          options={templateList}
          placeholder="请选择"
        />
        {renderTemplateRules()}
      </>
    } else if(ruleType === 'custom') {
      return <>
        <ProFormText
          label="规则名称"
          name="name"
          placeholder="请输入"
          rules={[{ required: true, message: '请输入规则名称' }]}
        />
        <ProFormText
          label="SQL"
          name="content"
          placeholder="请输入"
          rules={[{ required: true, message: '请输入SQL' }]}
        />
         <ProFormRadio.Group
          label="产出类型"
          name="outputType"
          initialValue={1}
          options={outputTypeList}
        />
        {renderTemplateRules()}
      </>
    } else {
      return null;
    }
  }

  return (
    <Spin spinning={loading}>
      <ProForm form={form} colon={false} className={styles.form} submitter={false} layout="horizontal" onValuesChange={onFormChanges}>
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
          tooltip={<>
            <div>告警等级为严重时，将通过电话+短信+钉钉进行告警；</div>
            <div>告警等级为重要时，将通过短信+钉钉进行告警；</div>
            <div>告警等级为一般时，将通过钉钉进行告警;</div>
          </>}
          initialValue={1}
          options={alarmLevelList}
        />
      </ProForm>
    </Spin>
  );
};

export default AddMonitorRule;
