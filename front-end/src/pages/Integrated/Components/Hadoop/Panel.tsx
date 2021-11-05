import React, { useState, useRef } from 'react';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import { EditableProTable } from '@ant-design/pro-table';
import { UploadOutlined } from '@ant-design/icons';
import type { UploadProps } from 'antd';
import { Button, Upload, message } from 'antd';
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

type DataSourceType = {
  id: React.Key;
  configValue?: string;
  configValueKey?: string;
  configValueRemarks?: string;
};

const defaultData: DataSourceType[] = new Array(3).fill(1).map((_, index) => {
  return {
    id: (Date.now() + index).toString(),
    configValueKey: `活动名称${index}`,
    configValue: '这个活动真好玩',
    configValueRemarks: 'open',
  };
});

export default () => {
  const [editableKeys, setEditableRowKeys] = useState<React.Key[]>(() =>
    defaultData.map((item) => item.id),
  );
  const actionRef = useRef<ActionType>();
  const [dataSource, setDataSource] = useState<DataSourceType[]>(() => defaultData);

  const columns: ProColumns<DataSourceType>[] = [
    {
      title: '参数名称',
      dataIndex: 'configValueKey',
      width: '220px',
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
      title: '备注',
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
      <EditableProTable<DataSourceType>
        headerTitle="core-site"
        columns={columns}
        rowKey="id"
        value={dataSource}
        actionRef={actionRef}
        onChange={setDataSource}
        recordCreatorProps={{
          newRecordType: 'dataSource',
          record: () => ({
            id: Date.now(),
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
    </>
  );
};
