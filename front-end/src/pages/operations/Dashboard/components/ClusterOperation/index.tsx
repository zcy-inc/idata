import React, { useEffect, useState } from 'react';
import ReactECharts from 'echarts-for-react';
import type { FC } from 'react';
import Title from '@/components/Title';

import { optionsPie } from './options';
import { getOperationLine } from '@/services/operations';
import { cloneDeep, get, set } from 'lodash';

interface ClusterOperationProps {}

const ClusterOperation: FC<ClusterOperationProps> = () => {
  const [options, setOptions] = useState(optionsPie);

  useEffect(() => {
    getOperationLine({ scope: 'yarn' })
      .then((res) => {
        const tmp = cloneDeep(options);
        const yAxisList = get(res.data, 'yAxisList', []);
        set(tmp, 'xAxis.data', res.data.xAxis);
        yAxisList.forEach((_) => {
          const data = _.yAxis.map((d) => Number(d.split('%')[0]));
          switch (_.name) {
            case '成功率':
              set(tmp, 'series.[0].data', data);
              break;
            case '失败':
              set(tmp, 'series.[1].data', data);
              break;

            default:
              break;
          }
        });
        setOptions(tmp);
      })
      .catch((err) => {});
  }, []);

  return (
    <>
      <Title>作业集群运行情况</Title>
      <ReactECharts option={options} />
    </>
  );
};

export default ClusterOperation;
