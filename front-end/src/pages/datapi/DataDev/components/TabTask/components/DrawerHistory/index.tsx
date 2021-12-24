import React, { useEffect, useState } from 'react';
import moment from 'moment';
import { Drawer, Table } from 'antd';
import type { FC } from 'react';
import styles from './index.less';
import { getTaskHistory } from '@/services/datadev';
import { Task, TaskHistoryItem } from '@/types/datadev';

interface DrawerHistoryProps {
  visible: boolean;
  onClose: () => void;
  data?: Task;
}
const fmt = 'YYYY-MM-DD HH:mm:ss';

const DrawerHistory: FC<DrawerHistoryProps> = ({ visible, onClose, data }) => {
  const [loading, setLoading] = useState(false);
  const [history, setHistory] = useState<TaskHistoryItem[]>([]);
  const [total, setTotal] = useState(0);

  useEffect(() => {
    if (visible && data) {
      getTaskHistoryWrapped(0);
    }
  }, [visible, data]);

  const getTaskHistoryWrapped = (pageNum: number) => {
    setLoading(false);
    getTaskHistory({
      condition: { id: data?.id as number },
      pageNum,
      pageSize: 10,
    })
      .then((res) => {
        setTotal(res.data.total);
        setHistory(res.data.content);
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

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
        columns={[
          {
            title: '开始时间',
            dataIndex: 'startTime',
            key: 'startTime',
            render: (_) => moment(_).format(fmt),
          },
          { title: '执行时长', dataIndex: 'duration', key: 'duration' },
          { title: '平均内存(GB)', dataIndex: 'avgMemory', key: 'avgMemory' },
          { title: '平均CPU核数', dataIndex: 'avgVcores', key: 'avgVcores' },
          { title: '最终状态', dataIndex: 'finalStatus', key: 'finalStatus' },
          {
            title: '操作',
            key: 'options',
            render: (_) => (
              <a href={_.businessLogsUrl} target="_blank">
                查看
              </a>
            ),
          },
        ]}
        dataSource={history}
        pagination={{
          total,
          showTotal: (t) => `共${t}条`,
          onChange: (page) => getTaskHistoryWrapped(page),
        }}
      />
    </Drawer>
  );
};

export default DrawerHistory;
