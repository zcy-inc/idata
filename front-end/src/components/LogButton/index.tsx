import React, { useState } from 'react';
import type { PropsWithChildren } from 'react';
import LogModal from '../LogModal';
import type { LogModalProps } from '../LogModal';
import { Button } from 'antd';

interface LogButtonProps<T = any> extends PropsWithChildren<unknown>, LogModalProps<T> {}

function LogButton<T>({ children, ...rest }: LogButtonProps<T>) {
  const [visible, setVisible] = useState(false);
  const close = () => setVisible(false);
  const open = () => setVisible(true);
  return (
    <div>
      <Button onClick={open}>{children}</Button>
      <LogModal {...rest} visible={visible} onCancel={close} />
    </div>
  );
}

export default LogButton;
