import React, { Fragment, useEffect, useState } from 'react';
import ProForm, {
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  ProFormGroup,
} from '@ant-design/pro-form';
import { Form, Popover, Table, Tabs, Select, Typography } from 'antd';
import { EditableProTable } from '@ant-design/pro-table';
import type { ProColumns } from '@ant-design/pro-table';
import type { FC } from 'react';
import styles from '../../index.less';

import IconFont from '@/components/IconFont';
import Title from '../../../components/Title';
import { getFolders } from '@/services/kpisystem';
import { rulesText, rulesSelect } from './constants';

export interface ViewModifierProps {}

const { TabPane } = Tabs;
const { Link } = Typography;

const ViewModifier: FC<ViewModifierProps> = ({}) => {
  const [folderOps, setFolderOps] = useState([]);
  const [formLabel] = Form.useForm();

  const [DWDData, setDWDData] = useState<any[]>([]);
  const [DWDKeys, setDWDKeys] = useState<React.Key[]>([]);

  useEffect(() => {
    getFolders()
      .then((res) => {
        const fd = res.data.map((_: any) => ({ label: _.folderName, value: `${_.id}` }));
        setFolderOps(fd);
      })
      .catch((err) => {});
  }, []);

  // 添加一行数据
  const addData = () => {
    const id = Date.now();
    const data = { id };
    setDWDData([...DWDData, data]);
    setDWDKeys([...DWDKeys, id]);
  };
  // 因为是自己维护的data, 所以手动录入数据
  const setValue = (schema: any, value: any) => {
    DWDData[schema.index][schema.dataIndex] = value;
    setDWDData([...DWDData]);
  };
  // 操作栏行为
  const onAction = (row: any, _: any) => {
    const i = DWDData.findIndex((_) => _.id === row.id);
    DWDData.splice(i, 1);
    DWDKeys.splice(i, 1);
    setDWDData([...DWDData]);
    setDWDKeys([...DWDKeys]);
  };

  const Cols: ProColumns[] = [
    {
      title: '表名',
      dataIndex: 'name',
      key: 'name',
      renderFormItem: (schema) => (
        <Select
          allowClear
          placeholder="请选择"
          options={[]}
          onChange={(value) => setValue(schema, value)}
        />
      ),
    },
    {
      title: '关联表字段',
      dataIndex: 'stringName',
      key: 'stringName',
      renderFormItem: (schema) => (
        <Select
          allowClear
          placeholder="请选择"
          options={[]}
          onChange={(value) => setValue(schema, value)}
        />
      ),
    },
    { title: '操作', valueType: 'option', fixed: 'right', width: 50 },
  ];
  const ColsPreview = [
    { title: '枚举值', dataIndex: 'enumValue', key: 'enumValue' },
    { title: '父级枚举值', dataIndex: 'parentValue', key: 'parentValue' },
  ];

  return (
    <Fragment>
      <Title>基本信息</Title>
      <ProForm
        className={`${styles.reset} ${styles['reset-inline']}`}
        layout="horizontal"
        colon={false}
        form={formLabel}
        submitter={false}
      >
        <ProFormGroup>
          <ProFormText
            name="name"
            label="修饰词名称"
            width="sm"
            placeholder="请输入"
            rules={rulesText}
          />
          <ProFormText
            name="en"
            label="英文别名"
            width="sm"
            placeholder="请输入"
            rules={rulesText}
          />
          <ProFormSelect
            name="id"
            label="枚举值"
            width="sm"
            placeholder="请输入"
            rules={rulesSelect}
            tooltip="若为空，请在数仓设计-新建枚举类型处新建。"
            options={[]}
          />
          <ProForm.Item>
            <Popover
              content={
                <Table columns={ColsPreview} dataSource={[]} pagination={false} size="small" />
              }
            >
              <Link>预览</Link>
            </Popover>
          </ProForm.Item>
        </ProFormGroup>
        <ProFormText name="define" label="定义" width="md" placeholder="请输入" rules={rulesText} />
        <ProFormTextArea name="remark" label="备注" width="md" placeholder="请输入" />
        <ProFormSelect
          name="folderId"
          label="位置"
          width="md"
          placeholder="根目录"
          options={folderOps}
        />
      </ProForm>
      <Title>关联信息</Title>
      <Tabs className={styles['reset-tabs']}>
        <TabPane key="main" tab="事实表">
          <EditableProTable
            rowKey="id"
            columns={Cols}
            value={DWDData}
            pagination={false}
            recordCreatorProps={false}
            style={{ marginTop: 24 }}
            editable={{
              type: 'multiple',
              editableKeys: DWDKeys,
              onChange: setDWDKeys,
              actionRender: (row, _) => [<Link onClick={() => onAction(row, _)}>删除</Link>],
            }}
          />
          <Link onClick={addData} style={{ display: 'inline-block', marginTop: 16 }}>
            <IconFont type="icon-tianjia" />
            添加字段
          </Link>
        </TabPane>
      </Tabs>
    </Fragment>
  );
};

export default ViewModifier;
