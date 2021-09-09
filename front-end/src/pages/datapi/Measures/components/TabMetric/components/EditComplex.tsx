import React, { forwardRef, Fragment, useEffect, useImperativeHandle, useState } from 'react';
import { Input } from 'antd';
import type { ForwardRefRenderFunction } from 'react';

import Title from '@/components/Title';
import { Metric } from '@/types/datapi';

export interface EditAtomicProps {
  initial?: Metric;
}

const { TextArea } = Input;

const EditAtomic: ForwardRefRenderFunction<unknown, EditAtomicProps> = ({ initial }, ref) => {
  const [data, setData] = useState('');

  useImperativeHandle(ref, () => ({ data }));

  useEffect(() => {
    if (initial) {
      setData(initial.specialAttribute.complexMetricFormula);
    }
  }, [initial]);

  return (
    <Fragment>
      <Title>生成复合指标</Title>
      <TextArea
        value={data}
        onChange={({ target: { value } }) => setData(value)}
        placeholder="请输入"
        style={{ marginTop: 16 }}
      />
    </Fragment>
  );
};

export default forwardRef(EditAtomic);
