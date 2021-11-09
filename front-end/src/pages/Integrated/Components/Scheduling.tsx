import React, { useState } from 'react';
import { EditableProTable } from '@ant-design/pro-table';
import { PoweroffOutlined } from '@ant-design/icons';
import { Button, Alert, Space, Spin, Form } from 'antd';
import { getConfigByType, editSystemConfig } from '@/services/system-controller'
import { useRequest } from 'umi';
import { dataToList, listToData } from '../utils'
import type { TConfigType, IDataSourceType } from '@/types/system-controller'
import type { FC } from 'react';
import type { ProColumns } from '@ant-design/pro-table';
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

const columns: ProColumns<IDataSourceType>[] = [
  {
    title: '参数名称',
    dataIndex: 'configValueKey',
    width: '280px',
    editable: false
  },
  {
    title: '值',
    dataIndex: 'configValue',
    formItemProps: {
      rules: [
        {
          required: true,
          whitespace: true,
          message: '此项是必填项',
        }]
    },
    width: '280px',
  },
  {
    title: '备注',
    dataIndex: 'configValueRemarks',
  },
];
const BaseConfiguration: FC<IBaseConfiguration> = (props) => {
  const { connection, type } = props;
  const [dataSource, setDataSource] = useState<IDataSourceType[]>([]);
  const [editableKeys, setEditableRowKeys] = useState<React.Key[]>([]);
  const [configId, setConfigId] = useState<string>("");
  const [editForm] = Form.useForm()
  const { loading } = useRequest(() => getConfigByType(type), {
    onSuccess: (data) => {
      const newDataSource = dataToList(data?.[0]?.valueOne)
      setConfigId(data?.[0]?.id);
      setDataSource(newDataSource)
      setEditableRowKeys(() =>
        newDataSource.map(({ configValueKey }) => configValueKey))
    }
  });

  const save = async () => {
    const { error } = await editForm.validateFields()
    if (!error) {
      const config = listToData(dataSource);
      editSystemConfig([{
        id: configId,
        valueOne: config
      }])
    }
  }
  return (
    <Spin spinning={loading}>
      <EditableProTable<IDataSourceType>
        columns={columns}
        rowKey="configValueKey"
        value={dataSource}
        onChange={setDataSource}
        recordCreatorProps={false}
        editable={{
          type: 'multiple',
          editableKeys,
          form: editForm,
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
        <Button type="primary" onClick={save}> 保存</Button>
      </div>
    </Spin>
  );
};
export default BaseConfiguration
