import React from 'react';
import type { CSSProperties, FC } from 'react';
import styles from './index.less';

interface TitleProps {
  style?: CSSProperties;
}

const Title: FC<TitleProps> = ({ style, children }) => {
  return (
    <div className={styles.title} style={style}>
      <div className={styles.border} />
      {children}
    </div>
  );
};

export default Title;
