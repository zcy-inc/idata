import React from 'react';
import { useBoolean } from 'ahooks';
import { PageContainer } from '@/components';
import { useModel } from 'umi';
import type { FC } from 'react';
import styles from './index.less';

import FolderTree from '../components/FolderTree';
import CreateFolder from '../components/CreateFolder';
import CreateLabel from '../components/CreateLabel';
import Workbench from '../components/Workbench';

const TableManage: FC = () => {
  const [visibleFolder, { setTrue: showFolder, setFalse: hideFolder }] = useBoolean(false);

  const actions = { showFolder };

  const { visibleLabel } = useModel('tabalmanage', (ret) => ({
    visibleLabel: ret.visibleLabel,
  }));

  return (
    <PageContainer contentClassName={styles.content}>
      <div className={styles.board}>
        <div className={styles.left}>
          <FolderTree actions={actions} />
        </div>
        <div className={styles.divider} />
        <div className={styles.right}>
          <Workbench />
        </div>
      </div>
      {visibleFolder && <CreateFolder visible={visibleFolder} onCancel={hideFolder} />}
      {visibleLabel && <CreateLabel />}
    </PageContainer>
  );
};

export default TableManage;
