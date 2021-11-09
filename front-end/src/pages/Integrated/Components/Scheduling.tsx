import React, { useState } from 'react';
import { EditableProTable } from '@ant-design/pro-table';
import { PoweroffOutlined } from '@ant-design/icons';
import { Button, Alert, Space, Spin, Form, message } from 'antd';
import { getConfigByType, editSystemConfig, checkConfigConnection } from '@/services/system-controller'
import { useRequest } from 'umi';
import { dataToList, listToData, configToConnection } from '../utils'
import type { TConfigType, IDataSourceType } from '@/types/system-controller'
import type { FC } from 'react';
import type { ProColumns } from '@ant-design/pro-table';
interface IBaseConfiguration {
  hasConnection?: boolean,
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
  },
  {
    title: '备注',
    width: '360px',
    dataIndex: 'configValueRemarks',
  },
];
const BaseConfiguration: FC<IBaseConfiguration> = (props) => {
  const { type, hasConnection } = props;
  const [dataSource, setDataSource] = useState<IDataSourceType[]>([]);
  const [editableKeys, setEditableRowKeys] = useState<React.Key[]>([]);
  const [configId, setConfigId] = useState<string>("");
  const [connectionStatus, setConnectionStatus] = useState<boolean | null>(null);
  const [editForm] = Form.useForm()
  const { loading: fetchLoading } = useRequest(() => getConfigByType(type), {
    onSuccess: (data) => {
      const newDataSource = dataToList(data?.[0]?.valueOne)
      setConfigId(data?.[0]?.id);
      setDataSource(newDataSource)
      setEditableRowKeys(() =>
        newDataSource.map(({ configValueKey }) => configValueKey))
    }
  });
  const { loading: saveLoading, run: save } = useRequest(async () => {
    const { error } = await editForm.validateFields()
    if (!error) {
      const config = listToData(dataSource);
      return editSystemConfig([{
        id: configId,
        keyOne:configId,
        valueOne: config
      }])
    }
    return false;
  }, {
    manual: true,
    onSuccess: () => {
      message.success('保存成功')
    }
  });
  const { loading: connectionLoading, run: checkConnection } = useRequest(async () => {
    const { error } = await editForm.validateFields()
    if (!error) {
      const config = listToData(dataSource);
      const connection = configToConnection(type, config)
      return checkConfigConnection(connection)
    }
    return false;
  }, {
    manual: true,
    onError: () => {
      setConnectionStatus(false)
    },
    onSuccess: (data) => {
      console.log(data);
      setConnectionStatus(!!data)
    }
  });
  return (
    <Spin spinning={fetchLoading || saveLoading || connectionLoading}>
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
      {hasConnection ?
        <Space>
          <Button
            type="primary"
            size="small"
            icon={<PoweroffOutlined />}
            onClick={checkConnection}
          >
            点击测试连通性
          </Button>
          {
            connectionStatus === true ? <Alert message="连接成功" type="success" showIcon /> : null
          }
          {
            connectionStatus === false ? <Alert message="连接失败" type="error" showIcon /> : null
          }
        </Space>
        : null}

      <div style={{ textAlign: 'right' }}>
        <Button type="primary" onClick={save}> 保存</Button>
      </div>
    </Spin>
  );
};
export default BaseConfiguration
