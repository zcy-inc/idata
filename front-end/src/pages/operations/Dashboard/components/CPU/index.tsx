import React, { useEffect, useState } from 'react';
import { Progress, Space } from 'antd';
import type { FC } from 'react';
import Title from '@/components/Title';
import { getUsageOverview } from '@/services/operations';
import { UsageOverview } from '@/types/operations';

const CPU: FC<{}> = () => {
  const [data, setData] = useState<UsageOverview>();

  useEffect(() => {
    getUsageOverview()
      .then((res) => {
        setData(res.data);
      })
      .catch((err) => {});
  }, []);

  const renderFormat = (label: string, p?: number) => (
    <>
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'flex-start' }}>
        <span style={{ color: '#141736', fontWeight: 500, fontSize: 28 }}>{p}</span>
        <span style={{ color: '#141736', fontSize: 12, marginTop: 3 }}>%</span>
      </div>
      <div style={{ color: '#2d3956', fontSize: 12, marginTop: 8 }}>{label}</div>
    </>
  );

  const renderValue = (v: string = '0') => Number(v.split('%')[0]);

  return (
    <>
      <Title>资源使用情况</Title>
      <Space
        size={40}
        style={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          height: '100%',
        }}
      >
        <Progress
          type="circle"
          percent={renderValue(data?.vCoreUsageRate)}
          strokeColor="#2d3956"
          strokeWidth={8}
          width={140}
          format={(p) => renderFormat('CPU使用率', p)}
        />
        <Progress
          type="circle"
          percent={renderValue(data?.memUsageRate)}
          strokeColor="#304ffe"
          strokeWidth={8}
          width={140}
          format={(p) => renderFormat('内存使用率', p)}
        />
      </Space>
    </>
  );
};

export default CPU;
