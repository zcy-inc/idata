import React, { useState } from 'react';
import { IconFont } from '@/components';
import type { FC } from 'react';

interface IconCreateProps {
  onClick: () => void;
}

const IconCreate: FC<IconCreateProps> = ({ onClick }) => {
  const [isHover, setIsHover] = useState(false);

  return (
    <div
      onMouseEnter={() => setIsHover(true)}
      onMouseLeave={() => setIsHover(false)}
      onClick={onClick}
      style={{ marginLeft: 8, cursor: 'pointer', lineHeight: 1 }}
    >
      {isHover ? (
        <IconFont type="icon-tianjia" style={{ fontSize: 18 }} />
      ) : (
        <IconFont type="icon-xinjian1" style={{ fontSize: 18 }} />
      )}
    </div>
  );
};

export default IconCreate;
