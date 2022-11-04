import React, { useState, useImperativeHandle } from 'react';
import { Form, Select, Button, Space } from 'antd';
import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';
import { TreeNodeOption } from '@/types/datapi';
import { getDimensionColumnInfos } from '@/services/datadev';
import { SelectValue } from 'antd/lib/select';

export interface CreateFolderProps {
  visible: boolean;
  onCancel: () => void;
}
interface FlatTreeNodeOption extends TreeNodeOption {
  label: string;
  value: string;
}
const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 16 }
};
const formItemLayoutWithOutLabel = {
  wrapperCol: { span: 16, offset: 8 },
};

const rules = [{ required: true, message: '必填' }];
const DimensionSelect = ({ dimTables, metricCode }: any, ref: React.Ref<unknown> | undefined) => {
  const [keyList, setKeyList] = useState<FlatTreeNodeOption[]>([]);
  const [form] = Form.useForm();

  useImperativeHandle(ref, () => ({
    handleSubmit
  }));
 
  const handleSubmit = () => {
    return form.validateFields();
  }

  const onTableIdChange = (val: SelectValue) => {
    if(val) {
      form.setFieldsValue({
        columnName: undefined
      });
      getDimensionColumnInfos(val, { metricCode }).then(res => {
        const strs = res.data?.map((_: any) => ({ label: _.columnName, value: _.columnName }));
        setKeyList(strs)
      });
    }
  }
  return (
    <Form form={form}>
      <Form.List name="dimTables" initialValue={[{tableId: undefined, columnName: undefined}]}>
        {(fields, {add, remove}) => (
          <>
            {fields.map(({name, key}, index) => (
              <Space key={key} align="baseline">
                <Form.Item
                 {...(index === 0 ? formItemLayout : formItemLayoutWithOutLabel)}
                  name={[name, 'tableId']}
                  rules={rules}
                  label={index === 0 ? '维度选择' : ''}
                  style={{ width: 244 }}
                >
                  <Select
                    options={dimTables}
                    onChange={onTableIdChange}
                    placeholder="请选择表"
                  />
                </Form.Item>
                <Form.Item
                  name={[name, 'columnName']}
                  rules={rules}
                  style={{ width: 170 }}
                >
                   <Select
                    options={keyList}
                    mode="multiple"
                    placeholder="请选择字段"
                  />
                </Form.Item>
                {index === 0 ? null : <MinusCircleOutlined onClick={() => remove(name)} />}
              </Space>
            ))}
            <Form.Item style={{padding: '0 34px 0 80px'}}>
              <Button
                type="dashed"
                style={{borderColor: '#dce1ef'}}
                onClick={() => add()} block icon={<PlusOutlined />}>
                添加维度
              </Button>
            </Form.Item>
          </>
        )}
      </Form.List>
      
    </Form>
  );
};

export default DimensionSelect;
