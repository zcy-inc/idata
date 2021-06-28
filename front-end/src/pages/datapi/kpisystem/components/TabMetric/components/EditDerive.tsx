import React, { forwardRef, Fragment, useEffect, useState } from 'react';
import ProForm, { ProFormSelect } from '@ant-design/pro-form';
import { Select, Typography } from 'antd';
import { EditableProTable } from '@ant-design/pro-table';
import type { ProColumns } from '@ant-design/pro-table';
import type { ForwardRefRenderFunction } from 'react';
import styles from '../../../../kpisystem/index.less';

import IconFont from '@/components/IconFont';
import Title from '../../../../components/Title';
import { rules } from '@/constants/datapi';
import { Metric, Table } from '@/types/datapi';

export interface EditDeriveProps {
  initial?: Metric;
}
interface TableOptions extends Table {
  label: string;
  value: string;
}

const { Link } = Typography;
const { require } = rules;

const EditDerive: ForwardRefRenderFunction<unknown, EditDeriveProps> = ({ initial }, ref) => {
  const [DWDData, setDWDData] = useState<any[]>([]);
  const [DWDKeys, setDWDKeys] = useState<React.Key[]>([]);

  useEffect(() => {}, []);

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
      title: '修饰词',
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
      title: '枚举值',
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

  return (
    <Fragment>
      <Title>配置派生指标</Title>
      <ProForm
        className={`${styles.reset} ${styles['reset-inline']}`}
        layout="horizontal"
        colon={false}
        submitter={false}
      >
        <ProFormSelect
          name="atomic"
          label="原子指标"
          width="md"
          placeholder="请选择"
          rules={require}
          options={[]} // TODO
        />
      </ProForm>
      <EditableProTable
        rowKey="id"
        columns={Cols}
        value={DWDData}
        pagination={false}
        recordCreatorProps={false}
        style={{ marginTop: 24 }}
        cardProps={{ bodyStyle: { padding: 0 } }}
        editable={{
          type: 'multiple',
          editableKeys: DWDKeys,
          onChange: setDWDKeys,
          actionRender: (row, _) => [<Link onClick={() => onAction(row, _)}>删除</Link>],
        }}
      />
      <Link onClick={addData} style={{ display: 'inline-block', marginTop: 16 }}>
        <IconFont type="icon-tianjia" />
        添加修饰词
      </Link>
    </Fragment>
  );
};

export default forwardRef(EditDerive);
