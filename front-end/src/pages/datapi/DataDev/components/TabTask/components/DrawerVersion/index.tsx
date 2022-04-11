import React, { useEffect, useState } from 'react';
import { Drawer, Table } from 'antd';
import type { FC } from 'react';
import styles from './index.less';
import { DIJobBasicInfo } from '@/types/datadev';
import { getTasks } from '@/services/task';
import { TaskListItem } from '@/types/tasks';
import moment from 'moment';
import { VersionStatusDisplayMap } from '@/constants/datadev';

interface DrawerConfigProps {
  visible: boolean;
  onClose: () => void;
  data?: DIJobBasicInfo;
}

const DrawerVersion: FC<DrawerConfigProps> = ({ visible, onClose, data }) => {
  const [versions, setVersions] = useState<TaskListItem[]>([]);
  const [total, setTotal] = useState<number>(-1);

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
            width: 130,
          },
          { title: '环境', dataIndex: 'environment', key: 'environment', width: 60 },
          {
            title: '状态',
            dataIndex: 'publishStatus',
            key: 'publishStatus',
            width: 70,
            render: (_) => VersionStatusDisplayMap[_],
          },
          {
            title: '提交备注',
            dataIndex: 'submitRemark',
            key: 'submitRemark',
            width: 178,
            ellipsis: true,
            render: (_) => _ || '-',
          },
          { title: '操作人', dataIndex: 'approveOperator', key: 'approveOperator', width: 80 },
          {
            title: '操作时间',
            dataIndex: 'approveTime',
            key: 'approveTime',
            width: 170,
            render: (_) => moment(_).format('YYYY-MM-DD HH:mm:ss'),
          },
        ]}
        dataSource={versions}
        pagination={{
          total,
          showTotal: (t) => `共${t}条`,
          onChange: (page) => getTasksWrapped(10 * (page - 1)),
        }}
      />
    </Drawer>
  );
};

export default DrawerVersion;
