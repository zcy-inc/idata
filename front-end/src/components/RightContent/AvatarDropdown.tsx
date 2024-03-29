import React, { useCallback } from 'react';
import { LogoutOutlined, CaretDownOutlined } from '@ant-design/icons';
import { Menu, Spin, Avatar } from 'antd';
import { useModel } from 'umi';
import { outLogin } from '@/services/user';
import { skip2Login } from '@zcy-data/idata-utils';
import HeaderDropdown from '../HeaderDropdown';
import styles from './index.less';

const loginOut = async () => {
  await outLogin();
  skip2Login();
};

const AvatarDropdown: React.FC = () => {
  const { initialState, setInitialState } = useModel('@@initialState');

  const onMenuClick = useCallback(
    (event: {
      key: React.Key;
      keyPath: React.Key[];
      item: React.ReactInstance;
      domEvent: React.MouseEvent<HTMLElement>;
    }) => {
      const { key } = event;
      if (key === 'logout' && initialState) {
        setInitialState({ ...initialState, currentUser: undefined });
        loginOut();
      }
    },
    [],
  );

  const menuHeaderDropdown = (
    <Menu className={styles.menu} selectedKeys={[]} onClick={onMenuClick}>
      <Menu.Item key="logout">
        <LogoutOutlined />
        退出登录
      </Menu.Item>
    </Menu>
  );

  return initialState?.currentUser ? (
    <HeaderDropdown overlay={menuHeaderDropdown}>
      <div className={`${styles.action}`}>
        <Avatar src={initialState?.currentUser.avatar} size={25} />
        <span className={styles.nickname}>{initialState?.currentUser.nickname}</span>
        <CaretDownOutlined className={styles.downIcon} />
      </div>
    </HeaderDropdown>
  ) : (
    <span className={`${styles.action} ${styles.account}`}>
      <Spin
        size="small"
        style={{
          marginLeft: 8,
          marginRight: 8,
        }}
      />
    </span>
  );
};

export default AvatarDropdown;
