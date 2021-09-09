import React, { Fragment, useEffect, useState } from 'react';
import { Table, Tabs } from 'antd';
import type { FC } from 'react';

import Title from '@/components/Title';
import { Metric } from '@/types/datapi';
import { AggregatorCodeTransform } from '@/constants/datapi';

interface EditAtomicProps {
  data: Metric;
}
interface DWDList {
  tableName: string;
  columnName: string;
  aggregatorCode: string;
}
interface DIMList {
  labelName: string;
  enName: string;
  tableName: string;
  columnName: string;
}
interface DIRList {
  labelName: string;
  bizProcessValue: string;
}

const { TabPane } = Tabs;
const ColsDWD = [
  { title: '表名', dataIndex: 'tableName', key: 'tableName' },
  { title: '关联表字段', dataIndex: 'columnName', key: 'columnName' },
  { title: '聚合方式', dataIndex: 'aggregatorCode', key: 'aggregatorCode' },
];
const ColsDIM = [
  { title: '维度名称', dataIndex: 'labelName', key: 'labelName' },
  { title: '英文别名', dataIndex: 'enName', key: 'enName' },
  { title: '表名', dataIndex: 'tableName', key: 'tableName' },
  { title: '关联表字段', dataIndex: 'columnName', key: 'columnName' },
];
const ColsDerive = [
  { title: '指标名称', dataIndex: 'labelName', key: 'labelName' },
  { title: '业务过程', dataIndex: 'bizProcessValue', key: 'bizProcessValue' },
];

const EditAtomic: FC<EditAtomicProps> = ({ data }) => {
  const [DWD, setDWD] = useState<DWDList[]>([]);
  const [DIM, setDIM] = useState<DIMList[]>([]);
  const [DIR, setDIR] = useState<DIRList[]>([]);

  useEffect(() => {
    if (data) {
      const dwd: DWDList[] = data.measureLabels?.map((item) => ({
        tableName: item.tableName,
        columnName: item.columnName,
        aggregatorCode: AggregatorCodeTransform[data.specialAttribute.aggregatorCode],
      }));
      const dim = data.dimensions?.map((dimension) => ({
        labelName: dimension.labelName,
        enName: dimension.enName as string,
        tableName: dimension.measureLabels?.[0]?.tableName,
        columnName: dimension.measureLabels?.[0]?.columnName,
      }));
      const dir = data.deriveMetrics?.map((metric) => ({
        labelName: metric.labelName,
        bizProcessValue: metric.bizProcessValue,
      }));

      setDWD(dwd);
      setDIM(dim);
      setDIR(dir);
    }
  }, [data]);

  return (
    <Fragment>
      <Title>关联信息</Title>
      <Tabs className="reset-tabs">
        <TabPane key="dwd" tab="事实表">
          <Table
            columns={ColsDWD}
            dataSource={DWD}
            pagination={false}
            scroll={{ x: 'max-content' }}
            style={{ marginTop: 16 }}
          />
        </TabPane>
        <TabPane key="dim" tab="维度">
          <Table
            columns={ColsDIM}
            dataSource={DIM}
            pagination={false}
            scroll={{ x: 'max-content' }}
            style={{ marginTop: 16 }}
          />
        </TabPane>
        <TabPane key="dir" tab="派生指标">
          <Table
            columns={ColsDerive}
            dataSource={DIR}
            pagination={false}
            scroll={{ x: 'max-content' }}
            style={{ marginTop: 16 }}
          />
        </TabPane>
      </Tabs>
    </Fragment>
  );
};

export default EditAtomic;
