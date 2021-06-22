import React, { Fragment, useState } from 'react';
import {
  Button,
  Card,
  Collapse,
  Divider,
  Dropdown,
  Input,
  Menu,
  Select,
  Space,
  Typography,
} from 'antd';
import { cloneDeep } from 'lodash';
import type { FC, Key } from 'react';
import styles from '../../../index.less';

import { IconFont } from '@/components';
import { getRandomStr } from '@/utils/tablemanage';
import { useRef } from 'react';

export interface ViewDIMProps {}
interface Layer {
  key: string;
  title: string;
  DIM?: { a: string | null; b: string | null }[];
  Mtr?: { a: string | null; b: string | null; c: string }[];
}
const initialLayer = {
  key: 'initial',
  title: '分层1',
  DIM: [],
  Mtr: [],
};
const { Panel } = Collapse;
const { Link } = Typography;

const EditRules: FC<ViewDIMProps> = ({}) => {
  const [layers, setLayers] = useState(new Map<string, Layer>([['initial', initialLayer]]));
  const [activeLayer, setActiveLayer] = useState<Layer>(initialLayer);
  const menuClickedLayer = useRef<Layer>();

  const renderLayers = () => {
    const tmp = [];
    for (let value of layers.values()) {
      tmp.push(value);
    }

    return tmp;
  };

  const onMenuAction = (key: Key) => {
    if (key === 'copy') {
      const copy: Layer = cloneDeep(menuClickedLayer.current);
      copy.key = Date.now().toString();
      copy.title = `${copy?.title}_copy`;
      layers.set(copy.key, copy);
      setLayers(new Map([...layers]));
      setActiveLayer(copy);
    }
    if (key === 'del') {
      layers.delete(menuClickedLayer.current?.key || ''); // TODO
      setLayers(new Map([...layers]));
      if (layers.size === 0) {
        setActiveLayer(null);
      } else {
        const tmp = renderLayers();
        const lastIndex = tmp.findIndex((_) => _.key === activeLayer.key) - 1;
        const curActive = tmp[lastIndex > 0 ? lastIndex : 0];
        setActiveLayer(curActive);
      }
    }
  };

  const menu = (
    <Menu onClick={({ key }) => onMenuAction(key)}>
      <Menu.Item key="copy">复制</Menu.Item>
      <Menu.Item key="del">删除</Menu.Item>
    </Menu>
  );

  const addLayer = () => {
    const key = Date.now().toString();
    const _ = { key, title: '分层_' + getRandomStr(5), DIM: [], Mtr: [] };
    layers.set(key, _);
    setLayers(layers);
    setActiveLayer(_);
  };

  const onAddItem = (e: React.MouseEvent<HTMLAnchorElement, MouseEvent>, type: 'DIM' | 'Mtr') => {
    e.stopPropagation();
    if (type === 'DIM') {
      activeLayer.DIM.push({ a: null, b: null });
    }
    if (type === 'Mtr') {
      activeLayer.Mtr.push({ a: null, b: null, c: '' });
    }
    layers.set(activeLayer?.key, activeLayer);
    setActiveLayer({ ...activeLayer });
    setLayers(new Map([...layers]));
  };

  const onDelItem = (i: number, type: 'DIM' | 'Mtr') => {
    if (type === 'DIM') {
      activeLayer.DIM.splice(i, 1);
    }
    if (type === 'Mtr') {
      activeLayer.Mtr.splice(i, 1);
    }
    layers.set(activeLayer?.key, activeLayer);
    setActiveLayer({ ...activeLayer });
    setLayers(new Map([...layers]));
  };

  const onChangeLayer = (key: string) => {
    setActiveLayer(layers.get(key) as Layer);
  };

  const onChangeVisible = (visible: boolean, layer: Layer) => {
    if (visible) {
      menuClickedLayer.current = layer;
    }
  };

  return (
    <Card className={styles['edit-content']}>
      <Space wrap>
        {renderLayers().map((layer) => (
          <Dropdown.Button
            key={layer.key}
            className={activeLayer.key === layer.key && styles['layer-active']}
            onClick={() => onChangeLayer(layer.key)}
            onVisibleChange={(v) => onChangeVisible(v, layer)}
            overlay={menu}
            trigger={['click']}
          >
            {layer.title}
          </Dropdown.Button>
        ))}
        <Button icon={<IconFont type="icon-xinjian" />} type="dashed" onClick={addLayer}>
          添加分层
        </Button>
      </Space>
      <Divider />
      {activeLayer && (
        <Fragment>
          <Input
            value={activeLayer?.title}
            onChange={({ target: { value } }) => {
              const _ = { ...activeLayer, title: value };
              layers.set(activeLayer?.key, _);
              setActiveLayer(_ as Layer);
              setLayers(new Map([...layers]));
            }}
          />
          <div className={styles['title-bar']}>
            <span className={styles.title}>规则1</span>
            {/* <Space>
              <Link>删除</Link>
              <Divider type="vertical" />
              <Link>添加</Link>
            </Space> */}
          </div>
          <Collapse style={{ marginTop: 16, background: '#fff' }}>
            <Panel
              header="维度信息"
              key="DIM"
              extra={<Link onClick={(e) => onAddItem(e, 'DIM')}>添加</Link>}
            >
              <Space direction="vertical">
                {activeLayer?.DIM?.map((_, i) => (
                  <Space key={i}>
                    <Select placeholder="请选择维度" style={{ width: 240 }} />
                    <Select placeholder="请选择" style={{ width: 240 }} />
                    <IconFont
                      type="icon-shanchuchanggui"
                      style={{ cursor: 'pointer' }}
                      onClick={() => onDelItem(i, 'DIM')}
                    />
                  </Space>
                ))}
              </Space>
            </Panel>
            <Panel
              header="指标信息"
              key="Mtr"
              extra={<Link onClick={(e) => onAddItem(e, 'Mtr')}>添加</Link>}
            >
              <Space direction="vertical">
                {activeLayer?.Mtr?.map((_, i) => (
                  <Space key={i}>
                    <Select placeholder="请选择指标" style={{ minWidth: 160 }} />
                    <Select placeholder="关系" style={{ minWidth: 160 }} />
                    <Input placeholder="请输入" style={{ minWidth: 160 }} />
                    <IconFont
                      type="icon-shanchuchanggui"
                      style={{ cursor: 'pointer' }}
                      onClick={() => onDelItem(i, 'Mtr')}
                    />
                  </Space>
                ))}
              </Space>
            </Panel>
          </Collapse>
        </Fragment>
      )}
    </Card>
  );
};

export default EditRules;
