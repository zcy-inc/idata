import React, { useState, useEffect } from 'react';
import { Select } from 'antd';
import { useModel } from 'umi';
import { getCachedWs, setWsCache } from '@/utils/utils';

const { Option } = Select;

const WsSelect: React.FC = () => {
  const { initialState } = useModel('@@initialState');
  const [ws, setWs] = useState<string>();
  useEffect(() => {
    setWs(getCachedWs());
  }, []);
  const onChange = (v: string) => {
    setWsCache(v);
    window.location.reload();
  };
  return (
    <Select value={ws} style={{ width: 100 }} onChange={onChange}>
      {initialState?.workspaces?.map(({ id, wsName, urlPath }) => (
        <Option key={id} value={urlPath}>
          {wsName}
        </Option>
      ))}
    </Select>
  );
};

export default WsSelect;
