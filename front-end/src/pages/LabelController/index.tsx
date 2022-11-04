import React from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import { Tabs } from 'antd';
import DynamicForm from './components/DynamicForm';
const { TabPane } = Tabs;
export default () => (
  <PageContainer
    fixedHeader
    header={{
      title: '元数据属性配置',
      breadcrumb: {
        // routes: [
        //   {
        //     path: '',
        //     breadcrumbName: '系统配置',
        //   },
        //   {
        //     path: '',
        //     breadcrumbName: '元数据属性配置',
        //   }
        // ],
      },
    }}
  >
    <div className="zcy-content" >
      <Tabs defaultActiveKey="1">
        <TabPane tab="表单基本信息" key="TABLE">
          <DynamicForm  subjectType="TABLE" />
        </TabPane>
        <TabPane tab="表结构设计" key="COLUMN">
          <DynamicForm  subjectType="COLUMN"/>
        </TabPane>
      </Tabs>
    </div>
  </PageContainer>
);
