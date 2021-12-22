import React, { useEffect, useState } from 'react';
import { Drawer, Table, Tabs } from 'antd';
import type { FC } from 'react';
import type { ColumnsType } from 'antd/es/table';
import styles from './index.less';

import Title from '@/components/Title';
import { ClusterListItem, OperationOverview, ScheduleListItem } from '@/types/operations';
import { getClusterList, getScheduleList } from '@/services/operations';

interface OverviewProps {
  schedule: OperationOverview;
  cluster: OperationOverview;
}
type Type = 'schedule' | 'cluster';
type OverViewTabKey = 'success' | 'ready' | 'running' | 'failure' | 'other';
enum OverViewState {
  SUCCESS = 7,
  READY = 1,
  RUNNING = 2,
  FAILURE = 6,
  OTHER = -1,
}
const OverViewStateMap = {
  success: OverViewState.SUCCESS,
  ready: OverViewState.READY,
  running: OverViewState.RUNNING,
  failure: OverViewState.FAILURE,
  other: OverViewState.OTHER,
};

const { TabPane } = Tabs;
const iconSchedule =
  'https://sitecdn.zcycdn.com/f2e-assets/c7efa852-da2a-4957-b7bd-d4111a8e6e14.png?x-oss-process=image/quality,Q_75/format,jpg';
const iconCluster =
  'https://sitecdn.zcycdn.com/f2e-assets/76fedc86-0c46-4478-8102-c35bcffc07fb.png?x-oss-process=image/quality,Q_75/format,jpg';

