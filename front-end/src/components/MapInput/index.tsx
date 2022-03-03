import React, { FC, useEffect, useState } from 'react';
import { Input, Row, Col } from 'antd';
import { PlusCircleOutlined, DeleteOutlined } from '@ant-design/icons';
import { cloneDeep } from 'lodash';

import styles from './index.less';

export interface MapInputProps {
  value?: Record<string, string>;
  onChange?: (value: Record<string, string>) => void;
  style?: React.CSSProperties;
}

export type MapInputItemValue = [string | undefined, string | undefined];

const MapInputItem: FC<{
  value: MapInputItemValue;
  onChange?: (value: MapInputItemValue) => void;
  onAdd?: () => void;
  onDelete?: () => void;
}> = ({ value, onChange, onAdd, onDelete }) => {
  const [key, val] = value;

  const handleKeyChange: React.ChangeEventHandler<HTMLInputElement> = (e) => {
    const newValue: MapInputItemValue = [e.target.value, val];
    onChange?.(newValue);
  };

  const handleValueChange: React.ChangeEventHandler<HTMLInputElement> = (e) => {
    const newValue: MapInputItemValue = [key, e.target.value];
    onChange?.(newValue);
  };
  return (
    <Row gutter={8} style={{ position: 'relative' }}>
      <Col span={8}>
        <Input placeholder="请输入key" value={key} onChange={handleKeyChange} />
      </Col>
      <Col span={16}>
        <Input placeholder="请输入value" value={val} onChange={handleValueChange} />
      </Col>
      <div className={styles.btns}>
        <PlusCircleOutlined style={{ color: '#304ffe' }} onClick={onAdd} />
        <DeleteOutlined style={{ marginLeft: 4 }} onClick={onDelete} />
      </div>
    </Row>
  );
};

const MapInput: FC<MapInputProps> = ({ value, onChange, style }) => {
  const defaultItem: MapInputItemValue = [undefined, undefined];
  const getDefaultItem = () => cloneDeep(defaultItem);
  const [itemValues, setItemValues] = useState<MapInputItemValue[]>([]);

  const emitChange = (newItemValues: MapInputItemValue[]) => {
    const newValue = newItemValues.reduce((acc, cur) => {
      if (cur[0]) {
        return Object.assign(acc, { [cur[0]]: cur[1] });
      }
      return acc;
    }, {});
    onChange?.(newValue);
  };

  const handleChange = (index: number, itemVal: MapInputItemValue) => {
    const newItemValues = cloneDeep(itemValues);
    newItemValues.splice(index, 1, itemVal);
    setItemValues(newItemValues);
    emitChange(newItemValues);
  };

  const handleAdd = (index: number) => {
    const newItemValues = cloneDeep(itemValues);
    newItemValues.splice(index + 1, 0, getDefaultItem());
    setItemValues(newItemValues);
  };

  const handleDelete = (index: number) => {
    const newItemValues = cloneDeep(itemValues);
    newItemValues.splice(index, 1);
    setItemValues(newItemValues);
    emitChange(newItemValues);
  };

  useEffect(() => {
    const newItemValues = cloneDeep(itemValues);
    // 如果是初始化赋值时将初始化值删除
    if (
      newItemValues.length === 1 &&
      typeof newItemValues[0][0] === 'undefined' &&
      typeof newItemValues[0][1] === 'undefined'
    ) {
      newItemValues.splice(0, 1);
    }
    for (let [k, v] of Object.entries(value || {})) {
      const index = newItemValues.findIndex((itemValue) => itemValue[0] === k);
      if (index > -1) {
        newItemValues[index][1] = v;
      } else {
        newItemValues.push([k, v]);
      }
    }
    if (newItemValues.length === 0) {
      newItemValues.push(getDefaultItem());
    }
    setItemValues(newItemValues);
  }, [value]);

  return (
    <div className={styles.mapInputWp} style={style}>
      {itemValues?.map((itemValue, index) => (
        <MapInputItem
          key={index}
          value={itemValue}
          onChange={(val) => handleChange(index, val)}
          onAdd={() => handleAdd(index)}
          onDelete={() => handleDelete(index)}
        />
      ))}
    </div>
  );
};

export default MapInput;
