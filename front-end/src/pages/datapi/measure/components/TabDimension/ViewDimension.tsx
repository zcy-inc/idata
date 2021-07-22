import React, { Fragment, useEffect, useState } from 'react';
import { Descriptions, Table, Tabs } from 'antd';
import type { FC } from 'react';
import styles from '../../index.less';

import Title from '../../../components/Title';
import { Dimension, LabelAttribute } from '@/types/datapi';
import { KpiLabelsMap } from '@/constants/datapi';

interface ViewDimensionProps {
  data: Dimension;
}
interface DIMList {
  tableName: string;
  columnName: string;
  degradeDim: string;
}
interface DWDList {
  tableName: string;
  columnName: string;
}

const { Item } = Descriptions;
const { TabPane } = Tabs;
const ColsDIM = [
  { title: '表名', dataIndex: 'tableName', key: 'tableName' },
  { title: '字段名', dataIndex: 'columnName', key: 'columnName' },
  { title: '是否退化维', dataIndex: 'degradeDim', key: 'degradeDim' },
];
const ColsDWD = [
  { title: '表名', dataIndex: 'tableName', key: 'tableName' },
  { title: '关联表字段', dataIndex: 'columnName', key: 'columnName' },
];

const ViewDimension: FC<ViewDimensionProps> = ({ data }) => {
  const [attributes, setAttributes] = useState<LabelAttribute[]>([]);
  const [DIM, setDIM] = useState<DIMList[]>([]);
  const [DWD, setDWD] = useState<DWDList[]>([]);

  useEffect(() => {
    if (data) {
      const list = data.measureLabels;
      const tmpM: DIMList[] = [];
      const tmpD: DWDList[] = [];
      list.forEach((item) => {
        if (item.labelParamValue === 'true') {
          tmpM.push({
            tableName: item.tableName,
            columnName: item.columnName,
            degradeDim: data.specialAttribute.degradeDim ? '是' : '否',
          });
        } else {
          tmpD.push({
            tableName: item.tableName,
            columnName: item.columnName,
          });
        }
      });

      setDIM(tmpM);
      setDWD(tmpD);
      setAttributes(data.labelAttributes);
    }
  }, [data]);

  return (
    <Fragment>
      <Title>基本信息</Title>
      <Descriptions
        column={3}
        colon={false}
        labelStyle={{ color: '#8A8FAE' }}
        style={{ margin: '16px 0' }}
      >
        <Item label="维度名称">{data?.labelName}</Item>
        {attributes.map((attribute) => (
          <Item label={KpiLabelsMap[attribute.attributeKey]}>
            {attribute.attributeValue || '-'}
          </Item>
        ))}
      </Descriptions>
      <Title>关联信息</Title>
      <Tabs className={styles['reset-tabs']}>
        <TabPane key="DIM" tab="主表">
          <Table
            rowKey="id"
            columns={ColsDIM}
            dataSource={DIM}
            pagination={false}
            scroll={{ x: 'max-content' }}
            style={{ marginTop: 24 }}
          />
        </TabPane>
        <TabPane key="dim" tab="事实表">
          <Table
            rowKey="id"
            columns={ColsDWD}
            dataSource={DWD}
            pagination={false}
            scroll={{ x: 'max-content' }}
            style={{ marginTop: 24 }}
          />
        </TabPane>
      </Tabs>
    </Fragment>
  );
};

export default ViewDimension;
