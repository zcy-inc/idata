import React, { useEffect, useState } from 'react';
import ReactECharts from 'echarts-for-react';
import type { FC } from 'react';
import Title from '@/components/Title';

import { optionsPie } from './options';
import { cloneDeep, get, set } from 'lodash';
import { OperationOverview } from '@/types/operations';
import { JobOperationStatus } from '@/constants/operations';

interface ClusterStateProps {
  cluster: OperationOverview;
}

const ClusterState: FC<ClusterStateProps> = ({ cluster }) => {
  const [options, setOptions] = useState(optionsPie);

  useEffect(() => {
    const tmp = cloneDeep(options);
    const list = get(cluster, 'nameValueResponseList', []);
    list.forEach((_) => {
      switch (_.name) {
        case JobOperationStatus.SUCCESS:
          set(tmp, 'series.[0].data.[0].value', _.value);
          break;
        case JobOperationStatus.READY:
          set(tmp, 'series.[0].data.[1].value', _.value);
          break;
        case JobOperationStatus.RUNNING:
          set(tmp, 'series.[0].data.[2].value', _.value);
          break;
        case JobOperationStatus.FAILURE:
          set(tmp, 'series.[0].data.[3].value', _.value);
          break;
        case JobOperationStatus.OTHER:
          set(tmp, 'series.[0].data.[4].value', _.value);
          break;

        default:
          break;
      }
    });
    setOptions(tmp);
  }, [cluster]);

  return (
    <>
      <Title>资源调度状态占比</Title>
      <ReactECharts option={options} />
    </>
  );
};

export default ClusterState;
