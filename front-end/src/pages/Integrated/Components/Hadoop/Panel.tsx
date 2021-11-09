import type { FC} from 'react';
import React, {useState, useRef,useEffect } from 'react';
import type { ProColumns, ActionType } from '@ant-design/pro-table';
import { EditableProTable } from '@ant-design/pro-table';
import { UploadOutlined } from '@ant-design/icons';
import type { UploadProps } from 'antd';
import { Button, Upload, message } from 'antd';
import { v4 as uuidv4 } from 'uuid';
const upLoadProps: UploadProps = {
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

interface HadoopPanelProps{
  id: string
  headerTitle: string
  dataSource?: IDataSourceType[]
}
import type { IDataSourceType } from '@/types/system-controller'

const HadoopPanel: FC<HadoopPanelProps>= (props) => {
  const {headerTitle,dataSource} =props;
  const [editableKeys, setEditableRowKeys] = useState<React.Key[]>([]);
  const actionRef = useRef<ActionType>();
  const [dataSourceState, setDataSource] = useState<IDataSourceType[]>([]);
  const columns: ProColumns<IDataSourceType>[] = [
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
  useEffect(()=>{
    setDataSource(dataSource||[])
  },[dataSource])
  return (
    <>
      <EditableProTable<IDataSourceType>
        headerTitle={headerTitle}
        columns={columns}
        rowKey="id"
        value={dataSourceState}
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
            <Upload {...upLoadProps}>
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
export  default HadoopPanel
