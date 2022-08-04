import React, { useEffect, useImperativeHandle, useState } from 'react';
import ProForm, { ProFormSelect, ProFormText, ProFormRadio, ProFormTimePicker, ProFormFieldSet, ProFormTextArea, ProFormDigit } from '@ant-design/pro-form';
import { Form, Spin } from 'antd';
import type { FC } from 'react';
import styles from './index.less';
import { monitorObjList, ruleTypeList, alarmLevelList, compareOptions, compareObjOptions, outputTypeList } from '@/constants/quality';
import { getTemplateList, getRecivers, getMonitorRule, getFiledList, getTableOwners } from '@/services/quality';
import type { MonitorObj, RuleType } from '@/types/quality';
import moment from 'moment';


const requiredValidator = (rule: any, value: any, callback: (arg0?: string) => any) => {
  if(!value) {
    return callback('必填')
  }
  return callback();
}

const numberValidator = (rule: any, value: any, callback: (arg0?: string) => any) => {
  if(!value) {
    return callback('必填')
  } else if(isNaN(value)) {
    return callback('请输入数字')
  } else if(Number(value) < 0) {
    return callback('请输入大于0的数字')
  } else if(value.split('.')[1]?.length > 2) {
    return callback('最多只允许保留两位小数')
  }
  return callback();
}

const rangeValidator = (value: any, callback: (arg0?: string) => any, isStart: boolean, compreValue: string) => {
  if(!value) {
    return callback('必填')
  } else if(isNaN(value)) {
    return callback('请输入数字')
  } else if(Number(value) < 0) {
    return callback('请输入大于0的数字')
  } else if(value.split('.')[1]?.length > 2) {
    return callback('最多只允许保留两位小数')
  } else if((isStart && value > compreValue && compreValue) || (!isStart || value < compreValue && compreValue)) {
    return callback('范围起始不能大于范围结束')
  }
  return callback();
}


// 建立一个清楚的关系
const resetFieldMap = {
  monitorObj: ['fieldName', 'templateId'],
  ruleType: ['fieldName', 'templateId'],
  templateId: ['compareType', 'checkType', 'fixValue', 'content', 'fieldDataRange'],
  checkType: ['fixValue', 'pre_period'],
}

