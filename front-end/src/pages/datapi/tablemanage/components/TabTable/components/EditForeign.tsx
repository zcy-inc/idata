import React, { Fragment, useEffect, useState, useImperativeHandle, forwardRef } from 'react';
import { Typography, Select } from 'antd';
import { EditableProTable } from '@ant-design/pro-table';
import type { ProColumns } from '@ant-design/pro-table';
import type { ForwardRefRenderFunction } from 'react';
import styles from '../../../index.less';

import IconFont from '@/components/IconFont';
import TableRelation from './TableRelation';
import ER from './ER';
import Title from '../../../../components/Title';

import { EROps } from '../constants';
import { getTableReferDbs, getTableReferTbs, getTableReferStr } from '@/services/tablemanage';

export interface EditForeignProps {
  initial?: any;
  _props: { strOps: any[] };
}

const { Link } = Typography;

const EditForeign: ForwardRefRenderFunction<unknown, EditForeignProps> = (
  { initial, _props: { strOps } },
  ref,
) => {
  const [data, setData] = useState<any[]>([]);
  const [keys, setKeys] = useState<React.Key[]>([]);
  const [dbs, setDbs] = useState<any[]>([]);
  const [tbs, setTbs] = useState<any[][]>([]);
  const [str, setStr] = useState<any[][]>([]);

  useImperativeHandle(ref, () => ({ data }));

  useEffect(() => {
    getTableReferDbs()
      .then((res) => {
        const dbs = res.data?.map((_: any) => ({
          label: _.labelParamValue,
          value: _.labelParamValue,
        }));
        setDbs(dbs);
      })
      .catch((err) => {});
  }, []);

  useEffect(() => {
    if (initial) {
      const foreignKeys = initial.foreignKeys;
      const promisesT: Promise<any>[] = [];
      const promisesC: Promise<any>[] = [];
      const _keys: React.Key[] = [];
      const _data = foreignKeys?.map((_: any, i: number) => {
        _keys.push(_.id);
        promisesT[i] = getTableReferTbs({ labelValue: _.referDbName });
        promisesC[i] = getTableReferStr({ tableId: _.referTableId });
        return {
          key: _.id,
          id: _.id,
          columnNames: _.columnNames.split(','),
          referDbName: _.referDbName,
          referTableId: _.referTableId,
          referColumnNames: _.referColumnNames.split(','),
          erType: _.erType,
        };
      });
      Promise.all(promisesT).then((resT) => {
        Promise.all(promisesC).then((resC) => {
          resT.forEach((_T: any, i: number) => {
            tbs[i] = _T.data?.map((_: any) => ({ label: _.tableName, value: _.id }));
          });
          resC.forEach((_C: any, i: number) => {
            str[i] = _C.data?.map((_: any) => ({ label: _.columnName, value: _.columnName }));
          });
          setTbs([...tbs]);
          setStr([...str]);
        });
      });
      setData(_data);
      setKeys(_keys);
    }
  }, [initial]);

  // 添加一行数据
  const addData = () => {
    const key = Date.now();
    const item = { key };
    setData([...data, item]);
    setKeys([...keys, key]);
  };

  // 录入数据
  const setValue = (schema: any, value: any) => {
    if (schema.dataIndex === 'referDbName') {
      getTableReferTbs({ labelValue: value })
        .then((res) => {
          const tmp = res.data?.map((_: any) => ({ label: _.tableName, value: _.id }));
          tbs[schema.index] = tmp;
          setTbs([...tbs]);
        })
        .catch((err) => {});
    }
    if (schema.dataIndex === 'referTableId') {
      getTableReferStr({ tableId: value })
        .then((res) => {
          const tmp = res.data?.map((_: any) => ({ label: _.columnName, value: _.columnName }));
          str[schema.index] = tmp;
          setStr([...str]);
        })
        .catch((err) => {});
    }
    data[schema.index][schema.dataIndex] = value;
    setData([...data]);
  };

  // 外键表的列
  const fkColumns: ProColumns[] = [
    {
      title: '字段',
      dataIndex: 'columnNames',
      key: 'columnNames',
      width: 200,
      renderFormItem: (schema) => (
        <Select
          mode="multiple"
          allowClear
          placeholder="请选择"
          maxTagCount="responsive"
          options={strOps}
          onChange={(value) => setValue(schema, value)}
        />
      ),
    },
    {
      title: '参考数据库',
      dataIndex: 'referDbName',
      key: 'referDbName',
      valueType: 'select',
      width: 200,
      renderFormItem: (schema) => (
        <Select
          allowClear
          placeholder="请选择"
          options={dbs}
          onChange={(value) => setValue(schema, value)}
        />
      ),
    },
    {
      title: '参考表',
      dataIndex: 'referTableId',
      key: 'referTableId',
      valueType: 'select',
      width: 200,
      renderFormItem: (schema: any) => (
        <Select
          allowClear
          placeholder="请选择"
          options={tbs[schema.index]}
          onChange={(value) => setValue(schema, value)}
        />
      ),
    },
    {
      title: '参考字段',
      dataIndex: 'referColumnNames',
      key: 'referColumnNames',
      width: 200,
      renderFormItem: (schema: any) => (
        <Select
          mode="multiple"
          allowClear
          placeholder="请选择"
          maxTagCount="responsive"
          options={str[schema.index]}
          onChange={(value) => setValue(schema, value)}
        />
      ),
    },
    {
      title: 'ER图关系类型',
      dataIndex: 'erType',
      key: 'erType',
      valueType: 'select',
      width: 200,
      renderFormItem: (schema) => (
        <Select
          allowClear
          placeholder="请选择"
          options={EROps}
          onChange={(value) => setValue(schema, value)}
        />
      ),
    },
    { title: '操作', valueType: 'option', fixed: 'right', width: 50 },
  ];

  // 外键表的操作
  const onAction = (row: any, _: any) => {
    const i = data.findIndex((_) => _.key === row.key);
    data.splice(i, 1);
    keys.splice(i, 1);
    setData([...data]);
    setKeys([...keys]);
  };

  return (
    <Fragment>
      <EditableProTable
        className={`${styles.reset} ${styles.fk}`}
        rowKey="key"
        columns={fkColumns}
        value={data}
        bordered
        scroll={{ x: 'max-content' }}
        style={{ padding: '24px 0' }}
        recordCreatorProps={false}
        editable={{
          type: 'multiple',
          editableKeys: keys,
          onChange: setKeys,
          actionRender: (row, _) => [
            <Link key="del" onClick={() => onAction(row, _)}>
              删除
            </Link>,
          ],
        }}
      />
      <Link className={styles['add-fk']} onClick={addData}>
        <IconFont type="icon-tianjia" />
        添加外键
      </Link>
      {initial && (
        <Fragment>
          <Title>关系图预览</Title>
          {/* <TableRelation id={initial.id} /> */}
          <ER id={initial.id} />
        </Fragment>
      )}
    </Fragment>
  );
};

export default forwardRef(EditForeign);
