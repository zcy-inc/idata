import React, { useEffect, useState } from 'react';
import { Radio, Table, Tabs, Typography } from 'antd';
import type { FC } from 'react';
import type { ColumnsType } from 'antd/es/table';
import { ConsumeResourceItem, ConsumeTimeItem } from '@/types/operations';
import { getResourceTop10, getTimeTop10 } from '@/services/operations';
import moment from 'moment';

interface SchedulOperationProps {}
type recentDay = 1 | 7 | 30;

const { TabPane } = Tabs;
const {} = Typography;
const format = 'YYYY-MM-DD HH:mm:ss';

const SchedulOperation: FC<SchedulOperationProps> = () => {
  const [activeKey, setActiveKey] = useState('time');
  const [recentTime, setRecentTime] = useState<recentDay>(1);
  const [dataTime, setDataTime] = useState<ConsumeTimeItem[]>([]);
  const [loadingTime, setLoadingTime] = useState(false);
  const [recentResource, setRecentResource] = useState<recentDay>(1);
  const [dataResource, setDataResource] = useState<ConsumeResourceItem[]>([]);
  const [loadingResource, setLoadingResource] = useState(false);

  useEffect(() => {
    if (activeKey === 'time') {
      setLoadingTime(true);
      getTimeTop10({ recentDays: recentTime })
        .then((res) => setDataTime(res.data))
        .catch((err) => {})
        .finally(() => setLoadingTime(false));
    }
    if (activeKey === 'resource') {
      setLoadingResource(true);
      getResourceTop10({ recentDays: recentResource })
        .then((res) => setDataResource(res.data))
        .catch((err) => {})
        .finally(() => setLoadingResource(false));
    }
  }, [activeKey, recentTime, recentResource]);

  const columnsTime: ColumnsType<ConsumeTimeItem> = [
    { title: 'ID', dataIndex: 'jobId', key: 'jobId' },
    { title: '名称', dataIndex: 'jobName', key: 'jobName', render: (_) => _ || '-' },
    { title: '运行时长（分）', dataIndex: 'duration', key: 'duration', render: (_) => _ || '-' },
    { title: '平均执行时长（分）', dataIndex: 'avgDurationStr', key: 'avgDurationStr' },
    {
      title: '开始时间',
      dataIndex: 'startTime',
      key: 'startTime',
      render: (_) => moment(_).format(format),
    },
    {
      title: '完成时间',
      dataIndex: 'finishTime',
      key: 'finishTime',
      render: (_) => moment(_).format(format),
    },
    {
      title: '操作',
      dataIndex: 'amContainerLogsUrl',
      key: 'amContainerLogsUrl',
      fixed: 'right',
      render: (_) => (
        <a href={_} target="_blank">
          查看日志
        </a>
      ),
    },
  ];
  const columnsResource: ColumnsType<ConsumeResourceItem> = [
    { title: 'ID', dataIndex: 'jobId', key: 'jobId' },
    { title: '名称', dataIndex: 'jobName', key: 'jobName', render: (_) => _ || '-' },
    { title: '平均CPU核数（个）', dataIndex: 'avgVcores', key: 'avgVcores' },
    { title: '平均内存（GB）', dataIndex: 'avgMemory', key: 'avgMemory' },
    {
      title: '完成时间',
      dataIndex: 'finishTime',
      key: 'finishTime',
      render: (_) => moment(_).format(format),
    },
    {
      title: '操作',
      dataIndex: 'amContainerLogsUrl',
      key: 'amContainerLogsUrl',
      fixed: 'right',
      render: (_) => (
        <a href={_} target="_blank">
          查看日志
        </a>
      ),
    },
  ];

  return (
    <Tabs onChange={(k) => setActiveKey(k)}>
      <TabPane tab="作业耗时" key="time">
        <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: 16 }}>
          <Radio.Group
            optionType="button"
            buttonStyle="solid"
            defaultValue={1}
            onChange={({ target: { value } }) => setRecentTime(value)}
            options={[
              { label: '当日', value: 1 },
              { label: '近7天', value: 7 },
              { label: '近30天', value: 30 },
            ]}
          />
        </div>
        <Table<ConsumeTimeItem>
          columns={columnsTime}
          dataSource={dataTime}
          loading={loadingTime}
          scroll={{ x: 'max-content' }}
        />
      </TabPane>
      <TabPane tab="作业耗资源" key="resource">
        <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: 16 }}>
          <Radio.Group
            optionType="button"
            buttonStyle="solid"
            defaultValue={1}
            onChange={({ target: { value } }) => setRecentResource(value)}
            options={[
              { label: '当日', value: 1 },
              { label: '近7天', value: 7 },
              { label: '近30天', value: 30 },
            ]}
          />
        </div>
        <Table<ConsumeResourceItem>
          columns={columnsResource}
          dataSource={dataResource}
          loading={loadingResource}
          scroll={{ x: 'max-content' }}
        />
      </TabPane>
    </Tabs>
  );
};

export default SchedulOperation;
