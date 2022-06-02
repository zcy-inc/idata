import React, { useState } from 'react';
import { Tabs, Spin } from 'antd';
import TaskSelect from './TaskSelect';

const { TabPane } = Tabs;
const initPanelList = [{
  title: '数据集成',
  key: '1',
  belongFunctions: ["DI"]
}, {
  title: '数据开发',
  key: '2',
  belongFunctions: ["DEV", "DEV.JOB"]
}]
export default ({dialog, getTreeWrapped}: {dialog: any, getTreeWrapped: any}, _: any) => {
  const [activeKey, setActiveKey] = useState('1');
  const  [panelList] = useState(initPanelList);
  const [loading, setLoading] = useState(false)
  return <Spin spinning={loading}>
    <Tabs activeKey={activeKey} onChange={val => setActiveKey(val)}>
  {panelList.map(panel => (
    <TabPane tab={panel.title} key={panel.key}>
      <TaskSelect belongFunctions={panel.belongFunctions} dialog={dialog} setLoading={setLoading} getTreeWrapped={getTreeWrapped} />
    </TabPane>
  ))}
</Tabs>
  </Spin>
}