import React, { forwardRef, Fragment, useEffect, useImperativeHandle, useState } from 'react';
import { Select, Tabs, Typography } from 'antd';
import { EditableProTable } from '@ant-design/pro-table';
import type { ProColumns } from '@ant-design/pro-table';
import type { ForwardRefRenderFunction } from 'react';
import styles from '../../../../kpisystem/index.less';

import IconFont from '@/components/IconFont';
import Title from '../../../../components/Title';
import { Metric, Table } from '@/types/datapi';
import { getTableReferStr, getTableReferTbs } from '@/services/tablemanage';

export interface EditAtomicProps {
  initial?: Metric;
}
interface TableOptions extends Table {
  label: string;
  value: string;
}

const { Link } = Typography;
const { TabPane } = Tabs;

const EditAtomic: ForwardRefRenderFunction<unknown, EditAtomicProps> = ({ initial }, ref) => {
  const [DWDData, setDWDData] = useState<any[]>([]);
  const [DWDKeys, setDWDKeys] = useState<React.Key[]>([]);
  const [DWDTables, setDWDTables] = useState<TableOptions[]>([]);
  const [DWDStrings, setDWDStrings] = useState<[][]>([]);

  useImperativeHandle(ref, () => ({
    DWD: DWDData,
  }));

  useEffect(() => {
    getTableReferTbs({ labelValue: 'dwd' })
      .then((res) => {
        const t = res.data.map((table: Table) => ({
          label: table.tableName,
          value: table.id,
        }));
        setDWDTables(t);
      })
      .catch((err) => {});
  }, []);

  useEffect(() => {
    if (initial) {
    }
  }, [initial]);

  // 添加一行数据
  const addData = () => {
    const id = Date.now();
    const data = { id };
    setDWDData([...DWDData, data]);
    setDWDKeys([...DWDKeys, id]);
  };

  const setValue = (schema: any, value: any) => {
    if (schema.dataIndex === 'tableName') {
      getTableReferStr({ tableId: value })
        .then((res) => {
          const strs = res.data.map((_: any) => ({ label: _.columnName, value: _.columnName }));
          DWDStrings[schema.index] = strs;
          setDWDStrings([...DWDStrings]);
        })
        .catch((err) => {});
    }
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
      dataIndex: 'tableName',
      key: 'tableName',
      renderFormItem: (schema) => (
        <Select
          allowClear
          placeholder="请选择"
          options={DWDTables}
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
          options={DWDStrings[schema.index as number]}
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
            <IconFont type="icon-tianjia" style={{ marginRight: 4 }} />
            添加字段
          </Link>
        </TabPane>
      </Tabs>
    </Fragment>
  );
};

export default forwardRef(EditAtomic);
