import React, { FC } from 'react';
import styles from './index.less';

export interface Option {
  label: string;
  value: number | string;
}

export interface RadioGroupProps {
  value?: string | number;
  options?: Option[];
  onChange?: (v: string | number, option: Option) => void;
  onBeforeChange?: (v: string | number, option: Option) => Promise<unknown>;
}

const cls = 'radioGroup';

const RadioGroup: FC<RadioGroupProps> = ({ value, options, onChange, onBeforeChange }) => {
  const handleChange = async (v: string | number, option: Option) => {
    await onBeforeChange?.(v, option);
    onChange?.(v, option);
  };
  return (
    <div className={styles[cls]}>
      {options?.map((option) => (
        <div
          key={option.value}
          className={`${styles[`${cls}-tab`]} ${
            option.value === value && styles[`${cls}-selected`]
          }`}
          onClick={() => handleChange(option.value, option)}
        >
          {option.label}
        </div>
      ))}
    </div>
  );
};

export default RadioGroup;
