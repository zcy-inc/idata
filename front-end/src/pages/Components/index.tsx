import React from 'react';
import { IconFont, PageContainer } from '@/components';
import { Button, Checkbox, Input, Radio, Select, Space, Switch } from 'antd';
import type { FC } from 'react';
import styles from './index.less';

const { Option } = Select;

const Components: FC = () => {
  return (
    <PageContainer contentClassName={styles['object-label']}>
      <div className={styles.box}>
        <Space>
          <Button type="primary">primary</Button>
          <Button className="normal">secondary</Button>
          <Button>default</Button>
          <Button type="dashed">dashed</Button>
          <Button type="text">text</Button>
          <Button type="link">link</Button>
        </Space>
      </div>
      <div className={styles.box}>
        <Space>
          <Button type="primary" disabled>
            primary
          </Button>
          <Button className="normal" disabled>
            secondary
          </Button>
          <Button disabled>default</Button>
          <Button type="dashed" disabled>
            dashed
          </Button>
          <Button type="text" disabled>
            text
          </Button>
          <Button type="link" disabled>
            link
          </Button>
        </Space>
      </div>
      <div className={styles.box}>
        <Space>
          <Switch />
          <Switch checked={true} />
          <Switch disabled />
          <Switch size="small" />
        </Space>
      </div>
      <div className={styles.box}>
        <Space>
          <Radio>radio</Radio>
          <Radio checked={true}>checked</Radio>
          <Radio disabled>radio</Radio>
          <Radio checked={true} disabled>
            checked
          </Radio>
        </Space>
      </div>
      <div className={styles.box}>
        <Space>
          <Checkbox indeterminate={true}>check all</Checkbox>
          <Checkbox>checkbox</Checkbox>
          <Checkbox checked={true}>checked</Checkbox>
          <Checkbox disabled>checkbox</Checkbox>
          <Checkbox checked={true} disabled>
            checked
          </Checkbox>
        </Space>
      </div>
      <div className={styles.box}>
        <Space>
          <Input size="large" type="number" placeholder="large" />
          <Input size="middle" type="number" placeholder="middle" />
          <Input size="small" type="number" placeholder="small" />
          <Input size="middle" type="number" placeholder="middle" disabled />
        </Space>
      </div>
      <div className={styles.box}>
        <Space>
          <Select
            size="large"
            placeholder="请选择"
            suffixIcon={<IconFont type="icon-xiala-changgui" />}
          >
            <Option value="1">1</Option>
            <Option value="2">2</Option>
          </Select>
          <Select size="middle" placeholder="请选择">
            <Option value="1">1</Option>
            <Option value="2">2</Option>
          </Select>
          <Select size="middle" placeholder="请选择" disabled>
            <Option value="1">1</Option>
            <Option value="2">2</Option>
          </Select>
        </Space>
      </div>
    </PageContainer>
  );
};

export default Components;
