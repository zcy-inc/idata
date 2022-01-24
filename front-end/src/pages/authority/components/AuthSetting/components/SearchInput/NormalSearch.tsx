import React from 'react';
import { Input } from 'antd';
import type { InputProps } from 'antd/es/input';
import { SearchOutlined } from '@ant-design/icons';

const NormalSearch: React.FC<InputProps> = (props) => {
  return <Input {...props} suffix={<SearchOutlined style={{ color: '#ADBBC4' }} />} />;
};

export default NormalSearch;
