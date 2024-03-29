import React from 'react';
import { useModel } from 'umi';
import { Empty, Tabs } from 'antd';
import type { FC, Key } from 'react';
import styles from './index.less';

import TabCloseIcon from '@/components/TabCloseIcon';
import TabLabel from '../TabLabel';

const { TabPane } = Tabs;

const Workbench: FC = ({}) => {
  const { tabs, activeTab, setActiveTab, removeTab } = useModel('objectlabel', (_) => ({
    tabs: _.tabs,
    activeTab: _.activeTab,
    setActiveTab: _.setActiveTab,
    removeTab: _.removeTab,
  }));

  return (
    <div className={styles.workbench}>
      {tabs.length ? (
        <Tabs
          type="editable-card"
          onChange={(key) => setActiveTab(key)}
          activeKey={activeTab}
          tabBarGutter={0}
          onEdit={(key, action) => action === 'remove' && removeTab(key as Key)}
          tabBarStyle={{ background: '#F1F3F9' }}
          hideAdd
        >
          {tabs.map((tab) => (
            <TabPane
              tabKey={`${tab.key}`}
              className={styles['tab-pane']}
              tab={tab.title}
              key={tab.key}
              closeIcon={<TabCloseIcon />}
            >
              <TabLabel initialMode={tab.mode} tabKey={`${tab.key}`} originId={tab.originId} />
            </TabPane>
          ))}
        </Tabs>
      ) : (
        <Empty />
      )}
    </div>
  );
};

export default Workbench;
