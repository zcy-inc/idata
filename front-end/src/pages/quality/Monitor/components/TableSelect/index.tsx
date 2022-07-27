import React, { useEffect, useState } from 'react';
import { Spin, Select } from 'antd';
import type { FC } from 'react';
import { getTables } from '@/services/quality';
import _ from 'lodash';
const TableSelect: FC< { onChange: any; value: any }> = (props) => {
  const [optipns, setOptions] = useState<{label: string; value: string; partitioned: boolean} []>([]);
  const [isFetching, setIsFetching] = useState(false);
  useEffect(() => {
    getOptions();
  },[])

  const getOptions = (tableName?: string | undefined) => {
    setIsFetching(true)
    getTables({tableName}).then(res => {
      setOptions(res.data?.map(item => ({value: (item.tableName as string), label: (item.tableName as string), partitioned: item.partitioned})))
    }).finally(() => {
      setIsFetching(false)
    })
  }

  const handleChange = (val: string) => {
    const exist = optipns.find(opt => opt.value === val);
    props.onChange && props.onChange(val, exist?.partitioned);
  }

  const onSearch = _.debounce(getOptions, 800);

  return (
    <Select
      placeholder="请选择"
      options={optipns}
      showSearch
      filterOption={false}
      notFoundContent={ isFetching ? <Spin size="small" /> : 'Not Found'}
      onSearch={onSearch}
      allowClear
      {...props}
      onChange={handleChange}
    />
  );
};

export default TableSelect;
