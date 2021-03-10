import React from 'react';
import { Input } from 'antd';
import omit from 'omit.js';
import cls from 'classnames';
import type { InputProps } from 'antd/es/input';
import { SearchOutlined } from '@ant-design/icons';
import styles from './index.less';

const UnderLinedSearch: React.FC<InputProps> = (props) => {
  const restProps = omit(props, ['className']);
  const cx = cls(props.className, [styles.underlined]);
  return (
    <Input className={cx} {...restProps} prefix={<SearchOutlined style={{ color: '#ADBBC4' }} />} />
  );
};

export default UnderLinedSearch;
