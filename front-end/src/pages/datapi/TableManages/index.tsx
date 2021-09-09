import React from 'react';
import SplitPane from 'react-split-pane';
import { PageContainer } from '@/components';
import { useModel } from 'umi';
import type { FC } from 'react';

import CreateLabel from './components/CreateLabel';
import FolderTree from './components/FolderTree';
import Workbench from './components/Workbench';

const TableManage: FC = () => {
  const { visibleLabel } = useModel('tablemanage', (ret) => ({
    visibleLabel: ret.visibleLabel,
  }));

  return (
    <PageContainer contentClassName="split-panel">
      <SplitPane
        className="board"
        defaultSize={300}
        style={{ position: 'relative' }}
        pane2Style={{ width: 0 }}
      >
        <FolderTree />
        <Workbench />
      </SplitPane>
      {visibleLabel && <CreateLabel />}
    </PageContainer>
  );
};

export default TableManage;
