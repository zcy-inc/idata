import React from 'react';
import { InputNumber } from 'antd';
import type { InputNumberProps } from 'antd/es/input-number';

export interface NumberRangeInputValue {
  minBound?: number; // 区间范围下限
  maxBound?: number; // 区间范围上限
}

interface NumberRangeInputProps extends Pick<InputNumberProps, 'formatter' | 'placeholder'> {
  style?: React.CSSProperties;
  value?: NumberRangeInputValue;
  onChange?: (value: NumberRangeInputValue) => void;
}

const NumberRangeInput: React.FC<NumberRangeInputProps> = ({
  style,
  value,
  onChange,
  formatter,
  placeholder = '请输入数值',
}) => {
  const { minBound, maxBound } = value || {};
  const onMinBoundChange = (v: number | string | undefined) => {
    if (typeof v === 'number' || typeof v === 'undefined') {
      onChange?.({ maxBound, minBound: v });
    } else if (v === '') {
      onChange?.({ maxBound, minBound: undefined });
    }
  };
  const onMaxBoundChange = (v: number | string | undefined) => {
    if (typeof v === 'number' || typeof v === 'undefined') {
      onChange?.({ maxBound: v, minBound });
    } else if (v === '') {
      onChange?.({ maxBound: undefined, minBound });
    }
  };
  return (
    <div style={style}>
      <InputNumber
        placeholder={placeholder}
        formatter={formatter}
        style={{ width: 130 }}
        value={minBound}
        onChange={onMinBoundChange}
      />
      <span style={{ margin: '0 8px' }}>至</span>
      <InputNumber
        formatter={formatter}
        placeholder={placeholder}
        style={{ width: 130 }}
        value={maxBound}
        onChange={onMaxBoundChange}
      />
    </div>
  );
};

export default NumberRangeInput;
