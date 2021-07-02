import React from 'react';
import { Modal } from 'antd';

import './index.less';

export interface LogModalProps<T = any> {
  title: string;
  logs?: T[];
  visible?: boolean;
  renderItem?: (log: T) => React.ReactNode;
  onCancel?: () => void;
}

function LogModal<T>({ title, visible, logs, renderItem, onCancel }: LogModalProps<T>) {
  const modalProps = {
    title,
    visible,
    mask: false,
    maskClosable: false,
    footer: null,
    onCancel,
    width: 600,
  };
  return (
    <Modal {...modalProps} className="log-modal">
      {logs?.map((log) => renderItem?.(log))}
    </Modal>
  );
}

export default LogModal;
