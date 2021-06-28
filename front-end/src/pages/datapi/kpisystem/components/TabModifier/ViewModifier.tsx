import React, { Fragment, useEffect, useState } from 'react';
import { Descriptions, Table, Tabs } from 'antd';
import type { FC } from 'react';
import styles from '../../index.less';

import Title from '../../../components/Title';
import { LabelAttribute, Modifier, TableLable } from '@/types/datapi';

export interface ViewModifierProps {
  data: Modifier;
}

const { Item } = Descriptions;
const { TabPane } = Tabs;
const Cols = [
  { title: '表名', dataIndex: '', key: '' },
  { title: '关联表字段', dataIndex: '', key: '' },
];

const ViewModifier: FC<ViewModifierProps> = ({ data }) => {
  const [attributes, setAttributes] = useState<LabelAttribute[]>([]);
  const [DWD, setDWD] = useState<TableLable[]>([]);

  useEffect(() => {
    if (data) {
      setDWD(data.targetLabels);
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
          <Item label={attribute.attributeKey}>{attribute.attributeValue}</Item>
        ))}
      </Descriptions>
      <Title>关联信息</Title>
      <Tabs className={styles['reset-tabs']}>
        <TabPane key="main" tab="事实表">
          <Table columns={Cols} dataSource={DWD} pagination={false} style={{ marginTop: 24 }} />
        </TabPane>
      </Tabs>
    </Fragment>
  );
};

export default ViewModifier;
