import React, { FC } from 'react';
import { Cascader, CascaderProps } from 'antd';
import { SingleValueType } from 'rc-cascader/es/cascader';
import { getTreeParents } from '@/utils/utils';

export interface CascaderSelectProps extends Omit<CascaderProps<any>, 'value' | 'onChange'> {
  value?: React.Key;
  onChange?: (value?: React.Key) => void;
}

export const CascaderSelect: FC<CascaderSelectProps> = ({ value, onChange, options, ...rest }) => {
  let allKeys: SingleValueType | undefined = undefined;
  if (value && options) {
    allKeys = getTreeParents(options, (node) => node.value, value).map((node) => node.value);
  }

  const handleChange = (value: any) => {
    let parsed: string | number | undefined = undefined;
    if (Array.isArray(value) && value.length > 0) {
      parsed = value[value.length - 1];
    }
    onChange?.(parsed);
  };
  return <Cascader size="large" {...rest} value={allKeys} options={options} onChange={handleChange} />;
};
