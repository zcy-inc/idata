import React from 'react';
import { Popover } from 'antd';
import { QuestionCircleOutlined } from '@ant-design/icons';
import styles from './index.less';

interface TipProps {
  style?: React.CSSProperties;
  tipTitle?: (() => React.ReactNode) | React.ReactNode;
  tipContent?: (() => React.ReactNode) | React.ReactNode;
}

const Tip: React.FC<TipProps> = ({ tipTitle, tipContent, style }) => {
  return (
    <Popover content={tipContent} title={tipTitle} trigger="hover" placement="bottom">
      <QuestionCircleOutlined className={styles.tip} style={style} />
    </Popover>
  );
};

export default Tip;
