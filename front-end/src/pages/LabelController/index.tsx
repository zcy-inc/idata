import React from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import { Tabs } from 'antd';
import DynamicForm from './Components/DynamicForm';
const { TabPane } = Tabs;
export default () => (
  <PageContainer
    fixedHeader
    header={{
      title: '元数据属性配置',
      breadcrumb: {
        routes: [
          {
            path: '',
            breadcrumbName: '系统配置',
          },
          {
            path: '',
            breadcrumbName: '元数据属性配置',
          }
        ],
      },
    }}
  >
    <div className="zcy-content" >
      <Tabs defaultActiveKey="1">
        <TabPane tab="表单基本信息" key="1">
          <DynamicForm  />
        </TabPane>
        <TabPane tab="表结构设计" key="3">
          <DynamicForm />
        </TabPane>
      </Tabs>
    </div>
  </PageContainer>
);
