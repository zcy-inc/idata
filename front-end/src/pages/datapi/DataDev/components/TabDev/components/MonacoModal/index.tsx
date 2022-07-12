import React, { useEffect, useState } from 'react';
import type { FC } from 'react';
import MonacoEditor from 'react-monaco-editor';
import { Modal } from 'antd';
import { getDevLog } from '@/services/datadev';
import './index.less';
import { FullscreenOutlined, FullscreenExitOutlined } from '@ant-design/icons';
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
  const [fullScreen, setFullScreen] = useState('normal');
  const modalProp = {
    full: {
      bodyStyle: {
        padding: 16,
        height: "calc(100vh - 55px)",
      },
      style: {
        maxWidth: "100vw",
        top: 0,
        paddingBottom: 0
      },
      width: "100vw", 
    },
    normal: {
      bodyStyle: {
        padding: 16,
      },
      width: 800, 
    }
  };
  const monacoProp = {
    full: '100vh',
    normal: 400,
  };

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

  const resizeModal = () => {
    fullScreen === 'full' && setFullScreen('normal');
    fullScreen === 'normal' && setFullScreen('full');
  }

  return <Modal
    title="查看日志"
    visible={visible}
    onCancel={onClose}
    footer={null}
    {
      ...modalProp[fullScreen]
    }
    >
      <MonacoEditor
        height={monacoProp[fullScreen]}
        language="json"
        theme="vs-dark"
        value={log}
        options={{ readOnly: true }}
      />
      {
        fullScreen === 'normal' && <FullscreenOutlined className='log-monaco_resize' onClick={resizeModal}/>
      }
      {
        fullScreen === 'full' && <FullscreenExitOutlined className='log-monaco_resize'  onClick={resizeModal}/>
      }
  </Modal>
};

export default MonacoModal;