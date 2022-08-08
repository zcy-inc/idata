import { useState } from 'react';
import { useRequest } from 'ahooks';
import type { DataSourceItem } from '@/types/datasource';
import { getDataSourceList, getDataSourceTypes } from '@/services/datasource';
import { DefaultOptionType } from 'antd/lib/select';

export type DSOption = DataSourceItem & DefaultOptionType;

// 数据源类型 和 数据源名称列表
const useDataSource = () => {
  // 获取数据源类型列表
  const { data: typeList = [] } = useRequest(() => getDataSourceTypes().then(({ data }) => data));
  const typeOptions = typeList.map((type) => ({ label: type, value: type }));
  const [sourceListMap, setSourceListMap] = useState<Record<string | number, DataSourceItem[]>>({});
  // 数据源名称列表
  const fetchSourceList = async (typeKey: string | number, type?: string) => {
    if (type) {
      const sourceList = await getDataSourceList({ type, limit: 10000, offset: 0 }).then(
        ({ data }) => data.content,
      );
      setSourceListMap((prev) => ({ ...prev, [typeKey]: sourceList }));
      return sourceList;
    }
    return [];
  };

  const getSourceOptions = (typeKey: string | number) => {
    return sourceListMap[typeKey]?.map(({ name, id, ...rest }) => ({
      ...rest,
      label: name,
      value: id,
    })) as DSOption[];
  };

  return {
    typeList,
    typeOptions,
    fetchSourceList,
    getSourceOptions,
  };
};

export default useDataSource;
