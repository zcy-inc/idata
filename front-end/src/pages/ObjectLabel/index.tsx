import React from 'react';
import { PageContainer } from '@/components';
import SplitPane from 'react-split-pane';
import type { FC } from 'react';

import FolderTree from './components/FolderTree';
import Workbench from './components/Workbench';

const ObjectLabel: FC = () => {
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
    </PageContainer>
  );
};

export default ObjectLabel;
