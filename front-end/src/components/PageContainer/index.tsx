import React, { Fragment, useContext } from 'react';
import { Breadcrumb } from 'antd';
import cls from 'classnames';
import { RouteContext } from '@ant-design/pro-layout';
import styles from './index.less';

interface PageContainerProps {
  className?: string;
  extra?: React.ReactNode;
}

const PageContainer: React.FC<PageContainerProps> = ({ children, className, extra }) => {
  const value = useContext(RouteContext);
  const { currentMenu } = value;
  // hide breadcrumbs and page title when layout is false in router config file
  if (currentMenu && (currentMenu as any).layout === false) {
    return <Fragment>{children}</Fragment>;
  }
  const wrapCls = cls(styles.pageContainer, {
    className,
  });
  return (
    <div className={wrapCls}>
      <dl>
        <dt>
          <Breadcrumb {...value.breadcrumb} separator="/" />
          <h1 className={styles.title}>{value.title}</h1>
        </dt>
        <dd>{extra}</dd>
      </dl>
      <div className={styles.content}>{children}</div>
    </div>
  );
};

export default PageContainer;
