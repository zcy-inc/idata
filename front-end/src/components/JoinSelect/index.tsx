import React, { FC } from 'react';
import { Select } from 'antd';
import { SelectProps } from 'antd/es/select';

const JoinSelect: FC<{ onChange?: (value?: string) => void; value?: string } & SelectProps> = ({
  value,
  onChange,
  ...rest
}) => {
  let innerVal = value?.split(',');

  const handleChange = (value: (number | string)[], option: any) => {
    const realVal = value?.join(',');
    onChange?.(realVal, option);
  };
  innerVal = innerVal || [];
  return <Select mode="multiple" value={innerVal.filter((item: any) => item)} onChange={handleChange} {...rest} />;
};

export default JoinSelect;
