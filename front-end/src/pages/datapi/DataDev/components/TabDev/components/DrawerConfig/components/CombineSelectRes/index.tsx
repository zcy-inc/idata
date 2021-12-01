import React from 'react';
import { Input, Select, Space } from 'antd';
import type { FC } from 'react';

interface CombineSelectDepProps {
  createRecordOutput: () => void;
  getRecord: () => void;
  value?: any;
  onChange?: (v: any) => void;
}

const width = 200;

const CombineSelectRes: FC<CombineSelectDepProps> = ({
  value,
  onChange,
  createRecordOutput,
  getRecord,
}) => {
  return (
    <>
      <Space>
        <Select
          style={{ width }}
          placeholder="选择数据源"
          options={[]}
          value={value}
          onChange={onChange}
        />
        <Input placeholder="请输入" style={{ width }} />
      </Space>
      <a style={{ marginLeft: 16 }} onClick={createRecordOutput}>
        添加
      </a>
      <a style={{ marginLeft: 16 }} onClick={getRecord}>
        自动获取
      </a>
    </>
  );
};

export default CombineSelectRes;
