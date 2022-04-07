import React, { useEffect, useState, useImperativeHandle } from 'react';
import ProForm, { ProFormSelect, ProFormText } from '@ant-design/pro-form';
import { Form, Spin } from 'antd';

import { getTables, getTableEnums, createModifier, getModifier } from '@/services/measure';
import { TreeNodeOption } from '@/types/datapi';
import { getTableReferStr } from '@/services/datadev';

export interface CreateFolderProps {
  visible: boolean;
  onCancel: () => void;
}
interface FlatTreeNodeOption extends TreeNodeOption {
  label: string;
  value: string;
}

const rules = [{ required: true, message: '必填' }];

const genRules = (name:string) => {
  return [{
    validator: (_: any, value: any) => value ? Promise.resolve() : Promise.reject(new Error(`请输入${name}`)),
  }]
}

const CreateFolder = ({ node }: any, ref: React.Ref<unknown> | undefined) => {
  const [tables, setTables] = useState<FlatTreeNodeOption[]>([]);
  const [keyList, setKeyList] = useState<FlatTreeNodeOption[]>([]);
  const [enums, setEnums] = useState<FlatTreeNodeOption[]>([]);
  const [form] = Form.useForm();
  const [loading, setLoading] = useState(false);
  useImperativeHandle(ref, () => ({
    handleSubmit
  }));
  useEffect(() => {
    getData(node);
    getTables()
      .then((res) => {
        const list = res.data?.map((_: any) => ({
          label: _.tableName,
          value: _.id,
        }));
        setTables(list);
      })
      .catch((err) => {});
  }, []);

  const getData = (node: { labelCode: string; }) => {
    setLoading(true);
    getModifier({modifierCode: node.labelCode }).then(res => {
      const data = {
        labelName: res.data?.labelName,
        tableId: res.data?.measureLabels[0]?.tableId,
        columnName: res.data?.measureLabels[0]?.columnName,
        labelParamValue: res.data?.measureLabels[0]?.labelParamValue,
        modifierDefine: res.data?.labelAttributes.find((item: { attributeKey: string; }) => item.attributeKey === 'modifierDefine')?.attributeValue
      }
      form.setFieldsValue(data);
      Object.keys(data).forEach((k) => {
        const value = data[k];
        onValuesChange({[k] : value});
      });
    }).finally(()=> {
      setLoading(false);
    })
  }

  const handleSubmit = () => {
    return form.validateFields().then(values => {
      const params = {
        labelName: values.labelName,
        measureLabels: [{
          tableId: values.tableId,
          columnName: values.columnName,
          labelParamValue: values.labelParamValue
        }],
        labelAttributes: [
          {attributeKey: 'modifierDefine', attributeType: 'STRING', attributeValue: values.modifierDefine}
        ]
      }
      return createModifier(params);
    });
   
  }

  const onValuesChange = (values: any) => {
    if(values.tableId) {
      form.setFieldsValue({
        columnName: undefined,
        labelParamValue: undefined
      });
      getTableReferStr({ tableId: values.tableId }).then(res => {
        const strs = res.data?.map((_: any) => ({ label: _.columnName, value: _.columnName }));
        setKeyList(strs)
      });
    }

    if(values.columnName) {
      form.setFieldsValue({
        labelParamValue: undefined
      });
      getTableEnums({tableId: form.getFieldValue('tableId'), columnName: values.columnName}).then(res => {
        const strs = res.data?.map((_: any) => ({ label: _, value: _ }));
        setEnums(strs);
      })
    }
  }

  return (
    <Spin spinning={loading}>
      <ProForm
        form={form}
        colon={false}
        labelCol={{ span: 5 }}
        layout="horizontal"
        submitter={false}
        onValuesChange={onValuesChange}
      >
        <ProFormText name="labelName" label="修饰词名称" rules={rules} />
        <ProFormSelect
          name="tableId"
          label="修饰词来源"
          rules={rules}
          options={tables}
          allowClear={false}
          fieldProps={{
            showSearch: true,
            filterOption: (v: string, option: any) => option.label.indexOf(v) >= 0,
          }}
        />
        <ProFormSelect
          name="columnName"
          label=" "
          rules={genRules('字段')}
          options={keyList}
          fieldProps={{
            showSearch: true,
            filterOption: (v: string, option: any) => option.label.indexOf(v) >= 0,
          }}
        />
        <ProFormSelect
          name="labelParamValue"
          label=" "
          rules={genRules('枚举值')}
          options={enums}
          fieldProps={{
            showSearch: true,
            filterOption: (v: string, option: any) => option.label.indexOf(v) >= 0,
          }}
        />
        <ProFormText
          name="modifierDefine"
          label="定义"
          rules={rules}
        />
      </ProForm>
    </Spin>
  );
};

export default CreateFolder;
