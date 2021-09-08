import React, { useEffect, useState } from 'react';
import MonacoEditor from 'react-monaco-editor';
import { Modal } from 'antd';
import type { FC } from 'react';

import { getDDL } from '@/services/tablemanage';
import { Table } from '@/types/datapi';

interface DDLModalProps {
  visible: boolean;
  onCancel: () => void;
  data?: Table;
}

const DDLModal: FC<DDLModalProps> = ({ visible, onCancel, data }) => {
  const [v, setV] = useState('');

  useEffect(() => {
    if (data) {
      getDDL({ tableId: data.id })
        .then((res) => {
          setV(res.data);
        })
        .catch((e) => {});
    }
  }, [visible]);

  return (
    <Modal
      title="DDL模式"
      visible={visible}
      onCancel={onCancel}
      width={800}
      footer={null}
      bodyStyle={{ padding: 16 }}
    >
      <MonacoEditor
        height="400"
        language="sql"
        theme="vs-dark"
        value={v}
        options={{ readOnly: true }}
      />
    </Modal>
  );
};

export default DDLModal;
