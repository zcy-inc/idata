import React, { useEffect, useState } from 'react';
import { Drawer, Table } from 'antd';
import type { FC } from 'react';
import styles from './index.less';

interface DrawerConfigProps {
  visible: boolean;
  onClose: () => void;
}

const DrawerVersion: FC<DrawerConfigProps> = ({ visible, onClose }) => {
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(false);
  }, [visible]);

  return (
    <Drawer
      className={styles['drawer-history']}
      visible={visible}
      onClose={onClose}
      destroyOnClose
      width={780}
      title="历史"
      footerStyle={{
        textAlign: 'right',
        boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.15)',
        zIndex: 1,
        padding: '12px 28px',
      }}
    >
      <Table
        rowKey="id"
        loading={loading}
        pagination={false}
        columns={[
          { title: '开始时间', dataIndex: 'start', key: 'start' },
          { title: '执行时长', dataIndex: 'duration', key: 'duration' },
          { title: '输出结果行数', dataIndex: 'line', key: 'line' },
          { title: '平均内存(GB)', dataIndex: 'memory', key: 'memory' },
          { title: '平均CPU核数', dataIndex: 'cpu', key: 'cpu' },
          { title: '最终状态', dataIndex: 'state', key: 'state' },
        ]}
        dataSource={[]}
      />
    </Drawer>
  );
};

export default DrawerVersion;
