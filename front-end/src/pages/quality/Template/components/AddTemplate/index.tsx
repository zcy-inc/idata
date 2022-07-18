import React, { useEffect, useImperativeHandle, useState } from 'react';
import ProForm, { ProFormSelect, ProFormText, ProFormRadio } from '@ant-design/pro-form';
import { Form } from 'antd';
import type { FC } from 'react';
import { monitorObjList, ruleTypeList, outputTypeList, categoryList } from '@/constants/quality';
import { getTemplateList, getTemplate } from '@/services/quality';
import type { MonitorObj, RuleType, TemplateItem } from '@/types/quality';


const AddTemplate: FC<{id?: number}> = ({id}, ref) => {
  const [form] = Form.useForm();
  const [templateList, setTemplateList] = useState<{label: string; value: number} []>([]);

  useEffect(() => {
    if(id) {
      getTemplate({id}).then(res => {
        console.log(res);
        form.setFieldsValue(res.data);
      })
    }
  }, [])

  useImperativeHandle(ref, () => ({
    handleSubmit
  }))

  const handleSubmit = () => {
    return form.validateFields()
  }

  return (
    <ProForm form={form} colon={false} submitter={false} layout="horizontal">
      <ProFormRadio.Group
        label="监控对象"
        name="monitorObj"
        initialValue="table"
        options={monitorObjList}
      />
      <ProFormSelect
        label="维度"
        name="category"
        options={categoryList}
        placeholder="请选择"
        rules={[{ required: true, message: '请选择维度' }]}
      />
      <ProFormText
        name="name"
        label="规则名称"
        rules={[{ required: true, message: '请输入规则名称' }]}
        placeholder="请输入"
      />
      <ProFormText
        name="content"
        label="SQL"
        rules={[{ required: true, message: '请输入SQL' }]}
        placeholder="请输入"
      />
      <ProFormRadio.Group
        label="产出类型"
        name="outputType"
        options={outputTypeList}
      />
    </ProForm>
  );
};

export default AddTemplate;
