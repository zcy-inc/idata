import React, { Fragment } from 'react';
import { Descriptions, Table, Tabs } from 'antd';
import type { FC } from 'react';
import styles from '../../index.less';

import Title from '../../../components/Title';

export interface ViewModifierProps {}

const { Item } = Descriptions;
const { TabPane } = Tabs;
const Cols = [
  { title: '表名', dataIndex: '', key: '' },
  { title: '关联表字段', dataIndex: '', key: '' },
];

const ViewModifier: FC<ViewModifierProps> = ({}) => {
  return (
    <Fragment>
      <Title>基本信息</Title>
      <Descriptions
        column={3}
        colon={false}
        labelStyle={{ color: '#8A8FAE' }}
        style={{ margin: '16px 0' }}
      >
        <Item label="维度名称">{'-'}</Item>
        <Item label="英文别名">{'-'}</Item>
        <Item label="枚举值">{'-'}</Item>
        <Item label="定义" span={3}>
          {'-'}
        </Item>
        <Item label="备注" span={3}>
          {'-'}
        </Item>
      </Descriptions>
      <Title>关联信息</Title>
      <Tabs className={styles['reset-tabs']}>
        <TabPane key="main" tab="事实表">
          <Table columns={Cols} dataSource={[]} pagination={false} style={{ marginTop: 24 }} />
        </TabPane>
      </Tabs>
    </Fragment>
  );
};

export default ViewModifier;
