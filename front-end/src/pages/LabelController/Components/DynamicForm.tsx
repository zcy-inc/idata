import React from 'react';
import type { ProFormColumnsType } from '@ant-design/pro-form';
import { BetaSchemaForm } from '@ant-design/pro-form';
import { Input,Space } from 'antd'
import { MinusCircleOutlined } from '@ant-design/icons';
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
        renderFormItem: () => {
          return (
            <Space>
              <Input  readOnly/>
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
