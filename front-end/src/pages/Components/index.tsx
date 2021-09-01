import React from 'react';
import { PageContainer } from '@/components';
import { Button, Space, Switch } from 'antd';
import type { FC } from 'react';
import styles from './index.less';

const Components: FC = () => {
  return (
    <PageContainer contentClassName={styles['object-label']}>
      <div style={{ marginBottom: 16 }}>
        <Space>
          <Button type="primary">primary</Button>
          <Button className="ant-btn-secondary">secondary</Button>
          <Button>default</Button>
          <Button disabled>disabled</Button>
          <Button type="dashed">dashed</Button>
          <Button type="text">text</Button>
          <Button type="link">link</Button>
        </Space>
      </div>
      <div style={{ marginBottom: 16 }}>
        <Space>
          <Switch />
          <Switch checked={true} />
          <Switch disabled />
          <Switch size="small" />
        </Space>
      </div>
    </PageContainer>
  );
};

export default Components;
