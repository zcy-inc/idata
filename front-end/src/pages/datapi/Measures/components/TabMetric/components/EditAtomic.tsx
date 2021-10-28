import React, { forwardRef, Fragment, useEffect, useImperativeHandle, useState } from 'react';
import { Select, Tabs } from 'antd';
import { EditableProTable } from '@ant-design/pro-table';
import { get } from 'lodash';
import type { ProColumns } from '@ant-design/pro-table';
import type { ForwardRefRenderFunction, Key } from 'react';

import Title from '@/components/Title';
import { Metric, Table } from '@/types/datapi';
import { getTableReferStr, getTableReferTbs } from '@/services/datadev';
import { AggregatorCodeOptions } from '@/constants/datapi';

export interface EditAtomicProps {
  initial?: Metric;
}
interface DWD {
  id: number;
  tableId?: number;
  columnName?: string;
  aggregatorCode?: string;
}
interface TableOptions {
  label: string;
  value: number;
}

// const { Link } = Typography;
const { TabPane } = Tabs;
const initialKey = Date.now();

const EditAtomic: ForwardRefRenderFunction<unknown, EditAtomicProps> = ({ initial }, ref) => {
  const [DWDData, setDWDData] = useState<DWD[]>([{ id: initialKey }]);
  const [DWDKeys, setDWDKeys] = useState<Key[]>([initialKey]);
  const [DWDTables, setDWDTables] = useState<TableOptions[]>([]);
  const [DWDStrings, setDWDStrings] = useState<[][]>([]);

  useImperativeHandle(ref, () => ({
    data: DWDData,
  }));

  useEffect(() => {
    getTableReferTbs({ labelValue: 'dwd' })
      .then((res) => {
        const t = res.data?.map((table: Table) => ({
          label: table.tableName,
          value: table.id,
        }));
        setDWDTables(t);
      })
      .catch((err) => {});
  }, []);

  useEffect(() => {
    if (initial) {
      let tableId = '';
      const tmpK: Key[] = [];
      const tmp: DWD[] = initial.measureLabels?.map((label) => {
        tmpK.push(label.id);
        tableId = `${label.tableId}`;
        return {
          id: label.id,
          tableId: label.tableId,
          columnName: label.columnName,
          aggregatorCode: initial.specialAttribute.aggregatorCode,
        };
      });
      if (tableId) {
        getTableReferStr({ tableId })
          .then((res) => {
            const strs = res.data?.map((_: any) => ({ label: _.columnName, value: _.columnName }));
            DWDStrings[0] = strs;
            setDWDStrings([...DWDStrings]);
          })
          .catch((err) => {});
      }

      if (tmp.length === 0) {
        tmp.push({ id: initialKey });
        tmpK.push(initialKey);
      }
      setDWDData(tmp);
      setDWDKeys(tmpK);
    }
  }, [initial]);

  // 添加一行数据
  // const addData = () => {
  //   if (DWDData.length > 0) {
  //     message.info('原子指标的事实表只能存在一张');
  //     return;
  //   }
  //   const id = Date.now();
  //   const data = { id };
  //   setDWDData([...DWDData, data]);
  //   setDWDKeys([...DWDKeys, id]);
  // };

  const setValue = (schema: any, value: any) => {
    const id = get(schema, 'entry.id');
    const d = DWDData[0] || {};
    const tmp = { ...d, id };
    if (schema.dataIndex === 'tableId') {
      getTableReferStr({ tableId: value })
        .then((res) => {
          const strs = res.data?.map((_: any) => ({ label: _.columnName, value: _.columnName }));
          DWDStrings[schema.index] = strs;
          setDWDStrings([...DWDStrings]);
        })
        .catch((err) => {});
    }
    tmp[schema.dataIndex] = value; // tmp.tableId = value
    setDWDData([tmp]);
  };

  // 操作栏行为
  // const onAction = (row: any, _: any) => {
  //   const i = DWDData.findIndex((_) => _.id === row.id);
  //   DWDData.splice(i, 1);
  //   DWDKeys.splice(i, 1);
  //   setDWDData([...DWDData]);
  //   setDWDKeys([...DWDKeys]);
  // };

  const Cols: ProColumns[] = [
    {
      title: '表名',
      dataIndex: 'tableId',
      key: 'tableId',
      renderFormItem: (schema) => (
        <Select
          placeholder="请选择"
          options={DWDTables}
          onChange={(value) => setValue(schema, value)}
          showSearch
          filterOption={(input, option) => `${option?.label}`.indexOf(input) >= 0}
        />
      ),
    },
    {
      title: '关联表字段',
      dataIndex: 'columnName',
      key: 'columnName',
      renderFormItem: (schema) => (
        <Select
          allowClear
          placeholder="请选择"
          options={DWDStrings[schema.index as number]}
          onChange={(value) => setValue(schema, value)}
          showSearch
          filterOption={(input, option) => `${option?.label}`.indexOf(input) >= 0}
        />
      ),
    },
    {
      title: '聚合方式',
      dataIndex: 'aggregatorCode',
      key: 'aggregatorCode',
      renderFormItem: (schema) => (
        <Select
          allowClear
          placeholder="请选择"
          options={AggregatorCodeOptions}
          onChange={(value) => setValue(schema, value)}
        />
      ),
    },
    // { title: '操作', valueType: 'option', fixed: 'right', width: 50 },
  ];

  return (
    <Fragment>
      <Title>关联信息</Title>
      <Tabs className="reset-tabs">
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
              onChange: (keys, rows) => setDWDKeys(keys),
              // actionRender: (row, _) => [<Link onClick={() => onAction(row, _)}>删除</Link>],
            }}
          />
          {/* <Link onClick={addData} style={{ display: 'inline-block', marginTop: 16 }}>
            <IconFont type="icon-tianjia" style={{ marginRight: 4 }} />
            添加字段
          </Link> */}
        </TabPane>
      </Tabs>
    </Fragment>
  );
};

export default forwardRef(EditAtomic);
