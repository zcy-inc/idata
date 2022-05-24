import React, { useState } from 'react';
import { Tabs } from 'antd';
import TaskSelect from './TaskSelect';
const { TabPane } = Tabs;
export default () => {
  const [activeKey, setActiveKey] = useState('1');

  return <div>
    <Tabs activeKey={activeKey} onChange={val => setActiveKey(val)}>
      <TabPane tab="数据集成" key="1">
        <TaskSelect belongFunctions={["DI"]} />
      </TabPane>
      <TabPane tab="数据开发" key="2">
        <TaskSelect belongFunctions={["DEV", "DEV.JOB", "DEV.FUN"]} />
      </TabPane>
    </Tabs>
  </div>
}