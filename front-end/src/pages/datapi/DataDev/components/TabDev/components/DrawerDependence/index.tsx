import React, { useState } from 'react';
import { Drawer, Tabs } from 'antd';
import type { FC } from 'react';
import styles from './index.less';

import { Task } from '@/types/datadev';
import { Environments } from '@/constants/datasource';

import G6 from './components/G6';

interface DrawerDependenceProps {
  visible: boolean;
  onClose: () => void;
  data?: Task;
}

const { TabPane } = Tabs;
const env = Object.values(Environments).map((_) => _);

const DrawerDependence: FC<DrawerDependenceProps> = ({ visible, onClose, data }) => {
  const [activeKey, setActiveKey] = useState<Environments>(Environments.PROD);

  return (
    <Drawer
      className={styles['drawer-config']}
      visible={visible}
      onClose={onClose}
      destroyOnClose
      width={780}
      title="依赖"
      footer={null}
      footerStyle={{
        textAlign: 'right',
        boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.15)',
        zIndex: 1,
        padding: '12px 28px',
      }}
    >
      <Tabs
        className="reset-tabs"
        defaultActiveKey={activeKey}
        onChange={(k) => {
          setActiveKey(k as Environments);
        }}
      >
        {env.map((_) => (
          <TabPane tab={_} key={_}></TabPane>
        ))}
      </Tabs>
      <G6 data={data} env={activeKey} />
    </Drawer>
  );
};

export default DrawerDependence;
