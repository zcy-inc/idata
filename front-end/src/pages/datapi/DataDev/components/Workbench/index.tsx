import React from 'react';
import { useModel } from 'umi';
import { Empty, Tabs } from 'antd';
import type { FC } from 'react';
import styles from './index.less';

import { IPane } from '@/models/datadev';
import { FolderBelong } from '@/constants/datadev';

import TabCloseIcon from '@/components/TabCloseIcon';
import ViewLabel from '../ViewLabel';
import TabEnum from '../TabEnum';
import TabTable from '../TabTable';
import TabDAG from '../TabDAG';
import TabTask from '../TabTask';

const { TabPane } = Tabs;

const Workbench: FC = ({}) => {
  const { panes, activeKey, onChangeTab, onRemovePane } = useModel('datadev', (ret) => ({
    panes: ret.panes,
    activeKey: ret.activeKey,
    onChangeTab: ret.onChangeTab,
    onRemovePane: ret.onRemovePane,
  }));

  const renderPane = (pane: IPane) => {
    switch (pane.belong) {
      case FolderBelong.DESIGNTABLE:
        return <TabTable pane={pane} />;
      case FolderBelong.DESIGNENUM:
        return <TabEnum pane={pane} />;
      case FolderBelong.DESIGNLABEL:
        return <ViewLabel pane={pane} />;
      case FolderBelong.DAG:
        return <TabDAG pane={pane} />;
      case FolderBelong.DI:
        return <TabTask pane={pane} />;
      default:
        return null;
    }
  };

  return (
    <div className={styles.workbench}>
      {panes.length ? (
        <Tabs
          className="workbench-tabs"
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
              {renderPane(pane)}
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
