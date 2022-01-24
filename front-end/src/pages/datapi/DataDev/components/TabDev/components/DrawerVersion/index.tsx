import React, { useEffect, useState } from 'react';
import MonacoEditor from 'react-monaco-editor';
import { Drawer, Modal, Table } from 'antd';
import type { FC } from 'react';
import styles from './index.less';
import { Task } from '@/types/datadev';
import { getTasks } from '@/services/task';
import { TaskListItem } from '@/types/tasks';
import moment from 'moment';
import { VersionStatusDisplayMap } from '@/constants/datadev';

interface DrawerConfigProps {
  visible: boolean;
  onClose: () => void;
  data?: Task;
}

const DrawerVersion: FC<DrawerConfigProps> = ({ visible, onClose, data }) => {
  const [versions, setVersions] = useState<TaskListItem[]>([]);
  const [total, setTotal] = useState<number>(-1);
  const [visibleLog, setVisibleLog] = useState(false);
  const [log, setLog] = useState('');

  useEffect(() => {
    if (visible) {
      getTasksWrapped(0);
    }
  }, [visible]);

  const getTasksWrapped = (offset: number) => {
    getTasks({ jobId: data?.id, limit: 10, offset })
      .then((res) => {
        setVersions(res.data.content);
        setTotal(res.data.total);
      })
      .catch((err) => {});
  };

  return (
    <Drawer
      className={styles['drawer-version']}
      visible={visible}
      onClose={onClose}
      destroyOnClose
      width={780}
      title="版本"
      footerStyle={{
        textAlign: 'right',
        boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.15)',
        zIndex: 1,
        padding: '12px 28px',
      }}
    >
      <Table
        rowKey="id"
        columns={[
          {
            title: '版本',
            dataIndex: 'jobContentVersionDisplay',
            key: 'jobContentVersionDisplay',
          },
          { title: '环境', dataIndex: 'environment', key: 'environment' },
          {
            title: '状态',
            dataIndex: 'publishStatus',
            key: 'publishStatus',
            render: (_) => VersionStatusDisplayMap[_],
          },
          {
            title: '提交备注',
            dataIndex: 'submitRemark',
            key: 'submitRemark',
            ellipsis: true,
            render: (_) => _ || '-',
          },
          {
            title: '操作人',
            dataIndex: 'approveOperator',
            key: 'approveOperator',
            render: (_) => _ || '-',
          },
          {
            title: '操作时间',
            dataIndex: 'approveTime',
            key: 'approveTime',
            render: (_) => moment(_).format('YYYY-MM-DD HH:mm:ss'),
          },
          {
            title: '操作',
            key: 'options',
            fixed: 'right',
            render: (_) => (
              <a
                onClick={() => {
                  setLog('');
                  setVisibleLog(true);
                }}
              >
                查看
              </a>
            ),
          },
        ]}
        dataSource={versions}
        scroll={{ x: 'max-content' }}
        pagination={{
          total,
          showTotal: (t) => `共${t}条`,
          onChange: (page) => getTasksWrapped(10 * (page - 1)),
        }}
      />
      <Modal title="日志" visible={visibleLog} footer={null} onCancel={() => setVisibleLog(false)}>
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
