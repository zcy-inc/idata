import React, { Fragment, useEffect, useState } from 'react';
import ProForm, {
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
  ProFormGroup,
} from '@ant-design/pro-form';
import { Form, Tabs, Radio, Select, Typography } from 'antd';
import { EditableProTable } from '@ant-design/pro-table';
import type { ProColumns } from '@ant-design/pro-table';
import type { FC } from 'react';
import styles from '../../index.less';

import IconFont from '@/components/IconFont';
import Title from '../../../components/Title';
import { getFolders } from '@/services/kpisystem';
import { rules, BooleanOps } from './constants';

export interface ViewDIMProps {}

const { TabPane } = Tabs;
const { Link } = Typography;

const ViewDIM: FC<ViewDIMProps> = ({}) => {
  const [folderOps, setFolderOps] = useState([]);
  const [formLabel] = Form.useForm();

  const [PRIData, setPRIData] = useState<any[]>([]);
  const [PRIKeys, setPRIKeys] = useState<React.Key[]>([]);

  const [DIMData, setDIMData] = useState<any[]>([]);
  const [DIMKeys, setDIMKeys] = useState<React.Key[]>([]);

  useEffect(() => {
    getFolders()
      .then((res) => {
        const fd = res.data.map((_: any) => ({ label: _.folderName, value: `${_.id}` }));
        setFolderOps(fd);
      })
      .catch((err) => {});
  }, []);

  // 添加一行数据
  const addData = (type: 'PRI' | 'DIM') => {
    console.log(type);

    const id = Date.now();
    const data = { id };

    if (type === 'PRI') {
      setPRIData([...PRIData, data]);
      setPRIKeys([...PRIKeys, id]);
    }
    if (type === 'DIM') {
      setDIMData([...DIMData, data]);
      setDIMKeys([...DIMKeys, id]);
    }
  };
  // 因为是自己维护的data, 所以手动录入数据
  const setValue = (schema: any, value: any, type: 'PRI' | 'DIM') => {
    if (type === 'PRI') {
      PRIData[schema.index][schema.dataIndex] = value;
      setPRIData([...PRIData]);
    }
    if (type === 'DIM') {
      DIMData[schema.index][schema.dataIndex] = value;
      setDIMData([...DIMData]);
    }
  };
  // 操作栏行为
  const onAction = (row: any, _: any, type: 'PRI' | 'DIM') => {
    if (type === 'PRI') {
      const i = PRIData.findIndex((_) => _.id === row.id);
      PRIData.splice(i, 1);
      PRIKeys.splice(i, 1);
      setPRIData([...PRIData]);
      setPRIKeys([...PRIKeys]);
    }
    if (type === 'DIM') {
      const i = DIMData.findIndex((_) => _.id === row.id);
      DIMData.splice(i, 1);
      DIMKeys.splice(i, 1);
      setDIMData([...DIMData]);
      setDIMKeys([...DIMKeys]);
    }
  };

  const ColsPRI: ProColumns[] = [
    {
      title: '表名',
      dataIndex: 'name',
      key: 'name',
      renderFormItem: (schema) => (
        <Select
          allowClear
          placeholder="请选择"
          options={[]}
          onChange={(value) => setValue(schema, value, 'PRI')}
        />
      ),
    },
    {
      title: '字段名',
      dataIndex: 'stringName',
      key: 'stringName',
      renderFormItem: (schema) => (
        <Select
          allowClear
          placeholder="请选择"
          options={[]}
          onChange={(value) => setValue(schema, value, 'PRI')}
        />
      ),
    },
    {
      title: '退化维',
      dataIndex: 'test',
      key: 'test',
      renderFormItem: (schema) => (
        <Radio.Group options={BooleanOps} onChange={(value) => setValue(schema, value, 'PRI')} />
      ),
    },
    { title: '操作', valueType: 'option', fixed: 'right', width: 50 },
  ];
  const ColsDIM: ProColumns[] = [
    {
      title: '表名',
      dataIndex: 'name',
      key: 'name',
      renderFormItem: (schema) => (
        <Select
          allowClear
          placeholder="请选择"
          options={[]}
          onChange={(value) => setValue(schema, value, 'PRI')}
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
          onChange={(value) => setValue(schema, value, 'PRI')}
        />
      ),
    },
    { title: '操作', valueType: 'option', fixed: 'right', width: 50 },
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
          <ProFormText name="name" label="维度名称" width="sm" placeholder="请输入" rules={rules} />
          <ProFormText name="en" label="英文别名" width="sm" placeholder="请输入" rules={rules} />
          <ProFormText name="id" label="ID" width="sm" placeholder="请输入" rules={rules} />
        </ProFormGroup>
        <ProFormText name="define" label="定义" width="md" placeholder="请输入" rules={rules} />
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
        <TabPane key="main" tab="主表">
          <EditableProTable
            rowKey="id"
            columns={ColsPRI}
            value={PRIData}
            pagination={false}
            recordCreatorProps={false}
            style={{ marginTop: 24 }}
            editable={{
              type: 'multiple',
              editableKeys: PRIKeys,
              onChange: setPRIKeys,
              actionRender: (row, _) => [
                <Link key="del" onClick={() => onAction(row, _, 'PRI')}>
                  删除
                </Link>,
              ],
            }}
          />
          <Link onClick={() => addData('PRI')} style={{ display: 'inline-block', marginTop: 16 }}>
            <IconFont type="icon-tianjia" />
            添加字段
          </Link>
        </TabPane>
        <TabPane key="dim" tab="事实表">
          <EditableProTable
            rowKey="id"
            columns={ColsDIM}
            value={DIMData}
            pagination={false}
            recordCreatorProps={false}
            style={{ marginTop: 24 }}
            editable={{
              type: 'multiple',
              editableKeys: DIMKeys,
              onChange: setDIMKeys,
              actionRender: (row, _) => [<Link onClick={() => onAction(row, _, 'DIM')}>删除</Link>],
            }}
          />
          <Link onClick={() => addData('DIM')} style={{ display: 'inline-block', marginTop: 16 }}>
            <IconFont type="icon-tianjia" />
            添加字段
          </Link>
        </TabPane>
      </Tabs>
    </Fragment>
  );
};

export default ViewDIM;