const AddMonitorRule: FC<{id: number; tableName?: string, baselineId: number; disabled: boolean;}> = ({id, tableName, baselineId, disabled = false}, ref) => {
  const [form] = Form.useForm();
  const [templateList, setTemplateList] = useState<{label: string; value: number;} []>([]);
  const [recivers, setRecivers] = useState<{label: string; value: number} []>([]);
  const [loading, setLoading] = useState(false);
  const [formValues, setFormValues] = useState<Record<string, any>>({
    monitorObj: 'table',
    alarmLevel: 1
  });
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

      } else if(tableName) {
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
    return form.validateFields().then(res => getValue(res))
  }

  const getValue = (res: any) => {
    const params = !!id ? {...res, id} : res;
    params.alarmReceivers = params.alarmReceivers.join(',');
    params.baselineId = baselineId;
    if(tableName) {
      params.tableName = tableName;
    }
    if(params.ruleType === 'custom') {
      params.templateId = -1;
    } else {
      const tempalteItem = templateList.find(template => template.value === params.templateId);
      params.name = tempalteItem?.label;
    }
    if (params.templateId === 2) {
      params.content = moment(params.content).format('HH:mm');
    }
    return params;
  }

  const collectResetFields = (fields: string []) => {
    let list = [...fields]
    fields.forEach(field => {
      if(resetFieldMap[field]) {
        list = list.concat(collectResetFields(resetFieldMap[field]))
      }
    })
    return list;
  }

  const onFormChanges = (values: { monitorObj: MonitorObj; ruleType: RuleType; }) => {
    let newFormValues = {
      ...formValues,
      ...values
    }
    // 清楚依赖项的值
    const key = Object.keys(values)[0] || '';
    if(resetFieldMap[key]) {
      let resetList = collectResetFields(resetFieldMap[key]);
      const resetObj: Record<string, any> = {};
      resetList.forEach(filed => {
        resetObj[filed] = undefined;
      });
      form.setFieldsValue(resetObj);
      newFormValues = {
        ...newFormValues,
        ...resetObj
      }
    }
    // 获取规则名称列表
    if(values.monitorObj || values.ruleType && newFormValues.ruleType &&  newFormValues.monitorObj && newFormValues.ruleType !== 'custom') {
      getTemplateList({
        monitorObj: newFormValues.monitorObj,
        type: newFormValues.ruleType,
        curPage: 1,
        pageSize: 10000,
        status: 1
      }).then(res => {
        setTemplateList(res.data.data.map((item) => ({
          label: item.name,
          value: item.id,
        })))
      })
    }

    // 获取字段选择下拉列表
    if(tableName && (
      (values.ruleType === 'system' && newFormValues.monitorObj === 'field') ||
      (values.monitorObj === 'field' && newFormValues.ruleType === 'system')
    )) {
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
      disabled={disabled}
      label=" "
      name="compareType"
      options={compareOptions}
      placeholder="请选择"
      rules={[{ validator: requiredValidator }]}
    />
    <ProFormSelect
      label=" "
      disabled={disabled}
      name="checkType"
      options={compareObjOptions}
      placeholder="请选择"
      rules={[{ validator: requiredValidator }]}
    />
    {checkType === 'fix'&&
      <ProFormText
        placeholder="请输入"
        disabled={disabled}
        name="fixValue"
        label=" "
        rules={[{ validator: numberValidator }]}
      />}
    {['up', 'down'].includes(formValues.compareType) && formValues.checkType &&  <>
      <div className={styles['inline-form-item-first']}>
        <ProFormText
          placeholder="请输入"
          disabled={disabled}
          name="rangeStart"
          rules={[{ validator: (rule, value, cb) => rangeValidator(value, cb, true, form.getFieldValue('rangeEnd')) }]}
          fieldProps={{
            suffix: "%",
          }}
        />
      </div>
      <span className={styles['inline-form-item-sperator']}>~</span>
      <div className={styles['inline-form-item']}>
        <ProFormText
          placeholder="请输入"
          disabled={disabled}
          name="rangeEnd"
          rules={[{ validator: (rule, value, cb) => rangeValidator(value, cb, false, form.getFieldValue('rangeStart')) }]}
          fieldProps={{
            suffix: "%",
          }}
        />
      </div>
    </>}
  </>
  }

  // 内置规则渲染
  const renderSystemRules = () => {
    const templateId = formValues.templateId;
    if(templateId === 2) { // 表产出时间
      return <ProFormTimePicker
        label=" "
        rules={[{ validator: requiredValidator }]}
        name="content"
        disabled={disabled}
        placeholder="请选择表产出时间"
        fieldProps={{
          format: "HH:mm",
          style:{width: 200}
        }}
      />
    } else if(templateId === 4) {
      return <ProFormText
        label=" "
        disabled={disabled}
        rules={[{ validator: requiredValidator }]}
        name="content"
        placeholder="请输入字段枚举值，用英文逗号隔开"
      />
    } else if(templateId === 5) {
      return <ProFormText
        label=" "
        disabled={disabled}
        rules={[{ validator: requiredValidator }]}
        name="fixValue"
        placeholder="请输入字段枚举数量"
      />
    } else if(templateId === 6) { // 范围
      return <>
        <div className={styles['inline-form-item-first']}>
          <ProFormText
            disabled={disabled}
            placeholder="请选择"
            rules={[{ validator: numberValidator }]}
            fieldProps={{
              className: 'inline-form-item'
            }}
          />
        </div>
        <span className={styles['inline-form-item-sperator']}>~</span>
        <div className={styles['inline-form-item']}>
          <ProFormText
            disabled={disabled}
            placeholder="请选择"
            rules={[{ validator: numberValidator }]}
            fieldProps={{
              className: 'inline-form-item'
            }}
          />
        </div>
      </>
    } else if(templateId === 1) { // 表行数
      return renderTemplateRules();
    }
    return null;
  }

  const renderRuleContent = () => {
    const ruleType = formValues.ruleType;
    if(ruleType === 'system') {
      return <>
        {formValues.monitorObj === 'field' && <ProFormSelect
            disabled={disabled}
            name="fieldName"
            label="字段名称"
            rules={[{ required: true, message: '请选择字段名称' }]}
            options={fieldOptions}
            placeholder="请选择"
          />
        }
        <ProFormSelect
          name="templateId"
          disabled={disabled}
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
          name="templateId"
          label="规则名称"
          disabled={disabled}
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
          disabled={disabled}
          placeholder="请输入"
          rules={[{ required: true, message: '请输入规则名称' }]}
        />
        <ProFormTextArea
          label="SQL"
          disabled={disabled}
          name="content"
          placeholder="请输入"
          rules={[{ required: true, message: '请输入SQL' }]}
        />
         <ProFormRadio.Group
          label="产出类型"
          disabled={disabled}
          name="outputType"
          initialValue={2}
          options={outputTypeList}
        />
        {renderTemplateRules()}
      </>
    } else {
      return null;
    }
  }

  const transformedMonitorObjList = tableName ? monitorObjList : monitorObjList.map(item => {
    if(item.value === 'field') {
      return {
        ...item,
        disabled: true
      }
    }
    return item
  })

  return (
    <Spin spinning={loading}>
      <ProForm form={form} colon={false} className={styles.form} submitter={false} layout="horizontal" onValuesChange={onFormChanges}>
        <ProFormRadio.Group
          label="监控对象"
          name="monitorObj"
          disabled={disabled}
          initialValue="table"
          options={transformedMonitorObjList}
        />
        <ProFormSelect
          disabled={disabled}
          label="规则类型"
          name="ruleType"
          options={ruleTypeList}
          placeholder="请选择"
          rules={[{ required: true, message: '请选择规则类型' }]}
        />
        {renderRuleContent()}
        <ProFormSelect
          disabled={disabled}
          name="alarmReceivers"
          label="告知人"
          tooltip="默认通知数仓表所属人"
          mode="multiple"
          options={recivers}
          placeholder="请选择"
          rules={[{ required: true, message: '请选择告知人', type: 'array' }]}
        />
        <ProFormRadio.Group
          label="告警等级"
          disabled={disabled}
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
