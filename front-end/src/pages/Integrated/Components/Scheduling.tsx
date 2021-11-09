import React, { useState, useRef } from 'react';
import { EditableProTable } from '@ant-design/pro-table';
import { PoweroffOutlined } from '@ant-design/icons';
import { Button, Alert, Space,Spin } from 'antd';
import { getConfigByType } from '@/services/system-controller'
import { useRequest } from 'umi';
import {dataToList} from '../utils'
import type { TConfigType,TDataSourceType } from '@/types/system-controller'
import type { FC } from 'react';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
interface IConnection {
  url: string;
  token?: string;
  username?: string;
  password?: string
}
interface IBaseConfiguration {
  connection?: IConnection
  type: TConfigType
}

const columns: ProColumns<TDataSourceType>[] = [
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
  const { connection,type } = props;
  const [dataSource, setDataSource] = useState<TDataSourceType[]>([]);
  const [editableKeys, setEditableRowKeys] = useState<React.Key[]>([]);
  const actionRef = useRef<ActionType>();
  const { loading } = useRequest(()=>getConfigByType(type), {
    onSuccess: (data) => {
      const newDataSource = dataToList(data?.[0]?.valueOne)
      setDataSource(newDataSource)
      setEditableRowKeys(() =>
      newDataSource.map(({ configValueKey }) => configValueKey))
    }
  });

  return (
    <Spin spinning={loading}>
      <EditableProTable<TDataSourceType>
        columns={columns}
        rowKey="configValueKey"
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
    </Spin>
  );
};
export default BaseConfiguration
