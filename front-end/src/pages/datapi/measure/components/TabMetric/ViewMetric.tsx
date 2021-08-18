import React, { Fragment, useEffect, useState } from 'react';
import { Descriptions } from 'antd';
import type { FC } from 'react';

import Title from '../../../components/Title';
import ViewAtomic from './components/ViewAtomic';
import ViewDerive from './components/ViewDerive';
import ViewComplex from './components/ViewComplex';
import { LabelAttribute, Metric } from '@/types/datapi';
import { KpiLabelsMap, LabelTag } from '@/constants/datapi';

export interface ViewModifierProps {
  data: Metric;
}

const { Item } = Descriptions;
const TagMap = (labelTag: LabelTag) => {
  switch (labelTag) {
    case LabelTag.ATOMIC_METRIC_LABEL:
    case LabelTag.ATOMIC_METRIC_LABEL_DISABLE:
      return '原子指标';
    case LabelTag.DERIVE_METRIC_LABEL:
    case LabelTag.DERIVE_METRIC_LABEL_DISABLE:
      return '派生指标';
    case LabelTag.COMPLEX_METRIC_LABEL:
    case LabelTag.COMPLEX_METRIC_LABEL_DISABLE:
    default:
      return '复合指标';
  }
};

const ViewModifier: FC<ViewModifierProps> = ({ data }) => {
  const [attributes, setAttributes] = useState<LabelAttribute[]>([]);

  const ViewMap = (labelTag: LabelTag) => {
    switch (labelTag) {
      case LabelTag.ATOMIC_METRIC_LABEL:
      case LabelTag.ATOMIC_METRIC_LABEL_DISABLE:
        return <ViewAtomic data={data} />;
      case LabelTag.DERIVE_METRIC_LABEL:
      case LabelTag.DERIVE_METRIC_LABEL_DISABLE:
        return <ViewDerive data={data} />;
      case LabelTag.COMPLEX_METRIC_LABEL:
      case LabelTag.COMPLEX_METRIC_LABEL_DISABLE:
      default:
        return <ViewComplex data={data} />;
    }
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
        <Item label="指标类型">{TagMap(data?.labelTag)}</Item>
        <Item label="指标名称">{data?.labelName}</Item>
        {attributes?.map((attribute) => (
          <Item label={KpiLabelsMap[attribute.attributeKey]}>
            {attribute.enumValue || attribute.attributeValue || '-'}
          </Item>
        ))}
      </Descriptions>
      {ViewMap(data?.labelTag)}
    </Fragment>
  );
};

export default ViewModifier;
