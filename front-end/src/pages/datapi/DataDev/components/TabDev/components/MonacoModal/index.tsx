import React, { useEffect, useState } from 'react';
import type { FC } from 'react';
import MonacoEditor from 'react-monaco-editor';
import { Modal } from 'antd';
import { getDevLog } from '@/services/datadev';

interface MonacoModalProps {
  visible: boolean;
  logParam: {
    jobId: number;
    env: string;
    taskId: number;
  }
  onClose: () => void;
}

const MonacoModal: FC<MonacoModalProps> = ({ visible, logParam, onClose }) => {
  
  const [log, setLog] = useState('');

  useEffect(() => {
    getDevLogHandle();
  }, []);

  const getDevLogHandle = async () => {
    try {
      const { success, data } = await getDevLog({
        ...logParam,
        lineNum: 0,
        skipLineNum: 2000,
      });
      success && setLog(data);
    } catch (e) {
      setLog(e);
    }
  }

  return <Modal
    title="日志"
    visible={visible}
    onCancel={onClose}
    footer={null}
    bodyStyle={{ padding: 16 }}
    width={800}
    >
      <MonacoEditor
        height="400"
        language="json"
        theme="vs-dark"
        value={log}
        options={{ readOnly: true }}
      />
  </Modal>
};

export default MonacoModal;