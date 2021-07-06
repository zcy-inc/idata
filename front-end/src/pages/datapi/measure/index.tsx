import React from 'react';
import { PageContainer } from '@/components';
import type { FC } from 'react';
import styles from './index.less';

import FolderTree from './components/FolderTree';
import Workbench from './components/Workbench';

const measure: FC = () => {
  return (
    <PageContainer contentClassName={styles['kpi-system']}>
      <div className={styles.board}>
        <div className={styles.left}>
          <FolderTree />
        </div>
        <div className={styles.divider} />
        <div className={styles.right}>
          <Workbench />
        </div>
      </div>
    </PageContainer>
  );
};

export default measure;
