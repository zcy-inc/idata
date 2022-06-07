import React, { useState } from 'react';
import { IconFont } from '@/components';
import type { FC } from 'react';

interface IconFilterProps {
  onClick?: (e: any) => void;
  onMouseEnter?: (e: any) => void;
  onMouseLeave?: (e: any) => void;
  onFocus?: (e: any) => void;
}

const IconFilter: FC<IconFilterProps> = ({ onClick, onFocus, onMouseEnter, onMouseLeave }) => {
  const [isHover, setIsHover] = useState(false);
  const hadnleMouseEnter = (e: any) => {
    setIsHover(true);
    onMouseEnter && onMouseEnter(e);
  }

  const handleMouseLeave = (e: any) => {
    setIsHover(false);
    onMouseLeave && onMouseLeave(e);
  }
  return (
    <IconFont
      onMouseEnter={hadnleMouseEnter}
      onMouseLeave={handleMouseLeave}
      onClick={onClick}
      onFocus={onFocus}
      style={{ marginLeft: 8, cursor: 'pointer', lineHeight: 1,  fontSize: 18 }}
      type={isHover ? 'icon-shaixuan-copy' : 'icon-shaixuan'}
    />
  );
};

export default IconFilter;
