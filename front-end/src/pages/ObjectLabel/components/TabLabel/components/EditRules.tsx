import React, { Fragment, useEffect, useState } from 'react';
import {
  Button,
  Card,
  Collapse,
  Divider,
  Dropdown,
  Input,
  Menu,
  message,
  Select,
  Space,
  Typography,
} from 'antd';
import { cloneDeep } from 'lodash';
import { useModel } from 'umi';
import type { FC } from 'react';
import styles from '../../../index.less';

import { IconFont } from '@/components';
import { getRandomStr } from '@/utils/tablemanage';
import { ObjectLabel } from '@/types/objectlabel';

export interface EditRulesProps {
  initial?: ObjectLabel;
}
type ActionKey = 'copy' | 'delete';
type Condition = 'equal' | 'greater' | 'less' | 'greaterOrEqual' | 'lessOrEqual' | 'between';
interface Indicator {
  indicatorCode: string | null;
  condition: Condition | null;
  params: string[] | number[];
}
interface Dimension {
  dimensionCode: string | null;
  params: string[];
}
interface Rule {
  ruleId: number;
  ruleName: string;
  indicator: Indicator[];
  dimension: Dimension[];
}
interface Layer {
  layerId: number;
  layerName: string;
  rules: Rule[];
}
const initialLayer: Layer = {
  layerId: Date.now(),
  layerName: '分层' + getRandomStr(5),
  rules: [
    {
      ruleId: Date.now(),
      ruleName: '规则1',
      indicator: [{ indicatorCode: null, condition: null, params: [] }],
      dimension: [{ dimensionCode: null, params: [] }],
    },
  ],
};
const ConditionOptions = [
  { label: '等于', value: 'equal' },
  { label: '大于', value: 'greater' },
  { label: '小于', value: 'less' },
  { label: '大于等于', value: 'greaterOrEqual' },
  { label: '小于等于', value: 'lessOrEqual' },
  { label: '介于两个值之间', value: 'between' },
];
const { Panel } = Collapse;
const { Link } = Typography;

