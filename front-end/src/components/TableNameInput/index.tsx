import React, { FC, useEffect, useState } from 'react';
import { Input, Row, message } from 'antd';
import { PlusCircleOutlined, DeleteOutlined, CopyOutlined } from '@ant-design/icons';
import { cloneDeep } from 'lodash';
import { copy } from '@/utils/utils';

import styles from './index.less';

export interface MapInputProps {
  value?: MapInputItemValue[];
  onChange?: (value: MapInputItemValue[]) => void;
  style?: React.CSSProperties;
  tableType?: string;
}

export type MapInputItemValue = { name?: string; alias?: string, error?: string };

const getDefaultItemValue = () => ({ ...{ name: undefined, alias: undefined } });
export const transformAlias = (tableType: string | undefined, name: string | undefined) => `${tableType && (tableType + '_')}${name.split('.').join('_')}`;

const MapInputItem: FC<{
  value: MapInputItemValue;
  tableType?: string;
  onChange?: (value: MapInputItemValue) => void;
  onAdd?: () => void;
  onDelete?: () => void;
}> = ({ value, tableType, onChange, onAdd, onDelete }) => {
  const handleKeyChange: React.ChangeEventHandler<HTMLInputElement> = (e) => {
    const newValue: MapInputItemValue = { ...value, name: e.target.value };
      onChange?.(newValue);
  };

  const handleKeyBlur: React.ChangeEventHandler<HTMLInputElement> = (e) => {
    if (!format(e.target.value)) {
      const newValue: MapInputItemValue = { ...value, name: e.target.value, error: '表名格式必须是[db.table]' };
      onChange?.(newValue);
    } else {
      const newValue: MapInputItemValue = { ...value, name: e.target.value, error: undefined };
      onChange?.(newValue);
    }
  };

  const format = (value:string) => {
    return value.split('.').length === 2;
  }
  const handleCopy = (value:string) => {
    copy(value);
    message.success('已复制到剪贴板');
  }

  return (
    <Row gutter={8} style={{ position: 'relative' }}>
      <Input placeholder="请按照格式[db.table]输入表名" value={value.name} onChange={handleKeyChange} onBlur={handleKeyBlur} />
      {
        value.name && !value.error && <p style={{opacity: 0.8, marginTop: '2px'}}>
          别名：{transformAlias(tableType, value.name)}
          <CopyOutlined style={{ marginLeft: 4 }} onClick={() => handleCopy(transformAlias(tableType, value.name) as string)} />
        </p>
      }
      {
        value.name && value.error && <p style={{opacity: 0.8, marginTop: '2px', color: 'red'}}>{value.error}</p>
      }
      <div className={styles.btns}>
        <PlusCircleOutlined style={{ color: '#304ffe' }} onClick={onAdd} />
        <DeleteOutlined style={{ marginLeft: 4 }} onClick={onDelete} />
      </div>
    </Row>
  );
};

const defaultItemValue = { name: undefined, value: undefined, error: undefined };

const TableNameInput: FC<MapInputProps> = ({ value, onChange, style, tableType }) => {
  const [innerValue, setInnerValue] = useState<MapInputItemValue[]>([]);

  useEffect(() => {
    if (!Array.isArray(value) || value.length === 0) {
      setInnerValue([cloneDeep(defaultItemValue)]);
    } else {
      setInnerValue(value);
    }
  }, [value]);

  const handleChange = (index: number, itemVal: MapInputItemValue) => {
    const newInnerValue = cloneDeep(innerValue);
    newInnerValue.splice(index, 1, itemVal);
    if (!value) {
      setInnerValue(newInnerValue);
    }
    onChange?.(newInnerValue);
  };

  const handleAdd = (index: number) => {
    const newInnerValue = cloneDeep(innerValue);
    newInnerValue.splice(index + 1, 0, getDefaultItemValue());
    if (!value) {
      setInnerValue(newInnerValue);
    }
    onChange?.(newInnerValue);
  };

  const handleDelete = (index: number) => {
    const newInnerValue = cloneDeep(innerValue);
    newInnerValue.splice(index, 1);
    if (!value) {
      setInnerValue(newInnerValue);
    }
    onChange?.(newInnerValue);
  };

  return (
    <div className={styles.mapInputWp} style={style}>
      {innerValue?.map((itemValue, index) => (
        <MapInputItem
          key={index}
          value={itemValue}
          tableType={tableType}
          onChange={(val) => handleChange(index, val)}
          onAdd={() => handleAdd(index)}
          onDelete={() => handleDelete(index)}
        />
      ))}
    </div>
  );
};

export default TableNameInput;
