import React, { useState } from 'react';
import { IconFont } from '@/components';
import type { FC } from 'react';

const TabCloseIcon: FC = ({}) => {
  const [isHover, setIsHover] = useState(false);

  return (
    <div onMouseEnter={() => setIsHover(true)} onMouseLeave={() => setIsHover(false)}>
      {isHover ? (
        <IconFont type="icon-guanbijihuo" style={{ fontSize: 16 }} />
      ) : (
        <IconFont type="icon-guanbichanggui" style={{ fontSize: 16 }} />
      )}
    </div>
  );
};

export default TabCloseIcon;
