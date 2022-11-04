import React, { ChangeEvent, FC } from 'react';
import { Tooltip, Select } from 'antd'
import { RetweetOutlined } from '@ant-design/icons';
import { DefaultOptionType } from 'antd/es/select';
import JoinSelect from '../JoinSelect';
import styles from './index.less';

enum Mode {
  INPUT = 'E',
  SELECT = 'S',
}

interface TableSelectInputValue {
  rawTable?: string;
  tableIdxBegin?: string;
  tableIdxEnd?: string;
  inputMode?: Mode;
}

const TableSelectInput: FC<{
  // mode 下拉选择：S、文本编辑：E
  value?: TableSelectInputValue;
  style?: React.CSSProperties;
  options: DefaultOptionType[];
  onRefresh?: () => void;
  onChange?: (val: TableSelectInputValue) => void;
  onBlur?: (val: TableSelectInputValue) => void;
  showChange?: boolean;
  disabled: boolean;
  selectMode?: "multiple" | "single";
}> = ({ onRefresh, style, options, onChange,onBlur, value = {}, showChange = true, disabled = false, selectMode = 'multiple' }) => {
  const inputMode = value?.inputMode || Mode.SELECT;

  const changeMode = () => {
    if (inputMode === Mode.INPUT) {
      const newValue = { inputMode: Mode.SELECT };
      onChange?.(newValue);
    } else {
      const newValue = { inputMode: Mode.INPUT };
      onChange?.(newValue);
    }
  };

  const changeStart = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue: TableSelectInputValue = { ...value, tableIdxBegin: e.target.value };
    onChange?.(newValue);
  };

  const changeEnd = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue: TableSelectInputValue = { ...value, tableIdxEnd: e.target.value };
    onChange?.(newValue);
  };

  const onSelect = (v?: string) => {
    const newValue: TableSelectInputValue = { inputMode: Mode.SELECT, rawTable: v };
    onChange?.(newValue);
    onBlur?.(newValue);
  };

  const handleInput = (e: ChangeEvent<HTMLInputElement>) => {
    const newValue: TableSelectInputValue = { ...value, rawTable: e.target.value };
    onChange?.(newValue);
  };

  return (
    <div className={`${styles.wp} table-select-input`} style={style}>
      {inputMode === Mode.INPUT && (
        <div className={styles.inpWp}>
          <input
            value={value?.rawTable}
            className={styles.inp}
            type="text"
            onChange={handleInput}
            disabled={disabled}
            onBlur={() => onBlur && onBlur(value)}
          />
          <div className={styles.numWp}>
            [
            <input
              className={styles.num}
              value={value?.tableIdxBegin}
              type="number"
              onChange={changeStart}
              onBlur={() => onBlur && onBlur(value)}
              disabled={disabled}
            />
            -
            <input
              className={styles.num}
              value={value?.tableIdxEnd}
              type="number"
              onBlur={() => onBlur && onBlur(value)}
              onChange={changeEnd}
              disabled={disabled}
            />
            ]
          </div>
        </div>
      )}

      {inputMode === Mode.SELECT && (
        selectMode === 'single' ?
        <Select size="large" options={options} value={value?.rawTable} onChange={onSelect} disabled={disabled} /> :
        <JoinSelect size="large" options={options} value={value?.rawTable} onChange={onSelect} disabled={disabled} />
      )}
      <div className={styles.extra}>
        {showChange &&  <Tooltip title="切换模式">
          <RetweetOutlined onClick={changeMode} style={{ marginRight: 8 }} />
        </Tooltip>}
        {onRefresh && <a onClick={onRefresh}>刷新视图</a>}
      </div>
    </div>
  );
};

export default TableSelectInput;
