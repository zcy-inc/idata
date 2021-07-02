import React, { useCallback } from 'react';
import type { FC } from 'react';
import { history } from 'umi';
import cls from 'classnames';

import styles from './index.less';

export enum operationTypeEnum {
  WARN,
  DEFAULT,
  DANGER,
  DISABLE,
  SAFE,
}

export interface Toperation {
  label: string;
  onClick?: () => void;
  type?: operationTypeEnum;
}

interface OperationProps {
  label: string;
  to?: string;
  onClick?: () => void;
  type?: operationTypeEnum;
}

export const OperationGroup: FC = ({ children }) => {
  return <div className={styles.group}>{children}</div>;
};

const Operation = ({ label, type, onClick, to }: OperationProps) => {
  const baseCls = 'operation';
  const operationCls = cls(baseCls, {
    [styles.base]: true,
    [styles.default]: !type || type === operationTypeEnum.DEFAULT,
    [styles.danger]: type === operationTypeEnum.DANGER,
    [styles.warn]: type === operationTypeEnum.WARN,
    [styles.safe]: type === operationTypeEnum.SAFE,
    [styles.disable]: type === operationTypeEnum.DISABLE,
  });
  const handleClick = useCallback(() => {
    if (type === operationTypeEnum.DISABLE) {
      return;
    }
    if (typeof to !== 'undefined') {
      history.push(to);
      return;
    }
    onClick?.();
  }, [to, onClick]);
  return (
    <span className={operationCls} onClick={handleClick}>
      {label}
    </span>
  );
};

Operation.Group = OperationGroup;

export default Operation;
