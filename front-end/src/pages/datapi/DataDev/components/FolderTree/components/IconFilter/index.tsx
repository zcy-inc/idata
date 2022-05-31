import React, { useState } from 'react';
import { IconFont } from '@/components';
import type { FC } from 'react';

const IconFilter: FC<{}> = ({}) => {
  const [isHover, setIsHover] = useState(false);

  return (
    <IconFont
      onMouseEnter={() => setIsHover(true)}
      onMouseLeave={() => setIsHover(false)}
      style={{ marginLeft: 8, cursor: 'pointer', lineHeight: 1,  fontSize: 18 }}
      type={isHover ? 'icon-shaixuan-copy' : 'icon-shaixuan'}
    />
  );
};

export default IconFilter;
