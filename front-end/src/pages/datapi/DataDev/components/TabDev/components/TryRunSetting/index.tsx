import React, {useImperativeHandle} from 'react';
import { Form, Input } from 'antd';
const Item = Form.Item;
const TryRunSetting = ({}: any, ref: any) => {
  useImperativeHandle(ref, () => ({
    form
  }))
  const [form] = Form.useForm();

  return <Form form={form} labelCol={{ span: 6 }} >
    <Item name="driverMemory" label="driveMemory" >
      <Input placeholder="请输入driveMemory，如1G" />
    </Item>
    <Item name="env" label="executorMemory">
      <Input placeholder="请输入executorMemory，如1G" />
    </Item>
  </Form>
}

export default TryRunSetting;