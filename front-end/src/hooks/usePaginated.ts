import { useCallback, useEffect, useState } from 'react';
import type { TableProps } from 'antd/es/table';
import type { TablePaginationConfig } from 'antd/es/table/interface';
import usePersistFn from './usePersistFn';
import useUpdateEffect from './useUpdateEffect';

interface TpaginatedFn<T> {
  (params: PaginatedParams): Promise<{ data: PaginatedData<T> }>;
}

export default <T>(service: TpaginatedFn<T>, options?: { refreshDeps?: any[] }) => {
  const { refreshDeps = [] } = options || {};
  const [loading, setLoading] = useState(true);
  const [data, setData] = useState<PaginatedData>({ content: [], total: 0 });
  const [count, setCount] = useState(0);
  const [params, setParams] = useState({
    current: 1,
    pageSize: 10,
  });
  const { current, pageSize } = params;

  const servicePersist = usePersistFn(service) as TpaginatedFn<T>;
  useEffect(() => {
    (async function fetchData() {
      try {
        setLoading(true);
        const { data: res } = await servicePersist({
          offset: (current - 1) * pageSize,
          limit: pageSize,
        });
        setData(res);
      } finally {
        setLoading(false);
      }
    })();
  }, [servicePersist, current, pageSize, count]);

  useUpdateEffect(() => {
    setCount((prevCount) => prevCount + 1);
  }, [...refreshDeps]);

  const refresh = useCallback(() => setCount((prevCount) => prevCount + 1), []);

  const changePageSize = useCallback((_, s: number) => {
    setParams((prev) => ({ ...prev, pageSize: s }));
  }, []);

  const changeTable = useCallback((p: TablePaginationConfig) => {
    setParams((prev) => ({ ...prev, current: p.current || 1 }));
  }, []);
  const result: {
    tableProps: Pick<TableProps<T>, 'dataSource' | 'loading' | 'onChange' | 'pagination'>;
    refresh: typeof refresh;
  } = {
    tableProps: {
      dataSource: data.content,
      loading,
      onChange: changeTable,
      pagination: data.total <= pageSize ? false : {
        total: data.total,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (t: number) => `共 ${t} 条`,
        current,
        onShowSizeChange: changePageSize,
      },
    },
    refresh,
  };
  return result;
};
