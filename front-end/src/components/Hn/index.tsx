import React from 'react';
import Tip from '../Tip';
import styles from './index.less';

export interface HnProps {
  style?: React.CSSProperties;
  tipTitle?: (() => React.ReactNode) | React.ReactNode;
  tipContent?: (() => React.ReactNode) | React.ReactNode;
}

export const H3: React.FC<HnProps> = ({ children, style }) => {
  return (
    <h3 className={styles.h3} style={style}>
      {children}
    </h3>
  );
};

export const H4: React.FC<HnProps> = ({ children, style, tipContent, tipTitle }) => {
  return (
    <h4 className={styles.h4} style={style}>
      {children}
      {(tipContent || tipTitle) && (
        <Tip tipContent={tipContent} tipTitle={tipTitle} style={{ marginLeft: 4 }} />
      )}
    </h4>
  );
};

export const H5: React.FC<HnProps> = ({ children, style }) => {
  return (
    <h5 className={styles.h5} style={style}>
      {children}
    </h5>
  );
};
