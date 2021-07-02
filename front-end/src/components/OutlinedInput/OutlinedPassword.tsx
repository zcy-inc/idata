import React, { useState } from 'react';
import OutlinedInput, { OutlinedInputProps } from './OutlinedInput';
import IconFont from '../IconFont';

import './OutlinedPassword.less';

const prefixCls = 'outlined-password';

const OutlinedPassword: React.FC<Omit<OutlinedInputProps, 'endAdornment' | 'type'>> = (props) => {
  const [visible, setVisible] = useState(false);
  const changeVisible = () => setVisible(!visible);
  const endAdornment = visible ? (
    <IconFont type="icon-eye-open" />
  ) : (
    <IconFont type="icon-eye-close" />
  );
  const type = visible ? 'text' : 'password';
  return (
    <OutlinedInput
      {...props}
      type={type}
      endAdornment={
        <div className={`${prefixCls}-suffix`} onClick={changeVisible}>
          {endAdornment}
        </div>
      }
    />
  );
};

export default OutlinedPassword;
