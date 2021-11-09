import React, { useState, useRef } from 'react';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import { EditableProTable } from '@ant-design/pro-table';
import { UploadOutlined } from '@ant-design/icons';
import type { UploadProps } from 'antd';
import { Button, Upload, message, Collapse } from 'antd';
import type { IDataSourceType } from '@/types/system-controller'
import { v4 as uuidv4 } from 'uuid';
const { Panel } = Collapse;
const props: UploadProps = {
  name: 'file',
  action: 'https://www.mocky.io/v2/5cc8019d300000980a055e76',
  headers: {
    authorization: 'authorization-text',
  },
  onChange(info) {
    if (info.file.status !== 'uploading') {
      console.log(info.file, info.fileList);
    }
    if (info.file.status === 'done') {
      message.success(`${info.file.name} file uploaded successfully`);
    } else if (info.file.status === 'error') {
      message.error(`${info.file.name} file upload failed.`);
    }
  },
  progress: {
    strokeColor: {
      '0%': '#108ee9',
      '100%': '#87d068',
    },
    strokeWidth: 3,
    format: percent => `${parseFloat(percent.toFixed(2))}%`,
  },
};


export default () => {
  const [editableKeys, setEditableRowKeys] = useState<React.Key[]>([]);
  const actionRef = useRef<ActionType>();
  const [dataSource, setDataSource] = useState<IDataSourceType[]>([]);

  const columns: ProColumns<IDataSourceType>[] = [
    {
      title: '参数名称',
      dataIndex: 'configValueKey',
      width: '280px',
      formItemProps: {
        rules: [
          {
            required: true,
            whitespace: true,
            message: '此项是必填项',
          }]
      }
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
      }
    },
    {
      title: '备注',
      width: '280px',
      dataIndex: 'configValueRemarks',
    }, {
      title: '操作',
      valueType: 'option',
      width: 250,
      render: () => {
        return null;
      },
    }
  ];

  return (
    <>
      <Collapse >
        <Panel header="集群全局参数" key="1">
          <EditableProTable<IDataSourceType>
            headerTitle="core-site"
            columns={columns}
            rowKey="id"
            value={dataSource}
            actionRef={actionRef}
            onChange={setDataSource}
            recordCreatorProps={{
              newRecordType: 'dataSource',
              record: () => ({
                id: uuidv4(),
              }),
            }}
            toolBarRender={() => {
              return [
                <Upload {...props}>
                  <Button icon={<UploadOutlined />}>上传文件</Button>
                </Upload>,
              ];
            }}
            editable={{
              type: 'multiple',
              editableKeys,
              onValuesChange: (record, recordList) => {
                setDataSource(recordList);
              },
              actionRender: (row, config, defaultDoms) => {
                return [defaultDoms.delete];
              },
              onChange: setEditableRowKeys,
            }}
          />
        </Panel>
        <Panel header="hdfs-site(HDFS参数)" key="2">
          <Upload {...props}>
            <Button icon={<UploadOutlined />}>上传文件</Button>
          </Upload>
          <EditableProTable<IDataSourceType>
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
        </Panel>
        <Panel header="yarn-site(集群资源管理参数)" key="3">
          <Upload {...props}>
            <Button icon={<UploadOutlined />}>上传文件</Button>
          </Upload>
          <EditableProTable<IDataSourceType>
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
        </Panel>
      </Collapse>
      <div style={{ textAlign: 'right',padding:'10px' } }>
        <Button type="primary" > 保存</Button>
      </div>
    </>
  );
};
