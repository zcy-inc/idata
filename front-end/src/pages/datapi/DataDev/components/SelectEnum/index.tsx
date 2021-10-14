import React, { useEffect, useState } from 'react';
import { Select } from 'antd';
import type { CSSProperties } from 'react';
import { SelectValue } from 'antd/es/select';

import { getEnumNames } from '@/services/datadev';
import { EnumName } from '@/types/datapi';
import { isEnumType } from '@/utils/datadev';

interface SelectEnumProps<VT> {
  value?: VT;
  disabled?: boolean;
  style?: CSSProperties;
  onChange?: (value: VT) => void;
}

const enumOptions = [
  { label: '文本', value: 'STRING' },
  { label: '布尔', value: 'BOOLEAN' },
  { label: '枚举', value: 'ENUM' },
];

function SelectEnum<VT extends SelectValue = SelectValue>({
  value = 'STRING',
  onChange,
  disabled,
  style,
}: SelectEnumProps<VT>) {
  const [enumName, setEnumName] = useState<VT>();
  const [enumValue, setEnumValue] = useState<VT>();
  const [enumOps, setEnumOps] = useState([]);

  useEffect(() => {
    isEnumType(value) ? getNames() : setEnumName(value);
  }, []);

  const getNames = () =>
    getEnumNames()
      .then((res) => {
        const data = Array.isArray(res.data) ? res.data : [];
        const ops = data.map((_: EnumName) => ({ label: _.enumName, value: _.enumCode }));
        setEnumName('ENUM' as any);
        setEnumOps(ops);
        onEnumValueChange(isEnumType(value) ? value : ops[0]?.value);
      })
      .catch((err) => {});

  const triggerChange = (v: VT) => {
    onChange?.(v);
  };

  const onEnumChange = (v: VT) => {
    setEnumName(v);
    triggerChange(v);
    if (isEnumType(v)) {
      getNames();
    }
  };

  const onEnumValueChange = (v: VT) => {
    setEnumValue(v);
    triggerChange(v);
  };

  return (
    <div style={{ display: 'flex', ...style }}>
      <Select<VT>
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
}

export default SelectEnum;