const Overview: FC<OverviewProps> = ({ schedule, cluster }) => {
  const [visible, setVisible] = useState(false);
  const [type, setType] = useState<Type>('schedule');
  const [activeKey, setActiveKey] = useState<OverViewTabKey>('success');
  const [total, setTotal] = useState(0);
  const [dataSchedule, setDataSchedule] = useState<ScheduleListItem[]>([]);
  const [dataCluster, setDataCluster] = useState<ClusterListItem[]>([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (visible) {
      if (type === 'schedule') {
        getScheduleListWrapped(1);
      }
      if (type === 'cluster') {
        getClusterListWrapped(1);
      }
    }
  }, [visible, type, activeKey]);

  const getScheduleListWrapped = (pageNum: number) => {
    setLoading(true);
    getScheduleList({
      pageSize: 10,
      pageNum,
      condition: { state: OverViewStateMap[activeKey] },
    })
      .then((res) => {
        setTotal(res.data.total);
        setDataSchedule(res.data.content);
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  const getClusterListWrapped = (pageNum: number) => {
    setLoading(true);
    getClusterList({
      pageSize: 10,
      pageNum,
      condition: { state: OverViewStateMap[activeKey] },
    })
      .then((res) => {
        setTotal(res.data.total);
        setDataCluster(res.data.content);
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  const renderLocaleString = (n: number) => n.toLocaleString();

  const Statistic: FC<{
    color: string;
    label: string;
    value: number;
    state: OverViewTabKey;
    type: Type;
  }> = ({ color, label, value, state }) => {
    return (
      <div
        className={styles['flex-item']}
        onClick={() => {
          setType(type);
          setActiveKey(state);
          setVisible(true);
        }}
      >
        <div style={{ height: 7, width: 7, backgroundColor: color, marginTop: 4 }}></div>
        <div className={styles.flexV} style={{ marginLeft: 8 }}>
          <span className={styles['total-label']}>{label}</span>
          <span className={styles['item-number']}>{renderLocaleString(value)}</span>
        </div>
      </div>
    );
  };

  const columnsSchedule: ColumnsType<ScheduleListItem> = [
    { title: 'ID', dataIndex: 'jobID', key: 'jobId' },
    { title: '名称', dataIndex: 'jobName', key: 'jobName' },
    { title: '状态', dataIndex: 'jobStatus', key: 'jobStatus' },
    { title: '操作', key: 'ops', render: (_) => <a onClick={() => {}}>查看日志</a> },
  ];

  const columnsCluster: ColumnsType<ClusterListItem> = [
    { title: 'ID', dataIndex: 'jobID', key: 'jobId' },
    { title: '名称', dataIndex: 'jobName', key: 'jobName' },
    { title: '状态', dataIndex: 'jobStatus', key: 'jobStatus' },
    {
      title: '操作',
      dataIndex: 'amContainerLogsUrl',
      key: 'amContainerLogsUrl',
      render: (_) => (
        <a href={_} target="_blank">
          查看日志
        </a>
      ),
    },
  ];

  return (
    <>
      <Title style={{ marginBottom: 48 }}>数据概览</Title>
      <div>
        <div style={{ display: 'flex', alignItems: 'center' }}>
          <div className={styles.flex}>
            <img src={iconSchedule} alt="icon" className={styles['img-icon']} />
            <div className={styles.flexV} style={{ marginLeft: 16 }}>
              <span className={styles['total-label']}>作业调度总数</span>
              <span style={{ width: 164 }}>
                <span className={styles['total-number']}>{renderLocaleString(schedule.total)}</span>
                <span className={styles['text-normal']} style={{ marginLeft: 8 }}>
                  个
                </span>
              </span>
            </div>
          </div>
          <div className={styles['item-box']}>
            <Statistic
              color="#05cc87"
              label="成功"
              value={schedule.success}
              state="success"
              type="schedule"
            />
            <Statistic
              color="#ffc950"
              label="队列中"
              value={schedule.ready}
              state="ready"
              type="schedule"
            />
            <Statistic
              color="#304ffe"
              label="运行中"
              value={schedule.running}
              state="running"
              type="schedule"
            />
            <Statistic
              color="#f1331f"
              label="失败"
              value={schedule.failure}
              state="failure"
              type="schedule"
            />
            <Statistic
              color="#9ea8c2"
              label="其他"
              value={schedule.other}
              state="other"
              type="schedule"
            />
          </div>
        </div>
        <div className={styles.divider} />
        <div style={{ display: 'flex' }}>
          <div className={styles.flex}>
            <img src={iconCluster} alt="icon" className={styles['img-icon']} />
            <div className={styles.flexV} style={{ marginLeft: 16 }}>
              <span className={styles['total-label']}>作业集群总数</span>
              <span>
                <span className={styles['total-number']}>{renderLocaleString(cluster.total)}</span>
                <span className={styles['text-normal']} style={{ marginLeft: 8 }}>
                  个
                </span>
              </span>
            </div>
          </div>
          <div className={styles['item-box']}>
            <Statistic
              color="#05cc87"
              label="成功"
              value={cluster.success}
              state="success"
              type="cluster"
            />
            <Statistic
              color="#ffc950"
              label="队列中"
              value={cluster.ready}
              state="ready"
              type="cluster"
            />
            <Statistic
              color="#304ffe"
              label="运行中"
              value={cluster.running}
              state="running"
              type="cluster"
            />
            <Statistic
              color="#f1331f"
              label="失败"
              value={cluster.failure}
              state="failure"
              type="cluster"
            />
            <Statistic
              color="#9ea8c2"
              label="其他"
              value={cluster.other}
              state="other"
              type="cluster"
            />
          </div>
        </div>
        <Drawer visible={visible} onClose={() => setVisible(false)} width={800}>
          <Tabs activeKey={activeKey} onChange={(k) => setActiveKey(k as OverViewTabKey)}>
            <TabPane tab="成功" key="success"></TabPane>
            <TabPane tab="队列中" key="ready"></TabPane>
            <TabPane tab="运行中" key="running"></TabPane>
            <TabPane tab="失败" key="failure"></TabPane>
            <TabPane tab="其他" key="other"></TabPane>
          </Tabs>
          {type === 'schedule' && (
            <Table<ScheduleListItem>
              loading={loading}
              columns={columnsSchedule}
              dataSource={dataSchedule}
              pagination={{
                total,
                showTotal: (t) => `共${t}条`,
                onChange: (p) => getScheduleListWrapped(p),
              }}
            />
          )}
          {type === 'cluster' && (
            <Table<ClusterListItem>
              loading={loading}
              columns={columnsCluster}
              dataSource={dataCluster}
              pagination={{
                total,
                showTotal: (t) => `共${t}条`,
                onChange: (p) => getClusterListWrapped(p),
              }}
            />
          )}
        </Drawer>
      </div>
    </>
  );
};

export default Overview;
