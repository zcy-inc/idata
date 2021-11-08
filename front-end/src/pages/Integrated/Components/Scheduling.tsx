import type { FC } from 'react';
import React, { useState, useRef } from 'react';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import { EditableProTable } from '@ant-design/pro-table';
import { PoweroffOutlined } from '@ant-design/icons';
import { Button, Alert, Space } from 'antd';
import props from '../../../.umi/plugin-layout/Layout';
type DataSourceType = {
  id: React.Key;
  configValue?: string;
  configValueKey?: string;
  configValueRemarks?: string;
};
interface IConnection {
  url: string;
  token?: string;
  username?: string;
  password?: string
}
interface IBaseConfiguration {
  connection?: IConnection
}
const defaultData: DataSourceType[] = new Array(3).fill(1).map((_, index) => {
  return {
    id: (Date.now() + index).toString(),
    configValueKey: `活动名称${index}`,
    configValue: '这个活动真好玩',
    configValueRemarks: 'open',
  };
});
const columns: ProColumns<DataSourceType>[] = [
  {
    title: '参数名称',
    dataIndex: 'configValueKey',
    width: '100px',
    editable: false
  },
  {
    title: '值',
    dataIndex: 'configValue',
    width: '280px',
  },
  {
    title: '备注',
    dataIndex: 'configValueRemarks',
  },
];
const BaseConfiguration: FC<IBaseConfiguration> = (props) => {
  const { connection } = props;
  const [editableKeys, setEditableRowKeys] = useState<React.Key[]>(() =>
    defaultData.map((item) => item.id),
  );
  const actionRef = useRef<ActionType>();
  const [dataSource, setDataSource] = useState<DataSourceType[]>(() => defaultData);

  return (
    <>
      <EditableProTable<DataSourceType>
        columns={columns}
        rowKey="id"
        value={dataSource}
        actionRef={actionRef}
        onChange={setDataSource}
        recordCreatorProps={false}
        editable={{
          type: 'multiple',
          editableKeys,
          onValuesChange: (record, recordList) => {
            setDataSource(recordList);
          },
          onChange: setEditableRowKeys,
        }}
      />
      {connection ?
        <Space>
          <Button
            type="primary"
            size="small"
            icon={<PoweroffOutlined />}
          >
            点击测试联调性
          </Button>
          <Alert message="连接成功" type="success" showIcon />
          <Alert message="连接失败" type="error" showIcon />
        </Space>
        : null}

      <div style={{ textAlign: 'right' }}>
        <Button type="primary"> 保存</Button>
      </div>
    </>
  );
};
export default BaseConfiguration