const EditRules: FC<EditRulesProps> = ({ initial }) => {
  const [layers, setLayers] = useState<Layer[]>([initialLayer]);
  const [activeKey, setActiveKey] = useState(0); // 取的是数组的index而非layerId
  const { setEditLayers } = useModel('objectlabel', (_) => ({
    setEditLayers: _.setEditLayers,
  }));

  useEffect(() => {
    if (initial) {
      const _layers: Layer[] = initial.ruleLayers.map((layer) => ({
        layerId: layer.layerId,
        layerName: layer.layerName,
        rules: layer.ruleDef.rules.map((rule) => ({
          ruleId: rule.ruleId,
          ruleName: rule.ruleName,
          indicator: rule.indicatorDefs.map((indicator) => ({
            indicatorCode: indicator.indicatorCode,
            condition: indicator.condition,
            params: indicator.params,
          })),
          dimension: rule.dimensionDefs.map((dimension) => ({
            dimensionCode: dimension.dimensionCode,
            params: dimension.params,
          })),
        })),
      }));
      setLayers(_layers);
    }
  }, [initial]);

  useEffect(() => {
    setEditLayers(layers);
  }, [layers]);

  const onMenuAction = (key: ActionKey, i: number) => {
    if (key === 'delete') {
      layers.splice(i, 1);
      setLayers([...layers]);
      setActiveKey(i >= layers.length ? layers.length - 1 : i);
    }
    if (key === 'copy') {
      const copy = cloneDeep(layers[i]);
      copy.layerId = Date.now();
      copy.layerName = `${copy.layerName}_cpoy_` + getRandomStr(5);
      layers.push(copy);
      setLayers([...layers]);
      setActiveKey(layers.length - 1);
    }
  };

  const menu = (i: number) => (
    <Menu onClick={({ key }) => onMenuAction(key as ActionKey, i)}>
      <Menu.Item key="copy">复制</Menu.Item>
      <Menu.Item key="delete">删除</Menu.Item>
    </Menu>
  );

  const createLayer = () => {
    const layerId = Date.now();
    const newLayer: Layer = {
      layerId,
      layerName: '分层' + getRandomStr(5),
      rules: [
        {
          ruleId: Date.now(),
          ruleName: '规则1',
          indicator: [{ indicatorCode: null, condition: null, params: [] }],
          dimension: [{ dimensionCode: null, params: [] }],
        },
      ],
    };
    layers.push(newLayer);
    setLayers([...layers]);
    setActiveKey(layers.length - 1);
  };

  const changeLayerName = (v: string) => {
    layers[activeKey].layerName = v;
    setLayers([...layers]);
  };

  const createIndicator = (iR: number) => {
    if (layers[activeKey].rules[iR].indicator.length > 0) {
      message.info('同一规则中只能存在一条指标');
      return;
    }
    layers[activeKey].rules[iR].indicator.push({
      indicatorCode: null,
      condition: null,
      params: [],
    });
    setLayers([...layers]);
  };

  const deleteIndicator = (iR: number, iI: number) => {
    layers[activeKey].rules[iR].indicator.splice(iI, 1);
    setLayers([...layers]);
  };

  const createDimension = (iR: number) => {
    layers[activeKey].rules[iR].indicator.push({
      indicatorCode: null,
      condition: null,
      params: [],
    });
    setLayers([...layers]);
  };

  const deleteDimension = (iR: number, iI: number) => {
    layers[activeKey].rules[iR].indicator.splice(iI, 1);
    setLayers([...layers]);
  };

  const setIndicator = (v: string, iR: number, iI: number, prop: string, iP?: number) => {
    if (prop === 'params') {
      layers[activeKey].rules[iR].indicator[iI].params[iP || 0] = v;
    } else {
      layers[activeKey].rules[iR].indicator[iI][prop] = v;
    }
    if (prop === 'indicatorCode') {
      layers[activeKey].rules[iR].dimension = [];
    }
    setLayers([...layers]);
  };

  const setDimension = (v: string, iR: number, iD: number, prop: string) => {
    if (prop === 'params') {
      layers[activeKey].rules[iR].dimension[iD].params[0] = v;
    } else {
      layers[activeKey].rules[iR].dimension[iD][prop] = v;
    }
    setLayers([...layers]);
  };

  return (
    <Card className={styles['edit-content']}>
      <Space wrap>
        {layers.map((layer, i) => (
          <Dropdown.Button
            key={layer.layerId}
            className={layers[activeKey]?.layerId === layer.layerId && styles['layer-active']}
            onClick={() => setActiveKey(i)}
            onVisibleChange={() => {}}
            overlay={menu(i)}
            trigger={['click']}
          >
            {layer.layerName}
          </Dropdown.Button>
        ))}
        <Button icon={<IconFont type="icon-xinjian" />} type="dashed" onClick={createLayer}>
          添加分层
        </Button>
      </Space>
      <Divider />
      {layers[activeKey]?.rules.map((rule, iR) => (
        <Fragment key={rule.ruleId}>
          <Input
            placeholder="请输入"
            value={layers[activeKey]?.layerName}
            onChange={({ target: { value } }) => changeLayerName(value)}
          />
          <div className={styles['title-bar']}>
            <span className={styles.title}>规则1</span>
          </div>
          <Collapse
            style={{ marginTop: 16, background: '#fff' }}
            defaultActiveKey={['indicator', 'dimension']}
          >
            <Panel
              header="指标信息"
              key="indicator"
              extra={
                <Link
                  onClick={(e) => {
                    e.stopPropagation();
                    createIndicator(iR);
                  }}
                >
                  添加
                </Link>
              }
            >
              <Space direction="vertical">
                {rule.indicator.map((_, iI) => (
                  <Space key={iI}>
                    <Select
                      placeholder="请选择指标"
                      style={{ minWidth: 160 }}
                      options={[]}
                      value={_.indicatorCode}
                      onChange={(v) => setIndicator(v as string, iR, iI, 'indicatorCode')}
                    />
                    <Select
                      placeholder="关系"
                      style={{ minWidth: 160 }}
                      options={ConditionOptions}
                      value={_.condition}
                      onChange={(v) => setIndicator(v as string, iR, iI, 'condition')}
                    />
                    {rule.indicator[iI].condition === 'between' ? (
                      <Input.Group compact>
                        <Input
                          style={{ width: 100, textAlign: 'center' }}
                          placeholder="最小值"
                          value={_.params[0]}
                          onChange={({ target: { value } }) =>
                            setIndicator(value, iR, iI, 'params')
                          }
                        />
                        <Input
                          className="site-input-split"
                          style={{ width: 30, pointerEvents: 'none', background: '#fff' }}
                          placeholder="~"
                          disabled
                        />
                        <Input
                          className="site-input-right"
                          style={{ width: 100, textAlign: 'center' }}
                          placeholder="最大值"
                          value={rule.indicator[iI].params[1]}
                          onChange={({ target: { value } }) =>
                            setIndicator(value, iR, iI, 'params', 1)
                          }
                        />
                      </Input.Group>
                    ) : (
                      <Input
                        placeholder="请输入"
                        style={{ minWidth: 160 }}
                        value={rule.indicator[iI].params[0]}
                        onChange={({ target: { value } }) => setIndicator(value, iR, iI, 'params')}
                      />
                    )}
                    <IconFont
                      type="icon-shanchuchanggui"
                      style={{ cursor: 'pointer' }}
                      onClick={() => deleteIndicator(iR, iI)}
                    />
                  </Space>
                ))}
              </Space>
            </Panel>
            <Panel
              header="维度信息"
              key="dimension"
              extra={
                <Link
                  onClick={(e) => {
                    e.stopPropagation();
                    createDimension(iR);
                  }}
                >
                  添加
                </Link>
              }
            >
              <Space direction="vertical">
                {rule.dimension.map((_, iD) => (
                  <Space key={iD}>
                    <Select
                      placeholder="请选择维度"
                      style={{ width: 240 }}
                      value={_.dimensionCode}
                      onChange={(v) => setDimension(v as string, iR, iD, 'dimensionCode')}
                    />
                    <Select
                      placeholder="请选择"
                      style={{ width: 240 }}
                      value={_.params[0]}
                      onChange={(v) => setDimension(v as string, iR, iD, 'params')}
                    />
                    <IconFont
                      type="icon-shanchuchanggui"
                      style={{ cursor: 'pointer' }}
                      onClick={() => deleteDimension(iR, iD)}
                    />
                  </Space>
                ))}
              </Space>
            </Panel>
          </Collapse>
        </Fragment>
      ))}
    </Card>
  );
};

export default EditRules;
