import React from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import { Tabs } from 'antd';
import Scheduling from './components/Scheduling';
import Hadoop from './components/Hadoop';
const { TabPane } = Tabs;
export default () => (
  <PageContainer
    fixedHeader
    header={{
      title: '集成配置',
      breadcrumb: {
        routes: [
          {
            path: '',
            breadcrumbName: '系统配置',
          },
          {
            path: '',
            breadcrumbName: '集成配置',
          }
        ],
      },
    }}
  >
    <div className="zcy-content" >
      <Tabs defaultActiveKey="1">
        <TabPane tab="调度系统" key="1">
          <Scheduling connection={{url:""}} type="DS" />
        </TabPane>
        <TabPane tab="Hive MetaStore" key="2">
        <Scheduling connection={{url:""}} type="DS"/>
        </TabPane>
        <TabPane tab="Hadoop" key="3">
          <Hadoop />
        </TabPane>
        <TabPane tab="抽数配置" key="4">
          <Scheduling type="DS"/>
        </TabPane>
        <TabPane tab="livy" key="5">
          <Scheduling type="DS"/>
        </TabPane>
      </Tabs>
    </div>
  </PageContainer>
);
