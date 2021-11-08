import { PageContainer } from '@ant-design/pro-layout';
import { Input } from 'antd';
import type { FC } from 'react';
import { history } from 'umi';

type TAuthorityProps = {
  match: {
    url: string;
    path: string;
  };
  location: {
    pathname: string;
  };
};

const tabList = [
  {
    key: 'role',
    tab: '角色管理',
  },
  {
    key: 'user',
    tab: '用户管理',
  }
];

const ListSearch: FC<TAuthorityProps> = (props) => {
  const handleTabChange = (key: string) => {
    const { match } = props;
    const url = match.url === '/' ? '' : match.url;
    switch (key) {
      case 'role':
        history.push(`${url}/role`);
        break;
      case 'user':
        history.push(`${url}/user`);
        break;
      default:
        break;
    }
  };

  const getTabKey = () => {
    const { match, location } = props;
    const url = match.path === '/' ? '' : match.path;
    const tabKey = location.pathname.replace(`${url}/`, '');
    if (tabKey && tabKey !== '/') {
      return tabKey;
    }
    return 'role';
  };

  return (
    <PageContainer
      tabList={tabList}
      tabActiveKey={getTabKey()}
      onTabChange={handleTabChange}
    >
      {props.children}
    </PageContainer>
  );
};

export default ListSearch;
