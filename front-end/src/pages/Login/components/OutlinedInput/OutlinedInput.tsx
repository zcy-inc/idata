import React, { useState } from 'react';
import cls from 'classnames';
import { omit } from 'lodash';

import './OutlinedInput.less';

const prefixCls = 'outlined-input';
const getNotched = (
  innerVal: React.InputHTMLAttributes<HTMLInputElement>['value'],
  focus: boolean,
) => {
  if (typeof innerVal !== 'undefined') {
    return true;
  }
  if (focus) {
    return true;
  }
  return false;
};

export interface OutlinedInputProps
  extends Omit<React.InputHTMLAttributes<HTMLInputElement>, 'size' | 'prefix'> {
  label?: string;
  endAdornment?: React.ReactNode;
}

// FIXME:  1. :-internal-autofill-selected 样式有问题; 2. 红色下划线问题；3. 受控/非受控状态是否都没有问题
const OutlinedInput = React.forwardRef<HTMLInputElement, OutlinedInputProps>(
  ({ label, onFocus, onBlur, value, onChange, endAdornment, ...rest }, ref) => {
    const { current: isControlled } = React.useRef(typeof value !== 'undefined');
    const [innerVal, setInnerVal] = useState<OutlinedInputProps['value']>();
    const [focus, setFocus] = useState(false);

    const handleFocus: React.FocusEventHandler<HTMLInputElement> = (e) => {
      setFocus(true);
      onFocus?.(e);
    };
    const hanldeBlur: React.FocusEventHandler<HTMLInputElement> = (e) => {
      setFocus(false);
      onBlur?.(e);
    };
    const hanldeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      if (!isControlled) {
        setInnerVal(e.target.value);
      }
      onChange?.(e);
    };

    const notched = getNotched(innerVal, focus);
    const wrapCls = cls({
      [`${prefixCls}-wrap`]: true,
      [`${prefixCls}-wrap-adornedEnd`]: !!endAdornment,
    });
    const labelCls = cls(`${prefixCls}-label`, {
      [`${prefixCls}-label-shrink`]: notched,
      [`${prefixCls}-label-shrink-active`]: focus,
    });
    const legendCls = cls(`${prefixCls}-legend`, {
      [`${prefixCls}-legend-notched`]: notched,
    });
    const notchedBorderCls = cls(`${prefixCls}-notched-border`, {
      [`${prefixCls}-notched-border-focused`]: focus,
    });
    const restInputProps = omit(rest, ['className']);
    return (
      <div className={wrapCls}>
        <label className={labelCls}>{label}</label>
        <input
          ref={ref}
          value={isControlled ? innerVal : value}
          onFocus={handleFocus}
          onBlur={hanldeBlur}
          onChange={hanldeChange}
          className={prefixCls}
          {...restInputProps}
        />
        {endAdornment}
        <fieldset className={notchedBorderCls} aria-hidden>
          <legend className={legendCls}>{label}</legend>
        </fieldset>
      </div>
    );
  },
);

export default OutlinedInput;
