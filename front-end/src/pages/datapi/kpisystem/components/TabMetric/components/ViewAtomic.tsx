import React, { Fragment, useEffect, useState } from 'react';
import { Table, Tabs } from 'antd';
import type { FC } from 'react';
import styles from '../../../../kpisystem/index.less';

import Title from '../../../../components/Title';

export interface EditAtomicProps {}

const { TabPane } = Tabs;
const ColsDWD = [
  { title: '表名', dataIndex: 'name', key: 'name' },
  { title: '关联表字段', dataIndex: 'stringName', key: 'stringName' },
  { title: '聚合方式', dataIndex: 'test', key: 'test' },
];
const ColsDIM = [
  { title: '维度名称', dataIndex: 'name', key: 'name' },
  { title: '英文别名', dataIndex: 'e', key: 'e' },
  { title: '表名', dataIndex: 't', key: 't' },
  { title: '关联表字段', dataIndex: 'r', key: 'r' },
];
const ColsDerive = [
  { title: '指标名称', dataIndex: 'name', key: 'name' },
  { title: '业务过程', dataIndex: 'test', key: 'test' },
];

const EditAtomic: FC<EditAtomicProps> = ({}) => {
  return (
    <Fragment>
      <Title>关联信息</Title>
      <Tabs className={styles['reset-tabs']}>
        <TabPane key="dwd" tab="事实表">
          <Table columns={ColsDWD} dataSource={[]} pagination={false} />
        </TabPane>
        <TabPane key="dim" tab="维度">
          <Table columns={ColsDIM} dataSource={[]} pagination={false} />
        </TabPane>
        <TabPane key="dir" tab="派生指标">
          <Table columns={ColsDerive} dataSource={[]} pagination={false} />
        </TabPane>
      </Tabs>
    </Fragment>
  );
};

export default EditAtomic;
