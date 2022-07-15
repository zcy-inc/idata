import React, { useEffect, useState } from 'react';
import { Spin } from 'antd'
import { ProFormSelect } from '@ant-design/pro-form';
import type { FC } from 'react';
import { getTables } from '@/services/quality';
import _ from 'lodash';
const TableSelect: FC = ({}, ref) => {
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
    <ProFormSelect
      name="tableName"
      label="适用表名"
      placeholder="请选择"
      rules={[{ required: true, message: '请选择适用表名' }]}
      options={optopns}
      fieldProps={{
        searchOnFocus: true,
        showSearch: true,
        filterOption: false,
        notFoundContent: isFetching ? <Spin size="small" /> : 'Not Found',
        onSearch
      }}
    />
  );
};

export default TableSelect;
