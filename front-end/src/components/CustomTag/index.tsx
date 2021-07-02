import React from 'react';
import cls from 'classnames';
import { Tag } from 'antd';

import style from './index.less';

const prefixCls = 'custom__tag';

interface CustomTagProps {
  type?: 'normal' | 'danger' | 'warn';
}

const CustomTag: React.FC<CustomTagProps> = ({ children, type = 'normal' }) => {
  const cx = cls(style.wrapper, prefixCls, `${prefixCls}__${type}`);
  return <Tag className={cx}>{children}</Tag>;
};

export default CustomTag;
