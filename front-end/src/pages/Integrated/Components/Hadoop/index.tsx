import type { FC } from 'react';
import React, { useState } from 'react';
import { Button, Collapse, Spin,message } from 'antd';
import { useRequest } from 'umi';
import { getConfigByType } from '@/services/system-controller'
import type { IConfigs } from '@/types/system-controller'
import {editSystemConfig} from '@/services/system-controller'
import _cloneDeep from 'lodash/cloneDeep'
import { dataToList,listToData } from "../../utils"
import HadoopPanel from './Panel';
const { Panel } = Collapse;


const Hadoop: FC = () => {
  const [configs, setConfigs] = useState<IConfigs[]>([]);
  const { loading: fetchLoading } = useRequest(() => getConfigByType("HADOOP"), {
    onSuccess: (data) => {
      setConfigs(data)
    }
  });
  const { loading: saveLoading, run: save } = useRequest(async () => {
      return editSystemConfig(configs)
  }, {
    manual: true,
    onSuccess: () => {
      message.success('保存成功')
    }
  });
// TODO 需要做一个临时的列表的数据接口，现有每次变化都转换一次效率和显示都是有一个小缺陷的
  return (
    <Spin spinning={fetchLoading||saveLoading}>
      <div style={{ textAlign: 'right', padding: '0 10px 15px 10px', background: '#fff' }}>
        <Button
          type="primary"
          onClick={save}>
          保存
        </Button>
      </div>
      <Collapse >
        {
          configs?.map((config,index) => {
            const { id, valueOne, keyOne } = config;
            const dataSource = dataToList(valueOne)
            const keys = dataSource.map((item) => item.id)
            return (
              <Panel header={keyOne} key={keyOne}>
                <HadoopPanel
                  id={id}
                  headerTitle={keyOne}
                  dataSource={dataSource}
                  setDataSource={(list)=>{
                      const data = listToData(list)
                      configs[index].valueOne=data;
                      setConfigs(_cloneDeep(configs));
                  }}
                  editableKeys={keys}
                  />
              </Panel>)
          })
        }

      </Collapse>
    </Spin>
  );
};

export default Hadoop
