import React, { useEffect, useState } from 'react';
import MonacoEditor from 'react-monaco-editor';
import { Button, Modal } from 'antd';
import type { FC } from 'react';

import { getDDL } from '@/services/datadev';
import { Table } from '@/types/datapi';

interface DDLModalProps {
  visible: boolean;
  onCancel: () => void;
  data?: Table;
  generateTableConstruct: (value: string) => Promise<any>;
}

const DDLModal: FC<DDLModalProps> = ({ visible, onCancel, data, generateTableConstruct }) => {
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
      footer={[
        <Button
          size="large"
          onClick={() =>
            generateTableConstruct(v).then((res) => {
              if (res.success) {
                onCancel();
              }
            })
          }
        >
          生成表结构
        </Button>,
      ]}
      bodyStyle={{ padding: 16 }}
    >
      <MonacoEditor
        height="400"
        language="sql"
        theme="vs-dark"
        value={v}
        onChange={(value, e) => setV(value)}
        options={{}}
      />
    </Modal>
  );
};

export default DDLModal;
