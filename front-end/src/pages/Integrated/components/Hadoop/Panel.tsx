import type { FC} from 'react';
import React  from 'react';
import type { ProColumns } from '@ant-design/pro-table';
import { EditableProTable } from '@ant-design/pro-table';
import { UploadOutlined } from '@ant-design/icons';
import type { UploadProps } from 'antd';
import { Button, Upload, message } from 'antd';
import {dataToList} from '../../utils'
import { v4 as uuidv4 } from 'uuid';

interface HadoopPanelProps{
  id: string
  headerTitle: string
  dataSource?: IDataSourceType[]
  setDataSource?: (data: IDataSourceType[]) => void
  editableKeys?: React.Key[]
  setEditableRowKeys?: (keys: React.Key[]) => void
}
import type { IDataSourceType } from '@/types/system-controller'

const HadoopPanel: FC<HadoopPanelProps>= (props) => {
  const {headerTitle,dataSource,setDataSource,editableKeys} =props;
  const columns: ProColumns<IDataSourceType>[] = [
    {
      title: '参数名称',
      dataIndex: 'configValueKey',
      width: '280px',
      formItemProps: {
        rules: [
          {
            required: true,
            whitespace: false,
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
            whitespace: false,
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
      width: '120px',
      render: () => {
        return null;
      },
    }
  ];
  const upLoadProps: UploadProps = {
    name: 'xmlFile',
    action: '/api/p1/sys/xmlConfigValue',
    onChange(info) {
      if (info.file.status !== 'uploading') {
          const data =info?.file?.response?.data;
          setDataSource(dataToList(data))
      }
      if (info.file.status === 'done') {
        message.success("上传成功");
      } else if (info.file.status === 'error') {
        message.error("上传失败");
      }
    },
    showUploadList:false
  };
  return (
    <>
      <EditableProTable<IDataSourceType>
        headerTitle={headerTitle}
        columns={columns}
        rowKey="id"
        value={dataSource}
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
            setDataSource?.(recordList);
          },
          actionRender: (row, config, defaultDoms) => {
            return [defaultDoms.delete];
          },
        }}
      />
    </>
  );
};
export  default HadoopPanel
