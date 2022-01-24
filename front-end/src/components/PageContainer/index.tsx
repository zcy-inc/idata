import React, { Fragment, useContext } from 'react';
import { Breadcrumb } from 'antd';
import cls from 'classnames';
import { RouteContext } from '@ant-design/pro-layout';
import styles from './index.less';

interface PageContainerProps {
  className?: string;
  contentClassName?: string;
  extra?: React.ReactNode;
}

const PageContainer: React.FC<PageContainerProps> = ({
  children,
  className,
  contentClassName,
  extra,
}) => {
  const value = useContext(RouteContext);
  const { currentMenu } = value;
  // hide breadcrumbs and page title when layout is false in router config file
  if (currentMenu && (currentMenu as any).layout === false) {
    return <Fragment>{children}</Fragment>;
  }
  const wrapCls = cls(styles.pageContainer, className);
  const contentCls = cls(styles.content, contentClassName);
  return (
    <div className={wrapCls}>
      {/* <dl>
        <dt>
          {value.breadcrumb?.routes ? (
            <Breadcrumb {...value.breadcrumb} separator="/" />
          ) : (
            <Breadcrumb
              routes={[
                {
                  breadcrumbName: value.currentMenu?.name as string,
                  path: value.currentMenu?.path as string,
                },
              ]}
              separator="/"
            />
          )}
          <h1 className={styles.title}>{value.title}</h1>
        </dt>
        <dd>{extra}</dd>
      </dl> */}
      <h1 className={styles.title}>{value.title}</h1>
      <div className={contentCls}>{children}</div>
    </div>
  );
};

export default PageContainer;
