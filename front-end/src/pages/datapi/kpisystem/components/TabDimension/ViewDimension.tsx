import React, { Fragment, useEffect, useState } from 'react';
import { Descriptions, Table, Tabs } from 'antd';
import type { FC } from 'react';
import styles from '../../index.less';

import Title from '../../../components/Title';
import { Dimension, LabelAttribute, TableLable } from '@/types/datapi';
import { KpiLabelsMap } from '@/constants/datapi';

export interface ViewDimensionProps {
  data: Dimension;
}

const { Item } = Descriptions;
const { TabPane } = Tabs;
const ColsDIM = [
  { title: '表名', dataIndex: '', key: '' },
  { title: '字段名', dataIndex: '', key: '' },
  { title: '表名', dataIndex: '', key: '' },
];
const ColsDWD = [
  { title: '表名', dataIndex: '', key: '' },
  { title: '关联表字段', dataIndex: '', key: '' },
];

const ViewDimension: FC<ViewDimensionProps> = ({ data }) => {
  const [attributes, setAttributes] = useState<LabelAttribute[]>([]);
  const [DIM, setDIM] = useState<TableLable[]>([]);
  const [DWD, setDWD] = useState<TableLable[]>([]);

  useEffect(() => {
    if (data) {
      const list = data.targetLabels;
      const tmpM: TableLable[] = [];
      const tmpD: TableLable[] = [];
      list.map((item) => {
        if (item.labelParamValue === 'true') {
          tmpM.push(item);
        } else {
          tmpD.push(item);
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
          <Item label={KpiLabelsMap[attribute.attributeKey]}>{attribute.attributeValue}</Item>
        ))}
      </Descriptions>
      <Title>关联信息</Title>
      <Tabs className={styles['reset-tabs']}>
        <TabPane key="DIM" tab="主表">
          <Table columns={ColsDIM} dataSource={DIM} pagination={false} style={{ marginTop: 24 }} />
        </TabPane>
        <TabPane key="dim" tab="事实表">
          <Table columns={ColsDWD} dataSource={DWD} pagination={false} style={{ marginTop: 24 }} />
        </TabPane>
      </Tabs>
    </Fragment>
  );
};

export default ViewDimension;
