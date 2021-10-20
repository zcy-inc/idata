import React, { useState } from 'react';
import { IconFont } from '@/components';
import type { FC } from 'react';

const IconFilter: FC<{}> = ({}) => {
  const [isHover, setIsHover] = useState(false);

  return (
    <div
      onMouseEnter={() => setIsHover(true)}
      onMouseLeave={() => setIsHover(false)}
      style={{ marginLeft: 8, cursor: 'pointer', lineHeight: 1 }}
    >
      {isHover ? (
        <IconFont type="icon-shaixuan-copy" style={{ fontSize: 18 }} />
      ) : (
        <IconFont type="icon-shaixuan" style={{ fontSize: 18 }} />
      )}
    </div>
  );
};

export default IconFilter;
