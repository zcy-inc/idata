import React, { useEffect, useState } from 'react';
import { Button, Drawer, Form, Tabs } from 'antd';
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

const { Item } = Form;
const { TabPane } = Tabs;
const width = 200;
const env = Object.values(Environments).map((_) => _);
const ruleSelc = [{ required: true, message: '请选择' }];

const DrawerDependence: FC<DrawerDependenceProps> = ({ visible, onClose, data }) => {
  const [activeKey, setActiveKey] = useState<Environments>(Environments.STAG);

  const [form] = Form.useForm();

  useEffect(() => {
    if (visible) {
    }
  }, [visible]);

  const onSave = (environment: Environments) => {
    console.log(environment);
  };

  return (
    <Drawer
      className={styles['drawer-config']}
      visible={visible}
      onClose={onClose}
      destroyOnClose
      width={780}
      title="依赖"
      footer={
        <Button size="large" onClick={() => onSave(activeKey)}>
          {`保存${activeKey}`}
        </Button>
      }
      footerStyle={{
        textAlign: 'right',
        boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.15)',
        zIndex: 1,
        padding: '12px 28px',
      }}
    >
      <Tabs
        className="reset-tabs"
        onChange={(k) => {
          setActiveKey(k as Environments);
        }}
      >
        {env.map((_) => (
          <TabPane tab={_} key={_}>
            <G6 />
          </TabPane>
        ))}
      </Tabs>
    </Drawer>
  );
};

export default DrawerDependence;
