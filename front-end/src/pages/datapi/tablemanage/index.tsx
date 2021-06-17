import React from 'react';
import { PageContainer } from '@/components';
import { useModel } from 'umi';
import type { FC } from 'react';
import styles from './index.less';

import CreateLabel from './components/CreateLabel';
import FolderTree from './components/FolderTree';
import Workbench from './components/Workbench';

const TableManage: FC = () => {
  const { visibleLabel } = useModel('tabalmanage', (ret) => ({
    visibleLabel: ret.visibleLabel,
  }));

  return (
    <PageContainer contentClassName={styles.content}>
      <div className={styles.board}>
        <div className={styles.left}>
          <FolderTree />
        </div>
        <div className={styles.divider} />
        <div className={styles.right}>
          <Workbench />
        </div>
      </div>
      {visibleLabel && <CreateLabel />}
    </PageContainer>
  );
};

export default TableManage;
