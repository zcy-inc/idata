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

const genRules = [{
  validator: (_: any, value: any) => value ? Promise.resolve() : Promise.reject(new Error('必填')),
}]

const CreateFolder = ({ node }: any, ref: React.Ref<unknown> | undefined) => {
  const [tables, setTables] = useState<FlatTreeNodeOption[]>([]);
  const [keyList, setKeyList] = useState<FlatTreeNodeOption[]>([]);
  const [enums, setEnums] = useState<FlatTreeNodeOption[]>([]);
  const [form] = Form.useForm();
  const [data, setData] = useState<any>({});
  const [loading, setLoading] = useState(false);
  useImperativeHandle(ref, () => ({
    handleSubmit
  }));
  useEffect(() => {
    if(node.labelCode) {
      getData(node);
    }
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
        labelCode: res.data?.labelCode,
        columnName: res.data?.measureLabels[0]?.columnName,
        labelParamValue: res.data?.measureLabels[0]?.labelParamValue?.split(',') || [],
        modifierDefine: res.data?.labelAttributes.find((item: { attributeKey: string; }) => item.attributeKey === 'modifierDefine')?.attributeValue
      }
      setData(data);
      form.setFieldsValue(data);
      Object.keys(data).forEach((k) => {
        const value = data[k];
        onValuesChange({[k] : value}, true);
      });
    }).finally(()=> {
      setLoading(false);
    })
  }

  const handleSubmit = () => {
    return form.validateFields().then(values => {
      let params: any = {
        labelName: values.labelName,
        measureLabels: [{
          tableId: values.tableId,
          columnName: values.columnName,
          labelParamValue: values.labelParamValue.join(',')
        }],
        labelAttributes: [
          {attributeKey: 'modifierDefine', attributeType: 'STRING', attributeValue: values.modifierDefine}
        ]
      }
      if(node.labelCode) {
        params.labelCode = data.labelCode;
      }
      return createModifier(params);
    });
   
  }

  const onValuesChange = (values: any, init = false) => {
    if(values.tableId) {
      !init && form.setFieldsValue({
        columnName: undefined,
        labelParamValue: undefined
      });
      getTableReferStr({ tableId: values.tableId }).then(res => {
        const strs = res.data?.map((_: any) => ({ label: _.columnName, value: _.columnName }));
        setKeyList(strs)
      });
    }

    if(values.columnName) {
      !init && form.setFieldsValue({
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
          rules={genRules}
          options={keyList}
          fieldProps={{
            showSearch: true,
            filterOption: (v: string, option: any) => option.label.indexOf(v) >= 0,
          }}
        />
        <ProFormSelect
          name="labelParamValue"
          label=" "
          rules={genRules}
          options={enums}
          fieldProps={{
            showSearch: true,
            filterOption: (v: string, option: any) => option.label.indexOf(v) >= 0,
            mode: 'multiple'
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
