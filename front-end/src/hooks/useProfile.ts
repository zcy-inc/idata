import { useEffect, useState, useRef } from 'react';
import { useParams } from 'umi';
import usePersistFn from './usePersistFn';
import useUpdateEffect from './useUpdateEffect';

const defaultOption = { manual: false, refreshDeps: [] };
interface TprofileFn<T> {
  (id: number): Promise<{ data: T }>;
}
interface TuseProfileOption {
  manual?: boolean;
  refreshDeps?: any[];
}

export default <T = any>(service: TprofileFn<T>, option?: TuseProfileOption) => {
  const optionRef = useRef({ ...defaultOption, ...option });
  const [data, setData] = useState<T>();
  const params = useParams<{ id?: string }>();

  const idRef = useRef<number>();
  if (typeof params.id !== 'undefined' && !Number.isNaN(Number(params.id))) {
    idRef.current = Number(params.id);
  }

  const fetchData = async (_id?: number) => {
    if (typeof _id !== 'undefined') {
      const r = await service(_id);
      setData(r.data);
    }
  };
  const run = usePersistFn(fetchData) as typeof fetchData;
  useEffect(() => {
    if (!optionRef.current.manual) {
      run(idRef.current);
    }
  }, [idRef, run, optionRef]);
  useUpdateEffect(() => {
    run(idRef.current);
  }, [idRef, ...(option?.refreshDeps || [])]);
  return { data, setData, id: idRef.current, run };
};
