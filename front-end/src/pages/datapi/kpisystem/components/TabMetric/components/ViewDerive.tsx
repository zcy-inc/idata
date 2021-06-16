import React, { Fragment, useEffect, useState } from 'react';
import { Descriptions, Table, Tabs } from 'antd';
import type { FC } from 'react';
import styles from '../../../../kpisystem/index.less';

import Title from '../../../../components/Title';

export interface EditAtomicProps {}

const { TabPane } = Tabs;
const { Item } = Descriptions;
const ColsDerive = [
  { title: '修饰词', dataIndex: 'name', key: 'name' },
  { title: '枚举值', dataIndex: 'test', key: 'test' },
];
const ColsDIM = [
  { title: '维度名称', dataIndex: 'name', key: 'name' },
  { title: '英文别名', dataIndex: 'e', key: 'e' },
  { title: '表名', dataIndex: 't', key: 't' },
  { title: '关联表字段', dataIndex: 'r', key: 'r' },
];

const EditAtomic: FC<EditAtomicProps> = ({}) => {
  return (
    <Fragment>
      <Title>配置派生指标</Title>
      <Descriptions colon={false} labelStyle={{ color: '#8A8FAE' }} style={{ margin: '16px 0' }}>
        <Item label="原子指标" style={{ paddingBottom: 0 }}>
          {'-'}
        </Item>
      </Descriptions>
      <Table columns={ColsDerive} dataSource={[]} pagination={false} style={{ marginBottom: 24 }} />
      <Title>关联信息</Title>
      <Tabs className={styles['reset-tabs']}>
        <TabPane key="dim" tab="维度">
          <Table columns={ColsDIM} dataSource={[]} pagination={false} style={{ marginTop: 16 }} />
        </TabPane>
      </Tabs>
    </Fragment>
  );
};

export default EditAtomic;
