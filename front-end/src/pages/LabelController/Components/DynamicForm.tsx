import React from 'react';
import type { ProFormColumnsType } from '@ant-design/pro-form';
import { BetaSchemaForm } from '@ant-design/pro-form';
import { Input, Space, Button } from 'antd'
import { MinusCircleOutlined, PlusOutlined,FormOutlined } from '@ant-design/icons';
import './index.less'

type DataItem = {
  name: string;
  state: string;
};

const columns: ProFormColumnsType<DataItem>[] = [
  {
    valueType: 'group',
    columns: [
      {
        title: '标题12',
        dataIndex: 'a',
        formItemProps: {
          rules: [
            {
              required: true,
              message: '此项为必填项',
            },
          ],
        },
        renderFormItem: () => {
          return (
            <Space>
                <Input readOnly />
                <FormOutlined
                  className="dynamic-delete-button"
                />
                <MinusCircleOutlined
                  className="dynamic-delete-button"
                />
            </Space>
          )
        }
      },
      {
        title: '标题2',
      },
      {
        title: '标题3',
      },
      {
        title: '标题4',
      },
      {
        title: '',
        renderFormItem: () => {
          return (
            <Button type="primary" icon={<PlusOutlined />}> 添加属性</Button>
          )
        }
      },
    ],
  },
];

export default () => {
  return (
    <>
      <BetaSchemaForm<DataItem>
        layout="horizontal"
        colon={false}
        submitter={false}
        columns={columns}
      />
    </>
  );
};
