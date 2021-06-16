import React, { Fragment, useEffect, useState } from 'react';
import { Input } from 'antd';
import type { FC } from 'react';

import Title from '../../../../components/Title';
export interface EditAtomicProps {}

const { TextArea } = Input;

const EditAtomic: FC<EditAtomicProps> = ({}) => {
  return (
    <Fragment>
      <Title>生成复合指标</Title>
      <TextArea placeholder="请输入" style={{ marginTop: 16 }} />
    </Fragment>
  );
};

export default EditAtomic;
