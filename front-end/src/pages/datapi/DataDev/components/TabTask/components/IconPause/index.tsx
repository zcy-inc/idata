import React, { useState } from 'react';
import { IconFont } from '@/components';
import type { FC } from 'react';

interface IconPauseProps {
  onClick: () => void;
}

const IconPause: FC<IconPauseProps> = ({ onClick }) => {
  const [isHover, setIsHover] = useState(false);

  return (
    <div
      onMouseEnter={() => setIsHover(true)}
      onMouseLeave={() => setIsHover(false)}
      onClick={onClick}
      style={{ cursor: 'pointer', marginRight: 12, lineHeight: 1 }}
    >
      {isHover ? <IconFont type="icon-zanting-copy" /> : <IconFont type="icon-zanting" />}
      <span style={{ color: isHover ? '#304ffe' : '#a4a6ad', marginLeft: 8 }}>暂停</span>
    </div>
  );
};

export default IconPause;
