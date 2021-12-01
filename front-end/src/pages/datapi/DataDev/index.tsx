import React from 'react';
import SplitPane from 'react-split-pane';
import { PageContainer } from '@/components';
import { useModel } from 'umi';
import type { FC } from 'react';

import CreateLabel from './components/CreateLabel';
import CreateTask from './components/CreateTask';
import CreateDev from './components/CreateDev';
import FolderTree from './components/FolderTree';
import Workbench from './components/Workbench';

const TableManage: FC = () => {
  const { visibleLabel, visibleTask, visibleDev } = useModel('datadev', (_) => ({
    visibleLabel: _.visibleLabel,
    visibleTask: _.visibleTask,
    visibleDev: _.visibleDev,
  }));

  return (
    <PageContainer contentClassName="split-panel">
      <SplitPane
        className="board"
        defaultSize={240}
        style={{ position: 'relative' }}
        pane2Style={{ width: 0 }}
      >
        <FolderTree />
        <Workbench />
      </SplitPane>
      {visibleLabel && <CreateLabel />}
      {visibleTask && <CreateTask />}
      {visibleDev && <CreateDev />}
    </PageContainer>
  );
};

export default TableManage;
