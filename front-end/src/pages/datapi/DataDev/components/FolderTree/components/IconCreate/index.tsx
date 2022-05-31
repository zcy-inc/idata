import React, { useState } from 'react';
import { IconFont } from '@/components';
import type { FC } from 'react';

interface IconCreateProps {
  onClick: () => void;
}

const IconCreate: FC<IconCreateProps> = ({ onClick }) => {
  const [isHover, setIsHover] = useState(false);

  return (
    <IconFont
      onClick={onClick}
      onMouseEnter={() => setIsHover(true)}
      onMouseLeave={() => setIsHover(false)}
      style={{ marginLeft: 8, cursor: 'pointer', lineHeight: 1,  fontSize: 18 }}
      type={isHover ? 'icon-tianjia' : 'icon-xinjian1'}
    />
  );
};

export default IconCreate;
