import React, { useEffect, useState } from 'react';
import { Spin, Select } from 'antd';
import type { SelectProps } from 'antd';
import type { FC } from 'react';
import { getTables } from '@/services/quality';
import _ from 'lodash';
const TableSelect: FC<SelectProps> = (props) => {
  const [optopns, setOptions] = useState<{label: string; value: string;} []>([]);
  const [isFetching, setIsFetching] = useState(false);
  useEffect(() => {
    getOptions();
  },[])

  const getOptions = (tableName?: string | undefined) => {
    setIsFetching(true)
    getTables({tableName}).then(res => {
      setOptions(res.data?.map(item => ({value: item.tableName, label: item.tableName})))
    }).finally(() => {
      setIsFetching(false)
    })
  }

  const onSearch = _.debounce(getOptions, 800);

  return (
    <Select
      placeholder="请选择"
      options={optopns}
      showSearch
      filterOption={false}
      notFoundContent={ isFetching ? <Spin size="small" /> : 'Not Found'}
      onSearch={onSearch}
      allowClear
      {...props}
    />
  );
};

export default TableSelect;
