import React, { ChangeEvent, FC } from 'react';
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
}> = ({ onRefresh, style, options, onChange, value = {} }) => {
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
  };

  const handleInput = (e: ChangeEvent<HTMLInputElement>) => {
    const newValue: TableSelectInputValue = { ...value, rawTable: e.target.value };
    onChange?.(newValue);
  };

  return (
    <div className={styles.wp} style={style}>
      {inputMode === Mode.INPUT && (
        <div className={styles.inpWp}>
          <input
            value={value?.rawTable}
            className={styles.inp}
            type="text"
            onChange={handleInput}
          />
          <div className={styles.numWp}>
            [
            <input
              className={styles.num}
              value={value?.tableIdxBegin}
              type="number"
              onChange={changeStart}
            />
            -
            <input
              className={styles.num}
              value={value?.tableIdxEnd}
              type="number"
              onChange={changeEnd}
            />
            ]
          </div>
        </div>
      )}
      {inputMode === Mode.SELECT && (
        <JoinSelect size="large" options={options} value={value?.rawTable} onChange={onSelect} />
      )}
      <div className={styles.extra}>
        <RetweetOutlined onClick={changeMode} style={{ marginRight: 8 }} />
        <a onClick={onRefresh}>刷新视图</a>
      </div>
    </div>
  );
};

export default TableSelectInput;
