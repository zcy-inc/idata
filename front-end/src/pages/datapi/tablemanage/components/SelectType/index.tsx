import React, { useEffect, useState } from 'react';
import { Select } from 'antd';
import { getEnumNames } from '@/services/tablemanage';
import { isEnumType } from '../../../utils';

interface SelectTypeProps {
  value?: string;
  onChange?: (value: string) => void;
  disabled?: boolean;
  style?: React.CSSProperties;
}

const enumOptions = [
  { label: '文本', value: 'STRING' },
  { label: '布尔', value: 'BOOLEAN' },
  { label: '枚举', value: 'ENUM' },
];

const SelectType: React.FC<SelectTypeProps> = ({ value = 'STRING', onChange, disabled, style }) => {
  const [_enum, setEnum] = useState<any>(null);
  const [enumValue, setEnumValue] = useState<any>(null);
  const [enumOps, setEnumOps] = useState<any[]>([]);

  useEffect(() => {
    isEnumType(value) ? getNames() : setEnum(value);
  }, []);

  const getNames = () =>
    getEnumNames()
      .then((res) => {
        const data = Array.isArray(res.data) ? res.data : [];
        const ops = data.map((_: any) => ({ label: _.enumName, value: _.enumCode }));
        setEnum('ENUM');
        setEnumOps(ops);
        onEnumValueChange(isEnumType(value) ? value : ops[0]?.value);
      })
      .catch((err) => {});

  const triggerChange = (v: string) => {
    onChange?.(v);
  };

  const onEnumChange = (v: string) => {
    setEnum(v);
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
        value={_enum}
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
