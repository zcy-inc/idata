import React, { useEffect, useState } from 'react';
import { Select } from 'antd';
import type { CSSProperties } from 'react';

import { getEnumNames } from '@/services/tablemanage';
import { EnumName } from '@/types/tablemanage';
import { isEnumType } from '@/utils/tablemanage';

interface SelectTypeProps {
  value?: string;
  disabled?: boolean;
  style?: CSSProperties;
  onChange?: (value: string) => void;
}

const enumOptions = [
  { label: '文本', value: 'STRING' },
  { label: '布尔', value: 'BOOLEAN' },
  { label: '枚举', value: 'ENUM' },
];

const SelectType: React.FC<SelectTypeProps> = ({ value = 'STRING', onChange, disabled, style }) => {
  const [enumName, setEnumName] = useState<string>();
  const [enumValue, setEnumValue] = useState<string>();
  const [enumOps, setEnumOps] = useState([]);

  useEffect(() => {
    isEnumType(value) ? getNames() : setEnumName(value);
  }, []);

  const getNames = () =>
    getEnumNames()
      .then((res) => {
        const data = Array.isArray(res.data) ? res.data : [];
        const ops = data.map((_: EnumName) => ({ label: _.enumName, value: _.enumCode }));
        setEnumName('ENUM');
        setEnumOps(ops);
        onEnumValueChange(isEnumType(value) ? value : ops[0]?.value);
      })
      .catch((err) => {});

  const triggerChange = (v: string) => {
    onChange?.(v);
  };

  const onEnumChange = (v: string) => {
    setEnumName(v);
    triggerChange(v);
    if (isEnumType(v)) {
      getNames();
    }
  };

  const onEnumValueChange = (v: string) => {
    setEnumValue(v);
    triggerChange(v);
  };

  return (
    <div style={{ display: 'flex', ...style }}>
      <Select
        key="enum"
        disabled={disabled}
        placeholder="请选择"
        value={enumName}
        onChange={onEnumChange}
        options={enumOptions}
        style={{ width: isEnumType(value) ? '50%' : '100%' }}
      />
      {isEnumType(value) && (
        <Select
          key="enumValue"
          disabled={disabled}
          value={enumValue}
          onChange={onEnumValueChange}
          options={enumOps}
          style={{ width: '50%', marginLeft: 4 }}
        />
      )}
    </div>
  );
};

export default SelectType;
