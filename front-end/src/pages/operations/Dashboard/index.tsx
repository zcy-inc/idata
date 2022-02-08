import React, { useEffect, useState } from 'react';
import { PageContainer } from '@/components';
import { Col, Row } from 'antd';
import type { FC } from 'react';
import styles from './index.less';

import OverView from './components/OverView';
import CPU from './components/CPU';
import ScheduleState from './components/ScheduleState';
import ScheduleOperation from './components/ScheduleOperation';
import ClusterState from './components/ClusterState';
import ClusterOperation from './components/ClusterOperation';
import List from './components/List';
import { getClusterOverview, getScheduleOverview } from '@/services/operations';
import { OperationOverview } from '@/types/operations';

const initialOverview = {
  total: 0,
  success: 0,
  ready: 0,
  running: 0,
  failure: 0,
  other: 0,
  nameValueResponseList: [],
};

const Dashboard: FC = () => {
  const [schedule, setSchedule] = useState<OperationOverview>(initialOverview);
  const [cluster, setCluster] = useState<OperationOverview>(initialOverview);

  useEffect(() => {
    getScheduleOverview()
      .then((res) => setSchedule(res.data))
      .catch((err) => {});
    getClusterOverview()
      .then((res) => setCluster(res.data))
      .catch((err) => {});
  }, []);

  return (
    <PageContainer>
      <div className={styles.card}>
        <Row>
          <Col span={16}>
            <OverView schedule={schedule} cluster={cluster} />
          </Col>
          <Col span={8}>
            <CPU />
          </Col>
        </Row>
      </div>
      <div className={styles['card-center']}>
        <Row>
          <Col span={12}>
            <ScheduleState schedule={schedule} />
          </Col>
          <Col span={12}>
            <ScheduleOperation />
          </Col>
        </Row>
        <Row style={{ marginTop: 24 }}>
          <Col span={12}>
            <ClusterState cluster={cluster} />
          </Col>
          <Col span={12}>
            <ClusterOperation />
          </Col>
        </Row>
      </div>
      <div className={styles['card-last']}>
        <List />
      </div>
    </PageContainer>
  );
};

export default Dashboard;
