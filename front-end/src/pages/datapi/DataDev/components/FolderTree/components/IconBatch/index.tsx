import React, { useState } from 'react';
import { IconFont } from '@/components';
import type { FC } from 'react';

interface IconCreateProps {
  onClick?: () => void;
  onMouseEnter?: (e: any) => void;
  onMouseLeave?: (e: any) => void;
  onFocus?: (e: any) => void;
}

const IconCreate: FC<IconCreateProps> = ({ onClick, onFocus, onMouseEnter, onMouseLeave }) => {
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
      onFocus={onFocus}
      onClick={onClick}
      style={{ marginLeft: 8, cursor: 'pointer', lineHeight: 1,  fontSize: 18 }}
      type={isHover ? 'icon-piliangcaozuojihuo-copy' : 'icon-piliangcaozuo'}
    />
  );
};

export default IconCreate;
