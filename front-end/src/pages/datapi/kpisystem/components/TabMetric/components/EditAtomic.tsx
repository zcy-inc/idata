import React, { Fragment, useEffect, useState } from 'react';
import { Select, Tabs, Typography } from 'antd';
import { EditableProTable } from '@ant-design/pro-table';
import type { ProColumns } from '@ant-design/pro-table';
import type { FC } from 'react';
import styles from '../../../../kpisystem/index.less';

import IconFont from '@/components/IconFont';
import Title from '../../../../components/Title';

export interface EditAtomicProps {}

const { Link } = Typography;
const { TabPane } = Tabs;

const EditAtomic: FC<EditAtomicProps> = ({}) => {
  const [DWDData, setDWDData] = useState<any[]>([]);
  const [DWDKeys, setDWDKeys] = useState<React.Key[]>([]);

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
    {
      title: '聚合方式',
      dataIndex: 'test',
      key: 'test',
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

  return (
    <Fragment>
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

export default EditAtomic;
