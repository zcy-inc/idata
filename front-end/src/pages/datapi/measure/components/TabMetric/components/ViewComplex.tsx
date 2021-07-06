import React, { Fragment } from 'react';
import { Typography } from 'antd';
import type { FC } from 'react';

import Title from '../../../../components/Title';
import { Metric } from '@/types/datapi';

export interface EditComplexProps {
  data: Metric;
}

const { Paragraph } = Typography;

const EditComplex: FC<EditComplexProps> = ({ data }) => {
  return (
    <Fragment>
      <Title>生成复合指标</Title>
      <Paragraph style={{ marginTop: 16 }}>
        {data.specialAttribute.complexMetricFormula || '-'}
      </Paragraph>
    </Fragment>
  );
};

export default EditComplex;
