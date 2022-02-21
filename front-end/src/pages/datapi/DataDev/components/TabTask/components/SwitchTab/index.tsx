import React from 'react';
import type { FC } from 'react';
import styles from './index.less';

type MapType = 'visual' | 'script';
interface SwitchTabProps {
  value: MapType;
  onChange: (v: MapType) => void;
}

const cls = 'switchTab';

const SwitchTab: FC<SwitchTabProps> = ({ value, onChange }) => {
  return (
    <div className={styles[cls]}>
      <div
        className={`${styles[`${cls}-tab`]} ${value === 'visual' && styles[`${cls}-selected`]}`}
        onClick={() => onChange('visual')}
      >
        可视化模式
      </div>
      <div
        className={`${styles[`${cls}-tab`]} ${value === 'script' && styles[`${cls}-selected`]}`}
        onClick={() => onChange('script')}
      >
        脚本模式
      </div>
    </div>
  );
};

export default SwitchTab;
