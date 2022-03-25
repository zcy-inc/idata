import React, { FC, useEffect, useState } from 'react';
import { Input, Row, Col } from 'antd';
import { PlusCircleOutlined, DeleteOutlined } from '@ant-design/icons';
import { cloneDeep } from 'lodash';

import styles from './index.less';

export interface MapInputProps {
  value?: MapInputItemValue[];
  onChange?: (value: MapInputItemValue[]) => void;
  style?: React.CSSProperties;
}

export type MapInputItemValue = { key?: string; value?: string };

const getDefaultItemValue = () => ({ ...{ key: undefined, value: undefined } });

const MapInputItem: FC<{
  value: MapInputItemValue;
  onChange?: (value: MapInputItemValue) => void;
  onAdd?: () => void;
  onDelete?: () => void;
}> = ({ value, onChange, onAdd, onDelete }) => {
  const handleKeyChange: React.ChangeEventHandler<HTMLInputElement> = (e) => {
    const newValue: MapInputItemValue = { ...value, key: e.target.value };
    onChange?.(newValue);
  };

  const handleValueChange: React.ChangeEventHandler<HTMLInputElement> = (e) => {
    const newValue: MapInputItemValue = { ...value, value: e.target.value };
    onChange?.(newValue);
  };
  return (
    <Row gutter={8} style={{ position: 'relative' }}>
      <Col span={8}>
        <Input placeholder="key" value={value.key} onChange={handleKeyChange} />
      </Col>
      <Col span={16}>
        <Input placeholder="value" value={value.value} onChange={handleValueChange} />
      </Col>
      <div className={styles.btns}>
        <PlusCircleOutlined style={{ color: '#304ffe' }} onClick={onAdd} />
        <DeleteOutlined style={{ marginLeft: 4 }} onClick={onDelete} />
      </div>
    </Row>
  );
};

const defaultItemValue = { key: undefined, value: undefined };

const MapInput: FC<MapInputProps> = ({ value, onChange, style }) => {
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
          onChange={(val) => handleChange(index, val)}
          onAdd={() => handleAdd(index)}
          onDelete={() => handleDelete(index)}
        />
      ))}
    </div>
  );
};

export default MapInput;
