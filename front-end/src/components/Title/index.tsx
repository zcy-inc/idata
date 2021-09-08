import React from 'react';
import type { FC } from 'react';
import styles from './index.less';

const Title: FC = ({ children }) => {
  return (
    <div className={styles.title}>
      <div className={styles.border} />
      {children}
    </div>
  );
};

export default Title;
