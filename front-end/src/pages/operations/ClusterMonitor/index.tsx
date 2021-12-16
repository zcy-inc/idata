import React, { useEffect, useState } from 'react';
import moment from 'moment';
import { Modal, Table } from 'antd';
import { PageContainer } from '@/components';
import type { FC } from 'react';
import type { ColumnsType } from 'antd/lib/table/Table';

import { Cluster } from '@/types/operations';
import { getClusters, stopCluster } from '@/services/operations';
import { ClusterState } from '@/constants/operations';

const { confirm } = Modal;

const ClusterMonitor: FC = () => {
  const [data, setData] = useState<Cluster[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    getClustersWrapped();
  }, []);

  const getClustersWrapped = () => {
    setLoading(true);
    getClusters({ state: ClusterState.RUNNING })
      .then((res) => setData(res.data))
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  const columns: ColumnsType<Cluster> = [
    { title: '集群应用ID', key: 'appId', dataIndex: 'appId' },
    { title: '集群应用名称', key: 'appName', dataIndex: 'appName' },
    { title: '应用类型', key: 'applicationType', dataIndex: 'applicationType' },
    { title: '用户', key: 'user', dataIndex: 'user' },
    { title: '队列', key: 'queue', dataIndex: 'queue' },
    {
      title: '开始时间',
      key: 'startedTime',
      dataIndex: 'startedTime',
      render: (_) => moment(_).format('YYYY-MM-DD HH:mm:ss'),
    },
    { title: '分配CPU核数', key: 'allocatedVCores', dataIndex: 'allocatedVCores' },
    { title: '分配内存（GB）', key: 'allocatedMem', dataIndex: 'allocatedMem' },
    {
      title: '操作',
      key: 'ops',
      fixed: 'right',
      render: (_) => (
        <a
          onClick={() =>
            confirm({
              title: '提示',
              content: '您确定要停止该集群吗？',
              onOk: () => stopCluster({ appId: _.appId }),
            })
          }
        >
          停止
        </a>
      ),
    },
  ];

  return (
    <PageContainer>
      <Table<Cluster>
        rowKey="id"
        columns={columns}
        dataSource={data}
        scroll={{ x: 'max-content' }}
        style={{ marginTop: 16 }}
        loading={loading}
        pagination={false}
      />
    </PageContainer>
  );
};

export default ClusterMonitor;
