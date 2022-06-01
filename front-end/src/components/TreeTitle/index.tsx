import React, { FC, useState } from 'react';
import { IconFont } from '@/components';
import { Dropdown, Menu } from 'antd';

export type Operation = { label?: string; key: string; onClick?: (e: MouseEvent) => void } | { type: 'divider' };

interface TreeTitleProps {
  text: React.ReactNode;
  icon?: string;
  operations?: Operation[];
  onClick?: (e: MouseEvent) => void | any;
  style?: React.CSSProperties;
}

const TreeTitle: FC<TreeTitleProps> = ({ icon, text, operations, style = {}, onClick }) => {
  // FIXME: 去掉设置curNode可能有问题
  //   const { setCurNode } = useModel('datadev', (_) => ({
  //     setCurNode: _.setCurNode,
  //   }));
  const [isHover, setIsHover] = useState(false);

  const renderOperationBtn = () => {
    if (!operations || operations.length === 0) {
      return null;
    }
    return (
      <Dropdown
        overlay={<Menu items={operations}></Menu>}
        placement="bottomLeft"
        trigger={['click']}
      >
        <IconFont
          type="icon-gengduo2"
          // onClick={() => setCurNode(node)}
          style={{
            visibility: visible ? 'visible' : 'hidden',
            position: 'absolute',
            right: 0,
          }}
        />
      </Dropdown>
    );
  };

  const visible = operations && isHover;
  return (
    <div
      onMouseEnter={() => setIsHover(true)}
      onMouseLeave={() => setIsHover(false)}
      style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', ...style }}
    >
      <div
        style={{
          paddingRight: 6,
          width: '100%',
          display: 'flex'
        }}
      >
        {icon && <IconFont type={icon} />}
        <div
          style={{
            width: '100%',
            whiteSpace: 'nowrap',
            textOverflow: 'ellipsis',
            overflow: 'hidden'
          }}
          onClick={(e: any) => {onClick && onClick(e)}}
        >{text}</div>
      </div>
      {renderOperationBtn()}
    </div>
  );
};

export default TreeTitle;
