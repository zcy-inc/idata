import React, { Fragment, useEffect, useState } from 'react';
import ProTable from '@ant-design/pro-table';
import { Button, Card, Descriptions, Typography } from 'antd';
import type { FC, Key } from 'react';
import styles from '../../index.less';

import Title from '../Title';
import ViewRules from './components/ViewRules';

import { getObjectLabelLayer, exportObjectLabel } from '@/services/objectlabel';
import { ObjectLabel, RuleLayer } from '@/types/objectlabel';

export interface ViewLabelProps {
  data: ObjectLabel;
}
interface Layer extends RuleLayer {
  key: number;
  label: string;
}
const { Item } = Descriptions;
const { Link } = Typography;
const mock: Layer[] = [
  {
    key: 1,
    label: '1',
    layerId: 1,
    layerName: 'in_vane s layer',
    ruleDef: {
      rules: [
        {
          ruleId: 11,
          ruleName: 'aa',
          indicatorDefs: [
            {
              indicatorCode: '111',
              condition: 'equal',
              params: [100, 200],
            },
          ],
          dimensionDefs: [
            {
              dimensionCode: '222',
              params: ['location'],
            },
          ],
        },
        {
          ruleId: 11,
          ruleName: 'aa',
          indicatorDefs: [
            {
              indicatorCode: '111',
              condition: 'equal',
              params: [100, 200],
            },
          ],
          dimensionDefs: [
            {
              dimensionCode: '222',
              params: ['location'],
            },
          ],
        },
        {
          ruleId: 11,
          ruleName: 'aa',
          indicatorDefs: [
            {
              indicatorCode: '111',
              condition: 'equal',
              params: [100, 200],
            },
          ],
          dimensionDefs: [
            {
              dimensionCode: '222',
              params: ['location'],
            },
          ],
        },
        {
          ruleId: 11,
          ruleName: 'aa',
          indicatorDefs: [
            {
              indicatorCode: '111',
              condition: 'equal',
              params: [100, 200],
            },
          ],
          dimensionDefs: [
            {
              dimensionCode: '222',
              params: ['location'],
            },
          ],
        },
        {
          ruleId: 11,
          ruleName: 'aa',
          indicatorDefs: [
            {
              indicatorCode: '111',
              condition: 'equal',
              params: [100, 200],
            },
          ],
          dimensionDefs: [
            {
              dimensionCode: '222',
              params: ['location'],
            },
          ],
        },
      ],
    },
  },
];

const ViewLabel: FC<ViewLabelProps> = ({ data }) => {
  const [visible, setVisible] = useState(false);
  const [key, setKey] = useState<Key>('');
  const [layers, setLayers] = useState<Layer[]>(mock);
  const [columns, setColumns] = useState([]);
  const [list, setList] = useState([{}]);

  const showModal = () => setVisible(true);
  const hideModal = () => setVisible(false);

  useEffect(() => {
    if (data) {
      const tmpL = data.ruleLayers.map((layer) => ({
        ...layer,
        key: layer.layerId,
        label: layer.layerName,
      }));
      setLayers(tmpL);
    }
  }, [data]);

  const getList = (layerId?: number) => {
    getObjectLabelLayer({ id: data.id, layerId: (layerId || key) as number })
      .then((res) => {})
      .catch((err) => {});
  };

  const onExport = () => {
    exportObjectLabel({ id: data.id, layerId: key as number })
      .then((res) => {
        const link = document.createElement('a');
        const body = document.querySelector('body');

        link.href = window.URL.createObjectURL(res.data); // 创建对象url param: blob
        link.download = 'fileName';

        // fix Firefox
        link.style.display = 'none';
        body?.appendChild(link);

        link.click();
        body?.removeChild(link);

        window.URL.revokeObjectURL(link.href); // 通过调用 URL.createObjectURL() 创建的 URL 对象
      })
      .catch((err) => {});
  };

  return (
    <Fragment>
      <Title>基本信息</Title>
      <Descriptions
        column={3}
        colon={false}
        labelStyle={{ color: '#8A8FAE' }}
        style={{ margin: '16px 0' }}
      >
        <Item label="标签名称">{data?.name}</Item>
        <Item label="标签英文名">{data?.nameEn}</Item>
        <Item label="标签主体">{data?.objectType}</Item>
        <Item label="标签规则">
          <Link onClick={showModal}>查看详情</Link>
        </Item>
        <Item label="更新人">{data?.editor}</Item>
        <Item label="最近编辑时间">{data?.editTime}</Item>
        <Item label="备注" span={3}>
          {data?.remark}
        </Item>
      </Descriptions>
      <Title>数据内容</Title>
      <Card className={`${styles.content} ${styles.reset}`}>
        <ProTable
          columns={columns}
          dataSource={list}
          search={false}
          options={false}
          toolbar={{
            menu: { type: 'tab', items: layers, onChange: (key) => setKey(key as Key) },
            actions: [
              <Button onClick={onExport}>导出</Button>,
              <Button onClick={() => getList()}>查询</Button>,
            ],
          }}
        />
      </Card>
      {visible && <ViewRules layers={layers} visible={visible} onCancel={hideModal} />}
    </Fragment>
  );
};

export default ViewLabel;
