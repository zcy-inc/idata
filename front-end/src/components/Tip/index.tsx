import React, { FC } from 'react';
import classnames from 'classnames';
import styles from './index.less';

const Tip: FC<{
  className?: string;
  label?: string;
  content?: React.ReactChild;
  extra?: React.ReactChild;
  style?: React.CSSProperties;
}> = ({ label, content, extra, className, style }) => {
  const cls = classnames([className, styles.tip]);
  return (
    <div className={cls} style={style}>
      <div className={styles.contentWp}>
        {label && <div className={styles.label}>{label}:&nbsp;</div>}
        <div>{content}</div>
      </div>
      {extra && <div className={styles.extra}>{extra}</div>}
    </div>
  );
};

export default Tip;
