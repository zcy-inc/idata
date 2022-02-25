import { useState, useEffect } from 'react';
import { DefaultOptionType } from 'antd/es/select';
import {
  getDIJobTypes,
  getDISyncMode,
} from '@/services/datadev';

// DI任务类型下拉列表
export const useDIJobTypeOptions = () => {
  const optionTuple = useState<DefaultOptionType[]>([]);
  const [_, setOptions] = optionTuple;

  useEffect(() => {
    const fetchData = async () => {
      const data = await getDIJobTypes();
      setOptions(data);
    };
    fetchData();
  }, []);

  return optionTuple;
};

// DI同步方式下拉列表
export const useDISyncModeOptions = () => {
  const optionTuple = useState<DefaultOptionType[]>([]);
  const [_, setOptions] = optionTuple;
  useEffect(() => {
    const fetchData = async () => {
      const data = await getDISyncMode();
      setOptions(data);
    };
    fetchData();
  }, []);
  return optionTuple;
};
