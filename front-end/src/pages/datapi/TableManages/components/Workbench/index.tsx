import React from 'react';
import { useModel } from 'umi';
import { Empty, Tabs } from 'antd';
import type { FC } from 'react';
import styles from './index.less';

import TabCloseIcon from '@/components/TabCloseIcon';
import ViewLabel from '../ViewLabel';
import TabEnum from '../TabEnum';
import TabTable from '../TabTable';

const { TabPane } = Tabs;

const Workbench: FC = ({}) => {
  const { panes, activeKey, onChangeTab, onRemovePane } = useModel('tablemanage', (ret) => ({
    panes: ret.panes,
    activeKey: ret.activeKey,
    onChangeTab: ret.onChangeTab,
    onRemovePane: ret.onRemovePane,
  }));

  return (
    <div className={styles.workbench}>
      {panes.length ? (
        <Tabs
          className="workbench"
          type="editable-card"
          onChange={(key) => onChangeTab(key)}
          activeKey={activeKey}
          onEdit={(key, action) => action === 'remove' && onRemovePane(key)}
          tabBarGutter={0}
          tabBarStyle={{ background: '#F1F3F9' }}
          hideAdd
        >
          {panes.map((pane) => (
            <TabPane
              className={styles['tab-pane']}
              tab={pane.title}
              key={pane.key}
              closeIcon={<TabCloseIcon />}
            >
              {pane.type === 'ENUM' ? (
                <TabEnum initialMode={pane.mode} fileCode={pane.code} />
              ) : pane.type === 'TABLE' ? (
                <TabTable initialMode={pane.mode} fileCode={pane.code} />
              ) : (
                <ViewLabel fileCode={pane.code} />
              )}
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
