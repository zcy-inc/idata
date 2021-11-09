import type { FC } from 'react';
import React, { useState } from 'react';
import { Button, Collapse, Spin } from 'antd';
import { useRequest } from 'umi';
import { getConfigByType } from '@/services/system-controller'
import type { IConfigs } from '@/types/system-controller'
import {dataToList} from "../../utils"
import HadoopPanel from './Panel';
const { Panel } = Collapse;


const Hadoop: FC = () => {
  const [configs, setConfigs] = useState<IConfigs[]>([]);
  const { loading: fetchLoading } = useRequest(() => getConfigByType("HADOOP"), {
    onSuccess: (data) => {
      setConfigs(data)
    }
  });


  return (
    <Spin spinning={fetchLoading}>
      <div style={{ textAlign: 'right', padding: '0 10px 15px 10px', background: '#fff' }}>
        <Button type="primary" > 保存</Button>
      </div>
      <Collapse >
        {
          configs?.map(config => {
            const { id, valueOne, keyOne } = config;
            const dataSource= dataToList(valueOne)
            return (
              <Panel header={keyOne}  key={keyOne}>
                <HadoopPanel
                  id={id}
                  headerTitle={keyOne}
                  dataSource={dataSource} />
              </Panel>)
          })
        }

      </Collapse>
    </Spin>
  );
};

export default Hadoop
