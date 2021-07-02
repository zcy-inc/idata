import React, { Fragment, useEffect, useState } from 'react';
import { Typography } from 'antd';
import type { FC } from 'react';

import Title from '../../../../components/Title';

export interface EditAtomicProps {}

const { Paragraph } = Typography;

const EditAtomic: FC<EditAtomicProps> = ({}) => {
  return (
    <Fragment>
      <Title>生成复合指标</Title>
      <Paragraph style={{ marginTop: 16 }}>{'-'}</Paragraph>
    </Fragment>
  );
};

export default EditAtomic;
