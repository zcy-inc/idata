import React, { useState } from 'react';
import { Tabs } from 'antd';
import TaskSelect from './TaskSelect';

const { TabPane } = Tabs;
const initPanelList = [{
  title: '数据集成',
  key: '1',
  selectedList: [],
  belongFunctions: ["DI"]
}, {
  title: '数据开发',
  key: '2',
  selectedList: [],
  belongFunctions: ["DEV", "DEV.JOB", "DEV.FUN"]
}]
export default ({dialog}: {dialog: any}, _: any) => {
  const [activeKey, setActiveKey] = useState('1');
  const  [panelList] = useState(initPanelList);
  return <Tabs activeKey={activeKey} onChange={val => setActiveKey(val)}>
  {panelList.map(panel => (
    <TabPane tab={panel.title} key={panel.key}>
      <TaskSelect belongFunctions={panel.belongFunctions} dialog={dialog} />
    </TabPane>
  ))}
</Tabs>
}