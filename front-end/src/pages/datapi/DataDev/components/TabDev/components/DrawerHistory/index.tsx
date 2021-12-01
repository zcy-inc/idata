import React, { useEffect, useState } from 'react';
import MonacoEditor from 'react-monaco-editor';
import { Drawer, Modal, Table } from 'antd';
import type { FC } from 'react';
import styles from './index.less';

interface DrawerConfigProps {
  visible: boolean;
  onClose: () => void;
}

const DrawerVersion: FC<DrawerConfigProps> = ({ visible, onClose }) => {
  const [loading, setLoading] = useState(false);
  const [visibleLog, setVisibleLog] = useState(false);
  const [log, setLog] = useState('');

  useEffect(() => {
    setLoading(false);
  }, [visible]);

  return (
    <Drawer
      className={styles['drawer-history']}
      visible={visible}
      onClose={onClose}
      destroyOnClose
      width={780}
      title="历史"
      footerStyle={{
        textAlign: 'right',
        boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.15)',
        zIndex: 1,
        padding: '12px 28px',
      }}
    >
      <Table
        rowKey="id"
        loading={loading}
        pagination={false}
        columns={[
          { title: '开始时间', dataIndex: 'start', key: 'start' },
          { title: '执行时长', dataIndex: 'duration', key: 'duration' },
          { title: '输出结果行数', dataIndex: 'line', key: 'line' },
          { title: '平均内存(GB)', dataIndex: 'memory', key: 'memory' },
          { title: '平均CPU核数', dataIndex: 'cpu', key: 'cpu' },
          { title: '最终状态', dataIndex: 'state', key: 'state' },
          {
            title: '操作',
            key: 'options',
            render: (_) => (
              <a
                onClick={() => {
                  setVisibleLog(true);
                  setLog('_.log');
                }}
              >
                查看
              </a>
            ),
          },
        ]}
        dataSource={[{}]}
      />
      <Modal title="日志" visible={visibleLog} onCancel={() => setVisibleLog(false)}>
        <MonacoEditor
          height="400"
          language="sql"
          theme="vs-dark"
          value={log}
          options={{ readOnly: true }}
        />
      </Modal>
    </Drawer>
  );
};

export default DrawerVersion;
