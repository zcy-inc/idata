import React, { useState } from 'react';
import { IconFont } from '@/components';
import type { FC } from 'react';

interface IconCreateProps {
  onClick: () => void;
}

const IconRun: FC<IconCreateProps> = ({ onClick }) => {
  const [isHover, setIsHover] = useState(false);

  return (
    <div
      onMouseEnter={() => setIsHover(true)}
      onMouseLeave={() => setIsHover(false)}
      onClick={onClick}
      style={{ cursor: 'pointer', marginRight: 12, lineHeight: 1 }}
    >
      {isHover ? <IconFont type="icon-yunhang-copy" /> : <IconFont type="icon-yunhang" />}
      <span style={{ color: isHover ? '#304ffe' : '#a4a6ad', marginLeft: 8 }}>恢复</span>
    </div>
  );
};

export default IconRun;
