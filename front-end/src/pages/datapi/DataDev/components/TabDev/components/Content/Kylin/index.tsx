import React, { forwardRef, useEffect, useImperativeHandle } from 'react';
import moment from 'moment';
import { DatePicker, Form, Input, Select } from 'antd';
import type { ForwardRefRenderFunction } from 'react';
import Title from '@/components/Title';

interface KylinProps {
  data: any;
}

const { Item } = Form;
const width = 200;
const ruleSlct = [{ required: true, message: '请选择' }];

const Kylin: ForwardRefRenderFunction<unknown, KylinProps> = ({ data }, ref) => {
  const [form] = Form.useForm();

  useImperativeHandle(ref, () => ({
    form: form,
  }));

  useEffect(() => {
    if (data) {
      form.setFieldsValue({
        cubeName: data.cubeName,
        buildType: data.buildType,
        startTime: data.startTime ? moment(data.startTime) : null,
        endTime: data.endTime ? moment(data.endTime) : null,
      });
    }
  }, [data]);

  return (
    <>
      <Title style={{ marginTop: 16 }}>应用配置</Title>
      <Form form={form} colon={false}>
        <Item name="cubeName" label="cube名称" rules={ruleSlct}>
          <Input size="large" style={{ width }} placeholder="请选择" />
        </Item>
        <Item name="buildType" label="构建类型" rules={ruleSlct}>
          <Select
            size="large"
            style={{ width }}
            placeholder="请选择"
            options={[
              { label: 'BUILD', value: 'BUILD' },
              { label: 'MERGE', value: 'MERGE' },
              { label: 'REFRESH', value: 'REFRESH' },
            ]}
          />
        </Item>
        <Item name="startTime" label="生效时间">
          <DatePicker showTime />
        </Item>
        <Item name="endTime" label="失效时间">
          <DatePicker showTime />
        </Item>
      </Form>
    </>
  );
};

export default forwardRef(Kylin);
