import React from 'react';
import { PageContainer } from '@/components';
import { useModel } from 'umi';
import SplitPane from 'react-split-pane';
import type { FC } from 'react';
import styles from './index.less';

import CreateLabel from './components/CreateLabel';
import FolderTree from './components/FolderTree';
import Workbench from './components/Workbench';

const TableManage: FC = () => {
  const { visibleLabel } = useModel('tablemanage', (ret) => ({
    visibleLabel: ret.visibleLabel,
  }));

  return (
    <PageContainer contentClassName={styles['tabel-manage']}>
      <SplitPane
        className={styles.board}
        defaultSize={300}
        style={{ position: 'relative' }}
        pane2Style={{ width: 0 }}
      >
        <div className={styles.left}>
          <FolderTree />
        </div>
        <div className={styles.right}>
          <Workbench />
        </div>
      </SplitPane>
      {visibleLabel && <CreateLabel />}
    </PageContainer>
  );
};

export default TableManage;
