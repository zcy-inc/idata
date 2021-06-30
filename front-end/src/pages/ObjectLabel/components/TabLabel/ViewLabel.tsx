import React, { Fragment, useEffect, useRef, useState } from 'react';
import { Button, Card, Descriptions, Typography, Skeleton, Tabs, Table, Space, Empty } from 'antd';
import type { FC, Key } from 'react';
import styles from '../../index.less';

import Title from '../Title';
import ViewRules from './components/ViewRules';

import { getObjectLabelLayer, exportObjectLabel } from '@/services/objectlabel';
import { ObjectLabel, RuleLayer } from '@/types/objectlabel';
import { ObjectTypeView } from './constants';

export interface ViewLabelProps {
  data: ObjectLabel;
}

const { Item } = Descriptions;
const { Link } = Typography;

const ViewLabel: FC<ViewLabelProps> = ({ data }) => {
  const [visible, setVisible] = useState(false);

  const [layers, setLayers] = useState<RuleLayer[]>([]);
  const [activeKey, setActiveKey] = useState<string>('');

  const [loading, setLoading] = useState(false);
  const [loadingExport, setLoadingExport] = useState(false);
  const [columns, setColumns] = useState<{ title: string; key: Key; dataIndex: Key }[][]>([]);
  const [list, setList] = useState<{ [key: string]: any }[][]>([]);

  const showModal = () => setVisible(true);
  const hideModal = () => setVisible(false);

  useEffect(() => {
    if (data) {
      setLayers(data.ruleLayers);
      setActiveKey(`${data.ruleLayers[0].layerId}`);
    }
  }, [data]);

  const getActiveIndex = () => layers.findIndex((layer) => `${layer.layerId}` === activeKey);

  const getList = () => {
    setLoading(true);
    getObjectLabelLayer({ id: data.id, layerId: activeKey })
      .then((res) => {
        const c = res.data.columns.map((column, i) => ({
          title: column.columnName,
          key: i,
          dataIndex: i,
        }));
        const d = res.data.data.map((r) => {
          const tmp = { id: Date.now() };
          r.forEach((v, i) => (tmp[i] = v));
          return tmp;
        });
        const i = getActiveIndex();
        columns[i] = c;
        list[i] = d;
        setColumns([...columns]);
        setList([...list]);
      })
      .catch((err) => {})
      .finally(() => setLoading(false));
  };

  const onExport = () => {
    setLoadingExport(true);
    exportObjectLabel({ id: data.id, layerId: activeKey })
      .then((res) => {
        const body = document.querySelector('body');
        const link = document.createElement('a');
        const blob = new Blob([res], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        });
        link.href = window.URL.createObjectURL(blob); // 创建URL对象 param: blob
        link.download = `${data.name}_${activeKey}`;
        // fix Firefox
        link.style.display = 'none';
        body?.appendChild(link);
        link.click();
        body?.removeChild(link);
        window.URL.revokeObjectURL(link.href); // 释放创建的URL对象
      })
      .catch((err) => {})
      .finally(() => setLoadingExport(false));
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
        <Item label="最近编辑时间">{data?.editTime}</Item>
        <Item label="备注" span={3}>
          {data?.remark}
        </Item>
      </Descriptions>
      <Title>数据内容</Title>
      <Card className={`${styles.content} ${styles.reset}`}>
        <Tabs
          onChange={setActiveKey}
          tabBarExtraContent={{
            right: (
              <Space>
                <Button onClick={onExport} loading={loadingExport} disabled={loading}>
                  导出
                </Button>
                <Button onClick={getList} disabled={loading || loadingExport}>
                  查询
                </Button>
              </Space>
            ),
          }}
        >
          {layers.map((layer, i) => (
            <Tabs.TabPane tab={layer.layerName} key={layer.layerId} style={{ marginTop: 16 }}>
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
