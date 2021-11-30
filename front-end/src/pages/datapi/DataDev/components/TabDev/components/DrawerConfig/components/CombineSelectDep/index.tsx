import React from 'react';
import { Select } from 'antd';
import type { FC } from 'react';

interface CombineSelectDepProps {
  createRecordParent: () => void;
  getRecord: () => void;
  value?: any;
  onChange?: (v: any) => void;
}

const width = 200;

const CombineSelectDep: FC<CombineSelectDepProps> = ({
  value,
  onChange,
  createRecordParent,
  getRecord,
}) => {
  return (
    <>
      <Select
        style={{ width }}
        placeholder="请选择"
        options={[]}
        value={value}
        onChange={onChange}
      />
      <a style={{ marginLeft: 16 }} onClick={createRecordParent}>
        添加
      </a>
      <a style={{ marginLeft: 16 }} onClick={getRecord}>
        自动获取
      </a>
    </>
  );
};

export default CombineSelectDep;
