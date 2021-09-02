import React, { CSSProperties, Fragment, useEffect, useState } from 'react';
import { Button, Card, Descriptions, Typography, Skeleton, Tabs, Table, Empty } from 'antd';
import { get } from 'lodash';
import type { FC, Key } from 'react';
import styles from '../../index.less';

import Title from '../Title';
import ViewRules from './components/ViewRules';

import { getObjectLabelLayer } from '@/services/objectlabel';
import { ObjectLabel, RuleLayer } from '@/types/objectlabel';
import { ObjectTypeView } from './constants';

export interface ViewLabelProps {
  data: ObjectLabel;
}
interface Column {
  columnName: string;
  columnType: string;
  dataType: string;
}

const { Item } = Descriptions;
const { Link, Text } = Typography;
const ellipsis: CSSProperties = {
  display: 'inline-block',
  whiteSpace: 'nowrap',
  overflow: 'hidden',
  textOverflow: 'ellipsis',
};

const ViewLabel: FC<ViewLabelProps> = ({ data }) => {
  const [visible, setVisible] = useState(false);

  const [layers, setLayers] = useState<RuleLayer[]>([]);
  const [activeKey, setActiveKey] = useState<string>('');

  const [loading, setLoading] = useState(false);
  const [columns, setColumns] = useState<{ title: string; key: Key; dataIndex: Key }[][]>([]);
  const [list, setList] = useState<{ [key: string]: any }[][]>([]);

  const showModal = () => setVisible(true);
  const hideModal = () => setVisible(false);

  useEffect(() => {
    if (data) {
      const tmpRuleLayers = get(data, 'ruleLayers', []);
      const tmpActiveKey = get(data, 'ruleLayers.[0].layerId', '');
      setLayers(tmpRuleLayers);
      setActiveKey(`${tmpActiveKey}`);
    }
  }, [data]);

  const getActiveIndex = () => layers.findIndex((layer) => `${layer.layerId}` === `${activeKey}`);

  const getList = () => {
    setLoading(true);
    getObjectLabelLayer({ id: data.id, layerId: activeKey })
      .then((res) => {
        // 处理列
        const resColumns: Column[] = get(res, 'data.columns', []);
        const c = resColumns.map((column, i) => ({
          title: column.columnName,
          key: i,
          dataIndex: i,
        }));
        // 处理数据
        const resData: string[][] = get(res, 'data.data', []);
        const d = resData.map((r) => {
          const tmp = { id: Date.now() };
          r.forEach((v, i) => (tmp[i] = v));
          return tmp;
        });
        // 获取当前显示的分层下标
        const i = getActiveIndex();

        columns[i] = c;
        list[i] = d;
        setColumns([...columns]);
        setList([...list]);
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
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
        <Item label="标签主体">{ObjectTypeView[data?.objectType]}</Item>
        <Item label="标签规则">
          <Link onClick={showModal}>查看详情</Link>
        </Item>
        <Item label="更新人">{data?.editor}</Item>
        <Item label="最近编辑时间" contentStyle={ellipsis}>
          {data?.editTime}
        </Item>
        <Item label="备注" span={3}>
          {data?.remark}
        </Item>
      </Descriptions>
      <Title>数据内容</Title>
      <Card className={`${styles.content} ${styles.reset}`}>
        <Tabs
          onChange={setActiveKey}
          tabBarExtraContent={{
            right: [
              <Button key="search" onClick={getList} disabled={loading}>
                查询
              </Button>,
            ],
          }}
        >
          {layers?.map((layer, i) => (
            <Tabs.TabPane
              tab={
                <Text ellipsis style={{ maxWidth: 88 }}>
                  {layer.layerName}
                </Text>
              }
              key={layer.layerId}
              style={{ marginTop: 16 }}
            >
              <Skeleton loading={loading} active>
                {columns[i] ? (
                  <Table
                    rowKey="id"
                    columns={columns[i]}
                    dataSource={list[i]}
                    pagination={false}
                    scroll={{ x: 'max-content' }}
                  />
                ) : (
                  <Empty />
                )}
              </Skeleton>
            </Tabs.TabPane>
          ))}
        </Tabs>
      </Card>
      {visible && <ViewRules layers={layers} visible={visible} onCancel={hideModal} />}
    </Fragment>
  );
};

export default ViewLabel;
