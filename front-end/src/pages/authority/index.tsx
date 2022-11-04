import { PageContainer } from '@ant-design/pro-layout';
import type { FC } from 'react';
import { history } from 'umi';
import styles from './index.less';

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
  },
  {
    key: 'group',
    tab: '用户组管理',
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
      case 'group':
        history.push(`${url}/group`);
        break;
      default:
        break;
    }
  };

  const getTabKey = () => {
    const { match, location } = props;
    const url = match.path === '/' ? '' : match.path;
    const tabKey = location.pathname.replace(`${url}/`, '');
    const paths = tabKey.split('/');
    if (paths[0] && paths[0] !== '/') {
      return paths[0];
    }

    return 'role';
  };

  return (
    <PageContainer
      className={styles.tabStyle}
      tabList={tabList}
      tabActiveKey={getTabKey()}
      onTabChange={handleTabChange}
      header={{
        title: '角色管理',
        breadcrumb: {},
      }}
    >
      <div className="zcy-content"  >
        {props.children}
      </div>
    </PageContainer>
  );
};

export default ListSearch;
