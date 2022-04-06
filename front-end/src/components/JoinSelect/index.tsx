import React, { FC } from 'react';
import { Select } from 'antd';
import { SelectProps } from 'antd/es/select';

const JoinSelect: FC<{ onChange: (value?: string) => void; value?: string } & SelectProps> = ({
  value,
  onChange,
  ...rest
}) => {
  const innerVal = value?.split(',');

  const handleChange = (value: (number | string)[], option: any) => {
    const realVal = value?.join(',');
    onChange?.(realVal, option);
  };
  return <Select mode="multiple" value={innerVal} onChange={handleChange} {...rest} />;
};

export default JoinSelect;
