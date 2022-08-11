import React, { useEffect, useState } from 'react';
import moment from 'moment';
import { Drawer, Table, Tabs } from 'antd';
import type { FC } from 'react';
import styles from './index.less';
import { Task, TaskHistoryItem, DIJobBasicInfo } from '@/types/datadev';
import { getDevHistory } from '@/services/datadev';
import { Environments } from '@/constants/datasource';
import MonacoModal from '@/pages/datapi/DataDev/components/TabDev/components/MonacoModal';

interface DrawerHistoryProps {
  visible: boolean;
  onClose: () => void;
  data?: Task | DIJobBasicInfo;
}
interface Log {
  jobId?: number;
  taskInstanceId?: number;
  environment?: string;
}

const fmt = 'YYYY-MM-DD HH:mm:ss';
const { TabPane } = Tabs;
const env = Object.values(Environments).map((_) => _);

const DrawerHistory: FC<DrawerHistoryProps> = ({ visible, onClose, data }) => {
  const [loading, setLoading] = useState(false);
  const [history, setHistory] = useState<TaskHistoryItem[]>([]);
  const [total, setTotal] = useState(0);
  const [log, setLog] = useState<Log>({});
  const [activeKey, setActiveKey] = useState<Environments>(Environments.PROD);

  useEffect(() => {
    if (visible && data) {
      getTaskHistoryWrapped(1);
    }
  }, [visible, data, activeKey]);

  const getTaskHistoryWrapped = (pageNum: number) => {
    setLoading(false);
    getDevHistory({
      environment: activeKey,
      jobId: data?.id as number,
      startTime: moment().endOf('day').subtract(7, 'days').valueOf(),
      endTime: moment().endOf('day').valueOf(),
      pageNum,
      pageSize: 10,
    })
      .then((res) => {
        setTotal(res.data.total);
        setHistory(res.data.content);
      })
      .catch((err) => {
        console.log(err);
        setTotal(0);
        setHistory([]);
      })
      .finally(() => setLoading(false));
  };

  const toggleMonacoModal = (l:any) => {
    setLog(l);
  };

  return (
    <>
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
        <Tabs
          className="reset-tabs"
          defaultActiveKey={activeKey}
          onChange={(k) => {
            setActiveKey(k as Environments);
          }}
        >
          {env.map((_) => (
            <TabPane tab={_} key={_}></TabPane>
          ))}
        </Tabs>
        <Table
          rowKey="id"
          loading={loading}
          columns={[
            {
              title: '提交时间',
              dataIndex: 'submitTime',
              key: 'submitTime',
              render: (_) => moment(_).format(fmt),
              fixed:'left',
              width: 200,
            },
            {
              title: '开始时间',
              dataIndex: 'startTime',
              key: 'startTime',
              render: (_) => _ ? moment(_).format(fmt) : '-',
              width: 200,
            },
            {
              title: '结束时间',
              dataIndex: 'endTime',
              key: 'endTime',
              render: (_) => _ ? moment(_).format(fmt) : '-',
              width: 200,
            },
            {
              title: '执行时长',
              dataIndex: 'duration',
              key: 'duration',
              render: (_) => _ || '-',
              width: 100,
            },
            { title: '工作流实例', dataIndex: 'processInstanceName', key: 'processInstanceName', width: 200, },
            { title: '状态', dataIndex: 'state', key: 'state', width: 200,},
            {
              title: '操作',
              key: 'options',
              fixed:'right',
              width: 100,
              render: (_) => (
                <a
                onClick={() => {
                  toggleMonacoModal(_);
                }}
                >
                  查看日志
                </a>
              ),
            },
          ]}
          scroll={{ x: 732 }}
          dataSource={history}
          pagination={{
            total,
            showTotal: (t) => `共${t}条`,
            onChange: (page) => getTaskHistoryWrapped(page),
          }}
        />
      </Drawer>
      {
        !!log.jobId && <MonacoModal 
        visible={!!log.jobId}
        logParam={{
          jobId: log.jobId,
          env: log.environment,
          taskId: log.taskInstanceId,
        }}
        onClose={() => {toggleMonacoModal({})}}
        />
      }
    </>
  );
};

export default DrawerHistory;
