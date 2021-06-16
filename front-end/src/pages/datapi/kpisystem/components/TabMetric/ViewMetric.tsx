import React, { Fragment } from 'react';
import { Descriptions } from 'antd';
import type { FC } from 'react';

import Title from '../../../components/Title';
import ViewAtomic from './components/ViewAtomic';
import ViewDerive from './components/ViewDerive';
import ViewComplex from './components/ViewComplex';

export interface ViewModifierProps {}

const { Item } = Descriptions;

const ViewModifier: FC<ViewModifierProps> = ({}) => {
  const ViewMap = {
    atomic: <ViewAtomic />,
    derive: <ViewDerive />,
    complex: <ViewComplex />,
  };

  return (
    <Fragment>
      <Title>基本信息</Title>
      <Descriptions
        column={3}
        colon={false}
        labelStyle={{ color: '#8A8FAE' }}
        style={{ margin: '16px 0' }}
      >
        <Item label="指标类型">{'-'}</Item>
        <Item label="指标名称">{'-'}</Item>
        <Item label="ID">{'-'}</Item>
        <Item label="英文别名">{'-'}</Item>
        <Item label="业务过程" span={2}>
          {'-'}
        </Item>
        <Item label="定义" span={3}>
          {'-'}
        </Item>
        <Item label="备注" span={3}>
          {'-'}
        </Item>
      </Descriptions>
      {ViewMap['complex']}
    </Fragment>
  );
};

export default ViewModifier;
