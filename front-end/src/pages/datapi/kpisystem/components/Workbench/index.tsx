import React from 'react';
import { useModel } from 'umi';
import { Empty, Tabs } from 'antd';
import type { FC } from 'react';
import styles from '../../index.less';

import { IconFont } from '@/components';

const { TabPane } = Tabs;

const Workbench: FC = ({}) => {
  const { tabs, activeTab, setActiveTab, removeTab } = useModel('kpisystem', (_) => ({
    tabs: _.tabs,
    activeTab: _.activeTab,
    setActiveTab: _.setActiveTab,
    removeTab: _.removeTab,
  }));

  return tabs.length ? (
    <Tabs
      type="editable-card"
      onChange={(key) => setActiveTab(key)}
      activeKey={activeTab}
      onEdit={(key, action) => action === 'remove' && removeTab(key)}
      tabBarGutter={0}
      tabBarStyle={{ background: '#F1F3F9' }}
      hideAdd
    >
      {tabs.map((tab) => (
        <TabPane
          className={styles['tab-pane']}
          tab={tab.title}
          key={tab.key}
          closeIcon={<IconFont type="icon-guanbichanggui" />}
        >
          <div>???</div>
        </TabPane>
      ))}
    </Tabs>
  ) : (
    <Empty />
  );
};

export default Workbench;
