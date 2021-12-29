import type { FC } from 'react';
import React from 'react';
import { Tabs } from 'antd';
import TaskList from './task-list';
import TaskGantt from './task-gantt';
import styles from './index.less';
import { PageContainer } from '@/components';
const { TabPane } = Tabs;
const TaskHistory: FC = () => {
  return (
    <PageContainer>
      <Tabs defaultActiveKey="1" className={styles['task-history-wrap']}>
        <TabPane tab="作业历史" key="1">
          <TaskList />
        </TabPane>
        <TabPane tab="甘特图" key="2">
          <TaskGantt />
        </TabPane>
      </Tabs>
    </PageContainer>
  );
};

export default TaskHistory;
