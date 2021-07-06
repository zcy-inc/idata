import React, { Fragment, useEffect, useState } from 'react';
import { Descriptions } from 'antd';
import type { FC } from 'react';

import Title from '../../../components/Title';
import ViewAtomic from './components/ViewAtomic';
import ViewDerive from './components/ViewDerive';
import ViewComplex from './components/ViewComplex';
import { LabelAttribute, Metric } from '@/types/datapi';
import { KpiLabelsMap } from '@/constants/datapi';

export interface ViewModifierProps {
  data: Metric;
}

const { Item } = Descriptions;
const TagMap = {
  ATOMIC_METRIC_LABEL: '原子指标',
  DERIVE_METRIC_LABEL: '派生指标',
  COMPLEX_METRIC_LABEL: '复合指标',
};

const ViewModifier: FC<ViewModifierProps> = ({ data }) => {
  const [attributes, setAttributes] = useState<LabelAttribute[]>([]);

  const ViewMap = {
    ATOMIC_METRIC_LABEL: <ViewAtomic data={data} />,
    DERIVE_METRIC_LABEL: <ViewDerive data={data} />,
    COMPLEX_METRIC_LABEL: <ViewComplex data={data} />,
  };

  useEffect(() => {
    if (data) {
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
        <Item label="指标类型">{TagMap[data?.labelTag]}</Item>
        <Item label="指标名称">{data?.labelName}</Item>
        {attributes.map((attribute) => (
          <Item label={KpiLabelsMap[attribute.attributeKey]}>
            {attribute.enumValue || attribute.attributeValue || '-'}
          </Item>
        ))}
      </Descriptions>
      {ViewMap[data?.labelTag]}
    </Fragment>
  );
};

export default ViewModifier;
