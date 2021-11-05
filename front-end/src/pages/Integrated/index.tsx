import React from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import { Tabs } from 'antd';
import Scheduling from './Components/Scheduling';
import Hadoop  from './Components/Hadoop';
import "./index.less";
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
          <Scheduling/>
        </TabPane>
        <TabPane tab="Hive MetaStore" key="2">
          <Scheduling />
        </TabPane>
        <TabPane tab="Hadoop" key="3">
          <Hadoop />
        </TabPane>
      </Tabs>
    </div>
  </PageContainer>
);
