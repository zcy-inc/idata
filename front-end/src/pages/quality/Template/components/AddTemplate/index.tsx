import React, { useEffect, useImperativeHandle } from 'react';
import ProForm, { ProFormSelect, ProFormText, ProFormRadio, ProFormTextArea } from '@ant-design/pro-form';
import { Form, Alert } from 'antd';
import type { FC } from 'react';
import { monitorObjList, outputTypeList, categoryList } from '@/constants/quality';
import { getTemplate } from '@/services/quality';


const AddTemplate: FC<{id?: number;disabled: boolean;}> = ({id, disabled= false}, ref) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if(id) {
      getTemplate({id}).then(res => {
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
        disabled={disabled}
        initialValue="table"
        options={monitorObjList}
      />
      <ProFormSelect
        label="维度"
        disabled={disabled}
        name="category"
        options={categoryList}
        placeholder="请选择"
        rules={[{ required: true, message: '请选择维度' }]}
      />
      <ProFormText
        name="name"
        disabled={disabled}
        label="规则名称"
        rules={[{ required: true, message: '请输入规则名称' }]}
        placeholder="请输入"
      />
      <ProFormTextArea
        name="content"
        disabled={disabled}
        label="SQL"
        rules={[{ required: true, message: '请输入SQL' }]}
        placeholder="请输入"
      />
      <Alert
        style={{margin: '0 0 24px 100px'}}
        message="sql支持${tableName}占位符。例，select count(*) from ${tableName}"
        type="info"
      />
      <ProFormRadio.Group
        label="产出类型"
        disabled={disabled}
        name="outputType"
        initialValue={2}
        options={outputTypeList}
      />
    </ProForm>
  );
};

export default AddTemplate;
